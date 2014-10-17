package fr.fryscop.network.protocole.epp.action;

import java.io.IOException;

import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_XMLException;

import fr.fryscop.network.protocole.epp.EppRequestObject;

public class EppDisconnect extends AbstractEppAction {

	public EppDisconnect() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction(EppRequestObject eppRequestObject) throws IOException, epp_XMLException, epp_Exception {
		// All done with this session, so let's log out...
        System.out.println("Logging out from the EPP Server");
        eppRequestObject.getEpp_client().logout(eppRequestObject.getCommand_data().getClientTrid());

        // ... and disconnect
        System.out.println("Disconnecting from the EPP Server");
        eppRequestObject.getEpp_client().disconnect();

	}

}
