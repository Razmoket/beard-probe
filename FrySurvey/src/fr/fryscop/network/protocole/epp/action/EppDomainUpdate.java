package fr.fryscop.network.protocole.epp.action;

import java.io.IOException;
import java.util.Date;

import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_XMLException;
import org.openrtk.idl.epprtk.contact.epp_ContactInfoReq;
import org.openrtk.idl.epprtk.contact.epp_ContactInfoRsp;
import org.openrtk.idl.epprtk.domain.epp_DomainUpdateReq;

import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;
import com.tucows.oxrs.epprtk.rtk.xml.EPPContactInfo;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainUpdate;

import fr.fryscop.network.protocole.epp.EppRequestObject;

public class EppDomainUpdate extends AbstractEppAction {

	public EppDomainUpdate() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction(EppRequestObject eppRequestObject) throws epp_Exception, epp_XMLException, IOException, EPPTransportException {

        // Let's modify the domain to have these hosts act
        // as its nameservers.

        // ***************************
        // Domain Update
        //
        // Adding two nameservers to this domain
        //
        // ***************************
        System.out.println("Creating the Domain Update command");
        epp_DomainUpdateReq domain_update_request = new epp_DomainUpdateReq();

        eppRequestObject.setCurrent_time( new Date());
        eppRequestObject.getCommand_data().setClientTrid( "ABC:"+eppRequestObject.getEpp_client_id()+":"+eppRequestObject.getCurrent_time().getTime() );
        domain_update_request.setCmd( eppRequestObject.getCommand_data() );

        domain_update_request.setName( eppRequestObject.getDomain_name() );

        // We determined a little earlier which operations to perform.
        if ( eppRequestObject.getAdd() != null )
        {
            domain_update_request.setAdd( eppRequestObject.getAdd() );
        }
        if ( eppRequestObject.getRemove() != null )
        {
            domain_update_request.setRemove( eppRequestObject.getRemove() );
        }

        EPPDomainUpdate domain_update = new EPPDomainUpdate();
        domain_update.setRequestData(domain_update_request);

        domain_update = (EPPDomainUpdate) eppRequestObject.getEpp_client().processAction(domain_update);

        // As long as no exception was thrown, the update was a success


        // ***************************
        // Let's do an info on one of the contacts to find its owner
        // and its status
        // ***************************
        System.out.println("Creating the Contact Info command");
        epp_ContactInfoReq contact_info_request = new epp_ContactInfoReq();

        eppRequestObject.setCurrent_time(new Date());
        eppRequestObject.getCommand_data().setClientTrid( "ABC:"+eppRequestObject.getEpp_client_id()+":"+eppRequestObject.getCurrent_time().getTime());
        contact_info_request.setCmd( eppRequestObject.getCommand_data() );

        contact_info_request.setId( eppRequestObject.getDomain_info_response().m_registrant );

        EPPContactInfo contact_info = new EPPContactInfo();
        contact_info.setRequestData(contact_info_request);

        contact_info = (EPPContactInfo) eppRequestObject.getEpp_client().processAction(contact_info);

        epp_ContactInfoRsp contact_info_response = contact_info.getResponseData();

        System.out.println("ContactInfo results: clID ["+contact_info_response.getClientId()+"] crID ["+contact_info_response.getCreatedBy()+"]");
        System.out.println("ContactInfo results: crDate ["+contact_info_response.getCreatedDate()+"] upDate ["+contact_info_response.getUpdatedDate()+"]");
        System.out.println("ContactInfo results: address street 1 ["+contact_info_response.getAddresses()[0].getAddress().getStreet1()+"]");
        System.out.println("ContactInfo results: address street 2 ["+contact_info_response.getAddresses()[0].getAddress().getStreet2()+"]");
        System.out.println("ContactInfo results: address street 3 ["+contact_info_response.getAddresses()[0].getAddress().getStreet3()+"]");
        System.out.println("ContactInfo results: fax ["+contact_info_response.getFax()+"]");

        //FIXME
        /*System.out.println("ContactInfo Results: status count ["+contact_info_response.getStatus().length+"]");
        for ( int i = 0; i < contact_info_response.getStatus().length; i++ )
        {
            System.out.println("\tstatus["+i+"] string ["+EPPContactBase.contactStatusToString( contact_info_response.getStatus()[i].getType() )+"]");
            System.out.println("\tstatus["+i+"] note ["+contact_info_response.getStatus()[i].getValue()+"]");
        }*/



	}

}
