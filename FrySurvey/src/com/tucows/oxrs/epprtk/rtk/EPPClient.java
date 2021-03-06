/*
 * *
 * * EPP RTK Java* Copyright (C) 2001-2002, Tucows, Inc.* Copyright (C) 2003, Liberty RMS*** This library is free software; you can redistribute it
 * and/or* modify it under the terms of the GNU Lesser General Public* License as published by the Free Software Foundation; either* version 2.1 of
 * the License, or (at your option) any later version.** This library is distributed in the hope that it will be useful,* but WITHOUT ANY WARRANTY;
 * without even the implied warranty of* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU* Lesser General Public License for more
 * details.** You should have received a copy of the GNU Lesser General Public* License along with this library; if not, write to the Free Software*
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA*
 */

/*
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/com/tucows/oxrs/epprtk/rtk/EPPClient.java,v 1.1 2004/12/07 15:53:26 ewang2004 Exp $ $Revision: 1.1 $
 * $Date: 2004/12/07 15:53:26 $
 */

package com.tucows.oxrs.epprtk.rtk;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.openrtk.idl.epprtk.epp_Action;
import org.openrtk.idl.epprtk.epp_Command;
import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_Extension;
import org.openrtk.idl.epprtk.epp_Greeting;
import org.openrtk.idl.epprtk.epp_LoginReq;
import org.openrtk.idl.epprtk.epp_LogoutReq;
import org.openrtk.idl.epprtk.epp_Options;
import org.openrtk.idl.epprtk.epp_PollOpType;
import org.openrtk.idl.epprtk.epp_PollReq;
import org.openrtk.idl.epprtk.epp_PollRsp;
import org.openrtk.idl.epprtk.epp_Session;
import org.openrtk.idl.epprtk.epp_XMLException;

import com.liberty.rtk.extension.epprtk.Rgp;
import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportBase;
import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;
import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportTCP;
import com.tucows.oxrs.epprtk.rtk.xml.EPPGreeting;
import com.tucows.oxrs.epprtk.rtk.xml.EPPLogin;
import com.tucows.oxrs.epprtk.rtk.xml.EPPLogout;
import com.tucows.oxrs.epprtk.rtk.xml.EPPPoll;

/**
 * EPP Client -- encapsulates all the connection and communication with the EPP Server.
 * 
 * @author Daniel Manley
 * @version $Revision: 1.1 $ $Date: 2004/12/07 15:53:26 $
 * @see com.tucows.oxrs.epprtk.rtk.example.DomainExample
 * @see com.tucows.oxrs.epprtk.rtk.example.ContactExample
 * @see com.tucows.oxrs.epprtk.rtk.example.HostExample
 **/
public class EPPClient extends RTKBase implements epp_Session {

	private static final long serialVersionUID = 1L;
	// Public class constants
	/**
	 * The current EPP version. Sent to the EPP Server on login.
	 */
	public static final String	VERSION	                  = "1.0";
	/**
	 * The default language/locale: "en". Sent to the EPP Server on login.
	 */
	public static final String	DEFAULT_LANG	          = "en";
	/**
	 * The default transport class if none is set in the rtk.properties file.
	 */
	public static final String	DEFAULT_TRANSPORT_CLASS	  = "EPPTransportTCPTLS";
	/**
	 * The default transport package if none is set in the rtk.properties file.
	 */
	public static final String	DEFAULT_TRANSPORT_PACKAGE	= "com.tucows.oxrs.epprtk.rtk.transport";

	// private memeber variable
	private String	           epp_host_name_;
	private int	               epp_host_port_;
	private int	               epp_timeout_;
	private String	           epp_client_id_;
	private String	           epp_password_;

	private String[]	       epp_services_;
	private String[]	       epp_service_extensions_;

	private String	           version_;
	private String	           lang_;

	private EPPTransportBase	transport_;

