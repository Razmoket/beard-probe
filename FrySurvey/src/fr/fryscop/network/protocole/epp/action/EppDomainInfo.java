package fr.fryscop.network.protocole.epp.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import org.openrtk.idl.epprtk.epp_AuthInfo;
import org.openrtk.idl.epprtk.epp_AuthInfoType;
import org.openrtk.idl.epprtk.epp_TransferOpType;
import org.openrtk.idl.epprtk.epp_TransferRequest;
import org.openrtk.idl.epprtk.domain.epp_DomainInfoReq;
import org.openrtk.idl.epprtk.domain.epp_DomainInfoRsp;
import org.openrtk.idl.epprtk.domain.epp_DomainTransferReq;
import org.openrtk.idl.epprtk.domain.epp_DomainTransferRsp;

import com.tucows.oxrs.epprtk.rtk.RTKBase;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainBase;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainInfo;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainTransfer;
import com.tucows.oxrs.epprtk.rtk.xml.EPPXMLBase;

public class EppDomainInfo extends AbstractEppAction {

	public EppDomainInfo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction() {
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

        current_time = new Date();
        client_trid = "ABC:"+epp_client_id+":"+current_time.getTime();
        command_data.setClientTrid( client_trid );
        domain_info_request.setCmd( command_data );

        domain_info_request.setName( domain_name );

        EPPDomainInfo domain_info = new EPPDomainInfo();
        domain_info.setRequestData(domain_info_request);

        domain_info = (EPPDomainInfo) epp_client.processAction(domain_info);

        epp_DomainInfoRsp domain_info_response = domain_info.getResponseData();

        System.out.println("DomainInfo Results: registrant ["+domain_info_response.getRegistrant()+"]");
        System.out.println("DomainInfo Results: status count ["+domain_info_response.getStatus().length+"]");
        for ( int i = 0; i < domain_info_response.getStatus().length; i++ )
        {
            System.out.println("\tstatus["+i+"] string ["+EPPDomainBase.domainStatusToString( domain_info_response.getStatus()[i].getType() )+"]");
            System.out.println("\tstatus["+i+"] note ["+domain_info_response.getStatus()[i].getValue()+"]");
        }
        // Save the expiration date for the renew command later
        domain_exp_date = RTKBase.UTC_FMT.parse(domain_info_response.getExpirationDate());
        // Save the list of nameservers
        domain_nameservers = domain_info_response.getNameServers();

        // Save the auth ID.
        domain_auth_info = domain_info_response.getAuthInfo();
        if ( domain_info_response.getAuthInfo() == null )
        {
            // We're out of luck, this domain is owned by another
            // registrar.  The session ends.

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
            while ( other_domain_auth_info.getValue() == null ||
                    other_domain_auth_info.getValue().length() == 0 )
            {
                other_domain_auth_info.setValue( buffed_reader.readLine() );
            }
            other_domain_auth_info.setType( epp_AuthInfoType.PW );

            System.out.println("Creating the Domain Transfer command");
            epp_DomainTransferReq domain_transfer_request = new epp_DomainTransferReq();

            current_time = new Date();
            client_trid = "ABC:"+epp_client_id+":"+current_time.getTime();
            command_data.setClientTrid( client_trid );
            domain_transfer_request.setCmd( command_data );

            transfer_request = new epp_TransferRequest();
            transfer_request.setOp( epp_TransferOpType.REQUEST );
            // we just asked for the auth info, so let's use it here.
            transfer_request.setAuthInfo( other_domain_auth_info );
            domain_transfer_request.setTrans( transfer_request );

            domain_transfer_request.setName( domain_name );

            EPPDomainTransfer domain_transfer = new EPPDomainTransfer();
            domain_transfer.setRequestData(domain_transfer_request);

            domain_transfer = (EPPDomainTransfer) epp_client.processAction(domain_transfer);

            epp_DomainTransferRsp domain_transfer_response = domain_transfer.getResponseData();
            System.out.println("DomainTransfer Results: transfer status ["+EPPXMLBase.transferStatusToString( domain_transfer_response.getTrnData().getTransferStatus() )+"]");

            // If an exception was thrown to this command, then probably
            // the auth ID we used was wrong, so maybe someone
            // transfered the domain away from us.

            System.out.println("Logging out from the EPP Server");
            epp_client.logout(client_trid);
            System.out.println("Disconnecting from the EPP Server");
            epp_client.disconnect();
            System.exit(1);
        }



	}

}
