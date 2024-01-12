package net.greeta.stock.product.handler;

import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaManager {
  private final KafkaListenerEndpointRegistry registry;

  public KafkaManager(KafkaListenerEndpointRegistry registry) {
    this.registry = registry;
  }

  public void pauseConsume(String containerId) {
    getContainer(containerId).ifPresent(MessageListenerContainer::pause);
  }

  public void resumeConsumer(String containerId) {
    getContainer(containerId).ifPresent(MessageListenerContainer::resume);
  }

  public void restartConsumer(String containerId) {
    getContainer(containerId).ifPresent(MessageListenerContainer::stop);
    getContainer(containerId).ifPresent(MessageListenerContainer::start);
  }

  private Optional<MessageListenerContainer> getContainer(String containerId) {
    return Optional.ofNullable(registry.getListenerContainer(containerId));
  }
}
