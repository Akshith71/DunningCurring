package com.example.DCMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DcmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DcmsApplication.class, args);
	}

}
