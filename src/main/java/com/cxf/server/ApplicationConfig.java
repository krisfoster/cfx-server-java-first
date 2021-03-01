package com.cxf.server;

import com.cxf.server.service.QuoteReporter;
import com.cxf.server.service.StockQuoteReporter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.clustering.FailoverFeature;
import org.apache.cxf.clustering.RetryStrategy;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

	// assumes the current class is called MyLogger
	private final static Logger LOGGER = Logger.getLogger(ApplicationConfig.class.getName());

	@Bean
	public Endpoint endpoint(Bus bus) {
		LOGGER.log(Level.INFO, "ENDPOINT Creating Server ednpoint object");
		EndpointImpl endpoint =
			new EndpointImpl(bus,new StockQuoteReporter());
		endpoint.publish("/stockQuote");
		LOGGER.log(Level.INFO, "End Creating server.............");
		return endpoint;
	}

	@Bean("stockQuoteSoapClient")
	public QuoteReporter stockQuoteSoapClient(Bus bus){
		LOGGER.log(Level.INFO, "Creating client.............");
		final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setBus(bus);
		factory.setServiceClass(QuoteReporter.class);
		factory.setAddress("http://localhost:8080/cxf/ws/stockQuote");
		final List<Feature> features = factory.getFeatures();
		features.add(getFailoverFeature());
		LOGGER.log(Level.INFO, "End Creating client.............");
		return (QuoteReporter) factory.create();
	}

	private Feature getFailoverFeature() {
		RetryStrategy retryStrategy = new RetryStrategy();
		retryStrategy.setMaxNumberOfRetries(1);
		retryStrategy.setDelayBetweenRetries(1000);
		List<String> addressList = new LinkedList();
		addressList.add("http://localhost:8080/cxf/ws/stockQuote");
		retryStrategy.setAlternateAddresses(addressList);
		FailoverFeature failoverFeature = new FailoverFeature();
		failoverFeature.setStrategy(retryStrategy);
		return failoverFeature;
	}
}