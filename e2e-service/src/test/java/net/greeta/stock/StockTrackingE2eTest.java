package net.greeta.stock;

import lombok.SneakyThrows;
import net.greeta.stock.client.AxonClient;
import net.greeta.stock.helper.RetryHelper;
import net.greeta.stock.order.OrderClient;
import net.greeta.stock.order.OrderTestDataService;
import net.greeta.stock.order.dto.OrderCreateDto;
import net.greeta.stock.order.dto.OrderSummaryDto;
import net.greeta.stock.order.model.OrderStatus;
import net.greeta.stock.product.ProductClient;
import net.greeta.stock.product.ProductTestDataService;
import net.greeta.stock.product.dto.CreateProductDto;
import net.greeta.stock.product.dto.ProductDto;
import net.greeta.stock.helper.CalculationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = {
        "classpath:application.yml"
})
public class StockTrackingE2eTest extends E2eTest {

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private ProductClient productClient;

    @Test
    void createProductTest() {
        CreateProductDto product = new CreateProductDto("test", BigDecimal.valueOf(10.00), 5);
        String productId = productClient.create(product);
        assertNotNull(productId);
        ProductDto result = RetryHelper.retry(() -> productClient.get(productId));
        assertEquals(productId, result.getProductId());
        assertEquals("test", result.getTitle());
        assertTrue(CalculationHelper.equalsToScale2(BigDecimal.valueOf(10.00), result.getPrice()));
        assertEquals(5, result.getQuantity());
    }

    @Test
    void createOrderThenProductQuantityReducedTest() {
        CreateProductDto product = new CreateProductDto("test", BigDecimal.valueOf(10.00), 5);
        String productId = productClient.create(product);
        assertNotNull(productId);
        ProductDto result = RetryHelper.retry(() -> productClient.get(productId));
        assertEquals(5, result.getQuantity());

        OrderCreateDto order = new OrderCreateDto(productId, 2);
        OrderSummaryDto orderSummary = orderClient.create(order);
        assertNotNull(orderSummary.getOrderId());
        assertEquals(OrderStatus.APPROVED, orderSummary.getOrderStatus());

        Boolean quantityReducedTo3 = RetryHelper.retry(() -> {
            var test = productClient.get(productId);
            return test.getQuantity() == 3;
        });
        assertTrue(quantityReducedTo3);
    }


}
