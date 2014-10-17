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
		eppRequestObject.setDomain_auth_info(new epp_AuthInfo());
		System.out.print("Dear registrant, please enter a passphrase for your new domain:(min 6, max 16) ");
		while (eppRequestObject.getDomain_auth_info().getValue() == null || eppRequestObject.getDomain_auth_info().getValue().length() == 0) {
			eppRequestObject.getDomain_auth_info().setValue(buffed_reader.readLine());
		}
		eppRequestObject.getDomain_auth_info().setType(epp_AuthInfoType.PW);

		// ***************************
		// Domain Create
		//
		// Domain is available in the registry, so create it now.
		//
		// ***************************
		System.out.println("Creating the Domain Create command");
		epp_DomainCreateReq domain_create_request = new epp_DomainCreateReq();

		eppRequestObject.setCurrent_time(new Date());
		eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
		domain_create_request.setCmd(eppRequestObject.getCommand_data());

		domain_create_request.setName(eppRequestObject.getDomain_name());
		epp_DomainPeriod period = new epp_DomainPeriod();
		// Note that some openrtk might not accept registration
		// periods by months.
		period.setUnit(epp_DomainPeriodUnitType.YEAR);
		period.setValue((short) 2);
		domain_create_request.setPeriod(period);

		domain_create_request.setRegistrant(eppRequestObject.getContact_id1());
		List<epp_DomainContact> domain_contacts = new ArrayList<epp_DomainContact>();
		// EPP Domain registries often require at least one
		// of each type of contact.
		domain_contacts.add(new epp_DomainContact(epp_DomainContactType.TECH, eppRequestObject.getContact_id2()));
		domain_contacts.add(new epp_DomainContact(epp_DomainContactType.ADMIN, eppRequestObject.getContact_id1()));
		domain_contacts.add(new epp_DomainContact(epp_DomainContactType.BILLING, eppRequestObject.getContact_id2()));
		domain_create_request.setContacts((epp_DomainContact[]) EPPXMLBase.convertListToArray((new epp_DomainContact()).getClass(), domain_contacts));

		domain_create_request.setAuthInfo(eppRequestObject.getDomain_auth_info());

		// From an EPP perspective, nameserver associations are
		// optional for a domain, so we're not specifying them
		// here. We will add them later in the domain update.

		EPPDomainCreate domain_create = new EPPDomainCreate();
		domain_create.setRequestData(domain_create_request);

		domain_create = (EPPDomainCreate) eppRequestObject.getEpp_client().processAction(domain_create);

		// We don't particularily care about the response here.
		// As long as an expection was not thrown, then the
		// creation was successful. We'll get the expiration
		// date later in a domain info.

	} // end if for domain is available in registry.

}
