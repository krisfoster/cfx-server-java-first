package com.cxf.server;

import com.cxf.server.graal.DumpingClassLoaderCapturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

@SpringBootApplication(proxyBeanMethods = false)
public class ServerApplication {

	@Autowired
	private DumpingClassLoaderCapturer capturer;

	private final static Logger LOGGER = Logger.getLogger(ServerApplication.class.getName());
	private final String SYS_PROPERTY_CAPTURE_DIR = "capture.dir";

	private String[] args;
	private File dumpDir;

	public static void main(String[] args) throws FileNotFoundException {
		ServerApplication server = new ServerApplication(args);
		server.run();
	}

	public ServerApplication(String[] args) throws FileNotFoundException {
		this.args = args;

		// If a capture dir system property is set, assume we are running in capture mode and
		// set up the classes dump directory
		if (Boolean.getBoolean(SYS_PROPERTY_CAPTURE_DIR)) {
			dumpDir = new File(System.getProperty("capture.dir"));
			if (dumpDir.exists() && !dumpDir.isDirectory()) {
				throw new FileNotFoundException(
						"Dump dir exists and is not a dir : " + dumpDir.getAbsolutePath());
			}
			if (!dumpDir.exists()) {
				LOGGER.info("Creating classes dump directory: " + dumpDir.getAbsolutePath());
				dumpDir.mkdirs();
			}
		}
	}

	public void run() {
		final SpringApplication app = new SpringApplication(ServerApplication.class);
		//app.addListeners(new AppListener());
		app.run(this.args);
		LOGGER.info("***** Application STARTED *****");
	}

	@PreDestroy
	public void onExit() {
		// Only attempt to dump the classes if we are running in capture mode
		if (Boolean.getBoolean(SYS_PROPERTY_CAPTURE_DIR)) {
			try {
				this.capturer.dumpTo(this.dumpDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
