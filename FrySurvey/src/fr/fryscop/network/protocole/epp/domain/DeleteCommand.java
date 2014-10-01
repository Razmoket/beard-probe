package fr.fryscop.network.protocole.epp.domain;

import fr.fryscop.network.protocole.epp.EppCommand;

public class DeleteCommand implements EppCommand {

	private final static String templateCommand = ""
			+ "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
			+ "	<epp xmlns=\"urn:ietf:params:xml:ns:epp-1.0\">"
			+ "	 <command>"
			+ "	   <delete>"
			+ "	    <domain:delete xmlns:domain=\"urn:ietf:params:xml:ns:domain-1.0\">"
			+ "	      <domain:name>$domain</domain:name>"
			+ "	    </domain:delete>"
			+ "	   </delete>"
			+ "	  <clTRID>test-epp-domain-check-$nowdate</clTRID>"
			+ "	 </command>"
			+ "	</epp>";
	private StringBuffer frameBuffer = new StringBuffer();
	
	
	@Override
	public void computeCommand() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFrameBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

}
