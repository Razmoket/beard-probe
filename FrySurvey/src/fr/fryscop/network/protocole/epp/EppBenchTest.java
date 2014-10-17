/*
**
** EPP RTK Java
** Copyright (C) 2001-2002, Tucows, Inc.
** Copyright (C) 2003, Liberty RMS
**
**
** This library is free software; you can redistribute it and/or
** modify it under the terms of the GNU Lesser General Public
** License as published by the Free Software Foundation; either
** version 2.1 of the License, or (at your option) any later version.
**
** This library is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
** Lesser General Public License for more details.
**
** You should have received a copy of the GNU Lesser General Public
** License along with this library; if not, write to the Free Software
** Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
**
*/

/*
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/com/tucows/oxrs/epprtk/rtk/example/SessionExample.java,v 1.1 2004/12/07 15:53:26 ewang2004 Exp $
 * $Revision: 1.1 $
 * $Date: 2004/12/07 15:53:26 $
 */

package fr.fryscop.network.protocole.epp;

import java.util.*;
import java.io.*;

import com.tucows.oxrs.epprtk.rtk.*;
import com.tucows.oxrs.epprtk.rtk.xml.*;

import org.openrtk.idl.epprtk.*;
import org.openrtk.idl.epprtk.domain.*;
import org.openrtk.idl.epprtk.host.*;
import org.openrtk.idl.epprtk.contact.*;


/**
 * Example code for a typical logical EPP sessions.
 * For more information on the creation of Domain, Host and Contact
 * objects, please see their respective example Java source files.
 *
 * @author Daniel Manley
 * @version $Revision: 1.1 $ $Date: 2004/12/07 15:53:26 $
 * @see com.tucows.oxrs.epprtk.rtk.example.DomainExample
 * @see com.tucows.oxrs.epprtk.rtk.example.HostExample
 * @see com.tucows.oxrs.epprtk.rtk.example.ContactExample
**/
public class EppBenchTest
{
	// 10.4.3.1 6666 -hinpizyz334-.ne 9qPv6MVYX2h1sudR nic.next P15 P15

    private static String USAGE = "Usage: com.tucows.oxrs.epprtk.rtk.example.SessionExample epp_host_name epp_host_port epp_client_id epp_password domain_name [contact_id1] [contact_id2]";

    /**
     * Main of the example.  Performs typical Domain, Host and Contact operations
     * in a logical order.
     */
    public static void main(String args[])
    {

    	System.setProperty("rtk.props.file",".\\conf\\rtk.properties");
    	System.out.println("Start of the Session example");

        Date domain_exp_date = null;
        epp_AuthInfo domain_auth_info = null;
        epp_AuthInfo contact1_auth_info = null;
        epp_AuthInfo contact2_auth_info = null;
        // This date will be used in the client trid
        // because the .biz registry requires unique
        // trid's per client session.
        Date current_time = new Date();

        try
        {

            String[] domain_nameservers = null;
            epp_Command command_data = null;
            epp_TransferRequest transfer_request = null;
            epp_CheckResult[] check_results = null;

            if (args.length < 5)
            {
                System.err.println(USAGE);
                System.exit(1);
            }

            String epp_host_name = args[0];
            String epp_host_port_string = args[1];
            String epp_client_id = args[2];
            String epp_password  = args[3];
            String domain_name  = args[4];
            String contact_id1 = null;
            String contact_id2 = null;
            if ( args.length > 5 )
            {
                contact_id1 = args[5];
            }
            if ( args.length > 6 )
            {
                contact_id2 = args[6];
            }

            if ( contact_id1 == null ) contact_id1 = epp_client_id + "001";
            if ( contact_id2 == null ) contact_id2 = epp_client_id + "002";
            
            int epp_host_port = Integer.parseInt(epp_host_port_string);

            EPPClient epp_client = new EPPClient(epp_host_name,
                                                 epp_host_port,
                                                 epp_client_id,
                                                 epp_password);

            epp_client.setLang("en");
            String [] extensions =  {"urn:ietf:params:xml:ns:rgp-1.0"};
            epp_client.setEPPServiceExtensions(extensions);
            //fin init
            try{
            	
            

            	

                



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
                System.err.println("\t"+results[0]);
                xcp.printStackTrace();
            }
            catch ( Exception xcp )
            {
                // Other unexpected exceptions
                System.err.println("EPP Action failed! ["+xcp.getClass().getName()+"] ["+xcp.getMessage()+"]");
                xcp.printStackTrace();
            }
            

        }
        catch ( epp_XMLException xcp )
        {
            System.err.println("epp_XMLException! ["+xcp.getErrorMessage()+"]");
            xcp.printStackTrace();
        }
        catch ( epp_Exception xcp )
        {
            System.err.println("epp_Exception!");
            epp_Result[] results = xcp.getDetails();
            // We're taking advantage epp_Result's toString() here
            // for debugging.  Take a look at the javadocs for
            // the full list of attributes in the class.
            System.err.println("\tresult: ["+results[0]+"]");
            xcp.printStackTrace();
        }
        catch ( Exception xcp )
        {
            System.err.println("Exception! ["+xcp.getClass().getName()+"] ["+xcp.getMessage()+"]");
            xcp.printStackTrace();
        }

    }
}
