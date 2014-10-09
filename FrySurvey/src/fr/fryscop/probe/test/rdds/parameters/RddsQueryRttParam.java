package fr.fryscop.probe.test.rdds.parameters;

public class RddsQueryRttParam extends AbstractRddsTestParameters {

	public RddsQueryRttParam(String serverName, String digNdd) {
		super("RddsQueryRttParam", serverName, digNdd);
	}

	private RddsQueryRttParam(String testName, String serverName, String digNdd) {
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
