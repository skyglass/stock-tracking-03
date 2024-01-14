package net.greeta.stock.product.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.greeta.stock.product.dto.AddStockDto;
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

	@PutMapping(path = "/add-stock")
	public ProductDto addStock(@Valid @RequestBody AddStockDto addStock) {
		return productService.addStock(addStock);
	}

	@GetMapping(path = "/{productId}")
	public ProductDto getProduct(@PathVariable String productId) {
		return productService.getProduct(productId);
	}

}
