package br.com.it3.agent;

import org.apache.camel.util.IOHelper;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	
	private static AbstractApplicationContext context;


	public static void main(String[] args) {
		System.out.println("Executando agent cloud-edi");
		
		context = new ClassPathXmlApplicationContext("META-INF/spring/context.xml");
		context.start();
		sleep();
		IOHelper.close(context);
		
		
	}
	
	
	private static void sleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
