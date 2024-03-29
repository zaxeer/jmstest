package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Base64;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ResourceUtils;

import com.example.demo.bean.Student;

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
		MessageFactory mf = MessageFactory.newInstance();

		SOAPMessage soapMessage = mf.createMessage();
		/** Get SOAP part. */
		SOAPPart soapPart = soapMessage.getSOAPPart();
		/* Get SOAP envelope. */
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		/* Get SOAP body. */
		SOAPBody soapBody = soapEnvelope.getBody();
		SOAPHeader soapHeader = soapEnvelope.getHeader();
		/* Create a name object. with name space */
		/* http://www.sun.com/imq. */
//		Name name = soapEnvelope.createName("HelloWorld", "hw","http://www.sun.com/imq");
//		SOAPElement element = soapBody.addChildElement(name);
//		Name head = soapEnvelope.createName("HelloWorld", "hw","ffff");
//		SOAPElement elementHeader = soapHeader.addChildElement(head);
//		
//		/* Add another child element.*/
//		element.addTextNode( "Welcome to GlassFish Web Services." );
//		elementHeader.addTextNode("head");

		Student student = new Student();
		com.example.demo.bean.Class classy = new com.example.demo.bean.Class();
		
		classy.setClassName("fifth");
		classy.setClassTeacher("yahee");
		classy.setRoom((short)12);

		student.setAge(new BigDecimal(22));
		student.setName("ddd");
		student.setClassId((short) 23);
		File file = ResourceUtils.getFile("classpath:flower.png");
		byte[] dataPic = Files.readAllBytes(file.toPath());		
		student.setPicture(Base64.getEncoder().encode(dataPic));

		//SOAPElement element1 = soapBody.addChildElement("data");
		JAXBElement<Student> jaxbElement1 = new JAXBElement<Student>(new QName("bodyNS", "student"), Student.class,
				student);
		Marshaller marshaller = JAXBContext.newInstance(Student.class).createMarshaller();
		marshaller.setProperty("jaxb.fragment", Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
		marshaller.marshal(jaxbElement1, soapBody);
		//soapBody.addChildElement(element1);

		// Document document2 =
		// DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		//SOAPElement element = soapHeader.addChildElement("meta", "ns", "http://www.rrot.com");
		JAXBElement<com.example.demo.bean.Class> jaxbElement = new JAXBElement<com.example.demo.bean.Class>(new QName("headerNS", "class"), com.example.demo.bean.Class.class,
				classy);
		Marshaller marshaller2 = JAXBContext.newInstance(com.example.demo.bean.Class.class).createMarshaller();
		marshaller2.setProperty("jaxb.fragment", Boolean.TRUE);
		marshaller2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
		marshaller2.marshal(jaxbElement, soapHeader);
		//soapHeader.addChildElement(element);

		soapMessage.saveChanges();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapMessage.writeTo(out);
		String strMsg = new String(out.toByteArray());
		jmsTemplate.convertAndSend(destination, strMsg);

	}

}
