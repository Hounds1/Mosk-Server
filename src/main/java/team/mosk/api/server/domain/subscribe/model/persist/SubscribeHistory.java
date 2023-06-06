package team.mosk.api.server.domain.subscribe.model.persist;

import lombok.*;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SubscribeHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_history_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    private LocalDate startDate;

    private LocalDate endDate;

    private int amount;

    private boolean paymentStatus;

    public static SubscribeHistory transfer(final Subscribe subscribe, final int amount) {
        return SubscribeHistory.builder()
                .store(subscribe.getStore())
                .startDate(subscribe.getStartDate())
                .endDate(subscribe.getEndDate())
                .amount(amount)
                .paymentStatus(true)
                .build();
    }

    public static SubscribeHistory FailedPaymentHistory(final Store store, final int amount) {
        return SubscribeHistory.builder()
                .store(store)
                .startDate(LocalDate.now())
                .endDate(null)
                .amount(amount)
                .paymentStatus(true)
                .build();
    }
}