	// keep track of the time of the last successfull responce from the server
	// used to determine when we need to send a keepAlive message.
	private long	           lastNetTransaction	      = 0;

	// is the session current/valid, used to check if an exception
	// occured and the session needs to be restarted.
	private boolean	           isValid	                  = false;

	// Is RTK version number included as extension in login
	private boolean	           sendver_=true;

	/**
	 * Default constructor -- uses default version, lang values.
	 */
	public EPPClient() {
		String method_name = "EPPClient()";
		version_ = VERSION;
		lang_ = DEFAULT_LANG;
		try {
			String sendver_str = RTKBase.getRTKProperties().getProperty("rtk.login.sendversion", "false");
			sendver_ = (Boolean.valueOf(sendver_str)).booleanValue();
		} catch (Exception xcp) {
			debug(DEBUG_LEVEL_ONE, method_name, "Configuration file not found or file read error! Default sendver value will be used.");
			sendver_ = false;
		}
	}

	/**
	 * Construtor with client id and password
	 * 
	 * @param epp_client_id
	 *            The EPP client id
	 * @param epp_password
	 *            The password associated with the client id
	 */
	public EPPClient(String epp_client_id, String epp_password) {
		this();

		epp_client_id_ = epp_client_id;
		epp_password_ = epp_password;
	}

	/**
	 * Constructor with EPP Hostname, EPP Host port, client id and password, in that order.
	 * 
	 * @param epp_host_name
	 *            The EPP Hostname (eg. "host.domain.tld")
	 * @param epp_host_port
	 *            The EPP port
	 * @param epp_client_id
	 *            The EPP client id
	 * @param epp_password
	 *            The password associated with the client id
	 */
	public EPPClient(String epp_host_name, int epp_host_port, String epp_client_id, String epp_password) {
		this(epp_client_id, epp_password);

		epp_host_name_ = epp_host_name;
		epp_host_port_ = epp_host_port;
	}

	/**
	 * Accessor method for the EPP version The EPP version is sent to the server on login.
	 * 
	 * @param value
	 *            The version string
	 */
	public void setVersion(String value) {
		version_ = value;
	}

	/**
	 * Accessor method for the version
	 * 
	 * @return String the version
	 */
	public String getVersion() {
		return version_;
	}

	/**
	 * Accessor method for the lang The lang setting is used in the creds portion of EPP commands (such as the login).
	 * 
	 * @param value
	 *            The lang/locale String (eg. "fr_CA")
	 */
	public void setLang(String value) {
		lang_ = value;
	}

	/**
	 * Accessor method for the lang
	 * 
	 * @return The langue
	 */
	public String getLang() {
		return lang_;
	}

	/**
	 * Accessor method for the EPP Hostname
	 * 
	 * @param value
	 *            The hostname string (eg. "host.domain.tld" or "100.101.200.201")
	 */
	public void setEPPHostName(String value) {
		epp_host_name_ = value;
	}

	/**
	 * Accessor method for the EPP Hostname
	 * 
	 * @return The EPP host name String
	 */
	public String getEPPHostName() {
		return epp_host_name_;
	}

	/**
	 * Accessor method for the EPP Host port
	 * 
	 * @param value
	 *            The int port value
	 */
	public void setEPPHostPort(int value) {
		epp_host_port_ = value;
	}

	/**
	 * Accessor method for the EPP Host port
	 */
	public int getEPPHostPort() {
		return epp_host_port_;
	}

	/**
	 * Accessor method for the EPP server timeout, in milliseconds Setting this value after a connection has been established has no effect.
	 * 
	 * @param value
	 *            The int timeout value, in milliseconds
	 */
	public void setEPPTimeout(int value) {
		epp_timeout_ = value;
	}

	/**
	 * Accessor method for the EPP server timeout, in milliseconds
	 * 
	 * @return The current timeout setting. in milliseconds
	 */
	public int getEPPTimeout() {
		return epp_timeout_;
	}

