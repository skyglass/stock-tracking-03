package net.greeta.stock.product.service;

import net.greeta.stock.model.OrderEvent;
import net.greeta.stock.product.dto.AddStockDto;
import net.greeta.stock.product.dto.ProductDto;
import net.greeta.stock.product.exception.InsufficientStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class ProductRetryableService {

    @Autowired
    ProductService productService;

    @Retryable(retryFor = {ConcurrencyFailureException.class,
            InsufficientStockException.class},
            maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public void handleOrderEvent(OrderEvent orderEvent) {
        productService.handleOrderEvent(orderEvent);
    }

    @Retryable(retryFor = {ConcurrencyFailureException.class,
            InsufficientStockException.class},
            maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public ProductDto addStock(AddStockDto addStock) {
        return productService.addStock(addStock);
    }
}
