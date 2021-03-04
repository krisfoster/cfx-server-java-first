package com.cxf.server;

import com.cxf.server.graal.AppListener;
import com.cxf.server.graal.DumpClasses;
import com.cxf.server.graal.DumpingClassLoaderCapturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextClosedEvent;

import javax.annotation.PreDestroy;
import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@SpringBootApplication(proxyBeanMethods = false)
public class ServerApplication {

	private final static Logger LOGGER = Logger.getLogger(ServerApplication.class.getName());

	private String[] args;

	public static void main(String[] args) {
		ServerApplication server = new ServerApplication(args);
		server.run();
	}

	public ServerApplication(String[] args) {
		this.args = args;
	}

	public void run() {
		final SpringApplication app = new SpringApplication(ServerApplication.class);
		//app.addListeners(new AppListener());
		app.run(this.args);
	}

}
