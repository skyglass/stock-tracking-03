package net.greeta.stock.product.exception;

public class InsufficientStockException extends IllegalArgumentException {

    public InsufficientStockException(String productTitle) {
        super(String.format("Insufficient number of items in stock for product %s", productTitle));
    }
}
