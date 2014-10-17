package fr.fryscop.network.protocole.epp.action;

import java.io.IOException;
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

		// ***************************
		// Domain Delete
		//
		// Finally, let's end the session by deleting the domain
		//
		// Recent tests with the .biz registry show that this
		// command will fail because the domain has nameservers
		// that are associated with a domain. Oddly enough, they
		// are associated with their own domain.
		//
		// ***************************
		System.out.println("Creating the Domain Delete command");
		epp_DomainDeleteReq domain_delete_request = new epp_DomainDeleteReq();

		eppRequestObject.setCurrent_time(new Date());
		eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
		domain_delete_request.setCmd(eppRequestObject.getCommand_data());

		domain_delete_request.setName(eppRequestObject.getDomain_name());

		EPPDomainDelete domain_delete = new EPPDomainDelete();
		domain_delete.setRequestData(domain_delete_request);

		domain_delete = (EPPDomainDelete) eppRequestObject.getEpp_client().processAction(domain_delete);

	}

}
