package fr.fryscop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.probe.configuration.Configuration;
import fr.fryscop.probe.test.TestLauncher;

public class FrySurvey {

	private static final Logger logger = LoggerFactory.getLogger(FrySurvey.class);

	public FrySurvey() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("* * * * * * * * * * * * * * * * * *");
		logger.info("* * * * * * Démarrage * * * * * * *");
		logger.info("* * * * * * * * * * * * * * * * * *");
		/*
		 * System.setProperty("ssl.props.location", ".\\ssl"); System.setProperty("rtk.props.file",".\\conf\\rtk.properties");
		 * logger.info("ssl.props.location=" + System.getProperty("ssl.props.location")); SurveyConnection surveyConnection = new
		 * SurveyConnection(args); surveyConnection.session();
		 */
		/* * * * * * * * * * * * * * * * * *
		 * 192.134.5.70 700 pouet pouet 10.4.3.1 6666 -hinpizyz334-.ne 9qPv6MVYX2h1sudR
		 * 
		 * * * * * * * * * * * * * * * * *
		 */
		try {
			TestLauncher configuredLauncher = Configuration.loadConfiguration();
			logger.info("ProbeLauncher configured.");
			configuredLauncher.initProbeList();
			logger.info("ProbeLauncher is starting probes.");
			configuredLauncher.startProbes();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}

}
