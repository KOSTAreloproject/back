package com.my.relo;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class ReloApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReloApplication.class, args);
	}

}
