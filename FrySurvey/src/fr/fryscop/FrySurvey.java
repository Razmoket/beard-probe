package fr.fryscop;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.network.protocole.epp.SurveyConnection;
import fr.fryscop.probe.configuration.log.ProbeLogger;

public class FrySurvey {
	
	private static final Logger logger = LoggerFactory.getLogger(FrySurvey.class);

	public FrySurvey(){}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("ssl.props.location", "C:\\Developpement\\workspace\\hubiC\\Developpement\\FrySurvey\\ssl");
		logger.info("ssl.props.location="+System.getProperty("ssl.props.location"));
		SurveyConnection surveyConnection = new SurveyConnection(args);
		surveyConnection.session();
		
		ProbeLogger.getInstance().getLogger().info("Test");
		
	}

}
