package com.cxf.server;

import com.cxf.server.graal.DumpingClassLoaderCapturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.logging.Logger;

@SpringBootApplication(proxyBeanMethods = false)
public class ServerApplication {

	@Autowired
	private DumpingClassLoaderCapturer capturer;

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

	@PreDestroy
	public void onExit() {
		System.out.println("I am dead!");
		System.out.println(">> DUMP >> " + this.capturer);
		try {
			this.capturer.dumpTo(new File("./build/dump2/"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
