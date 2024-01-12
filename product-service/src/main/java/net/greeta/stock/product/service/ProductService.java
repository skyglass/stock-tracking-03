package net.greeta.stock.product.service;

import net.greeta.stock.model.OrderEvent;
import net.greeta.stock.product.dto.CreateProductDto;
import net.greeta.stock.product.dto.ProductDto;
import net.greeta.stock.product.model.EventDto;

public interface ProductService {
  void handleOrderEvent(OrderEvent orderEvent);

  ProductDto createProduct(CreateProductDto product);

  ProductDto addStock(String productId, Integer quantity);

  ProductDto getProduct(String productId);

  void handleSuccess(EventDto eventDto);

  void handleFailure(EventDto eventDto, String message);

}
