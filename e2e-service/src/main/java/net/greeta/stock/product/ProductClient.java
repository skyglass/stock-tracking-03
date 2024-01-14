package net.greeta.stock.product;

import jakarta.validation.Valid;
import net.greeta.stock.product.dto.AddStockDto;
import net.greeta.stock.product.dto.CreateProductDto;
import net.greeta.stock.product.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product")
public interface ProductClient {

    @PostMapping("/")
    public ProductDto create(@RequestBody CreateProductDto product);

    @GetMapping("/{productId}")
    public ProductDto get(@PathVariable String productId);

    @PutMapping(path = "/add-stock")
    public ProductDto addStock(@Valid @RequestBody AddStockDto addStock);
}