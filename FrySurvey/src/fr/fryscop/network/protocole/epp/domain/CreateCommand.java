package fr.fryscop.network.protocole.epp.domain;

import fr.fryscop.network.protocole.epp.EppCommand;
import fr.fryscop.network.protocole.epp.EppTransactionIdGenerator;

public class CreateCommand implements EppCommand{

	private final static String templateCommand = "<epp xmlns=\"urn:ietf:params:xml:ns:epp-1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:ietf:params:xml:ns:epp-1.0 epp-1.0.xsd\">"
			+ "<command>"
			+ "    <create>"
			+ "      <domain:create xmlns:domain=\"urn:ietf:params:xml:ns:domain-1.0\" xsi:schemaLocation=\"urn:ietf:params:xml:ns:domain-1.0 domain-1.0.xsd\">"
			+ "        <domain:name>$domain</domain:name>"
			+ "        <domain:registrant>$nichandle</domain:registrant>"
			+ "        <domain:contact type=\"admin\">$nichandle</domain:contact>"
			+ "        <domain:contact type=\"tech\">$tech</domain:contact>"
			+ "        <domain:contact type=\"billing\">$nichandle</domain:contact>"
			+ "        <domain:authInfo>"
			+ "          <domain:pw>pouet</domain:pw>"
			+ "        </domain:authInfo>"
			+ "      </domain:create>"
			+ "    </create>"
			+ "    <clTRID>$transaction</clTRID>"
			+ "  </command>"
			+ "</epp>";
	
	private StringBuffer frameBuffer;

	private String domain;
	private String adminNichandle;
	private String techNichandle;
	private String billingNichandle;
	private String transactionID;
	
	public CreateCommand(){
		super();
	}
	
	public CreateCommand(String domain, String adminNichandle, String techNichandle, String billingNichandle) {
		super();
		this.domain = domain;
		this.adminNichandle = adminNichandle;
		this.techNichandle = techNichandle;
		this.billingNichandle = billingNichandle;
	}
	
	@Override
	public void computeCommand(){
		frameBuffer = new StringBuffer();
		this.setTransactionID(EppTransactionIdGenerator.getInstance().getNewTransactionId());
		String tempTraitement = templateCommand.replaceAll("$domain", this.domain);
		tempTraitement = tempTraitement.replaceAll("$nichandle", this.adminNichandle);
		tempTraitement = tempTraitement.replaceAll("$tech", this.techNichandle);
		tempTraitement = tempTraitement.replaceAll("$transaction", this.transactionID);
		
		
	}

	@Override
	public String getFrameBuffer() {
		return frameBuffer.toString();
	}


	public String getDomain() {
		return domain;
	}


	public void setDomain(String domain) {
		this.domain = domain;
	}


	public String getAdminNichandle() {
		return adminNichandle;
	}


	public void setAdminNichandle(String adminNichandle) {
		this.adminNichandle = adminNichandle;
	}


	public String getTechNichandle() {
		return techNichandle;
	}


	public void setTechNichandle(String techNichandle) {
		this.techNichandle = techNichandle;
	}


	public String getBillingNichandle() {
		return billingNichandle;
	}


	public void setBillingNichandle(String billingNichandle) {
		this.billingNichandle = billingNichandle;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	

}
