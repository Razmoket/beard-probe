package fr.fryscop.probe.test.rdds.parameters;

import fr.fryscop.probe.test.AbstractTestParameters;

public abstract class AbstractRddsTestParameters extends AbstractTestParameters{

	public AbstractRddsTestParameters(String testName, String serverName, String digNdd) {
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
