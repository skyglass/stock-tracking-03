package net.greeta.stock.product.data;

import jakarta.persistence.*;
import lombok.*;
import net.greeta.stock.model.OrderEvent;
import net.greeta.stock.model.OrderEventResult;
import net.greeta.stock.order.model.OrderStatus;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_event_result")
public class OrderEventResultEntity {
  @Id
  @Column(unique = true, updatable = false, nullable = false)
  private String id;

  @Column(length = 128, nullable = false, updatable = false)
  private String orderId;

  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @Column(length = 1024, nullable = false)
  private String detail;

  @Column(updatable = false, nullable = false)
  private Instant startAt;

  @Column(updatable = false, nullable = false)
  private Instant completeAt;

  @PrePersist
  private void prePersist() {
    completeAt = Instant.now();
  }

  public OrderEventResultEntity(OrderEvent event, String detail, OrderStatus status) {
    this.id = event.getId();
    this.orderId = event.getOrderId();
    this.detail = detail;
    this.orderStatus = status;
    startAt = event.getStartAt();
  }
}
