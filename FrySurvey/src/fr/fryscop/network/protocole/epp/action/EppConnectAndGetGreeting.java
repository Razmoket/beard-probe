package fr.fryscop.network.protocole.epp.action;

import java.io.IOException;
import java.util.Date;

import org.openrtk.idl.epprtk.epp_Command;
import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_Greeting;
import org.openrtk.idl.epprtk.epp_PollRsp;
import org.openrtk.idl.epprtk.epp_Response;
import org.openrtk.idl.epprtk.epp_Result;
import org.openrtk.idl.epprtk.epp_XMLException;

import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;

import fr.fryscop.network.protocole.epp.EppRequestObject;

public class EppConnectAndGetGreeting  extends AbstractEppAction{

	public EppConnectAndGetGreeting() {
		// TODO Auto-generated constructor stub
	}

	@Override
    public void doAction(EppRequestObject eppRequestObject) throws epp_Exception, epp_XMLException, IOException, EPPTransportException {
		//test connect
        System.out.println("Connecting to the EPP Server and getting the greeting");

        /*
         * Uncomment following line if you don't want to send RTK version
         * number on Login. Although Liberty RTK recomends to use this extension
         * tag on Login request.
         */
        //eppRequestObject.getEpp_client().setVersionSentOnLogin( false );

        epp_Greeting greeting = eppRequestObject.getEpp_client().connectAndGetGreeting();

        System.out.println("greeting's server: ["+greeting.getServerId()+"]");
        System.out.println("greeting's server-date: ["+greeting.getServerDate()+"]");
        System.out.println("greeting's service menu: ["+greeting.getSvcMenu()+"]");

        // The .biz registry requires unique client trid's for
        // a session, so we're using the date here to keep it unique
        String client_trid = "ABC:"+eppRequestObject.getEpp_client_id()+":"+eppRequestObject.getCurrent_time().getTime();

        eppRequestObject.setCommand_data(new epp_Command()); 
        eppRequestObject.getCommand_data().setClientTrid( client_trid );
        

        System.out.println("Logging into the EPP Server");
        eppRequestObject.getEpp_client().login(client_trid);

        try
        {
            // ***************************
            // Poll (for waiting messages)
            // ***************************
            System.out.println("Polling the server...");
            eppRequestObject.setCurrent_time(new Date()) ;
            client_trid = "ABC:"+eppRequestObject.getEpp_client_id()+":"+eppRequestObject.getCurrent_time().getTime();
            epp_PollRsp poll_response = eppRequestObject.getEpp_client().poll(client_trid);

            epp_Response response = poll_response.getRsp();
            System.out.println("Poll results: "+response);
        }
        catch ( epp_XMLException xcp )
        {
            // Either the request was missing some required data in
            // validation before sending to the server, or the server's
            // response was either unparsable or missing some required data.
            System.err.println("epp_XMLException! ["+xcp.getErrorMessage()+"]");
            xcp.printStackTrace();
        }
        catch ( epp_Exception xcp )
        {
            // The EPP Server has responded with an error code with
            // some optional messages to describe the error.
            System.err.println("epp_Exception!");
            epp_Result[] results = xcp.getDetails();
            System.err.println("\tcode: ["+results[0].getCode()+"] lang: ["+results[0].getLang()+"] msg: ["+results[0].getMsg()+"]");
            if ( results[0].getValues() != null && results[0].getValues().length > 0 )
            {
                System.err.println("\tvalue: ["+results[0].getValues()[0]+"]");
            }
            xcp.printStackTrace();
        }
        catch ( Exception xcp )
        {
            // Other unexpected exceptions
            System.err.println("EPP Poll failed! ["+xcp.getClass().getName()+"] ["+xcp.getMessage()+"]");
            xcp.printStackTrace();
        }
        // fin connect
	    
    }
	
	

}
