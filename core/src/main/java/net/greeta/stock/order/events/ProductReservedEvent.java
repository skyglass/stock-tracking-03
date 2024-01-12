package net.greeta.stock.order.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReservedEvent {
	private final String productId;
	private final int quantity;
	private final String orderId;
}
