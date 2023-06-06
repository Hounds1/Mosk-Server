package team.mosk.api.server.domain.subscribe.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SubscribePaymentRequest {

    @NotBlank(message = "결제키는 필수값 입니다.")
    private String paymentKey;

    @NotBlank(message = "주문번호는 필수값 입니다.")
    private String orderId;

    @NotNull(message = "가격은 필수값 입니다.")
    private Long amount;

    @NotNull(message = "기간은 필수값 입니다.")
    private Long period;
}
