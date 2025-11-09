package com.interviewbuddy.interviewbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class InterviewbuddyApplication {

	@GetMapping("/helloworld")
	public String HelloWorld(){
		return "Hello world";
	}
	public static void main(String[] args) {
		SpringApplication.run(InterviewbuddyApplication.class, args);
	}

}
