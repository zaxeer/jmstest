package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Base64;

import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;

import com.example.demo.bean.ObjectFactory;
import com.example.demo.bean.Student;

@SpringBootApplication
public class ReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceiverApplication.class, args);
	}

	@JmsListener(destination = "${local.qname}")
	public void getData(TextMessage txtMessage) throws Exception {

		SOAPMessage message = MessageFactory.newInstance().createMessage(null,
				new ByteArrayInputStream(txtMessage.getText().getBytes()));

		JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		JAXBElement<Student> root = jaxbUnmarshaller.unmarshal(message.getSOAPBody().extractContentAsDocument(),
				Student.class);
		System.out.println(root.getValue().getName());
		FileUtils.writeByteArrayToFile(new File("I:\\temp\\img.png"),
				Base64.getDecoder().decode(root.getValue().getPicture()));

		JAXBContext jaxbContext2 = JAXBContext.newInstance(com.example.demo.bean.Class.class);
		Unmarshaller jaxbUnmarshaller2 = jaxbContext2.createUnmarshaller();

		JAXBElement<com.example.demo.bean.Class> head = jaxbUnmarshaller2
				.unmarshal(message.getSOAPHeader().getFirstChild(), com.example.demo.bean.Class.class);
		
		System.out.println(head.getValue().getClassName());
		System.out.println(head.getValue().getClassTeacher());
		System.out.println(head.getValue().getRoom());
		
	}

}
