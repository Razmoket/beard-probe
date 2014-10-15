package fr.fryscop.probe.configuration;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import fr.fryscop.probe.test.TestLauncher;
import fr.fryscop.probe.test.dns.DnsProbe;

public class Configuration {

	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

	private final static String CONFIGURATION_FILE_NAME= "probes-launcher.xml"; 
	
	public static TestLauncher loadConfiguration() throws Exception{
		//ApplicationContext context = new ClassPathXmlApplicationContext(CONFIGURATION_FILE_NAME);
		
		ClassPathResource classPathResource = null;
        try {
        	logger.debug("load " + CONFIGURATION_FILE_NAME);
            classPathResource = new ClassPathResource(CONFIGURATION_FILE_NAME);
            XmlBeanFactory beanFactory = new XmlBeanFactory(classPathResource);
            
            TestLauncher launcher = beanFactory.getBean(TestLauncher.class);
            return launcher;
        } catch (Exception e) {
            String fileName = "";
            if (classPathResource != null) {
                try {
                    fileName = "[" + classPathResource.getFile().getAbsolutePath() + "]";
                } catch (IOException e1) {
                    fileName = "[" + CONFIGURATION_FILE_NAME + " not found" + "]";
                }
            }
            throw new Exception("loadConfiguration() failed " + fileName, e);
        }
	}
}