	/**
	 * Accessor method for the client id. This value is also used in the creation of the default EPPTransID. Changing this value after a session has
	 * been logged in will have no effect.
	 * 
	 * @param value
	 *            The client id String
	 */
	public void setEPPClientID(String value) {
		epp_client_id_ = value;
	}

	/**
	 * Accessor method for the client id.
	 * 
	 * @return The current client id.
	 */
	public String getEPPClientID() {
		return epp_client_id_;
	}

	/**
	 * Accessor method for the password
	 * 
	 * @param value
	 *            The password String
	 */
	public void setEPPPassword(String value) {
		epp_password_ = value;
	}

	/**
	 * Accessor method for the password
	 * 
	 * @return The current EPP Password
	 */
	public String getEPPPassword() {
		return epp_password_;
	}

	/**
	 * Accessor method for the epp services. This value is used in the EPP Login command.
	 * 
	 * @param services
	 *            The service URI array
	 */
	public void setEPPServices(String[] services) {
		epp_services_ = services;
	}

	/**
	 * Accessor method for the epp services.
	 * 
	 * @return The current services in an array.
	 */
	public String[] getEPPServices() {
		return epp_services_;
	}

	/**
	 * Accessor method for the epp service extensions
	 * 
	 * @param service_extensions
	 *            The service extension array
	 */
	public void setEPPServiceExtensions(String[] service_extensions) {
		epp_service_extensions_ = service_extensions;
	}

	/**
	 * Accessor method for the epp service extensions.
	 * 
	 * @return The current service extensions in an array.
	 */
	public String[] getEPPServiceExtensions() {
		return epp_service_extensions_;
	}

	/**
	 * Accessor method for the time of the last successfull network transaction.
	 * 
	 * Useful for tracking when a keepAlive (poll) transaction needs to be sent.
	 * 
	 * @return Last transation time in seconds.
	 */
	public long getLastNetTransaction() {
		return this.lastNetTransaction;
	}

	/**
	 * returns if the connections is valid. A connection is valid if the following are true
	 * <ul>
	 * <li>a coonection has been established to the server
	 * <li>an I/O Error has not been thrown
	 * <li>a login has been successfully completed
	 * </ul>
	 * 
	 * @return The flag indicating the state of the connection to the server
	 */
	public boolean isValid() {
		return this.isValid;
	}

	/**
	 * Bypass to the EPPClient's connect() methods. Allows a connection to be established externally. Mostly useful if opting to not use JSSE in
	 * favour of another SSL package. This method also sets the SO Timeout to the value from getEPPTimeout(). Creates a new instance of
	 * EPPTransportTCP using the given socket.
	 * 
	 * @param value
	 *            The Socket to the EPP Server
	 * @throws epp_Exception
	 * @throws UnknownHostException
	 *             if the EPP host cannot be found
	 * @throws SocketException
	 * @throws IOException
	 * @see #getEPPTimeout()
	 * @see com.tucows.oxrs.epprtk.rtk.transport.EPPTransportTCP
	 */
	public void setSocketToEPPServer(Socket value) throws epp_Exception, UnknownHostException, SocketException, IOException, EPPTransportException {
		String method_name = "setSocketToEPPServer";
		debug(DEBUG_LEVEL_THREE, method_name, "Enterd");

		transport_ = new EPPTransportTCP(value, epp_timeout_);
		transport_.connect();

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");
	}

	/**
	 * Bypass to the EPPClient's connect() methods. Allows a connection and transport to be established externally. Mostly useful if opting to not use
	 * JSSE in favour of another SSL package.
	 * 
	 * @param value
	 *            The Transport to the EPP Server
	 */
	public void setTransport(EPPTransportBase value) {
		String method_name = "setTransport";
		debug(DEBUG_LEVEL_THREE, method_name, "Enterd");

		transport_ = value;

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");
	}

