package net.greeta.stock.product.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.greeta.stock.product.dto.CreateProductDto;
import net.greeta.stock.product.dto.ProductDto;
import net.greeta.stock.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ProductDto createProduct(@Valid @RequestBody CreateProductDto product) {
		return productService.createProduct(product);
	}

	@PutMapping(path = "/add-stock/{productId}/{quantity}")
	public ProductDto addStock(@PathVariable String productId, @PathVariable Integer quantity) {
		return productService.addStock(productId, quantity);
	}

	@GetMapping(path = "/{productId}")
	public ProductDto getProduct(@PathVariable String productId) {
		return productService.getProduct(productId);
	}

}
