/*
 *   Copyright © 2021 Pearson VUE. All rights reserved.
 */

package com.cxf.server.controller;

import com.cxf.server.DumpingClassLoaderCapturer;
import com.cxf.server.beans.Quote;
import com.cxf.server.service.QuoteReporter;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *   //todo: Finish Description!!!
 *
 */
@RestController
public class StockController {

	private final QuoteReporter quoteReporter;
	private final DumpingClassLoaderCapturer capture;

	public StockController(@Qualifier("stockQuoteSoapClient") QuoteReporter quoteReporter, DumpingClassLoaderCapturer capture) {
		this.quoteReporter = quoteReporter;
		this.capture = capture;
	}


	@RequestMapping(value = "/quote/{ticker}")
	public Quote getQuote(@PathVariable("ticker") String ticker, @RequestParam(defaultValue = "false") Boolean doDump){
		if(doDump){
			try {
				capture.dumpTo(new File("dump"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return quoteReporter.getStockQuote(ticker);
	}
}
