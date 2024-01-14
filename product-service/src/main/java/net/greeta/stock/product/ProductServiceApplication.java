package net.greeta.stock.product;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

@EnableDiscoveryClient
@SpringBootApplication
@EnableRetry
public class ProductServiceApplication {

	private static final CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		SpringApplication.run(ProductServiceApplication.class, args);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> latch.countDown()));
		try {
			latch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
