package fr.fryscop.probe.test.rdds.parameters;

import fr.fryscop.probe.test.AbstractTestParameters;

public abstract class AbstractRddsTestParameters extends AbstractTestParameters{

	private final static String DEFAULT_WHOIS_NAME = "whois.nic.";
	public AbstractRddsTestParameters(String testName, String tld, String digNdd) {
	    super(testName,digNdd);
	    setServerName( tld);
    }

	private String serverName = null;


	public String getServerName() {
		return serverName;
	}

	public void setServerName(String tld) {
		this.serverName = DEFAULT_WHOIS_NAME + tld;
	}
}
