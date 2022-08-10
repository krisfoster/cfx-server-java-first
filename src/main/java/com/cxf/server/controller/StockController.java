/*
 *   Copyright © 2021 Pearson VUE. All rights reserved.
 */

package com.cxf.server.controller;

//import com.cxf.server.DumpingClassLoaderCapturer;
import com.cxf.server.beans.Quote;
import com.cxf.server.service.QuoteReporter;

//import java.io.File;
//import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.sf.ehcache.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;

/**
 *   //todo: Finish Description!!!
 *
 */
@RestController
public class StockController {

	@Autowired
	private CacheManager cacheManager;

	private final QuoteReporter quoteReporter;

	public StockController(@Qualifier("stockQuoteSoapClient") QuoteReporter quoteReporter) {
		this.quoteReporter = quoteReporter;
	}


	@Cacheable(cacheNames="employeeCache", key="#ticker")
	@RequestMapping(value = "/quote/{ticker}")
	public Quote getQuote(@PathVariable("ticker") String ticker){
		return quoteReporter.getStockQuote(ticker);
	}

	@RequestMapping(value = "/cache/{ticker}")
	public String readCache(@PathVariable("ticker") String ticker){
		Cache cache = cacheManager.getCache("employeeCache");
		return cache.get(ticker).get().toString();
	}
}
