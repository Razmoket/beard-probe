package fr.fryscop;

import fr.fryscop.network.protocole.epp.SurveyConnection;

public class FrySurvey {
	
	
	public FrySurvey(){}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("ssl.props.location", "C:\\Developpement\\workspace\\hubiC\\Developpement\\FrySurvey\\ssl");
		System.out.println("ssl.props.location="+System.getProperty("ssl.props.location"));
		SurveyConnection surveyConnection = new SurveyConnection(args);
		surveyConnection.session();
	}

}
