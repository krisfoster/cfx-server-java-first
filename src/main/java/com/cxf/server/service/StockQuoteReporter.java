/*
 *   Copyright © 2021 Pearson VUE. All rights reserved.
 */

package com.cxf.server.service;

import com.cxf.server.beans.Quote;

import java.util.Date;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

/**
 *   //todo: Finish Description!!!
 *
 */
@Service("StockQuoteReporter")
@WebService(endpointInterface="com.cxf.server.service.QuoteReporter",
	targetNamespace="http://cxf.apache.org",
	portName="StockQuotePort",
	serviceName="StockQuoteReporter"
)

public class StockQuoteReporter implements QuoteReporter {
	@Override
	public Quote getStockQuote(String ticker) {
		Quote retVal = new Quote();
		retVal.setTicker(ticker);
		retVal.setPrice("100.00");
		Date retDate = new Date();
		retVal.setTime(retDate.toString());
		return retVal;
	}
}
