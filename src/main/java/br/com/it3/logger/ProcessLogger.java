package br.com.it3.logger;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ProcessLogger implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("Processando transferencia do arquivo " + exchange.getIn().getHeader("CamelFileName"));
		
	}

}
