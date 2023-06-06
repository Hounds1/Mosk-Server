package team.mosk.api.server.domain.subscribe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistory;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeHistoryResponse {

    private Long id;
    private Long storeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int amount;

    public static SubscribeHistoryResponse of(final SubscribeHistory subscribeHistory) {
        return new SubscribeHistoryResponse(subscribeHistory.getId(),
                subscribeHistory.getStore().getId(),
                subscribeHistory.getStartDate(),
                subscribeHistory.getEndDate(),
                subscribeHistory.getAmount());
    }
}
