package net.greeta.stock.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import net.greeta.stock.order.model.OrderStatus;

import java.time.Instant;

@Data
public class OrderEvent {

    private String id;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("product_id")
    private String productId;

    private Integer quantity;

    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    @JsonDeserialize(converter = MicroToInstantConverter.class)
    @JsonProperty("start_at")
    private Instant startAt;
}
