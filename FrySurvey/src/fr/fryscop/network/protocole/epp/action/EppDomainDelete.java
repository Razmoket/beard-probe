package fr.fryscop.network.protocole.epp.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_XMLException;
import org.openrtk.idl.epprtk.domain.epp_DomainDeleteReq;

import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainDelete;

import fr.fryscop.network.protocole.epp.EppRequestObject;

public class EppDomainDelete extends AbstractEppAction {

	public EppDomainDelete() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction(EppRequestObject eppRequestObject) throws epp_Exception, epp_XMLException, IOException, EPPTransportException {

        // Let's ask to see if the user wants to delete the domain.
        // You would not want to delete the domain if you want to
        // see domain transfer in action.
        buffed_reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Do you wish to delete your domain [y]? ");
        answer = buffed_reader.readLine();

        while ( ( answer != null ) &&
                ( answer.length() != 0 ) &&
                ( ! answer.equalsIgnoreCase("n") ) &&
                ( ! answer.equalsIgnoreCase("y") ) )
        {
            answer = buffed_reader.readLine();
        }

        if ( ! answer.equalsIgnoreCase("n") )
        {

            // ***************************
            // Domain Delete
            //
            // Finally, let's end the session by deleting the domain
            //
            // Recent tests with the .biz registry show that this
            // command will fail because the domain has nameservers
            // that are associated with a domain.  Oddly enough, they
            // are associated with their own domain.
            //
            // ***************************
            System.out.println("Creating the Domain Delete command");
            epp_DomainDeleteReq domain_delete_request = new epp_DomainDeleteReq();

            current_time = new Date();
            client_trid = "ABC:"+epp_client_id+":"+current_time.getTime();
            command_data.setClientTrid( client_trid );
            domain_delete_request.setCmd( command_data );

            domain_delete_request.setName( domain_name );

            EPPDomainDelete domain_delete = new EPPDomainDelete();
            domain_delete.setRequestData(domain_delete_request);

            domain_delete = (EPPDomainDelete) epp_client.processAction(domain_delete);

        }



	}

}
