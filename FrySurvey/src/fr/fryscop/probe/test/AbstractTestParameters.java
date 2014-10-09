package fr.fryscop.probe.test;

import fr.fryscop.probe.ProbeStatus;

public abstract class AbstractTestParameters {

	
	public AbstractTestParameters(String testName, String digNdd) {
	    super();
	    this.testName = testName;
	    this.digNdd = digNdd;
    }

	protected String testName;
	protected String digNdd;

	private ProbeStatus status =  ProbeStatus.Ok;
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

	public abstract String[] getTestArg();

	public long getUnavaibilityDuration() {
		return unavaibilityDuration;
	}

}
