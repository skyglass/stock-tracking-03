package net.greeta.stock.product;

import net.greeta.stock.product.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product2")
public interface Product2Client {

    @GetMapping("/{productId}")
    public ProductDto get(@PathVariable String productId);
}
