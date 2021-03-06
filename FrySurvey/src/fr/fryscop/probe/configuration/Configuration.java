package fr.fryscop.probe.configuration;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import fr.fryscop.probe.test.TestLauncher;
import fr.fryscop.tools.FolderSpyWorker;

public class Configuration {

	private static final Logger	logger	                = LoggerFactory.getLogger(Configuration.class);

	private final static String	CONFIGURATION_FILE_NAME	= "probes-launcher.xml";
	private final static String	CONFIGURATION_FTP_FILE_NAME	= "ftp_sync.xml";

	public static TestLauncher loadConfiguration() throws Exception {
		logger.info("load " + CONFIGURATION_FILE_NAME);
		// ApplicationContext context = new ClassPathXmlApplicationContext(CONFIGURATION_FILE_NAME);

		ClassPathResource classPathResource = null;
		try {
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

	public static FolderSpyWorker loadFtpConfiguration() throws Exception {
		logger.info("load " + CONFIGURATION_FTP_FILE_NAME);
		// ApplicationContext context = new ClassPathXmlApplicationContext(CONFIGURATION_FILE_NAME);

		ClassPathResource classPathResource = null;
		try {
			classPathResource = new ClassPathResource(CONFIGURATION_FTP_FILE_NAME);
			XmlBeanFactory beanFactory = new XmlBeanFactory(classPathResource);

			FolderSpyWorker launcher = beanFactory.getBean(FolderSpyWorker.class);
			return launcher;
		} catch (Exception e) {
			String fileName = "";
			if (classPathResource != null) {
				try {
					fileName = "[" + classPathResource.getFile().getAbsolutePath() + "]";
				} catch (IOException e1) {
					fileName = "[" + CONFIGURATION_FTP_FILE_NAME + " not found" + "]";
				}
			}
			throw new Exception("loadConfiguration() failed " + fileName, e);
		}
	}
}
