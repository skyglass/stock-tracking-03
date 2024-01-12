package net.greeta.stock.product.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product", indexes = {@Index(columnList = "createdAt DESC")})
public class ProductEntity implements Serializable {

	@Version
	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
	private Instant version;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique = true, updatable = false, nullable = false)
	private UUID id;
	
	@Column(nullable = false, unique=true)
	private String title;

	@Column(nullable = false)
	private BigDecimal price;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private Instant createdAt;

	@PrePersist
	private void prePersist() {
		createdAt = Instant.now();
	}

}