	/**
	 * Connects to the EPP Server using the given hostname and port and using the previously set client id and password. It is recommended to use
	 * connectAndGetGreeting() to connect and retrieve the EPPGreeting in one single call.
	 * 
	 * @param epp_host_name
	 *            The EPP Hostname (eg. "host.domain.tld")
	 * @param epp_host_port
	 *            The EPP port
	 * @throws epp_Exception
	 * @throws UnknownHostException
	 *             if the EPP host cannot be found
	 * @throws SocketException
	 * @throws IOException
	 * @throws EPPTransportException
	 *             if there are problem initializing the transport class
	 * @see #connectAndGetGreeting()
	 */
	public void connect(String epp_host_name, int epp_host_port) throws epp_Exception, UnknownHostException, SocketException, IOException, EPPTransportException {
		setEPPHostName(epp_host_name);
		setEPPHostPort(epp_host_port);

		connect();
	}

	/**
	 * Connects to the EPP Server using the given client id and password and using the previously set EPP hostname and port. It is recommended to use
	 * connectAndGetGreeting() to connect and retrieve the EPPGreeting in one single call.
	 * 
	 * @param epp_client_id
	 *            The EPP client id
	 * @param epp_password
	 *            The password associated with the client id
	 * @throws epp_Exception
	 * @throws UnknownHostException
	 *             if the EPP host cannot be found
	 * @throws SocketException
	 * @throws IOException
	 * @throws EPPTransportException
	 *             if there are problem initializing the transport class
	 * @see #connectAndGetGreeting()
	 */
	public void connect(String epp_client_id, String epp_password) throws epp_Exception, UnknownHostException, SocketException, IOException, EPPTransportException {
		setEPPClientID(epp_client_id);
		setEPPPassword(epp_password);

		connect();
	}

	/**
	 * Connects to and logs into the EPP Server using the given hostname, port, client id and password It is recommended to use
	 * connectAndGetGreeting() to connect and retrieve the EPPGreeting in one single call.
	 * 
	 * @param epp_host_name
	 *            The EPP Hostname (eg. "host.domain.tld")
	 * @param epp_host_port
	 *            The EPP port
	 * @param epp_client_id
	 *            The EPP client id
	 * @param epp_password
	 *            The password associated with the client id
	 * @throws epp_Exception
	 * @throws UnknownHostException
	 *             if the EPP host cannot be found
	 * @throws SocketException
	 * @throws IOException
	 * @throws EPPTransportException
	 *             if there are problem initializing the transport class
	 * @see #connectAndGetGreeting()
	 */
	public void connect(String epp_host_name, int epp_host_port, String epp_client_id, String epp_password) throws epp_Exception, UnknownHostException, SocketException,
	        IOException, EPPTransportException {
		setEPPHostName(epp_host_name);
		setEPPHostPort(epp_host_port);
		setEPPClientID(epp_client_id);
		setEPPPassword(epp_password);

		connect();
	}

	/**
	 * Connects to the EPP Server using previously set hostname and port. It is recommended to use connectAndGetGreeting() to connect and retrieve the
	 * EPPGreeting in one single call.
	 * 
	 * @throws epp_Exception
	 * @throws UnknownHostException
	 *             if the EPP host cannot be found
	 * @throws SocketException
	 * @throws IOException
	 * @throws EPPTransportException
	 *             if there are problem initializing the transport class
	 * @see #connectAndGetGreeting()
	 */
	public void connect() throws epp_Exception, UnknownHostException, SocketException, IOException, EPPTransportException {
		String method_name = "connect()";

		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		// keep current isValid value;
		boolean valid = this.isValid;	
		this.isValid = false;

		// not actually storing anything from the return of getTransport() because
		// it initializes the transport_ variable directly.
		getTransport();
		transport_.connect();

		// if no exception occured, give the isValid value back
		this.isValid = valid;

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");

		return;
	}

