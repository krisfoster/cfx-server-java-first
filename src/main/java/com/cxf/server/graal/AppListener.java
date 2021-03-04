package com.cxf.server.graal;

import com.cxf.server.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class AppListener implements ApplicationListener<ContextClosedEvent> {

    private final static Logger LOGGER = Logger.getLogger(AppListener.class.getName());

    @Autowired
    private DumpingClassLoaderCapturer capturer;

    public AppListener() {
        //
        LOGGER.info("AppListener <init>");
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        //try {
            System.out.println("Closing app listener");
            LOGGER.info("Dumping classes");
            System.out.println(">>> " + capturer);
            //capturer.dumpTo(new File("./build/dump"));
        //} catch (IOException ioex) {
        //    //
        //    LOGGER.warning("Failed to create file for class dumping!");
        //}
    }
}
