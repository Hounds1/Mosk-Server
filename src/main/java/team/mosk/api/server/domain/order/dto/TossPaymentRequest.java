package team.mosk.api.server.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.order.model.order.Order;
import team.mosk.api.server.domain.subscribe.dto.SubscribePaymentRequest;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class TossPaymentRequest {

  private String paymentKey; //결제 키값 최대 200자
  private String orderId; // 주문 ID입니다. 충분히 무작위한 값을 직접 생성해서 사용
  private int amount; // 결제할 금액

  public static TossPaymentRequest of(Order order, String paymentKey) {
    String orderId = UUID.randomUUID()
            .toString()
            .replaceAll("-", "")
            .substring(0, 16);

    return TossPaymentRequest.builder()
            .paymentKey(paymentKey)
            .orderId(orderId)
            .amount(order.getTotalPrice())
            .build();
  }

  public static TossPaymentRequest of(final SubscribePaymentRequest request) {
    String orderId = UUID.randomUUID()
            .toString()
            .replaceAll("-", "")
            .substring(0, 16);

    return TossPaymentRequest.builder()
            .paymentKey(request.getPaymentKey())
            .orderId(orderId)
            .amount(request.getAmount().intValue())
            .build();
  }

  @Builder
  public TossPaymentRequest(String paymentKey, String orderId, int amount) {
    this.paymentKey = paymentKey;
    this.orderId = orderId;
    this.amount = amount;
  }

}
