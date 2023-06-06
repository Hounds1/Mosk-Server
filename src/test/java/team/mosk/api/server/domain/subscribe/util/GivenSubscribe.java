package team.mosk.api.server.domain.subscribe.util;

import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.subscribe.model.persist.Subscribe;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistory;

import java.time.LocalDate;

public class GivenSubscribe {

    public static SubscribeHistory toEntityWithStoreAndCount(final Long period, final int amount) {
        Store dummy = Store.builder()
                .id(1L)
                .storeName("TEST")
                .build();

        LocalDate now = LocalDate.now();

        return SubscribeHistory.builder()
                .id(1L)
                .store(dummy)
                .startDate(now)
                .endDate(now.plusMonths(period))
                .amount(amount)
                .build();
    }
}
