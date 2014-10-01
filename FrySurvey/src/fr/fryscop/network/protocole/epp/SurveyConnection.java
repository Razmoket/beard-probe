
package fr.fryscop.network.protocole.epp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openrtk.idl.epprtk.epp_AuthInfo;
import org.openrtk.idl.epprtk.epp_AuthInfoType;
import org.openrtk.idl.epprtk.epp_Command;
import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_Greeting;
import org.openrtk.idl.epprtk.epp_Result;
import org.openrtk.idl.epprtk.epp_XMLException;

import com.tucows.oxrs.epprtk.rtk.EPPClient;
import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;

public class SurveyConnection {

	protected String	epp_host_name	= null;
	protected int	    epp_host_port	= 0;
	protected String	epp_client_id	= null;
	protected String	epp_password	= null;

	protected EPPClient	epp_client	  = null;

	private int	        argc	      = 0;
	private String[]	args	      = null;

	protected final String nextArgument() {
		if (argc >= args.length) return null;

		return args[argc++];
	}

	protected final void assertNotNull(String arg) {	
		if (arg == null) {
			System.out.println(getUsage());
			System.exit(1);
		}
	}

	public SurveyConnection(String args[]) {
		this.args = args;

		epp_host_name = nextArgument();
		assertNotNull(epp_host_name);

		String port_as_string = nextArgument();
		assertNotNull(port_as_string);

		epp_client_id = nextArgument();
		assertNotNull(epp_client_id);

		epp_password = nextArgument();
		assertNotNull(epp_password);

		epp_host_port = Integer.parseInt(port_as_string);
	}

	protected String getUsage() {
		return "Usage: " + this.getClass() + " epp_host_name" + " epp_host_port" + " epp_client_id" + " epp_password";
	}

	public void session() {
		System.out.println("Start of the Session example");

		try {
			login();
			//process();
			logout();
		} catch (epp_XMLException xcp) {
			System.err.println("epp_XMLException! [" + xcp.getErrorMessage() + "]");
		} catch (epp_Exception xcp) {
			System.err.println("epp_Exception!");
			epp_Result[] results = xcp.getDetails();
			// We're taking advantage epp_Result's toString() here
			// for debugging. Take a look at the javadocs for
			// the full list of attributes in the class.
			System.err.println("\tresult: [" + results[0] + "]");
		} catch (Exception xcp) {
			System.err.println("Exception! [" + xcp.getClass().getName() + "] [" + xcp.getMessage() + "]");
			xcp.printStackTrace();
		}
	}

	protected void login() throws epp_Exception, IOException, epp_XMLException, EPPTransportException {
		epp_client = new EPPClient(epp_host_name, epp_host_port, epp_client_id, epp_password);
		epp_client.setLang("en");

		System.out.println("Connecting to the EPP Server and getting the greeting");

		/*
		 * Uncomment following line if you don't want to send RTK version number on Login. Although Liberty RTK recomends to use this extension tag on
		 * Login request.
		 */
		// epp_client.setVersionSentOnLogin( false );

		epp_Greeting greeting = epp_client.connectAndGetGreeting();

		System.out.println("greeting's server: [" + greeting.getServerId() + "]");
		System.out.println("greeting's server-date: [" + greeting.getServerDate() + "]");
		System.out.println("greeting's service menu: [" + greeting.getSvcMenu() + "]");

		System.out.println("Logging into the EPP Server");
		epp_client.login(getClientTrid());
	}

	//protected abstract void process() throws epp_Exception, IOException, epp_XMLException;

	protected void logout() throws epp_Exception, IOException, epp_XMLException {
		// All done with this session, so let's log out...
		System.out.println("Logging out from the EPP Server");
		epp_client.logout(getClientTrid());

		// ... and disconnect
		System.out.println("Disconnecting from the EPP Server");
		epp_client.disconnect();
	}

	// ================ utility methods ===============//

	// generate unique client transaction id
	protected String getClientTrid() {
		return "ABC:" + epp_client_id + ":" + System.currentTimeMillis();
	}

	protected epp_Command createEPPCommand() {
		epp_Command command_data = new epp_Command();
		command_data.setClientTrid(getClientTrid());

		return command_data;
	}

	protected epp_AuthInfo getUserInputAuthInfo() throws IOException {
		epp_AuthInfo authInfo = new epp_AuthInfo();
		BufferedReader buffed_reader = new BufferedReader(new InputStreamReader(System.in));
		while (authInfo.getValue() == null || authInfo.getValue().length() == 0) {
			authInfo.setValue(buffed_reader.readLine());
		}
		authInfo.setType(epp_AuthInfoType.PW);

		return authInfo;
	}
}
