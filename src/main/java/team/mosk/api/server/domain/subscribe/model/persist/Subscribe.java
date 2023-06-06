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
public class Subscribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_id")
    private Long id;

    @OneToOne(mappedBy = "subscribe", cascade = CascadeType.ALL)
    private Store store;

    private LocalDate startDate;

    private LocalDate endDate;

    public void renewEndDate(final Long period) {
        this.endDate.plusMonths(period);
    }

    public void resetStartDate() {
        this.startDate = LocalDate.now();
    }

    public static Subscribe createEntity(final Store store, final Long period) {
        LocalDate now = LocalDate.now();

        return Subscribe.builder()
                .store(store)
                .startDate(now)
                .endDate(now.plusMonths(period))
                .build();
    }
}
