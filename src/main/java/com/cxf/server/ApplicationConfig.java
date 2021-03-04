package com.cxf.server;

import com.cxf.server.graal.DumpingClassLoaderCapturer;
import com.cxf.server.service.QuoteReporter;
import com.cxf.server.service.StockQuoteReporter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.clustering.FailoverFeature;
import org.apache.cxf.clustering.RetryStrategy;
import org.apache.cxf.common.spi.GeneratedClassClassLoaderCapture;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

	@Autowired
	private ApplicationContext appContext;

	// assumes the current class is called MyLogger
	private final static Logger LOGGER = Logger.getLogger(ApplicationConfig.class.getName());

	@Bean
	public DumpingClassLoaderCapturer getClassLoader() {
		return new DumpingClassLoaderCapturer();
	}

	@Bean
	public Bus bus(DumpingClassLoaderCapturer capturer, Bus cxfBus) {
		LOGGER.log(Level.INFO, "BUS START");
		final Bus bus = cxfBus;
		bus.setId("krf_ID");
		BusFactory.setDefaultBus(bus);
		if (Boolean.getBoolean("capture")) {
			// Capture
			LOGGER.info("Capturer setup for BUS");
			bus.setExtension(capturer, GeneratedClassClassLoaderCapture.class);
		}
		if (Boolean.getBoolean("native")) {
			// Runtime
			LOGGER.info("Runtime setup for BUS");

			bus.setExtension(new WrapperHelperClassLoader(bus), WrapperHelperCreator.class);
			bus.setExtension(new ExtensionClassLoader(bus), ExtensionClassCreator.class);
			bus.setExtension(new ExceptionClassLoader(bus), ExceptionClassCreator.class);
			bus.setExtension(new WrapperClassLoader(bus), WrapperClassCreator.class);
			bus.setExtension(new FactoryClassLoader(bus), FactoryClassCreator.class);
			bus.setExtension(new GeneratedNamespaceClassLoader(bus), NamespaceClassCreator.class);

		}
		LOGGER.log(Level.INFO, "BUS END");
		return bus;
	}

	@Bean
	public Endpoint endpoint(Bus bus) {
		LOGGER.log(Level.INFO, "ENDPOINT Creating Server endpoint object");
		LOGGER.info("BUS ID: " + bus.getId());
		EndpointImpl endpoint = new EndpointImpl(bus, new StockQuoteReporter());
		endpoint.publish("/stockQuote");
		LOGGER.log(Level.INFO, "End Creating server.............");
		return endpoint;
	}

	@Bean("stockQuoteSoapClient")
	public QuoteReporter stockQuoteSoapClient(Bus bus, DumpingClassLoaderCapturer capturer) {
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