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
import org.openrtk.idl.epprtk.contact.epp_ContactAddress;
import org.openrtk.idl.epprtk.contact.epp_ContactCheckReq;
import org.openrtk.idl.epprtk.contact.epp_ContactCheckRsp;
import org.openrtk.idl.epprtk.contact.epp_ContactCreateReq;
import org.openrtk.idl.epprtk.contact.epp_ContactCreateRsp;
import org.openrtk.idl.epprtk.contact.epp_ContactNameAddress;
import org.openrtk.idl.epprtk.contact.epp_ContactPhone;
import org.openrtk.idl.epprtk.contact.epp_ContactPostalInfoType;
import org.openrtk.idl.epprtk.domain.epp_DomainCheckReq;
import org.openrtk.idl.epprtk.domain.epp_DomainCheckRsp;

import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;
import com.tucows.oxrs.epprtk.rtk.xml.EPPContactCheck;
import com.tucows.oxrs.epprtk.rtk.xml.EPPContactCreate;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainCheck;
import com.tucows.oxrs.epprtk.rtk.xml.EPPXMLBase;

import fr.fryscop.network.protocole.epp.EppRequestObject;

public class EppDomainCheck extends AbstractEppAction {

	public EppDomainCheck() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction(EppRequestObject eppRequestObject) throws epp_Exception, epp_XMLException, IOException, EPPTransportException {
		// ***************************
		// Domain Check
		//
		// First, the registrar should check if the given domain
		// is available in the registry. If it does not, we'll skip the domain
		// create step.
		//
		// ***************************
		System.out.println("Creating the Domain Check command");
		epp_DomainCheckReq domain_check_request = new epp_DomainCheckReq();

		// The .biz registry requires unique client trid's for
		// a session, so we're using the date here to keep it unique
		eppRequestObject.setCurrent_time(new Date());
		eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
		domain_check_request.setCmd(eppRequestObject.getCommand_data());

		List<String> domain_list = new ArrayList<String>();
		domain_list.add(eppRequestObject.getDomain_name());
		domain_check_request.setNames(EPPXMLBase.convertListToStringArray(domain_list));

		EPPDomainCheck domain_check = new EPPDomainCheck();
		domain_check.setRequestData(domain_check_request);

		domain_check = (EPPDomainCheck) eppRequestObject.getEpp_client().processAction(domain_check);

		epp_DomainCheckRsp domain_check_response = domain_check.getResponseData();
		eppRequestObject.setCheck_results(domain_check_response.getResults());
		System.out.println("DomainCheck results: domain [" + eppRequestObject.getDomain_name() + "] available? ["
		        + EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), eppRequestObject.getDomain_name()) + "]");

		if (EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), eppRequestObject.getDomain_name()) != null
		        && EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), eppRequestObject.getDomain_name()).booleanValue()) {

			// We're going to be creating the domain in the registry.
			// Let's see if the user gave us a contact_id to use
			// in the domain creation, or if we have to create a contact
			// as well.

			boolean contact1_avail = false;
			boolean contact2_avail = false;

			if (eppRequestObject.getContact_id1() != null) {
				// ***************************
				// Contact Check
				//
				// Make sure the contact_roid we were given is not available
				// in the registry.
				//
				// ***************************
				System.out.println("Creating the Contact Check command for [" + eppRequestObject.getContact_id1() + "]");
				epp_ContactCheckReq contact_check_request = new epp_ContactCheckReq();

				eppRequestObject.setCurrent_time(new Date());
				eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
				contact_check_request.setCmd(eppRequestObject.getCommand_data());

				List<String> contact_list = new ArrayList<String>();
				contact_list.add(eppRequestObject.getContact_id1());
				if (eppRequestObject.getContact_id2() != null) {
					contact_list.add(eppRequestObject.getContact_id2());
				}
				contact_check_request.setIds(EPPXMLBase.convertListToStringArray(contact_list));

				EPPContactCheck contact_check = new EPPContactCheck();
				contact_check.setRequestData(contact_check_request);

				contact_check = (EPPContactCheck) eppRequestObject.getEpp_client().processAction(contact_check);

				epp_ContactCheckRsp contact_check_response = contact_check.getResponseData();
				eppRequestObject.setCheck_results(contact_check_response.getResults());
				System.out.println("ContactCheck results: contact [" + eppRequestObject.getContact_id1() + "] available? ["
				        + EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), eppRequestObject.getContact_id1()) + "]");
				System.out.println("ContactCheck results: contact [" + eppRequestObject.getContact_id2() + "] available? ["
				        + EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), eppRequestObject.getContact_id2()) + "]");
				if (EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), eppRequestObject.getContact_id1()) != null) {
					contact1_avail = EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), eppRequestObject.getContact_id1()).booleanValue();
				}
				if (eppRequestObject.getContact_id2() != null && EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), eppRequestObject.getContact_id2()) != null) {
					contact2_avail = EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), eppRequestObject.getContact_id2()).booleanValue();
				}
			}

			// id 1 will be used as the registrant for the domain.
			if (eppRequestObject.getContact_id1() == null || contact1_avail) {
				// ***************************
				// Contact Create
				//
				// The given contact_id1 is available in the registry
				// or there was no contact_id1 specified, so let's
				// create one now.
				//
				// ***************************
				System.out.println("Creating the Contact Create command");
				epp_ContactCreateReq contact_create_request = new epp_ContactCreateReq();

				eppRequestObject.setCurrent_time(new Date());
				eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
				contact_create_request.setCmd(eppRequestObject.getCommand_data());
				contact_create_request.setId(eppRequestObject.getContact_id1());

				BufferedReader buffed_reader = new BufferedReader(new InputStreamReader(System.in));
				eppRequestObject.setContact1_auth_info(new epp_AuthInfo());
				System.out.print("Dear registrant, please enter a passphrase for the new registrant contact(min 6, max 16): ");
				while (eppRequestObject.getContact1_auth_info().getValue() == null || eppRequestObject.getContact1_auth_info().getValue().length() == 0) {
					eppRequestObject.getContact1_auth_info().setValue(buffed_reader.readLine());
				}
				eppRequestObject.getContact1_auth_info().setType(epp_AuthInfoType.PW);
				contact_create_request.setAuthInfo(eppRequestObject.getContact1_auth_info());

				epp_ContactNameAddress[] name_address = new epp_ContactNameAddress[1];
				name_address[0] = new epp_ContactNameAddress();
				name_address[0].setType(epp_ContactPostalInfoType.INT);
				name_address[0].setName("John Doe");
				name_address[0].setOrg("ACME Solutions");
				epp_ContactAddress address = new epp_ContactAddress();
				address.setStreet1("100 Centre St");
				address.setCity("Townsville");
				address.setStateProvince("County Derry");
				address.setPostalCode("Z1Z1Z1");
				address.setCountryCode("CA");
				name_address[0].setAddress(address);

				contact_create_request.setAddresses(name_address);
				contact_create_request.setVoice(new epp_ContactPhone("1234", "+1.4165559999"));
				contact_create_request.setFax(new epp_ContactPhone("9876", "+1.4165558888"));
				contact_create_request.setEmail("john.doe@company.info");

				EPPContactCreate contact_create = new EPPContactCreate();
				contact_create.setRequestData(contact_create_request);

				contact_create = (EPPContactCreate) eppRequestObject.getEpp_client().processAction(contact_create);

				epp_ContactCreateRsp contact_create_response = contact_create.getResponseData();
				System.out.println("ContactCreate results: contact id [" + contact_create_response.getId() + "]");

			}

			// id 2 will be used as the "tech" contact for the domain
			// we'll be creating later.
			if (eppRequestObject.getContact_id2() == null || contact2_avail) {
				// ***************************
				// Contact Create
				//
				// The given contact_id2 is available in the registry
				// or there was no contact_id2 specified, so let's
				// create one now.
				//
				// ***************************
				System.out.println("Creating the Contact Create command");
				epp_ContactCreateReq contact_create_request = new epp_ContactCreateReq();

				eppRequestObject.setCurrent_time(new Date());
				eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
				contact_create_request.setCmd(eppRequestObject.getCommand_data());
				contact_create_request.setId(eppRequestObject.getContact_id2());

				BufferedReader buffed_reader = new BufferedReader(new InputStreamReader(System.in));
				eppRequestObject.setContact2_auth_info(new epp_AuthInfo());
				System.out.print("Dear registrant, please enter a passphrase for the new tech contact:(min 6, max 16) ");
				while (eppRequestObject.getContact2_auth_info().getValue() == null || eppRequestObject.getContact2_auth_info().getValue().length() == 0) {
					eppRequestObject.getContact2_auth_info().setValue(buffed_reader.readLine());
				}
				eppRequestObject.getContact2_auth_info().setType(epp_AuthInfoType.PW);
				contact_create_request.setAuthInfo(eppRequestObject.getContact2_auth_info());

				epp_ContactNameAddress[] name_address = new epp_ContactNameAddress[1];
				name_address[0] = new epp_ContactNameAddress();
				name_address[0].setType(epp_ContactPostalInfoType.INT);
				name_address[0].setName("Jane Doe");
				name_address[0].setOrg("ACME Technicians");
				epp_ContactAddress address = new epp_ContactAddress();
				address.setStreet1("101 Centre St");
				address.setCity("Townsville");	
				address.setStateProvince("County Derry");
				address.setPostalCode("Z1Z1Z1");
				address.setCountryCode("CA");
				name_address[0].setAddress(address);

				contact_create_request.setAddresses(name_address);
				contact_create_request.setVoice(new epp_ContactPhone("1234", "+1.4165551111"));
				contact_create_request.setFax(new epp_ContactPhone("9876", "+1.4165552222"));
				contact_create_request.setEmail("jane.doe@company.info");

				EPPContactCreate contact_create = new EPPContactCreate();
				contact_create.setRequestData(contact_create_request);

				contact_create = (EPPContactCreate) eppRequestObject.getEpp_client().processAction(contact_create);

				epp_ContactCreateRsp contact_create_response = contact_create.getResponseData();
				System.out.println("ContactCreate results: contact id [" + contact_create_response.getId() + "]");

			}

		}

	}

}
