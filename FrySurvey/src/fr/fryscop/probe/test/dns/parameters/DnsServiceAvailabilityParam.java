package fr.fryscop.probe.test.dns.parameters;

public class DnsServiceAvailabilityParam extends DnsTestParameters {

	public DnsServiceAvailabilityParam(String digNdd){
		this("dnsServiceAvailability", null, digNdd);
		
	}
	private DnsServiceAvailabilityParam(String testName, String serverName, String digNdd) {
	    super(testName, serverName, digNdd);
    }

	@Override
	public String[] getDigArg() {
		String argDig[] = { this.getDigNdd() };
		return argDig;
	}

}