	// map transport_ to appropriate class according to previously set protocol or configuration file.
	private EPPTransportBase getTransport() throws IOException, EPPTransportException {
		String method_name = "getTransport()";
		String transport_str = null;

		// if the transport has been previously set, like through
		// setTransport(), then return it here.
		if (transport_ != null) { return transport_; }

		transport_str = (String) RTKBase.getRTKProperties().getProperty("rtk.transport", DEFAULT_TRANSPORT_CLASS);
		System.err.println("==> transport_str:"+transport_str);

		if (transport_str.indexOf('.') == -1) {
			transport_str = DEFAULT_TRANSPORT_PACKAGE + "." + transport_str;
		}

		// now try to instantiate the transport...
		try {
			debug(DEBUG_LEVEL_TWO, method_name, "Trying to instantiate transport class [" + transport_str + "]");
			transport_ = (EPPTransportBase) Class.forName(transport_str).newInstance();
		} catch (IllegalAccessException xcp) {
			throw new EPPTransportException("Transport class [" + transport_str + "] could not be instantiated. Access error.");
		} catch (InstantiationException xcp) {
			throw new EPPTransportException("Transport class [" + transport_str + "] could not be instantiated. [" + xcp.getMessage() + "]");
		} catch (ClassNotFoundException xcp) {
			throw new EPPTransportException("Transport class [" + transport_str + "] not found. Please check your classpath and the package name for the transport.");
		}

		// ENABLE BC PROVIDER for PKCS12 keystore
		try {
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (Exception xcp) {
			throw new IOException(xcp.getMessage());
		}

		transport_.initialize(epp_host_name_, epp_host_port_, epp_timeout_);

		return transport_;
	}

	/**
	 * Reads a complete XML message from the EPP Server. Uses the instance transport to read from the server. You would normally pass the returned XML
	 * response to a fromXML() in the class that created the request in the first place. eg. EPPDomainCreate If an exception occurs, it is internall
	 * caught here, the isValid value is set to false and the exception is rethrown.
	 * 
	 * @return Full XML String
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if there was a socket error in reading from the EPP Server
	 */
	public String readFromServer() throws epp_Exception {
		String method_name = "readFromServer()";

		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		String xml_from_server = null;
		try {
			xml_from_server = transport_.readFromServer();
		} catch (epp_Exception excp) {
			this.isValid = false;
			throw excp;
		}

		this.lastNetTransaction = System.currentTimeMillis();

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");

		return xml_from_server;
	}

	/**
	 * Sends raw XML to the server and returns the XML response. Method required by the epp_Action interface.
	 * 
	 * @param request_xml
	 *            The XML request String to send to the server.
	 * @return The raw XML String response from the server.
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if a communication error occurs
	 */
	public String processXML(String request_xml) throws epp_Exception {
		String method_name = "processXML(String)";

		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		debug(DEBUG_LEVEL_TWO, method_name, "Request XML is [" + request_xml + "]");
		writeToServer(request_xml);

		String response_xml = readFromServer();
		debug(DEBUG_LEVEL_TWO, method_name, "Response XML is [" + response_xml + "]");

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");
		return response_xml;

	}

	/**
	 * Sends EPP request to the server and retrieves the response. Method required by the epp_Action interface.<br>
	 * Calls processXML().
	 * 
	 * @param request
	 *            epp_Action The action to send to the registry
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if any request data or response data is missing
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if a communication error occurs or if the server returns an error code
	 * @see #processXML(String)
	 */
	public epp_Action processAction(epp_Action request) throws epp_XMLException, epp_Exception {
		String method_name = "processAction(epp_Action)";

		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		String request_xml = request.toXML();

		epp_Action response = request;

		String response_xml = processXML(request_xml);

		response.fromXML(response_xml);

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");
		return response;

	}

	/**
	 * Sends an XML string to the EPP Server. Uses the instance transport to send the message to the server.<br>
	 * readFromServer() should be called to read the response. If an exception occurs, it is internall caught here, the isValid value is set to false
	 * and the exception is rethrown.
	 * 
	 * @param outbound_xml
	 *            String The EPP XML String to send to the server.
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if there was a socket error in writing to the EPP Server. The epp_Exception will contain a result with the cope
	 *             epp_Session.RTK_COMMUNICATIONS_FAILURE
	 */
	public void writeToServer(String outbound_xml) throws epp_Exception {
		String method_name = "writeToServer(String)";

		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		try {
			transport_.writeToServer(outbound_xml);
		} catch (epp_Exception excp) {
			this.isValid = false;
			throw excp;
		}

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");
	}

	/**
	 * Connects to the EPP Server and reads the greeting from it. Calls the connect() method to establish a connection to the EPP server. And calls
	 * getGreeting() to read the greeting from the server. If an exception occurs, it is internall caught here, the isValid value is set to false and
	 * the exception is rethrown.
	 * 
	 * @return The greeting from the EPP Server.
	 * @throws IOException
	 *             if there was a socket error in connecting to the EPP Server
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if the server greeting is not present
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if the server's greeting is not parsable
	 * @throws com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException
	 *             if there are problems initializing the transport
	 * @see #connect()
	 * @see #getGreeting()
	 */
	public epp_Greeting connectAndGetGreeting() throws epp_Exception, IOException, epp_XMLException, EPPTransportException {
		String method_name = "connectAndGetGreeting()";
		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		epp_Greeting hello_from_server;

		try {
			connect();

			hello_from_server = getGreeting();

		} catch (IOException xcp) {
			debug(DEBUG_LEVEL_ONE, method_name, xcp);
			this.isValid = false;
			throw xcp;
		} catch (EPPTransportException xcp) {
			debug(DEBUG_LEVEL_ONE, method_name, xcp);
			this.isValid = false;
			throw xcp;
		} catch (epp_XMLException xcp) {
			debug(DEBUG_LEVEL_ONE, method_name, xcp);
			this.isValid = false;
			throw xcp;
		}

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");

		return hello_from_server;
	}

	/**
	 * Retrieves the greeting from the EPP Server and returns it. This method is useful if a new connection is provided to the EPPClient (eg. in the
	 * case of an SSL connection not using the JSSE standard) and the greeting must be read separately. If an exception occurs, it is internall caught
	 * here, the isValid value is set to false and the exception is rethrown.
	 * 
	 * @return The greeting from the EPP Server
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if the server greeting is not present
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if the server's greeting is not parsable
	 */
	public epp_Greeting getGreeting() throws epp_Exception, epp_XMLException {
		String method_name = "getGreeting()";
		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		EPPGreeting hello_from_server;

		try {
			String xml_from_server = readFromServer();
			debug(DEBUG_LEVEL_TWO, method_name, "Greeting from server: [" + xml_from_server + "]");

			hello_from_server = new EPPGreeting();
			hello_from_server.fromXML(xml_from_server);

		} catch (epp_XMLException xcp) {
			debug(DEBUG_LEVEL_ONE, method_name, xcp);
			this.isValid = false;
			throw xcp;
		}

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");
		this.isValid = true;

		return hello_from_server.getResponseData();
	}

	/**
	 * Issues a hello request to the EPP Server. This forces the server to respond with an epp_Greeting, which is otherwise only sent on initial
	 * connections to the EPP Server.
	 * 
	 * @return The greeting from the EPP Server
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if the server greeting is not present
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if the server's greeting is not parsable
	 */
	public epp_Greeting hello() throws epp_Exception, epp_XMLException {
		String method_name = "hello()";
		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		EPPGreeting hello_from_server;

		hello_from_server = new EPPGreeting();
		hello_from_server = (EPPGreeting) processAction((epp_Action) hello_from_server);

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");

		return hello_from_server.getResponseData();
	}

	/**
	 * Closes the connection to the EPP Server. Nulls the transport so it must be recreated for subsequent connections with the same instance of
	 * EPPClient. If setTransport() or setSocketToEPPServer() were called to create the transport, then they must be called again.
	 * 
	 * @throws IOException
	 *             if there was a Socket problem
	 */
	public void disconnect() throws IOException {
		String method_name = "disconnect()";
		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		this.isValid = false;
		transport_.disconnect();
		transport_ = null;

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");
	}

	/**
	 * Logs into the EPP Server using previously set client id and password. Call setEPPServices() or setEPPServiceExtensions() before this method if
	 * you want to add epp services or epp service extensions.
	 * 
	 * @param client_tr_id
	 *            The client TRID String
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if the request XML could not be built or if the response XML could not be parsed.
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if the EPP Server responded with an error code
	 */
	public void login(String client_tr_id) throws epp_XMLException, epp_Exception {
		login(client_tr_id, null);
	}

	/**
	 * Logs into the EPP Server using the given client id, password. Call setEPPServices() or setEPPServiceExtensions() before this method if you want
	 * to add epp services or epp service extensions.
	 * 
	 * @param String
	 *            the client TRID
	 * @param client_id
	 *            The EPP client id
	 * @param password
	 *            The password associated with the client id
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if the request XML could not be built or if the response XML could not be parsed.
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if the EPP Server responded with an error code
	 */
	public void login(String client_tr_id, String epp_client_id, String epp_password) throws epp_XMLException, epp_Exception {
		setEPPClientID(epp_client_id);
		setEPPPassword(epp_password);
		login(client_tr_id, null);
	}

	/**
	 * Logs into the EPP Server using the given client id, password and new password. Use this method if you want to change your EPP client password
	 * in the server.<BR>
	 * Call setEPPServices() or setEPPServiceExtensions() before this method if you want to add epp services or epp service extensions.
	 * 
	 * @param client_tr_id
	 *            The client TRID String
	 * @param client_id
	 *            The EPP client id
	 * @param password
	 *            The password associated with the client id
	 * @param new_password
	 *            The password associated with the client id, use null if you do not want to change the password
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if the request XML could not be built or if the response XML could not be parsed.
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if the EPP Server responded with an error code
	 */
	public void login(String client_tr_id, String client_id, String password, String new_password) throws epp_XMLException, epp_Exception {
		setEPPClientID(client_id);
		setEPPPassword(password);
		login(client_tr_id, new_password);
	}

	/**
	 * Logs into the EPP Server using the previously set client id, and password. If the new password is not null, it is also passed to the server to
	 * change your EPP client password.<BR>
	 * This method also includes default domain, host and contact object/XML information in the services section. If you wish to set your own services
	 * and service extensions, call setEPPServices() and setEPPServiceExtensions() before this method, or you'll have to populate the EPPLogin command
	 * manually and call processAction() yourself.<br>
	 * If an exception occurs, it is internall caught here, the isValid value is set to false and the exception is rethrown.
	 * 
	 * @param String
	 *            the client TRID
	 * @param new_password
	 *            The password associated with the client id. Use null if you do not want to change the password
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if the request XML could not be built or if the response XML could not be parsed.
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if the EPP Server responded with an error code
	 */
	public void login(String client_tr_id, String new_password) throws epp_XMLException, epp_Exception {
		String method_name = "login()";
		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		epp_Command command_data = new epp_Command(null, client_tr_id);
		if (sendver_) {
			epp_Extension exts[] = new epp_Extension[1];
			exts[0] = new com.tucows.oxrs.epprtk.rtk.xml.extension.RTKVersion();
		//	exts[1] = new com.liberty.rtk.extension.epprtk.Rgp();
			command_data.setExtensions(exts);
		}

		epp_LoginReq login_req = new epp_LoginReq(command_data, epp_client_id_, epp_password_, new_password, new epp_Options(version_, lang_), null, null);

		if (epp_services_ != null) login_req.setServices(epp_services_);
		else {
			String[] services = new String[3];
			services[0] = "urn:ietf:params:xml:ns:contact-1.0";
			services[1] = "urn:ietf:params:xml:ns:domain-1.0";
			services[2] = "urn:ietf:params:xml:ns:host-1.0";
		//	services[3] = "urn:ietf:params:xml:ns:rgp-1.0";
			login_req.setServices(services);
		}

		if (epp_service_extensions_ != null) login_req.setExtensions(epp_service_extensions_);

		login_req.setCmd(command_data);

		EPPLogin login_action = new EPPLogin();
		login_action.setRequestData(login_req);

		try {
			processAction((epp_Action) login_action);
		} catch (epp_XMLException e) {
			isValid = false;
			throw e;
		} catch (epp_Exception e) {
			isValid = false;
			throw e;
		}

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");

	}

	/**
	 * Logs out of EPP Server
	 * 
	 * @param client_trid
	 *            the client TRID, can be null
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if the request XML could not be built or if the response XML could not be parsed.
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if the EPP Server responded with an error code
	 */
	public void logout(String client_trid) throws epp_XMLException, epp_Exception {
		String method_name = "logout()";
		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		EPPLogout logout_action = new EPPLogout();
		epp_LogoutReq logout_request = new epp_LogoutReq();
		logout_request.m_client_trid = client_trid;
		logout_action.setRequestData(logout_request);

		processAction((epp_Action) logout_action);

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");

	}

	/**
	 * Returns whether RTK version number will be send on Login.
	 * 
	 * @return true in case RTK version number will be send on Login, else false
	 */
	public boolean isVersionSentOnLogin() {
		return sendver_;
	}

	/**
	 * Set whether to send RTK version number on Login.
	 * 
	 * @param send
	 *            If true, RTK version number will be send in svcExtension section on Login
	 */
	public void setVersionSentOnLogin(boolean send) {
		sendver_ = send;
	}

	/**
	 * Sends a poll request to the server. If there is a message in the response, the user should subsequently call poll(String, String), providing
	 * the message's id to acknowledge receipt of the message from the server.
	 * 
	 * @param client_tr_id
	 *            The client TRID for the EPP request
	 * @return The poll response as epp_PollRsp
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if the request XML could not be built or if the response XML could not be parsed.
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if the EPP Server responded with an error code
	 * @see #poll(String, String)
	 */
	public epp_PollRsp poll(String client_tr_id) throws epp_XMLException, epp_Exception {
		return poll(client_tr_id, null);
	}

	/**
	 * Sends a poll request or acknowledge to the server.
	 * 
	 * @param client_tr_id
	 *            The client TRID for the EPP request
	 * @param message_id
	 *            The id of the message to ACK. If null, the Poll op will be REQ.
	 * @return The poll response as epp_PollRsp
	 * @throws org.openrtk.idl.epprtk.epp_XMLException
	 *             if the request XML could not be built or if the response XML could not be parsed.
	 * @throws org.openrtk.idl.epprtk.epp_Exception
	 *             if the EPP Server responded with an error code
	 */
	public epp_PollRsp poll(String client_tr_id, String message_id) throws epp_XMLException, epp_Exception {
		String method_name = "poll()";
		debug(DEBUG_LEVEL_THREE, method_name, "Entered");

		epp_PollReq poll_request = new epp_PollReq();

		epp_Command command_data = new epp_Command(null, client_tr_id);
		poll_request.m_cmd = command_data;
		if (message_id == null) {
			poll_request.m_op = epp_PollOpType.REQ;
		} else {
			poll_request.m_op = epp_PollOpType.ACK;
			poll_request.m_msgID = message_id;
		}

		EPPPoll poll = new EPPPoll();
		poll.setRequestData(poll_request);

		poll = (EPPPoll) processAction(poll);

		epp_PollRsp poll_response = poll.getResponseData();

		debug(DEBUG_LEVEL_THREE, method_name, "Leaving");

		return poll_response;

	}

}
