package com.cxf.server.config;

import com.cxf.server.service.QuoteReporter;
import com.cxf.server.service.StockQuoteReporter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.clustering.FailoverFeature;
import org.apache.cxf.clustering.RetryStrategy;
import org.apache.cxf.common.spi.GeneratedNamespaceClassLoader;
import org.apache.cxf.common.spi.NamespaceClassCreator;
import org.apache.cxf.endpoint.dynamic.ExceptionClassCreator;
import org.apache.cxf.endpoint.dynamic.ExceptionClassLoader;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxb.FactoryClassCreator;
import org.apache.cxf.jaxb.FactoryClassLoader;
import org.apache.cxf.jaxb.WrapperHelperClassLoader;
import org.apache.cxf.jaxb.WrapperHelperCreator;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.spi.WrapperClassCreator;
import org.apache.cxf.jaxws.spi.WrapperClassLoader;
import org.apache.cxf.wsdl.ExtensionClassCreator;
import org.apache.cxf.wsdl.ExtensionClassLoader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration

public class ApplicationConfigOld {

	// assumes the current class is called MyLogger
	private final static Logger LOGGER = Logger.getLogger(ApplicationConfigOld.class.getName());

	@Bean
	@Profile("!capture")
	public Bus bus(Bus cxfBus){
		LOGGER.log(Level.INFO,"Setting up Bus to create native image");
		final Bus bus = cxfBus;
		bus.setId("krd_ID");
		BusFactory.setDefaultBus(bus);
		bus.setExtension(new WrapperHelperClassLoader(cxfBus), WrapperHelperCreator.class);
		bus.setExtension(new ExtensionClassLoader(cxfBus), ExtensionClassCreator.class);
		bus.setExtension(new ExceptionClassLoader(cxfBus), ExceptionClassCreator.class);
		bus.setExtension(new WrapperClassLoader(cxfBus), WrapperClassCreator.class);
		bus.setExtension(new FactoryClassLoader(cxfBus), FactoryClassCreator.class);
		bus.setExtension(new GeneratedNamespaceClassLoader(cxfBus), NamespaceClassCreator.class);
		return bus;
	}

	@Bean
	public Endpoint endpoint(Bus bus) {
		LOGGER.log(Level.INFO, "ENDPOINT Creating Server ednpoint object");
		LOGGER.info("BUS ID: " + bus.getId());
		EndpointImpl endpoint =
			new EndpointImpl(bus,new StockQuoteReporter());
		endpoint.publish("/stockQuote");
		LOGGER.log(Level.INFO, "End Creating server.............");
		return endpoint;
	}

	@Bean("stockQuoteSoapClient")
	public QuoteReporter stockQuoteSoapClient(Bus bus){
		LOGGER.log(Level.INFO, "Creating client.............");
		LOGGER.info("BUS ID: " + bus.getId());
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

	@PreDestroy
	public void onShutDown() {
		System.out.println("closing application context..let's do the final resource cleanup");
	}
}