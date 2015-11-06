package br.com.it3.logger;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.io.FileUtils;

import br.com.it3.websocket.WebsocketClientEndpoint;

public class ProcessLogger implements Processor {
	
	static URI endpoint;
	static WebsocketClientEndpoint ws;
	
	static {
		try {
			endpoint = new URI("ws://localhost:8080/cloud-edi-web/message");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		ws = new WebsocketClientEndpoint(endpoint);
	}
	 

	@Override
	public void process(Exchange exchange) {
		try {
			System.out.println("[onprocess] Processando transferencia do arquivo " + exchange.getIn().getHeader("CamelFileNameProduced"));
			processMessage(exchange);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private void processMessage(Exchange exchange) {
		Message in = exchange.getIn();
		
		File file = null;
		
		if (in != null) {
			file = new File(in.getHeader("CamelFileAbsolutePath").toString());
			System.out.println("[" + exchange.getFromRouteId() + "] file in=" + file.getAbsolutePath());
		}

		Object hash = exchange.getIn().getHeader("crc");
			
		if (file != null && file.exists()) {
			try {
				long crc32 = FileUtils.checksumCRC32(file);
				if (hash == null) {
					exchange.getIn().setHeader("hash", crc32);
				}
				sendMessageLog(exchange, crc32);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (hash == null) {
				hash = exchange.getIn().getHeader("hash");
			}
			sendMessageLog(exchange, (long)hash);
		}
	}
	
	private void sendMessageLog(Exchange exchange, long crc32) {
		Message in = exchange.getIn();
		JsonObject obj = Json.createObjectBuilder()
				.add("FileName", in.getHeader("CamelFileName").toString())
				.add("FileLength", in.getHeader("CamelFileLength").toString())
				.add("FileLastModified", in.getHeader("CamelFileLastModified").toString())
				.add("RouteFromId", exchange.getFromRouteId())
				.add("crc32", crc32)
				.build();
				
		ws.sendMessage(obj.toString());
	}

}
