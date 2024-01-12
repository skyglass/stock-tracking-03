package net.greeta.stock.order.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.greeta.stock.order.dto.OrderCreateDto;
import net.greeta.stock.order.dto.OrderSummaryDto;
import net.greeta.stock.order.service.OrderService;
import net.greeta.stock.product.dto.ProductDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderCommandController {

	private final OrderService orderService;

	@PostMapping
	public OrderSummaryDto createOrder(@Valid @RequestBody OrderCreateDto order) {
		return orderService.createOrder(order);
	}

	@GetMapping(path = "/{orderId}")
	public OrderSummaryDto getOrder(@PathVariable String orderId) {
		return orderService.getOrder(orderId);
	}

}
