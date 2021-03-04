package com.cxf.server.graal;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.apache.cxf.common.spi.GeneratedClassClassLoaderCapture;

import java.io.File;

public class DumpClasses {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("please provide wsdl and classes dump location");
            System.exit(0);
        }

        final Bus bus = new ExtensionManagerBus();
        BusFactory.setDefaultBus(bus);

        final DumpingClassLoaderCapturer capturer = new DumpingClassLoaderCapturer();
        bus.setExtension(capturer, GeneratedClassClassLoaderCapture.class);

        //Client.run(args, bus);

        capturer.dumpTo(new File(args[1]));
        System.exit(0);
    }
}
