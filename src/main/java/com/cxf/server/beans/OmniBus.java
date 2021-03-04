package com.cxf.server.beans;

import com.cxf.server.ApplicationConfig;
import org.apache.cxf.bus.spring.SpringBus;

import javax.annotation.PreDestroy;
import java.util.logging.Logger;

public class OmniBus extends SpringBus {

    private final static Logger LOGGER = Logger.getLogger(OmniBus.class.getName());

    public OmniBus() {
        super();
        LOGGER.info("OmniBus <init>");
    }

    @Override
    public void shutdown () {
        LOGGER.info("OmniBus <shutdown...>");
        super.shutdown();
    }

    @Override
    public void shutdown (boolean wait) {
        LOGGER.info("OmniBus <shutdown...>");
        super.shutdown(wait);
    }

    @Override
    public void destroyBeans() {
        LOGGER.info("OmniBus <destroyBeans>");
        super.destroyBeans();
    }

    public void cleanup() {
        //
        LOGGER.info("OmniBus <cleanup-NEW>");
    }

    @PreDestroy
    public void onExit() {
        LOGGER.info("OmniBus <pre-destroy>");
    }
}
