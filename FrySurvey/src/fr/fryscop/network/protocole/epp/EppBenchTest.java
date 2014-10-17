package fr.fryscop.network.protocole.epp;

import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_Result;
import org.openrtk.idl.epprtk.epp_XMLException;

import fr.fryscop.network.protocole.epp.action.EppConnectAndGetGreeting;
import fr.fryscop.network.protocole.epp.action.EppDisconnect;

/**
 * Example code for a typical logical EPP sessions. For more information on the creation of Domain, Host and Contact objects, please see their
 * respective example Java source files.
 * 
 * @author Daniel Manley
 * @version $Revision: 1.1 $ $Date: 2004/12/07 15:53:26 $
 * @see com.tucows.oxrs.epprtk.rtk.example.DomainExample
 * @see com.tucows.oxrs.epprtk.rtk.example.HostExample
 * @see com.tucows.oxrs.epprtk.rtk.example.ContactExample
 **/
public class EppBenchTest {
	// 10.4.3.1 6666 -hinpizyz334-.ne 9qPv6MVYX2h1sudR nic.next P15 P15

	// private static String USAGE =
	// "Usage: com.tucows.oxrs.epprtk.rtk.example.SessionExample epp_host_name epp_host_port epp_client_id epp_password domain_name [contact_id1] [contact_id2]";

	/**
	 * Main of the example. Performs typical Domain, Host and Contact operations in a logical order.
	 */
	public static void main(String args[]) {

		System.setProperty("rtk.props.file", ".\\conf\\rtk.properties");
		System.out.println("Start of the Session example");

		EppRequestObject eppRequestObject = new EppRequestObject();
		eppRequestObject.init(args);

		try {	
			new EppConnectAndGetGreeting().doAction(eppRequestObject);
			
			new EppDisconnect().doAction(eppRequestObject);
		} catch (epp_XMLException xcp) {
			// Either the request was missing some required data in
			// validation before sending to the server, or theserver's
			// response was either unparsable or missing some required data.
			System.err.println("epp_XMLException! [" + xcp.getErrorMessage() + "]");
			xcp.printStackTrace();
		} catch (epp_Exception xcp) { // The EPP Server has responded with an error code with
			// some optional messages to describe the error.
			System.err.println("epp_Exception!");
			epp_Result[] results = xcp.getDetails();
			System.err.println("\t" + results[0]);
			xcp.printStackTrace();
		} catch (Exception xcp) { // Other unexpected exceptions
			System.err.println("EPP Action failed! [" + xcp.getClass().getName() + "] [" + xcp.getMessage() + "]");
			xcp.printStackTrace();
		}
	}
}
