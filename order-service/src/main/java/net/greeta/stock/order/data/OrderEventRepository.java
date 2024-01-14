package net.greeta.stock.order.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderEventRepository extends JpaRepository<OrderEventEntity, UUID> {
  Optional<OrderEventEntity> findById(UUID id);
}
