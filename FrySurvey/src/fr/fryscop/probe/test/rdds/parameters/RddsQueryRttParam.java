package fr.fryscop.probe.test.rdds.parameters;

public class RddsQueryRttParam extends AbstractRddsTestParameters {

	public RddsQueryRttParam(String tld, String digNdd) {
		super("RddsQueryRttParam", tld, digNdd);
	}

	private RddsQueryRttParam(String testName, String tld, String digNdd) {
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
