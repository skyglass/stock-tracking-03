package net.greeta.stock.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.order.Order2Client;
import net.greeta.stock.order.Order3Client;
import net.greeta.stock.order.OrderClient;
import net.greeta.stock.order.dto.OrderCreateDto;
import net.greeta.stock.order.dto.OrderSummaryDto;
import net.greeta.stock.product.dto.AddStockDto;
import net.greeta.stock.product.dto.ProductDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductTestHelper {

    private final ProductClient productClient;

    private final Product2Client product2Client;

    @Async
    public CompletableFuture<ProductDto> addAsyncStock(String productId, int quantity, int hash) {
        log.info("Add Stock with quantity {} for product {}", quantity, productId);
        return CompletableFuture.completedFuture(addStock(productId, quantity, hash));
    }

    public ProductDto addStock(String productId, int quantity, int hash) {
        log.info("Adding stock with quantity {} for product {}", quantity, productId);
        AddStockDto addStock = new AddStockDto(productId, quantity);
        return addStock(addStock, hash);
    }

    private ProductDto addStock(AddStockDto addStock, int hash) {
        if (hash % 2 == 0) {
            return productClient.addStock(addStock);
        } else {
            return product2Client.addStock(addStock);
        }
    }

}
