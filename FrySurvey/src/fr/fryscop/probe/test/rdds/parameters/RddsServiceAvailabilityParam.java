package fr.fryscop.probe.test.rdds.parameters;

public class RddsServiceAvailabilityParam extends AbstractRddsTestParameters {

	public RddsServiceAvailabilityParam(String serverName, String digNdd) {
		super("RddsServiceAvailabilityParam", serverName, digNdd);
	}

	private RddsServiceAvailabilityParam(String testName, String serverName, String digNdd) {
		super(testName, serverName, digNdd);
	}

	@Override
	public String[] getTestArg() {
		String argWhois[] = { digNdd +	"@" + serverName };
		return argWhois;
	}

	@Override
	public String getTestName() {
		return super.getTestName() + "|" + super.getServerName();
	}

}
