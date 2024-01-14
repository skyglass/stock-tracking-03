package net.greeta.stock.order.data;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;
import net.greeta.stock.order.model.OrderStatus;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_event", indexes = {@Index(columnList = "orderId"), @Index(columnList = "startAt")})
public class OrderEventEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @Column(length = 128, nullable = false, updatable = false)
    String orderId;

    @Column(length = 128, nullable = false, updatable = false)
    String productId;

    @Column(nullable = false, updatable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private Instant startAt;

    @PrePersist
    private void prePersist() {
        startAt = Instant.now();
    }

    public OrderEventEntity(String orderId, String productId, int quantity, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
    }
}
