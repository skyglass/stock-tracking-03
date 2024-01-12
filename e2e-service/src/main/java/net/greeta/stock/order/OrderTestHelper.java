package net.greeta.stock.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.order.dto.OrderCreateDto;
import net.greeta.stock.order.dto.OrderSummaryDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderTestHelper {

    private final OrderClient orderClient;

    private final Order2Client order2Client;

    private final Order3Client order3Client;

    @Async
    public CompletableFuture<OrderSummaryDto> createAsyncOrder(String productId, int quantity, int hash) {
        log.info("Creating order with quantity {} for product {}", quantity, productId);
        OrderCreateDto order = new OrderCreateDto(productId, quantity);
        return CompletableFuture.completedFuture(createOrder(order, hash));
    }

    public OrderSummaryDto createOrder(String productId, int quantity) {
        log.info("Creating order with quantity {} for product {}", quantity, productId);
        OrderCreateDto order = new OrderCreateDto(productId, quantity);
        OrderSummaryDto orderSummary = orderClient.create(order);
        return orderSummary;
    }

    private OrderSummaryDto createOrder(OrderCreateDto order, int hash) {
        if (hash % 3 == 0) {
            return orderClient.create(order);
        } else if (hash % 3 == 1) {
            return order2Client.create(order);
        } else {
            return order3Client.create(order);
        }
    }

}
