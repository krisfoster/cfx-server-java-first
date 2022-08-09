/*
 *   Copyright © 2021 Pearson VUE. All rights reserved.
 */

package com.cxf.server.service;

import com.cxf.server.beans.Quote;
import com.sun.xml.ws.developer.StreamingAttachment;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.MTOM;

/**
 *   //todo: Finish Description!!!
 *
 */
@MTOM(enabled = true)
@StreamingAttachment(parseEagerly=true, memoryThreshold=4000000L)
@WebService(name="quoteReporter",
		targetNamespace="http://demo.mycompany.com/types")
@SOAPBinding(
		parameterStyle = ParameterStyle.WRAPPED
)
public interface QuoteReporter {

	@WebMethod(operationName="getStockQuote")
	@WebResult(targetNamespace="http://demo.mycompany.com/types",
		name="updatedQuote")
	public Quote getStockQuote(@WebParam(name="stockTicker", mode= Mode.IN) String ticker);
}
