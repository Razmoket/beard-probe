package fr.fryscop.network.protocole.epp;

public class LoginCommand implements EppCommand {

	private final static String templateCommand = ""
			+ "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<epp xmlns=\"urn:ietf:params:xml:ns:epp-1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:ietf:params:xml:ns:epp-1.0 epp-1.0.xsd\">"
			+ "  <command>"
			+ "    <login>"
			+ "      <clID>$EPP_login</clID>"
			+ "      <pw>$EPP_pass</pw>"
			+ "      <options>"
			+ "        <version>1.0</version>"
			+ "        <lang>en</lang>"
			+ "      </options>"
			+ "      <svcs>"
			+ "        <objURI>urn:ietf:params:xml:ns:domain-1.0</objURI>"
			+ "        <objURI>urn:ietf:params:xml:ns:contact-1.0</objURI>";
	
	private final static String templateCommandExtFr = "<svcExtension>"
			+ "          <extURI>urn:ietf:params:xml:ns:rgp-1.0</extURI>"
			+ "          <extURI>http://www.afnic.fr/xml/epp/frnic-1.2</extURI>"
			+ "          <extURI>urn:ietf:params:xml:ns:secDNS-1.1</extURI>"
			+ "        </svcExtension>"
			+ "      </svcs>";
      		
	private final static String templateCommandExtgTLD = "<objURI>urn:ietf:params:xml:ns:host-1.0</objURI>"
			+ "        <svcExtension>"
			+ "          <extURI>urn:ietf:params:xml:ns:rgp-1.0</extURI>"
			+ "          <extURI>urn:ietf:params:xml:ns:secDNS-1.1</extURI>"
			+ "        </svcExtension>"
			+ "      </svcs>";
	
	private final static String templateCommandEnd = " </login>"
			+ "    <clTRID>$sp</clTRID>"
			+ "  </command>"
			+ "</epp>"; 
	
	private StringBuffer frameBuffer;
	
	
	private String eppLogin;
	private String eppPassword;
	private boolean isFrenchRegistry=false;
	private String transactionID;
	
	
	
	@Override
	public void computeCommand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFrameBuffer() {
		if(frameBuffer == null){
			return null;
		}
		return frameBuffer.toString();
	}

	public String getEppLogin() {
		return eppLogin;
	}

	public void setEppLogin(String eppLogin) {
		this.eppLogin = eppLogin;
	}

	public String getEppPassword() {
		return eppPassword;
	}

	public void setEppPassword(String eppPassword) {
		this.eppPassword = eppPassword;
	}

	public boolean isFrenchRegistry() {
		return isFrenchRegistry;
	}

	public void setFrenchRegistry(boolean isFrenchRegistry) {
		this.isFrenchRegistry = isFrenchRegistry;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
		
	
}
