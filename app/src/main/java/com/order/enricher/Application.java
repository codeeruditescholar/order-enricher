package com.order.enricher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.order.enricher"})
public class Application {

  public static void main(final String[] args) {
    SpringApplication.run(Application.class);
  }
}
