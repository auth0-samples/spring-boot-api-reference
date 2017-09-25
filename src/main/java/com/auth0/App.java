package com.auth0;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@ComponentScan({"com.auth0", "com.auth0.controller", "com.auth0.service"})
@EnableAutoConfiguration
@PropertySources({
		@PropertySource("classpath:application.properties"),
		@PropertySource("classpath:auth0.properties")
})
public class App {

	public static void main(String[] args) {

   System.setProperty("http.proxyHost", "127.0.0.1");
   System.setProperty("http.proxyPort", "8889");

    SpringApplication.run(App.class, args);

	}
}
