package fr.fryscop.probe.test.dns.parameters;

public class DnsServiceAvailabilityParam extends AbstractDnsTestParameters {

	public DnsServiceAvailabilityParam(String digNdd) {
		this("dnsServiceAvailability", null, digNdd);

	}

	private DnsServiceAvailabilityParam(String testName, String serverName, String digNdd) {
		super(testName, serverName, digNdd);
	}

	@Override
	public String[] getTestArg() {
		String argDig[] = { this.getDigNdd() };
		return argDig;
	}

	@Override
	public String getTestName() {
		return super.getTestName() + "| ";
	}

}
