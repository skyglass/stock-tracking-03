package net.greeta.stock.order.service;

import net.greeta.stock.order.dto.OrderCreateDto;
import net.greeta.stock.order.dto.OrderSummaryDto;
import net.greeta.stock.model.OrderEventResult;

public interface OrderService {
  OrderSummaryDto createOrder(OrderCreateDto order);

  OrderSummaryDto getOrder(String orderId);

  void onOrderEventComplete(OrderEventResult eventResult) throws Exception;
}
