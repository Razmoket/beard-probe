package fr.fryscop.probe.configuration.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbeLogger {

	private static final Logger logger = LoggerFactory.getLogger(ProbeLogger.class);
	
	
	private static ProbeLogger instance = new ProbeLogger();
	
	private ProbeLogger(){}
	
	public static ProbeLogger getInstance(){
		return instance;
	}
	
	public static Logger getLogger() {
		return logger;
	}

}
