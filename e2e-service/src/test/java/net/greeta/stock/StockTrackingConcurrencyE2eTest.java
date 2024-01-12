package net.greeta.stock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.helper.RetryHelper;
import net.greeta.stock.order.OrderTestHelper;
import net.greeta.stock.order.dto.OrderSummaryDto;
import net.greeta.stock.order.model.OrderStatus;
import net.greeta.stock.product.Product2Client;
import net.greeta.stock.product.ProductClient;
import net.greeta.stock.product.dto.CreateProductDto;
import net.greeta.stock.product.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = {
        "classpath:application.yml"
})
@Slf4j
public class StockTrackingConcurrencyE2eTest extends E2eTest {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private Product2Client product2Client;

    @Autowired
    private OrderTestHelper orderTestHelper;

    @Test
    @SneakyThrows
    void createParallelOrdersThenStockIsZeroTest() {
        CreateProductDto product = new CreateProductDto("test", BigDecimal.valueOf(10.00), 20);
        String productId = productClient.create(product);
        assertNotNull(productId);
        ProductDto result = RetryHelper.retry(() -> productClient.get(productId));
        assertEquals(20, result.getQuantity());

        // Start the clock
        long start = Instant.now().toEpochMilli();

        int numberOfOrders = 10;
        List<CompletableFuture<OrderSummaryDto>> createdOrders = new ArrayList<>();
        // Kick of multiple, asynchronous lookups
        for (int i = 0; i < numberOfOrders; i++) {
            CompletableFuture<OrderSummaryDto> orderSummary = orderTestHelper.createAsyncOrder(productId, 2, i);
            createdOrders.add(orderSummary);
        }

        // Wait until they are all done
        CompletableFuture.allOf(createdOrders.toArray(new CompletableFuture[0])).join();

        log.info("Elapsed time: " + (Instant.now().toEpochMilli() - start));
        for (CompletableFuture<OrderSummaryDto> orderSummary: createdOrders) {
            assertNotNull(orderSummary.get().getOrderId());
            assertEquals(OrderStatus.APPROVED, orderSummary.get().getOrderStatus());
            log.info("--> " + orderSummary.get().getOrderId());
        }

        AtomicInteger productClientHash = new AtomicInteger(0);
        Boolean quantityReducedToZero = RetryHelper.retry(() -> {
            var test = productClientHash.getAndIncrement() % 2 == 0
                    ? productClient.get(productId)
                    : product2Client.get(productId);
            return test.getQuantity() == 0;
        });
        assertTrue(quantityReducedToZero);

        //Check that the next order is rejected because the stock is zero
        OrderSummaryDto rejectedOrder = orderTestHelper.createOrder(productId, 1);
        assertNotNull(rejectedOrder.getOrderId());
        assertEquals(OrderStatus.REJECTED, rejectedOrder.getOrderStatus());
    }


}

