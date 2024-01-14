package net.greeta.stock;

import lombok.SneakyThrows;
import net.greeta.stock.order.OrderTestDataService;
import net.greeta.stock.product.ProductTestDataService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class E2eTest {

    @Autowired
    private OrderTestDataService orderTestDataService;

    @Autowired
    private ProductTestDataService productTestDataService;

    @BeforeEach
    @SneakyThrows
    void cleanup() {
        orderTestDataService.resetDatabase();
        productTestDataService.resetDatabase();
    }
}
