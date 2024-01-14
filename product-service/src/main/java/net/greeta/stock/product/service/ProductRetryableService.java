package net.greeta.stock.product.service;

import net.greeta.stock.model.OrderEvent;
import net.greeta.stock.product.exception.InsufficientStockException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class ProductRetryableService {

    @Autowired
    ProductService productService;

    @Retryable(retryFor = {ConcurrencyFailureException.class,
            StaleStateException.class,
            InsufficientStockException.class},
            maxAttempts = 5, backoff = @Backoff(delay = 2000))
    public void handleOrderEvent(OrderEvent orderEvent) {
        productService.handleOrderEvent(orderEvent);
    }
}
