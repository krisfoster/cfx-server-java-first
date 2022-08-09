/*
 *   Copyright © 2021 Pearson VUE. All rights reserved.
 */

package com.cxf.server.config;

import com.cxf.server.DumpingClassLoaderCapturer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.common.spi.GeneratedClassClassLoaderCapture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *   //todo: Finish Description!!!
 *
 */
@Configuration
public class CaptureConfig {

	private final static Logger LOGGER = Logger.getLogger(CaptureConfig.class.getName());

	@Bean
	@Profile("capture")
	public Bus bus(DumpingClassLoaderCapturer capture,Bus cxfBus){
		LOGGER.log(Level.INFO,"Setting up Bus to capture run time classes***********");
		cxfBus.setId("krd_ID");
		BusFactory.setDefaultBus(cxfBus);
		cxfBus.setExtension(capture, GeneratedClassClassLoaderCapture.class);
		return cxfBus;
	}
}
