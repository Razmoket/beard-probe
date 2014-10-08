package fr.fryscop.probe.test.dns.parameters;

public class TcpDnsResolutionParam extends DnsTestParameters {

	public TcpDnsResolutionParam( String digNdd) {
		super("tcpDnsResolution", null, digNdd);
	}
	
	private TcpDnsResolutionParam(String testName, String serverName, String digNdd) {
		super(testName, serverName, digNdd);
	}

	@Override
	public String[] getDigArg() {
		String argDig[] = { digNdd, "+tcp" };
		return argDig;
	}

}
