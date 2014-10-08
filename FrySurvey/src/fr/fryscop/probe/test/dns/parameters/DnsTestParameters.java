package fr.fryscop.probe.test.dns.parameters;

import fr.fryscop.probe.ProbeStatus;

public abstract class DnsTestParameters {

	public DnsTestParameters(String testName, String serverName, String digNdd) {
	    super();
	    this.testName = testName;
	    this.serverName = serverName;
	    this.digNdd = digNdd;
    }

	String testName;
	String serverName = null;
	String digNdd;

	ProbeStatus status =  ProbeStatus.Ok;
	private long startUnavaibility = 0;
	private long unavaibilityDuration = 0;

	public long startUnavaibility() {
		if (this.getStatus() != ProbeStatus.Unavailable) {
			this.setStatus(ProbeStatus.Unavailable);
			startUnavaibility = System.currentTimeMillis();
		}
		unavaibilityDuration = System.currentTimeMillis() - startUnavaibility;
		return unavaibilityDuration;
	}

	public long stopUnavaibility() {
		if (this.getStatus() == ProbeStatus.Unavailable) {
			this.setStatus(ProbeStatus.Ok);
			unavaibilityDuration = System.currentTimeMillis() - startUnavaibility;
			return unavaibilityDuration;
		} else {
			return 0;
		}
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDigNdd() {
		return digNdd;
	}

	public void setDigNdd(String digNdd) {
		this.digNdd = digNdd;
	}

	public ProbeStatus getStatus() {
		return status;
	}

	public void setStatus(ProbeStatus status) {
		this.status = status;
	}

	public abstract String[] getDigArg();

	public long getUnavaibilityDuration() {
		return unavaibilityDuration;
	}

}
