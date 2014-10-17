package fr.fryscop.network.protocole.epp.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openrtk.idl.epprtk.epp_AuthInfo;
import org.openrtk.idl.epprtk.epp_AuthInfoType;
import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_XMLException;
import org.openrtk.idl.epprtk.domain.epp_DomainContact;
import org.openrtk.idl.epprtk.domain.epp_DomainContactType;
import org.openrtk.idl.epprtk.domain.epp_DomainCreateReq;
import org.openrtk.idl.epprtk.domain.epp_DomainPeriod;
import org.openrtk.idl.epprtk.domain.epp_DomainPeriodUnitType;

import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainCreate;
import com.tucows.oxrs.epprtk.rtk.xml.EPPXMLBase;

import fr.fryscop.network.protocole.epp.EppRequestObject;

public class EppDomainCreate extends AbstractEppAction {

	public EppDomainCreate() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction(EppRequestObject eppRequestObject) throws epp_Exception, epp_XMLException, IOException, EPPTransportException {

        // Since we're going to create the domain,
        // we have to ask the registrant for
        // authorization information (a secret password,
        // or something similar).
        BufferedReader buffed_reader = new BufferedReader(new InputStreamReader(System.in));
        domain_auth_info = new epp_AuthInfo();
        System.out.print("Dear registrant, please enter a passphrase for your new domain:(min 6, max 16) ");
        while ( domain_auth_info.getValue() == null ||
                domain_auth_info.getValue().length() == 0 )
        {
            domain_auth_info.setValue( buffed_reader.readLine() );
        }
        domain_auth_info.setType( epp_AuthInfoType.PW );

        // ***************************
        // Domain Create
        //
        // Domain is available in the registry, so create it now.
        //
        // ***************************
        System.out.println("Creating the Domain Create command");
        epp_DomainCreateReq domain_create_request = new epp_DomainCreateReq();

        current_time = new Date();
        client_trid = "ABC:"+epp_client_id+":"+current_time.getTime();
        command_data.setClientTrid( client_trid );
        domain_create_request.setCmd( command_data );

        domain_create_request.setName( domain_name );
        epp_DomainPeriod period = new epp_DomainPeriod();
        // Note that some openrtk might not accept registration
        // periods by months.
        period.setUnit( epp_DomainPeriodUnitType.YEAR );
        period.setValue( (short) 2 );
        domain_create_request.setPeriod( period );

        domain_create_request.setRegistrant( contact_id1 );
        List domain_contacts = new ArrayList();
        // EPP Domain registries often require at least one
        // of each type of contact.
        domain_contacts.add( new epp_DomainContact( epp_DomainContactType.TECH, contact_id2 ) );
        domain_contacts.add( new epp_DomainContact( epp_DomainContactType.ADMIN, contact_id1 ) );
        domain_contacts.add( new epp_DomainContact( epp_DomainContactType.BILLING, contact_id2 ) );
        domain_create_request.setContacts( (epp_DomainContact[]) EPPXMLBase.convertListToArray((new epp_DomainContact()).getClass(), domain_contacts) );

        domain_create_request.setAuthInfo( domain_auth_info );

        // From an EPP perspective, nameserver associations are
        // optional for a domain, so we're not specifying them
        // here.  We will add them later in the domain update.

        EPPDomainCreate domain_create = new EPPDomainCreate();
        domain_create.setRequestData(domain_create_request);

        domain_create = (EPPDomainCreate) epp_client.processAction(domain_create);

        // We don't particularily care about the response here.
        // As long as an expection was not thrown, then the
        // creation was successful.  We'll get the expiration
        // date later in a domain info.

    } // end if for domain is available in registry.


	}

}
