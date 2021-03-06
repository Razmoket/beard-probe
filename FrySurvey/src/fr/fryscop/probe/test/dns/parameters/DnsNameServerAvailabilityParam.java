package fr.fryscop.probe.test.dns.parameters;

public class DnsNameServerAvailabilityParam extends AbstractDnsTestParameters {

	public DnsNameServerAvailabilityParam( String serverName, String digNdd) {
		this("dnsNameServerAvailability", serverName, digNdd);
	}

	private DnsNameServerAvailabilityParam(String testName, String serverName, String digNdd) {
		super(testName, serverName, digNdd);
	}

	@Override
	public String[] getTestArg() {
		String argDig[] = { "@" + serverName, digNdd, "ANY", "+short", "+tcp" };
		return argDig;
	}

	@Override
    public String getTestName() {
	    return super.getTestName()+ "|" + super.getServerName();
    }


}
