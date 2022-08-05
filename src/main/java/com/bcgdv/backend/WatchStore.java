package com.bcgdv.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bcgdv.backend.model.Discount;
import com.bcgdv.backend.model.Discount.DiscountType;
import com.bcgdv.backend.model.Watch;
import com.bcgdv.backend.service.WatchService;

@SpringBootApplication
public class WatchStore {

  public static void main(String[] args) {
	  SpringApplication.run(WatchStore.class, args);
  }

  // Populate the initial watch products into the DB for testing
  // In a real world scenario this would be better handled with a SQL script located under resources
  @Bean
  CommandLineRunner runner(WatchService watchService) {
    Discount threeFor200 = new Discount(DiscountType.MULTI_BUY, "3 for 200", new Integer(3), new Double(200.00));
    Discount twoFor120 = new Discount(DiscountType.MULTI_BUY, "2 for 120", new Integer(2), new Double(120.00));
    return args -> {
      watchService.save(new Watch(1L, "Rolex", 100.00, threeFor200));
      watchService.save(new Watch(2L, "Michael Kors", 80.00, twoFor120));
      watchService.save(new Watch(3L, "Swatch", 50.00 ));
      watchService.save(new Watch(4L, "Casio", 30.00));
    };
  }
}
