package fr.fryscop.probe.test.dns.parameters;

import fr.fryscop.probe.test.AbstractTestParameters;

public abstract class AbstractDnsTestParameters extends AbstractTestParameters {

	public AbstractDnsTestParameters(String testName, String serverName, String digNdd) {
	    super(testName,digNdd);
	    this.serverName = serverName;
    }

	String serverName = null;


	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
