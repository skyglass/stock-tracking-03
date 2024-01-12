package net.greeta.stock.order.service;

import net.greeta.stock.order.data.OrderEntity;
import net.greeta.stock.order.data.OrderRepository;
import net.greeta.stock.order.dto.OrderCreateDto;
import net.greeta.stock.order.dto.OrderSummaryDto;
import net.greeta.stock.model.OrderEventResult;
import net.greeta.stock.order.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
  @Autowired
  OrderRepository orderRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public OrderSummaryDto createOrder(OrderCreateDto order) {
    OrderEntity orderEntity = OrderEntity.builder()
            .productId(order.getProductId()).quantity(order.getQuantity())
            .orderStatus(OrderStatus.CREATED).build();

    orderEntity = orderRepository.saveAndFlush(orderEntity);

    return new OrderSummaryDto(orderEntity.getId().toString(),
            orderEntity.getOrderStatus(), "");
  }

  @Override
  public OrderSummaryDto getOrder(String orderId) {
    var orderEntity = orderRepository.findById(UUID.fromString(orderId)).orElse(null);
    if (orderEntity == null) {
      throw new IllegalArgumentException(String.format("Order with id = %s not found", orderId));
    }
    return new OrderSummaryDto(orderEntity.getId().toString(),
            orderEntity.getOrderStatus(), "");
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @Retryable(retryFor = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 100))
  public void onOrderEventComplete(OrderEventResult result) throws Exception {
    OrderEntity order = orderRepository.findById(UUID.fromString(result.getOrderId())).orElse(null);
    order.setOrderStatus(result.getOrderStatus());
    orderRepository.save(order);
  }

}
