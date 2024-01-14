package net.greeta.stock;

import net.greeta.stock.helper.RetryHelper;
import net.greeta.stock.order.OrderClient;
import net.greeta.stock.order.dto.OrderCreateDto;
import net.greeta.stock.order.dto.OrderSummaryDto;
import net.greeta.stock.order.model.OrderStatus;
import net.greeta.stock.product.ProductClient;
import net.greeta.stock.product.dto.CreateProductDto;
import net.greeta.stock.product.dto.ProductDto;
import net.greeta.stock.helper.CalculationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

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
        CreateProductDto createProduct = new CreateProductDto("test", BigDecimal.valueOf(10.00), 5);
        ProductDto product = productClient.create(createProduct);
        assertNotNull(product);
        assertNotNull(product.getProductId());
        String productId = product.getProductId();
        ProductDto result = RetryHelper.retry(() -> productClient.get(productId));
        assertEquals(productId, result.getProductId());
        assertEquals("test", result.getTitle());
        assertTrue(CalculationHelper.equalsToScale2(BigDecimal.valueOf(10.00), result.getPrice()));
        assertEquals(5, result.getQuantity());
    }

    @Test
    void createOrderThenProductQuantityReducedTest() {
        CreateProductDto createProduct = new CreateProductDto("test", BigDecimal.valueOf(10.00), 5);
        ProductDto product = productClient.create(createProduct);
        assertNotNull(product);
        assertNotNull(product.getProductId());
        String productId = product.getProductId();
        ProductDto result = RetryHelper.retry(() -> productClient.get(productId));
        assertEquals(5, result.getQuantity());

        OrderCreateDto order = new OrderCreateDto(productId, 2);
        OrderSummaryDto orderSummary = orderClient.create(order);
        assertNotNull(orderSummary.getOrderId());
        assertEquals(OrderStatus.CREATED, orderSummary.getOrderStatus());

        Boolean orderApproved =  RetryHelper.retry(() -> {
            var test = orderClient.getOrder(orderSummary.getOrderId());
            return test.getOrderStatus() == OrderStatus.APPROVED;
        });
        assertTrue(orderApproved);

        Boolean quantityReducedTo3 = RetryHelper.retry(() -> {
            var test = productClient.get(productId);
            return test.getQuantity() == 3;
        });
        assertTrue(quantityReducedTo3);
    }


}
