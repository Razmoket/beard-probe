package fr.fryscop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.network.protocole.epp.SurveyConnection;

public class FrySurvey {

	private static final Logger logger = LoggerFactory.getLogger(FrySurvey.class);

	public FrySurvey() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("ssl.props.location", ".\\ssl");
		logger.info("ssl.props.location=" + System.getProperty("ssl.props.location"));
		SurveyConnection surveyConnection = new SurveyConnection(args);
		surveyConnection.session();

	}

}
