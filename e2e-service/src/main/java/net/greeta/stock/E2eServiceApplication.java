package net.greeta.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class E2eServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(E2eServiceApplication.class, args);
    }
}
