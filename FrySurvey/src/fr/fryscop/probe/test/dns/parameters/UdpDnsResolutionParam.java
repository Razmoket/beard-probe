package fr.fryscop.probe.test.dns.parameters;

public class UdpDnsResolutionParam extends AbstractDnsTestParameters {

	public UdpDnsResolutionParam(String digNdd) {
		super("UdpDnsResolution", null, digNdd);
	}
	
	private UdpDnsResolutionParam(String testName, String serverName, String digNdd) {
		super(testName, serverName, digNdd);
	}

	@Override
	public String[] getTestArg() {
		String argDig[] = { digNdd };
		return argDig;
	}

	@Override
    public String getTestName() {
	    return super.getTestName()+ "| " ;
    }
}
