package br.com.it3.agent;

import org.apache.camel.spring.Main;

public class Agent {

	public static void main(String[] args) {
		
		Main main = new Main();
		main.setApplicationContextUri("file:conf/context.xml");
		main.enableHangupSupport();
		try {
			System.out.println("Iniciando agent. Use CTRL + C para finalizar.\n");
			main.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Finalizando agent");
		
	}
}
