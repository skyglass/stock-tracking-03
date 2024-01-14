package net.greeta.stock.product;

import jakarta.validation.Valid;
import net.greeta.stock.product.dto.AddStockDto;
import net.greeta.stock.product.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product2")
public interface Product2Client {

    @GetMapping("/{productId}")
    public ProductDto get(@PathVariable String productId);

    @PutMapping(path = "/add-stock")
    public ProductDto addStock(@Valid @RequestBody AddStockDto addStock);
}
