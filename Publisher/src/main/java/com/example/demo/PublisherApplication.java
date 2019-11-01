package com.example.demo;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

import com.example.demo.bean.Root;

@SpringBootApplication
public class PublisherApplication implements CommandLineRunner {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Value("${local.qname}")
	private String destination;
	
	public static void main(String[] args) {
		SpringApplication.run(PublisherApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		Root root = new Root();
		root.setCorelId(UUID.randomUUID().toString());
		root.setData("data");
		root.setFileName("test");
		root.setReceiver("EC");
		root.setSender("DE");
		root.setUserId("TETS");
		jmsTemplate.convertAndSend(destination, root);
	}

}
