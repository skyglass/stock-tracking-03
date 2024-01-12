package net.greeta.stock.order;

import net.greeta.stock.order.dto.OrderCreateDto;
import net.greeta.stock.order.dto.OrderSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order2")
public interface Order2Client {

    @PostMapping("/")
    public OrderSummaryDto create(@RequestBody OrderCreateDto order);
}
