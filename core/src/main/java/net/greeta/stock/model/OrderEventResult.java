package net.greeta.stock.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.greeta.stock.order.model.OrderStatus;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderEventResult {

  @JsonProperty("order_id")
  private String orderId;

  private OrderStatus orderStatus;

  private String detail;

  @JsonProperty("start_at")
  @JsonDeserialize(converter = MicroToInstantConverter.class)
  private Instant startAt;

  @JsonProperty("complete_at")
  @JsonDeserialize(converter = MicroToInstantConverter.class)
  private Instant completeAt;
}
