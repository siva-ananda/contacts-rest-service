package be.steformations.sivananda.service.contacts.rest.server;

import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import be.steformations.sivananda.service.contacts.rest.TagRestService;

public class ContactRestServer {
	
	public static void main(String[] args) throws Exception{
		
		URI uri = new URI("http://localhost:8080/contacts-rest/rs");
		
		ResourceConfig config = new ResourceConfig();
		config.register(TagRestService.class);
		
		JdkHttpServerFactory.createHttpServer(uri, config);
		
		System.out.println("Contact Rest Service ready");
		
	}

}
