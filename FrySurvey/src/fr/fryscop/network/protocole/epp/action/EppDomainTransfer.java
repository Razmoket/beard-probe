package fr.fryscop.network.protocole.epp.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_Result;
import org.openrtk.idl.epprtk.epp_Session;
import org.openrtk.idl.epprtk.epp_TransferOpType;
import org.openrtk.idl.epprtk.epp_TransferRequest;
import org.openrtk.idl.epprtk.epp_TransferStatusType;
import org.openrtk.idl.epprtk.domain.epp_DomainTransferReq;
import org.openrtk.idl.epprtk.domain.epp_DomainTransferRsp;

import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainTransfer;
import com.tucows.oxrs.epprtk.rtk.xml.EPPXMLBase;

public class EppDomainTransfer extends AbstractEppAction {

	public EppDomainTransfer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction() {

        // ***************************
        // Domain Transfer (Query)
        //
        // Now, let's pretent that some time has passed...
        // Let's check up on our domain to see
        // if anyone happens to be requesting a transfer on it.
        //
        // ***************************
        try
        {
            System.out.println("Creating the Domain Transfer command");
            epp_DomainTransferReq domain_transfer_request = new epp_DomainTransferReq();

            current_time = new Date();
            client_trid = "ABC:"+epp_client_id+":"+current_time.getTime();
            command_data.setClientTrid( client_trid );
            domain_transfer_request.setCmd( command_data );

            transfer_request = new epp_TransferRequest();
            transfer_request.setOp( epp_TransferOpType.QUERY );
            // Use the auth info from the creation of the domain
            transfer_request.setAuthInfo( domain_auth_info );
            domain_transfer_request.setTrans( transfer_request );

            domain_transfer_request.setName( domain_name );

            EPPDomainTransfer domain_transfer = new EPPDomainTransfer();
            domain_transfer.setRequestData(domain_transfer_request);

            domain_transfer = (EPPDomainTransfer) epp_client.processAction(domain_transfer);

            epp_DomainTransferRsp domain_transfer_response = domain_transfer.getResponseData();
            System.out.println("DomainTransfer Results: transfer status ["+EPPXMLBase.transferStatusToString( domain_transfer_response.getTrnData().getTransferStatus() )+"]");

            if ( domain_transfer_response.getTrnData().getTransferStatus() == epp_TransferStatusType.PENDING )
            {
                // hmmm... there's a transfer pending on this domain,

                System.out.println("Creating the Domain Transfer command");
                domain_transfer_request = new epp_DomainTransferReq();

                current_time = new Date();
                client_trid = "ABC:"+epp_client_id+":"+current_time.getTime();
                command_data.setClientTrid( client_trid );
                domain_transfer_request.setCmd( command_data );

                transfer_request = new epp_TransferRequest();

                // Let's find out from the registrant/registrar if they want
                // the transfer approved.
                BufferedReader buffed_reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Do you wish to approve the domain's transfer [y]? ");
                String answer = buffed_reader.readLine();

                while ( ( answer != null ) &&
                        ( answer.length() != 0 ) &&
                        ( ! answer.equalsIgnoreCase("y") ) &&
                        ( ! answer.equalsIgnoreCase("n") ) )
                {
                    answer = buffed_reader.readLine();
                }

                if ( ! answer.equalsIgnoreCase("n") )
                {
                    System.out.println("Going to approve the transfer");
                    transfer_request.setOp( epp_TransferOpType.APPROVE );
                }
                else
                {
                    System.out.println("Going to reject the transfer");
                    transfer_request.setOp( epp_TransferOpType.REJECT );
                }


                // Use the auth info from the creation of the domain
                transfer_request.setAuthInfo( domain_auth_info );
                domain_transfer_request.setTrans( transfer_request );

                domain_transfer_request.setName( domain_name );

                domain_transfer = new EPPDomainTransfer();
                domain_transfer.setRequestData(domain_transfer_request);

                domain_transfer = (EPPDomainTransfer) epp_client.processAction(domain_transfer);

                domain_transfer_response = domain_transfer.getResponseData();
                System.out.println("DomainTransfer Results: transfer status ["+EPPXMLBase.transferStatusToString( domain_transfer_response.getTrnData().getTransferStatus() )+"]");

                if ( transfer_request.getOp() == epp_TransferOpType.APPROVE )
                {
                    // We've approved the domain's transfer, so
                    // since we don't own it anymore, we can't
                    // continue working on it.
                    System.out.println("Logging out from the EPP Server");
                    epp_client.logout(client_trid);
                    System.out.println("Disconnecting from the EPP Server");
                    epp_client.disconnect();
                    System.exit(1);
                }

            }
        }
        catch ( epp_Exception xcp )
        {
        	xcp.printStackTrace();
        	// If an exception was thrown to this command, then maybe
            // the auth info we used was wrong, or maybe someone
            // transfered the domain away from us, or maybe
            // there is not transfer information to report on.
            epp_Result[] results = xcp.getDetails();
            if ( results[0].getCode() == epp_Session.EPP_OBJECT_NOT_PENDING_TRANSFER )
            {
                System.out.println("The domain is not currently in pending transfer state");
            }
            else if ( results[0].getCode() == epp_Session.EPP_UNIMPLEMENTED_OPTION )
            {
                System.out.println("This EPP command option has not been implemented in the registry yet.  That's OK, let's continue...");
            }
            else
            {
                // Something else unexpected happended, so throw the exception up
                throw xcp;
            }
        }

	}

}
