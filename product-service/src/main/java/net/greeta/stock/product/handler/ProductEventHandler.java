package net.greeta.stock.product.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.product.data.OrderEventResultRepository;
import net.greeta.stock.product.model.EventDto;
import net.greeta.stock.product.service.ProductRetryableService;
import net.greeta.stock.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventHandler {

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private OrderEventResultRepository orderEventResultRepository;
  @Autowired
  private ThreadPoolTaskExecutor executor;
  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRetryableService productRetryableService;
  @Autowired
  private KafkaManager kafkaManager;

  @PostConstruct
  private void init() {
    executor.setCorePoolSize(1);
  }

  @KafkaListener(
      id = "order_event_consumer",
      topics = "order-service.public.order_event",
      groupId = "order-service.public.order_event:consumer",
      concurrency = "1")
  void consume(String message, Acknowledgment acknowledgment) {
    kafkaManager.pauseConsume("order_event_consumer");
    executor
        .submitListenable(
            () -> {
              try {
                process(message, acknowledgment);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            })
        .addCallback(
            (res) -> {
              kafkaManager.resumeConsumer("order_event_consumer");
            },
            ex -> {
              log.info(ex.getMessage());
              try {
                Thread.sleep(15000);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              kafkaManager.resumeConsumer("order_event_consumer");
              kafkaManager.restartConsumer("order_event_consumer");
            });
  }

  private void process(String message, Acknowledgment acknowledgment) {
    // read event
    EventDto eventDto;
    try {
      eventDto = objectMapper.readValue(message, EventDto.class);
    } catch (Exception e) {
      e.printStackTrace();
      log.info(e.getMessage());
      throw new RuntimeException("cannot deserialize order event: " + message);
    }
    log.info(eventDto.getEvent().toString());

    // check processed before
    if (orderEventResultRepository.existsById(eventDto.getEvent().getId())) {
      acknowledgment.acknowledge();
      log.info("duplicated event !");
      return;
    }

    try {
      productRetryableService.handleOrderEvent(eventDto.getEvent());
    } catch (Exception e) {
      e.printStackTrace();
      productService.handleFailure(eventDto, e.getMessage());
      acknowledgment.acknowledge();
      return;
    }

    // success
    productService.handleSuccess(eventDto);
    acknowledgment.acknowledge();
  }
}
