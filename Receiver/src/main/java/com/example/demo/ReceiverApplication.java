package com.example.demo;

import java.io.ByteArrayInputStream;

import javax.jms.Message;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import org.apache.activemq.util.ByteSequence;
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
	public void getData(TextMessage txtMessage) throws Exception {

		System.out.println(txtMessage.getText());
		SOAPMessage message = MessageFactory.newInstance().createMessage(null,
				new ByteArrayInputStream(txtMessage.getText().getBytes()));

		JAXBContext jaxbContext = JAXBContext.newInstance(Root.Student.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Root.Student updateMyDetails = (Root.Student) jaxbUnmarshaller
				.unmarshal(message.getSOAPBody().extractContentAsDocument());
		System.out.println(updateMyDetails);
	}

}
