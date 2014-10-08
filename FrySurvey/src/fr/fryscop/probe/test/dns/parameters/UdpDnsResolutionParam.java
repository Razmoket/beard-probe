package fr.fryscop.probe.test.dns.parameters;

public class UdpDnsResolutionParam extends DnsTestParameters {

	public UdpDnsResolutionParam(String digNdd) {
		super("UdpDnsResolution", null, digNdd);
	}
	
	private UdpDnsResolutionParam(String testName, String serverName, String digNdd) {
		super(testName, serverName, digNdd);
	}

	@Override
	public String[] getDigArg() {
		String argDig[] = { digNdd };
		return argDig;
	}

}
