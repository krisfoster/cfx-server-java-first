/*
 *   Copyright © 2021 Pearson VUE. All rights reserved.
 */

package com.cxf.server.listener;

import com.cxf.server.DumpingClassLoaderCapturer;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 *   //todo: Finish Description!!!
 *
 */
@Component
public class Applistener implements ApplicationListener<ContextClosedEvent> {

	@Autowired
	private DumpingClassLoaderCapturer capturer;

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		System.out.println("shutdown listener"+ capturer.hashCode());
		try {
			capturer.dumpTo(new File("dump"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
