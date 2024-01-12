package net.greeta.stock;

import lombok.SneakyThrows;
import net.greeta.stock.client.AxonClient;
import net.greeta.stock.order.OrderTestDataService;
import net.greeta.stock.product.ProductTestDataService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class E2eTest {

    @Autowired
    private AxonClient axonClient;

    @Autowired
    private OrderTestDataService orderTestDataService;

    @Autowired
    private ProductTestDataService productTestDataService;

    @BeforeEach
    @SneakyThrows
    void cleanup() {
        var result = axonClient.purgeEvents();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        orderTestDataService.resetDatabase();
        productTestDataService.resetDatabase();
        TimeUnit.MILLISECONDS.sleep(Duration.ofSeconds(3).toMillis());
    }
}
