package fr.fryscop.network.protocole.epp;

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

	//private static String USAGE = "Usage: com.tucows.oxrs.epprtk.rtk.example.SessionExample epp_host_name epp_host_port epp_client_id epp_password domain_name [contact_id1] [contact_id2]";

	/**
	 * Main of the example. Performs typical Domain, Host and Contact operations in a logical order.
	 */
	public static void main(String args[]) {

		System.setProperty("rtk.props.file", ".\\conf\\rtk.properties");
		System.out.println("Start of the Session example");

		EppRequestObject eppRequestObject = new EppRequestObject();
		eppRequestObject.init(args);

		/*
		 * try {
		 * 
		 * try {
		 * 
		 * } catch (epp_XMLException xcp) { // Either the request was missing some required data in // validation before sending to the server, or the
		 * server's // response was either unparsable or missing some required data. System.err.println("epp_XMLException! [" + xcp.getErrorMessage()
		 * + "]"); xcp.printStackTrace(); } catch (epp_Exception xcp) { // The EPP Server has responded with an error code with // some optional
		 * messages to describe the error. System.err.println("epp_Exception!"); epp_Result[] results = xcp.getDetails(); System.err.println("\t" +
		 * results[0]); xcp.printStackTrace(); } catch (Exception xcp) { // Other unexpected exceptions System.err.println("EPP Action failed! [" +
		 * xcp.getClass().getName() + "] [" + xcp.getMessage() + "]"); xcp.printStackTrace(); }
		 * 
		 * } catch (epp_XMLException xcp) { System.err.println("epp_XMLException! [" + xcp.getErrorMessage() + "]"); xcp.printStackTrace(); } catch
		 * (epp_Exception xcp) { System.err.println("epp_Exception!"); epp_Result[] results = xcp.getDetails(); // We're taking advantage epp_Result's
		 * toString() here // for debugging. Take a look at the javadocs for // the full list of attributes in the class.
		 * System.err.println("\tresult: [" + results[0] + "]"); xcp.printStackTrace(); } catch (Exception xcp) { System.err.println("Exception! [" +
		 * xcp.getClass().getName() + "] [" + xcp.getMessage() + "]"); xcp.printStackTrace(); }
		 */
	}
}
