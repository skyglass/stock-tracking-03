package net.greeta.stock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.helper.RetryHelper;
import net.greeta.stock.order.OrderClient;
import net.greeta.stock.order.OrderTestHelper;
import net.greeta.stock.order.dto.OrderSummaryDto;
import net.greeta.stock.order.model.OrderStatus;
import net.greeta.stock.product.Product2Client;
import net.greeta.stock.product.ProductClient;
import net.greeta.stock.product.ProductTestHelper;
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
    private OrderClient orderClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private Product2Client product2Client;

    @Autowired
    private OrderTestHelper orderTestHelper;

    @Autowired
    private ProductTestHelper productTestHelper;

    @Test
    @SneakyThrows
    void createParallelOrdersThenStockIsZeroTest() {
        CreateProductDto createProduct = new CreateProductDto("test", BigDecimal.valueOf(10.00), 20);
        ProductDto product = productClient.create(createProduct);
        assertNotNull(product);
        assertNotNull(product.getProductId());
        String productId = product.getProductId();
        ProductDto result = RetryHelper.retry(() -> productClient.get(productId));
        assertEquals(20, result.getQuantity());

        // Start the clock
        long start = Instant.now().toEpochMilli();

        int numberOfOrders = 15;
        List<CompletableFuture<OrderSummaryDto>> createdOrders = new ArrayList<>();
        // Kick of multiple, asynchronous lookups
        for (int i = 0; i < numberOfOrders; i++) {
            CompletableFuture<OrderSummaryDto> orderSummary = orderTestHelper.createAsyncOrder(productId, 2, i);
            createdOrders.add(orderSummary);
        }

        int numberOfStockUpdates = 5;
        List<CompletableFuture<ProductDto>> addedStocks = new ArrayList<>();
        // Kick of multiple, asynchronous lookups
        for (int i = 0; i < numberOfStockUpdates; i++) {
            CompletableFuture<ProductDto> addStockResult = productTestHelper.addAsyncStock(productId, 2, i);
            addedStocks.add(addStockResult);
        }

        // Wait until they are all done
        CompletableFuture.allOf(createdOrders.toArray(new CompletableFuture[0])).join();
        CompletableFuture.allOf(addedStocks.toArray(new CompletableFuture[0])).join();

        for (CompletableFuture<OrderSummaryDto> orderFuture: createdOrders) {
            OrderSummaryDto order = orderFuture.get();
            assertNotNull(order.getOrderId());
            assertEquals(OrderStatus.CREATED, order.getOrderStatus());
            log.info("--> " + order.getOrderId());
            Boolean orderApproved =  RetryHelper.retry(() -> {
                var test = orderClient.getOrder(order.getOrderId());
                return test.getOrderStatus() == OrderStatus.APPROVED;
            });
            assertTrue(orderApproved);
        }

        for (CompletableFuture<ProductDto> addStockResultFuture: addedStocks) {
            ProductDto addStockResult = addStockResultFuture.get();
            assertNotNull(addStockResult);
            assertEquals(productId, addStockResult.getProductId());
            log.info("Add Stock --> " + addStockResult.getProductId());
        }

        log.info("Elapsed time: " + (Instant.now().toEpochMilli() - start));

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
        assertEquals(OrderStatus.CREATED, rejectedOrder.getOrderStatus());
        Boolean orderRejected =  RetryHelper.retry(() -> {
            var test = orderClient.getOrder(rejectedOrder.getOrderId());
            return test.getOrderStatus() == OrderStatus.REJECTED;
        });
        assertTrue(orderRejected);
    }


}

