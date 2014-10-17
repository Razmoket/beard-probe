package fr.fryscop.network.protocole.epp.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;

import org.openrtk.idl.epprtk.epp_AuthInfo;
import org.openrtk.idl.epprtk.epp_AuthInfoType;
import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_TransferOpType;
import org.openrtk.idl.epprtk.epp_TransferRequest;
import org.openrtk.idl.epprtk.epp_XMLException;
import org.openrtk.idl.epprtk.domain.epp_DomainInfoReq;
import org.openrtk.idl.epprtk.domain.epp_DomainTransferReq;
import org.openrtk.idl.epprtk.domain.epp_DomainTransferRsp;

import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainBase;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainInfo;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainTransfer;
import com.tucows.oxrs.epprtk.rtk.xml.EPPXMLBase;

import fr.fryscop.network.protocole.epp.EppRequestObject;
import fr.fryscop.tools.ISO8601Utils;

public class EppDomainInfo extends AbstractEppAction {

	public EppDomainInfo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction(EppRequestObject eppRequestObject) throws epp_Exception, epp_XMLException, IOException, EPPTransportException {
		// OK, before trying to do anything to this domain,
		// we should check to see if we have a situation where
		// the domain already existed and it's not owned by us.
		// If we're not the sponsoring registrar then we can't do
		// anything to it and the session end here.

		// ***************************
		// Domain Info
		//
		// Info will return to us a list of nameservers and
		// the auth Info if we're the owner.
		//
		// ***************************
		System.out.println("Creating the Domain Info command");
		epp_DomainInfoReq domain_info_request = new epp_DomainInfoReq();

		eppRequestObject.setCurrent_time(new Date());
		eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
		domain_info_request.setCmd(eppRequestObject.getCommand_data());

		domain_info_request.setName(eppRequestObject.getDomain_name());

		EPPDomainInfo domain_info = new EPPDomainInfo();
		domain_info.setRequestData(domain_info_request);

		domain_info = (EPPDomainInfo) eppRequestObject.getEpp_client().processAction(domain_info);

		eppRequestObject.setDomain_info_response(domain_info.getResponseData());

		System.out.println("DomainInfo Results: registrant [" + eppRequestObject.getDomain_info_response().getRegistrant() + "]");
		System.out.println("DomainInfo Results: status count [" + eppRequestObject.getDomain_info_response().getStatus().length + "]");
		for (int i = 0; i < eppRequestObject.getDomain_info_response().getStatus().length; i++) {
			System.out.println("\tstatus[" + i + "] string [" + EPPDomainBase.domainStatusToString(eppRequestObject.getDomain_info_response().getStatus()[i].getType()) + "]");
			System.out.println("\tstatus[" + i + "] note [" + eppRequestObject.getDomain_info_response().getStatus()[i].getValue() + "]");
		}
		// Save the expiration date for the renew command later
		try {
			eppRequestObject.setDomain_exp_date(ISO8601Utils.parse(eppRequestObject.getDomain_info_response().getExpirationDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Save the list of nameservers
		eppRequestObject.setDomain_nameservers(eppRequestObject.getDomain_info_response().getNameServers());

		// Save the auth ID.
		eppRequestObject.setDomain_auth_info(eppRequestObject.getDomain_info_response().getAuthInfo());
		if (eppRequestObject.getDomain_info_response().getAuthInfo() == null) {
			// We're out of luck, this domain is owned by another
			// registrar. The session ends.

			System.out.println("Domain belongs to another registrar, building transfer command.");

			// ***************************
			// Domain Transfer (Request)
			//
			// Ok, so the domain is not owned by us, so let's try to transfer it
			//
			// ***************************

			// First we have to know the auth info, so let's ask the registrant
			BufferedReader buffed_reader = new BufferedReader(new InputStreamReader(System.in));
			epp_AuthInfo other_domain_auth_info = new epp_AuthInfo();
			System.out.print("Dear registrant, please enter the passphrase for the domain you wish to transfer:(min 6, max 16) ");
			while (other_domain_auth_info.getValue() == null || other_domain_auth_info.getValue().length() == 0) {
				other_domain_auth_info.setValue(buffed_reader.readLine());
			}
			other_domain_auth_info.setType(epp_AuthInfoType.PW);

			System.out.println("Creating the Domain Transfer command");
			epp_DomainTransferReq domain_transfer_request = new epp_DomainTransferReq();

			eppRequestObject.setCurrent_time(new Date());
			eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
			domain_transfer_request.setCmd(eppRequestObject.getCommand_data());

			eppRequestObject.setTransfer_request(new epp_TransferRequest());
			eppRequestObject.getTransfer_request().setOp(epp_TransferOpType.REQUEST);
			// we just asked for the auth info, so let's use it here.
			eppRequestObject.getTransfer_request().setAuthInfo(other_domain_auth_info);
			domain_transfer_request.setTrans(eppRequestObject.getTransfer_request());

			domain_transfer_request.setName(eppRequestObject.getDomain_name());

			EPPDomainTransfer domain_transfer = new EPPDomainTransfer();
			domain_transfer.setRequestData(domain_transfer_request);

			domain_transfer = (EPPDomainTransfer) eppRequestObject.getEpp_client().processAction(domain_transfer);

			epp_DomainTransferRsp domain_transfer_response = domain_transfer.getResponseData();
			System.out.println("DomainTransfer Results: transfer status [" + EPPXMLBase.transferStatusToString(domain_transfer_response.getTrnData().getTransferStatus()) + "]");

			// If an exception was thrown to this command, then probably
			// the auth ID we used was wrong, so maybe someone
			// transfered the domain away from us.

		}

	}

}
