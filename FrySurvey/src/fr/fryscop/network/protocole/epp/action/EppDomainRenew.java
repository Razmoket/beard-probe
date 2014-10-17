package fr.fryscop.network.protocole.epp.action;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_XMLException;
import org.openrtk.idl.epprtk.domain.epp_DomainPeriod;
import org.openrtk.idl.epprtk.domain.epp_DomainPeriodUnitType;
import org.openrtk.idl.epprtk.domain.epp_DomainRenewReq;
import org.openrtk.idl.epprtk.domain.epp_DomainRenewRsp;

import com.tucows.oxrs.epprtk.rtk.RTKBase;
import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainRenew;

import fr.fryscop.network.protocole.epp.EppRequestObject;
import fr.fryscop.tools.ISO8601Utils;

public class EppDomainRenew extends AbstractEppAction {

	public EppDomainRenew() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction(EppRequestObject eppRequestObject) throws epp_Exception, epp_XMLException, IOException, EPPTransportException {

		// ***************************
		// Domain Renew
		//
		// Now, assuming no exception was thrown from the transfer
		// query request, we can probably try to renew the
		// domain.
		//
		// ***************************
		System.out.println("Creating the Domain Renew command");
		epp_DomainRenewReq domain_renew_request = new epp_DomainRenewReq();

		eppRequestObject.setCurrent_time(new Date());
		eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
		domain_renew_request.setCmd(eppRequestObject.getCommand_data());

		domain_renew_request.setName(eppRequestObject.getDomain_name());
		// How about for another 6 years?
		// Note that some openrtk might not accept renewal
		// periods by months.
		epp_DomainPeriod period = new epp_DomainPeriod();
		period.setUnit(epp_DomainPeriodUnitType.YEAR);
		period.setValue((short) 6);
		domain_renew_request.setPeriod(period);
		// The domain's current expiration must also be specified
		// to unintentional multiple renew request from succeeding.
		// The format of the expiration date must be "YYYY-MM-DD"
		domain_renew_request.setCurrentExpirationDate(RTKBase.DATE_FMT.format(eppRequestObject.getDomain_exp_date()));

		EPPDomainRenew domain_renew = new EPPDomainRenew();
		domain_renew.setRequestData(domain_renew_request);

		domain_renew = (EPPDomainRenew) eppRequestObject.getEpp_client().processAction(domain_renew);

		epp_DomainRenewRsp domain_renew_response = domain_renew.getResponseData();
		// The domain renew action returns the domain's new expiration
		// date if the request was successful
		System.out.println("DomainRenew results: new exDate [" + domain_renew_response.getExpirationDate() + "]");
		try {
			eppRequestObject.setDomain_exp_date(ISO8601Utils.parse(domain_renew_response.getExpirationDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
