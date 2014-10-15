package fr.fryscop.probe.test.rdds.parameters;

public class RddsServiceAvailabilityParam extends AbstractRddsTestParameters {

	public RddsServiceAvailabilityParam(String tld, String digNdd) {
		super("RddsServiceAvailabilityParam", tld, digNdd);
	}

	private RddsServiceAvailabilityParam(String testName, String tld, String digNdd) {
		super(testName, tld, digNdd);
	}

	@Override
	public String[] getTestArg() {
		String argWhois[] = { digNdd +	"@" + getServerName() };
		return argWhois;
	}

	@Override
	public String getTestName() {
		return super.getTestName() + "|" + super.getServerName();
	}

}
