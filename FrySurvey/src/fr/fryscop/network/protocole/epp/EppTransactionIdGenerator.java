package fr.fryscop.network.protocole.epp;

public class EppTransactionIdGenerator {

	private static final String templateTransactionId = "FrySurvey-";
	private static String currentTransactionId = null;

	private static EppTransactionIdGenerator instance = null;

	private static String environment = "Test";

	private EppTransactionIdGenerator() {
	}

	public static EppTransactionIdGenerator getInstance() {
		if (instance == null) {
			instance = new EppTransactionIdGenerator();
		}
		return instance;

	}

	public String getCurrentTransactionId() {
		if (currentTransactionId == null) {
			currentTransactionId = new String();
			return getNewTransactionId();
		}
		return currentTransactionId;
	}

	public String getNewTransactionId() {
		if (currentTransactionId == null) {
			currentTransactionId = new String();
		}
		currentTransactionId = templateTransactionId + environment + "-" + System.currentTimeMillis();
		return currentTransactionId;
	}

	public static String getEnvironment() {
		return environment;
	}

	public static void setEnvironment(String environment) {
		EppTransactionIdGenerator.environment = environment;
	}

}
