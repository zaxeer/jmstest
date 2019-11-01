package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;

import com.example.demo.bean.Root;

@SpringBootApplication
public class ReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceiverApplication.class, args);
	}
	
	@JmsListener(destination = "${local.qname}")
	public void getData(Root destination) {
		System.out.println(destination);
	}

}
