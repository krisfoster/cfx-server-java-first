/*
 *   Copyright © 2021 Pearson VUE. All rights reserved.
 */

package com.cxf.server.controller;

import com.cxf.server.beans.Quote;
import com.cxf.server.service.QuoteReporter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *   //todo: Finish Description!!!
 *
 */
@RestController
public class StockController {

	private final QuoteReporter quoteReporter;


	public StockController(@Qualifier("stockQuoteSoapClient") QuoteReporter quoteReporter) {
		this.quoteReporter = quoteReporter;
	}


	@RequestMapping(value = "/quote/{ticker}")
	public Quote getQuote(@PathVariable("ticker") String ticker){
		return quoteReporter.getStockQuote(ticker);
	}
}
