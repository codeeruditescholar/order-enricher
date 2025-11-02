package com.order.enricher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.order.enricher"})
@EnableJpaRepositories(basePackages = "com.order.enricher.repository")
@EntityScan(basePackages = "com.order.enricher.domain")
public class Application {

  public static void main(final String[] args) {
    SpringApplication.run(Application.class);
  }
}
