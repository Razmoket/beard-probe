package fr.fryscop.network.protocole.epp.action;

public class EppDisconnect extends AbstractEppAction {

	public EppDisconnect() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction() {
		// All done with this session, so let's log out...
        System.out.println("Logging out from the EPP Server");
        epp_client.logout(client_trid);

        // ... and disconnect
        System.out.println("Disconnecting from the EPP Server");
        epp_client.disconnect();

	}

}
