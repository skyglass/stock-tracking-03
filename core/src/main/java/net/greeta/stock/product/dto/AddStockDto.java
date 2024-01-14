package net.greeta.stock.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AddStockDto {

    @NotBlank(message="Product title is a required field")
    private String productId;

    @NotNull(message="Quantity is a required field")
    @Min(value=1, message="Quantity cannot be lower than 1")
    private Integer quantity;

}
