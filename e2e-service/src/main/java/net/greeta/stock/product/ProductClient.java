package net.greeta.stock.product;

import net.greeta.stock.product.dto.CreateProductDto;
import net.greeta.stock.product.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product")
public interface ProductClient {

    @PostMapping("/")
    public String create(@RequestBody CreateProductDto product);

    @GetMapping("/{productId}")
    public ProductDto get(@PathVariable String productId);
}