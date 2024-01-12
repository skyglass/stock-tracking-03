package net.greeta.stock.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "axon")
public interface AxonClient {

    @DeleteMapping("/purge-events?targetContext=default")
    public ResponseEntity<String> purgeEvents();
}