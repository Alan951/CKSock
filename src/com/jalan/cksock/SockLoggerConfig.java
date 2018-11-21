package com.jalan.cksock;



import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class SockLoggerConfig {
	
	public static Logger setCustomPattern(Logger logger, String pattern) {
		logger.removeAllAppenders();
		logger.addAppender(new ConsoleAppender(new PatternLayout(pattern)));
		logger.setAdditivity(false);
		
		return logger;
	}
	
}
