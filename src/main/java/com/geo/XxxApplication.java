package com.geo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class XxxApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		ApplicationContext apx = SpringApplication.run(XxxApplication.class, args);
	}

}
