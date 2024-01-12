package net.greeta.stock.order.dto;

import lombok.Value;
import net.greeta.stock.order.model.OrderStatus;

@Value
public class OrderSummaryDto {

	private final String orderId;
	private final OrderStatus orderStatus;
	private final String message;
	
}
