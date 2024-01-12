package net.greeta.stock.order.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.greeta.stock.model.OrderEventResult;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonDeserialize(using = EventResultDtoDeserializer.class)
public class EventResultDto {
  OrderEventResult result;
}
