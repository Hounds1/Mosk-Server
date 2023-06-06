package team.mosk.api.server.domain.subscribe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.mosk.api.server.IntegrationTestSupport;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.domain.store.service.StoreService;
import team.mosk.api.server.domain.store.util.WithAuthUser;
import team.mosk.api.server.domain.subscribe.dto.SubscribeResponse;
import team.mosk.api.server.domain.subscribe.error.SubInfoNotFoundException;
import team.mosk.api.server.domain.subscribe.model.persist.Subscribe;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistory;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistoryRepository;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeRepository;
import team.mosk.api.server.global.error.exception.ErrorCode;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class SubscribeServiceTest extends IntegrationTestSupport {

    @Autowired
    SubscribeService subscribeService;

    @Autowired
    SubscribeReadService subscribeReadService;

    @Autowired
    SubscribeRepository subscribeRepository;

    @Autowired
    SubscribeHistoryRepository subscribeHistoryRepository;

    @Autowired
    StoreService storeService;

    @Autowired
    StoreRepository storeRepository;

    static Store store;

    @BeforeEach
    void init() {
        Store newStore = Store.builder()
                .email("test@test.test")
                .password("12345")
                .storeName("TestStore")
                .ownerName("TestOwner")
                .call("02-000-0000")
                .crn("000-00-00000")
                .address("TestAddress")
                .build();

        StoreResponse response = storeService.create(newStore);

        store = storeRepository.findById(response.getId()).orElseThrow(
                () -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND)
        );
    }

    @Test
    @DisplayName("결제가 완료되어 메소드가 호출되면 구독정보를 저장한다.")
    @WithAuthUser
    void newSubscribe() {
        final Long period = 3L;
        final int amount = 150000;

        SubscribeResponse response = subscribeService.newSubscribe(store.getId(), period, amount);

        LocalDate now = LocalDate.now();

        /**
         * 현재 계정에 적용이 시작된 구독 정보
         */
        Subscribe findSub = subscribeRepository.findById(response.getId())
                        .orElseThrow(() -> new SubInfoNotFoundException(ErrorCode.SUB_INFO_NOT_FOUND));

        assertThat(findSub.getStore().getId()).isEqualTo(store.getId());
        assertThat(findSub.getStartDate()).isEqualTo(now);
        assertThat(findSub.getEndDate()).isEqualTo(now.plusMonths(period));
        /**
         * 현재 계정에 적용이 시작된 구독 정보
         */
        List<SubscribeHistory> histories = subscribeHistoryRepository.findAllByStoreId(store.getId());
        if (histories != null) {
            SubscribeHistory findHistory = histories.get(0);
            assertThat(findHistory.getStore().getId()).isEqualTo(findSub.getStore().getId());
            assertThat(findHistory.getStartDate()).isEqualTo(findSub.getStartDate());
            assertThat(findHistory.getEndDate()).isEqualTo(findSub.getEndDate());
            assertThat(findHistory.getAmount()).isEqualTo(amount);
        }
    }
}
