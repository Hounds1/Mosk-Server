package team.mosk.api.server.domain.subscribe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.order.dto.TossPaymentRequest;
import team.mosk.api.server.domain.order.error.PaymentGatewayException;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.domain.subscribe.dto.SubscribePaymentRequest;
import team.mosk.api.server.domain.subscribe.dto.SubscribeResponse;
import team.mosk.api.server.domain.subscribe.error.SubInfoNotFoundException;
import team.mosk.api.server.domain.subscribe.model.persist.Subscribe;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistory;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistoryRepository;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeRepository;
import team.mosk.api.server.global.client.PaymentClient;
import team.mosk.api.server.global.error.exception.ErrorCode;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscribeService {

    private final StoreRepository storeRepository;
    private final SubscribeRepository subscribeRepository;
    private final SubscribeHistoryRepository subscribeHistoryRepository;
    private final PaymentClient paymentClient;

    public SubscribeResponse subscribePayment(final SubscribePaymentRequest request, final CustomUserDetails details) {

        final Long period = request.getPeriod();
        
        try {
            paymentClient.paymentApproval(TossPaymentRequest.of(request));
        } catch (PaymentGatewayException e) {
            failedSubscribePayment(details.getId(), request.getAmount().intValue());
            throw new PaymentGatewayException(ErrorCode.PAYMENT_GATEWAY_UNSTABLE);
        }
        
        return newSubscribe(details.getId(), period, request.getAmount().intValue());
    }

    public SubscribeResponse newSubscribe(final Long storeId, final Long period, final int amount) {

        if (!existsSubscribe(storeId)) {
            return renewSubscribe(storeId, period, amount);
        }

        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND));

        Subscribe sub = Subscribe.createEntity(findStore, period);

        Subscribe savedSub = subscribeRepository.save(sub);
        transferToHistory(savedSub, amount);

        return SubscribeResponse.of(savedSub);
    }

    public SubscribeResponse renewSubscribe(final Long storeId, final Long period, final int amount) {
        Subscribe findSub = subscribeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new SubInfoNotFoundException(ErrorCode.SUB_INFO_NOT_FOUND));

        if(findSub.getEndDate().isBefore(LocalDate.now())) {
            findSub.resetStartDate();
        }

        transferToHistory(findSub, amount);

        findSub.renewEndDate(period);

        return SubscribeResponse.of(findSub);
    }

    public void transferToHistory(final Subscribe subscribe, final int amount) {
        SubscribeHistory newHistory = SubscribeHistory.transfer(subscribe, amount);

        subscribeHistoryRepository.save(newHistory);
    }

    public void failedSubscribePayment(final Long storeId, final int amount) {
        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND));
        SubscribeHistory failedHistory = SubscribeHistory.FailedPaymentHistory(findStore, amount);

        subscribeHistoryRepository.save(failedHistory);
    }

    public boolean existsSubscribe(final Long storeId) {
        return subscribeRepository.findByStoreId(storeId).isEmpty();
    }
}
