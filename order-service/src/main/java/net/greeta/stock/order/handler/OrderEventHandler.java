package net.greeta.stock.order.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.order.model.EventResultDto;
import net.greeta.stock.model.OrderEventResult;
import net.greeta.stock.order.service.OrderService;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventHandler {
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private OrderService orderService;

  @KafkaListener(
      topics = "product-service.public.order_event_result",
      groupId = "product-service.public.order_event_result:consumer",
      concurrency = "1")
  void consume(String message, Consumer consumer) throws JsonProcessingException {
    OrderEventResult result = objectMapper.readValue(message, EventResultDto.class).getResult();
    log.info(result.toString());
    try {
      orderService.onOrderEventComplete(result);
      consumer.commitSync();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex.getMessage());
    }
  }
}
