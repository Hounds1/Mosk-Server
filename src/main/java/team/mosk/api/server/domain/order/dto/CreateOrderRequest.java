package team.mosk.api.server.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateOrderRequest {

    private Long productId;

    private List<Long> optionIds;

    private int quantity;

    @Builder
    public CreateOrderRequest(Long productId, List<Long> optionIds, int quantity) {
        this.productId = productId;
        this.optionIds = optionIds;
        this.quantity = quantity;
    }
}
