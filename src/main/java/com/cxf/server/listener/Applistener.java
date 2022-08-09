/*
 *   Copyright © 2021 Pearson VUE. All rights reserved.
 */

package com.cxf.server.listener;

import com.cxf.server.DumpingClassLoaderCapturer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *   //todo: Finish Description!!!
 *
 */
@Component
public class Applistener implements ApplicationListener<ContextClosedEvent> {

	private final DumpingClassLoaderCapturer capturer;

	private final Environment environment;

	public Applistener(DumpingClassLoaderCapturer capturer, Environment environment) {
		this.capturer = capturer;
		this.environment = environment;
	}


	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		System.out.println("shutdown listener"+ capturer.hashCode());
		System.out.println(Arrays.toString(environment.getActiveProfiles()));
		final boolean captureProfile = Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.equalsIgnoreCase("capture"));
		System.out.println("captureProfile:"+captureProfile);
		final File dumpfolder = new File("src/main/resources");
		System.out.println(dumpfolder.getAbsolutePath());
		System.out.println("dumpfolder:"+dumpfolder.exists());
		if(captureProfile){
			try {
				capturer.dumpTo(dumpfolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
