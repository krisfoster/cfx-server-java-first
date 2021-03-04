/*
 *   Copyright © 2021 Pearson VUE. All rights reserved.
 */

package com.cxf.server.controller;

import com.cxf.server.ApplicationConfig;
import com.cxf.server.beans.Quote;
import com.cxf.server.graal.DumpingClassLoaderCapturer;
import com.cxf.server.service.QuoteReporter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *   //todo: Finish Description!!!
 *
 */
@RestController
public class StockController {

	private final static Logger LOGGER = Logger.getLogger(StockController.class.getName());

	private final QuoteReporter quoteReporter;

	private final DumpingClassLoaderCapturer clsLoader;

	public StockController(@Qualifier("stockQuoteSoapClient") QuoteReporter quoteReporter, DumpingClassLoaderCapturer clsLoader) {
		this.quoteReporter = quoteReporter;
		this.clsLoader = clsLoader;
	}

	@RequestMapping(value = "/quote/{ticker}")
	public Quote getQuote(@PathVariable("ticker") String ticker){
		LOGGER.info("STOCK QUOTE");
		if (Boolean.getBoolean("capture")) {
			try {
				LOGGER.info("Dumping class files");
				this.clsLoader.dumpTo(new File("./build/dump/"));
				LOGGER.info("Log files dumped to ./build/dump/");
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}

		return quoteReporter.getStockQuote(ticker);
	}
}
