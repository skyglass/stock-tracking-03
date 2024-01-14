package net.greeta.stock.product.service;

import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.model.OrderEvent;
import net.greeta.stock.order.model.OrderStatus;
import net.greeta.stock.product.data.OrderEventResultEntity;
import net.greeta.stock.product.data.OrderEventResultRepository;
import net.greeta.stock.product.data.ProductEntity;
import net.greeta.stock.product.data.ProductRepository;
import net.greeta.stock.product.dto.AddStockDto;
import net.greeta.stock.product.dto.CreateProductDto;
import net.greeta.stock.product.dto.ProductDto;
import net.greeta.stock.product.exception.InsufficientStockException;
import net.greeta.stock.product.model.EventDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

  @Autowired
  ProductRepository productRepository;

  @Autowired
  OrderEventResultRepository eventResultRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ProductDto createProduct(CreateProductDto product) {
    if(product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Price cannot be less or equal than zero");
    }

    if(StringUtils.isBlank(product.getTitle())) {
      throw new IllegalArgumentException("Title cannot be empty");
    }


    ProductEntity productEntity = ProductEntity.builder()
            .title(product.getTitle())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .build();

    productEntity = productRepository.saveAndFlush(productEntity);

    return new ProductDto(productEntity.getId().toString(), productEntity.getTitle(),
            productEntity.getPrice(), productEntity.getQuantity());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ProductDto addStock(AddStockDto addStock) {
    String productId = addStock.getProductId();
    Integer quantity = addStock.getQuantity();
    var productEntity = productRepository.findById(UUID.fromString(productId)).orElse(null);
    if (productEntity == null) {
      throw new IllegalArgumentException(String.format("Product with id = %s not found", productId));
    }

    productEntity.setQuantity(productEntity.getQuantity() + quantity);

    productEntity = productRepository.saveAndFlush(productEntity);

    return new ProductDto(productEntity.getId().toString(), productEntity.getTitle(),
            productEntity.getPrice(), productEntity.getQuantity());
  }

  @Override
  public ProductDto getProduct(String productId) {
    var productEntity = productRepository.findById(UUID.fromString(productId)).orElse(null);
    if (productEntity == null) {
      throw new IllegalArgumentException(String.format("Product with id = %s not found", productId));
    }
    return new ProductDto(productEntity.getId().toString(), productEntity.getTitle(),
            productEntity.getPrice(), productEntity.getQuantity());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void handleOrderEvent(OrderEvent orderEvent) {
    var productEntity = productRepository.findById(UUID.fromString(orderEvent.getProductId())).orElse(null);
    if (productEntity == null) {
      throw new IllegalArgumentException(String.format("Product with id = %s not found", orderEvent.getProductId()));
    }

    log.debug("ProductReservedEvent: Current product quantity " + productEntity.getQuantity());

    if (productEntity.getQuantity() < orderEvent.getQuantity()) {
      throw new InsufficientStockException(productEntity.getTitle());
    }

    productEntity.setQuantity(productEntity.getQuantity() - orderEvent.getQuantity());

    productRepository.save(productEntity);

    log.debug("ProductReservedEvent: New product quantity " + productEntity.getQuantity());

    log.info("ProductReservedEvent is called for productId:" + orderEvent.getProductId() +
            " and orderId: " + orderEvent.getOrderId());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void handleSuccess(EventDto eventDto) {
    OrderEvent event = eventDto.getEvent();
    OrderEventResultEntity result = new OrderEventResultEntity(event, "", OrderStatus.APPROVED);
    eventResultRepository.saveAndFlush(result);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void handleFailure(EventDto eventDto, String message) {
    OrderEvent event = eventDto.getEvent();
    OrderEventResultEntity result = new OrderEventResultEntity(event, "", OrderStatus.REJECTED);
    eventResultRepository.saveAndFlush(result);
  }

}
