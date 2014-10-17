package fr.fryscop.network.protocole.epp.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_XMLException;
import org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove;
import org.openrtk.idl.epprtk.host.epp_HostAddress;
import org.openrtk.idl.epprtk.host.epp_HostAddressType;
import org.openrtk.idl.epprtk.host.epp_HostCheckReq;
import org.openrtk.idl.epprtk.host.epp_HostCheckRsp;
import org.openrtk.idl.epprtk.host.epp_HostCreateReq;
import org.openrtk.idl.epprtk.host.epp_HostInfoReq;
import org.openrtk.idl.epprtk.host.epp_HostInfoRsp;

import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;
import com.tucows.oxrs.epprtk.rtk.xml.EPPHostBase;
import com.tucows.oxrs.epprtk.rtk.xml.EPPHostCheck;
import com.tucows.oxrs.epprtk.rtk.xml.EPPHostCreate;
import com.tucows.oxrs.epprtk.rtk.xml.EPPHostInfo;
import com.tucows.oxrs.epprtk.rtk.xml.EPPXMLBase;

import fr.fryscop.network.protocole.epp.EppRequestObject;

public class EppHostCreate extends AbstractEppAction {

	public EppHostCreate() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction(EppRequestObject eppRequestObject) throws epp_Exception, epp_XMLException, IOException, EPPTransportException {

        // The domain is ours.
        // Now let's create some hosts in this domain.
        // If the domain had the possibility of existing before and it
        // did than maybe the hosts exists in the registry too.
        // Let's check....

        // ***************************
        // Host Check
        //
        // Check for the existance of two hosts, ns1 and ns2
        // in the domain given to us by the user.
        //
        // ***************************
        System.out.println("Creating the Host Check command");
        epp_HostCheckReq host_check_request = new epp_HostCheckReq();

        current_time = new Date();
        client_trid = "ABC:"+epp_client_id+":"+current_time.getTime();
        command_data.setClientTrid( client_trid );
        host_check_request.setCmd( command_data );

        List host_list = (List)new ArrayList();
        host_list.add("ns1."+domain_name);
        host_list.add("ns2."+domain_name);
        host_check_request.setNames( EPPXMLBase.convertListToStringArray(host_list) );

        EPPHostCheck host_check = new EPPHostCheck();
        host_check.setRequestData(host_check_request);

        host_check = (EPPHostCheck) epp_client.processAction(host_check);

        epp_HostCheckRsp host_check_response = host_check.getResponseData();
        check_results = host_check_response.getResults();
        System.out.println("HostCheck results: host [ns1."+domain_name+"] avail? ["+EPPXMLBase.getAvailResultFor(check_results, "ns1."+domain_name)+"]");
        System.out.println("HostCheck results: host [ns2."+domain_name+"] avail? ["+EPPXMLBase.getAvailResultFor(check_results, "ns2."+domain_name)+"]");

        if ( EPPXMLBase.getAvailResultFor(check_results, "ns1."+domain_name) == null ||
             EPPXMLBase.getAvailResultFor(check_results, "ns1."+domain_name).booleanValue() == true )
        {
            // ***************************
            // Host Create
            //
            // Host ns1."domain_name" is available, so let's create it.
            //
            // ***************************
            System.out.println("Creating the Host Create command");
            epp_HostCreateReq host_create_request = new epp_HostCreateReq();

            current_time = new Date();
            client_trid = "ABC:"+epp_client_id+":"+current_time.getTime();
            command_data.setClientTrid( client_trid );
            host_create_request.setCmd( command_data );

            host_create_request.setName( "ns1."+domain_name );

            List ip_list = (List)new ArrayList();
            // Some registries restrict the number of IPs per address type to 1,
            // so, we'll only use 1 in this example.  Also, some registries
            // restrict the number of times an IP address may be used to 1,
            // so we'll ask the user for a unique value.
            BufferedReader buffed_reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Dear registrant, please enter an IPv4 address for the nameserver "+host_create_request.getName()+"\n(it must not already be used and must not be a restricted address): ");
            String ipAddr = null;
            while ( ipAddr == null || ipAddr.length() == 0 )
            {
                ipAddr = buffed_reader.readLine();
            }
            ip_list.add(new epp_HostAddress(epp_HostAddressType.IPV4, ipAddr));
            host_create_request.setAddresses( (epp_HostAddress[])EPPXMLBase.convertListToArray((new epp_HostAddress()).getClass(), ip_list) );

            EPPHostCreate host_create = new EPPHostCreate();
            host_create.setRequestData(host_create_request);

            host_create = (EPPHostCreate) epp_client.processAction(host_create);

            // As long as an exception is not thrown than the host
            // create succeeded.

        }

        if ( EPPXMLBase.getAvailResultFor(check_results, "ns2."+domain_name) == null ||
             EPPXMLBase.getAvailResultFor(check_results, "ns2."+domain_name).booleanValue() == true )
        {
            // ***************************
            // Host Create
            //
            // Host ns2."domain_name" is available, so let's create it.
            //
            // ***************************
            System.out.println("Creating the Host Create command");
            epp_HostCreateReq host_create_request = new epp_HostCreateReq();

            current_time = new Date();
            client_trid = "ABC:"+epp_client_id+":"+current_time.getTime();
            command_data.setClientTrid( client_trid );
            host_create_request.setCmd( command_data );

            host_create_request.setName( "ns2."+domain_name );

            List ip_list = (List)new ArrayList();
            // Like in the creation of the first host, we'll ask the 
            // registrant for a valid IPv4 address
            BufferedReader buffed_reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Dear registrant, please enter an IPv4 address for the nameserver "+host_create_request.getName()+"\n(it must not already be used and must not be a restricted address): ");
            String ipAddr = null;
            while ( ipAddr == null || ipAddr.length() == 0 )
            {
                ipAddr = buffed_reader.readLine();
            }
            ip_list.add(new epp_HostAddress(epp_HostAddressType.IPV4, ipAddr));
            host_create_request.setAddresses( (epp_HostAddress[])EPPXMLBase.convertListToArray((new epp_HostAddress()).getClass(), ip_list) );

            EPPHostCreate host_create = new EPPHostCreate();
            host_create.setRequestData(host_create_request);

            host_create = (EPPHostCreate) epp_client.processAction(host_create);

            // As long as an exception is not thrown than the host
            // create succeeded.

        }

        // ***************************
        // Let's do an info on one of the hosts to find its owner
        // and its status
        // ***************************
        System.out.println("Creating the Host Info command");
        epp_HostInfoReq host_info_request = new epp_HostInfoReq();

        current_time = new Date();
        client_trid = "ABC:"+epp_client_id+":"+current_time.getTime();
        command_data.setClientTrid( client_trid );
        host_info_request.setCmd( command_data );

        host_info_request.setName( "ns2."+domain_name );

        EPPHostInfo host_info = new EPPHostInfo();
        host_info.setRequestData(host_info_request);

        host_info = (EPPHostInfo) epp_client.processAction(host_info);

        epp_HostInfoRsp host_info_response = host_info.getResponseData();

        System.out.println("HostInfo results: clID ["+host_info_response.getClientId()+"] crID ["+host_info_response.getCreatedBy()+"]");
        System.out.println("HostInfo results: crDate ["+host_info_response.getCreatedDate()+"] upDate ["+host_info_response.getUpdatedDate()+"]");
        System.out.println("HostInfo results: number of ipaddresses ["+( host_info_response.getAddresses() == null ? 0 : host_info_response.getAddresses().length )+"]");
        for ( int i = 0; i < host_info_response.getAddresses().length; i++ )
        {
            System.out.println("\taddress["+i+"] type ["+EPPHostBase.hostAddressTypeToString(host_info_response.getAddresses()[i].getType())+"] value ["+host_info_response.getAddresses()[i].getIp()+"]");
        }

        System.out.println("HostInfo Results: status count ["+host_info_response.getStatus().length+"]");
        for ( int i = 0; i < host_info_response.getStatus().length; i++ )
        {
            System.out.println("\tstatus["+i+"] string ["+EPPHostBase.hostStatusToString( host_info_response.getStatus()[i].getType() )+"]");
            System.out.println("\tstatus["+i+"] note ["+host_info_response.getStatus()[i].getValue()+"]");
        }


        
        // OK, the domain exists, the hosts exists.  Are
        // they already assigned to the domain as nameservers?

        if ( domain_nameservers == null )
        {
            // No nameservers serving the domain
            // so let's add the two we created (or which already existed)
            List add_list = (List) new ArrayList();
            System.out.println("adding both ns1 and ns2 to domain");
            add_list.add("ns1."+domain_name);
            add_list.add("ns2."+domain_name);
            add = new epp_DomainUpdateAddRemove();
            add.setNameServers( EPPXMLBase.convertListToStringArray(add_list) );
        }
        else
        {
            // Already nameservers for this domain,
            // so let's see if the two we want are there and
            // add or remove accordingly.
            int nameserver_count = domain_nameservers.length;
            List remove_list = (List) new ArrayList();
            List add_list = (List) new ArrayList();
            boolean found_ns1 = false;
            boolean found_ns2 = false;

            for ( int index = 0; index < nameserver_count; index++ )
            {
                if ( domain_nameservers[index].equalsIgnoreCase("ns1."+domain_name) )
                {
                    System.out.println("removing ns1 from domain");
                    remove_list.add("ns1."+domain_name);
                    found_ns1 = true;
                }
                else if ( domain_nameservers[index].equalsIgnoreCase("ns2."+domain_name) )
                {
                    System.out.println("removing ns2 from domain");
                    remove_list.add("ns2."+domain_name);
                    found_ns2 = true;
                }

            }

            if ( found_ns1 == false )
            {
                System.out.println("adding ns1 to domain");
                add_list.add("ns1."+domain_name);
            }
            if ( found_ns2 == false )
            {
                System.out.println("adding ns2 to domain");
                add_list.add("ns2."+domain_name);
            }

            if ( add_list.size() > 0 )
            {
                add = new epp_DomainUpdateAddRemove();
                add.setNameServers( EPPXMLBase.convertListToStringArray(add_list) );
            }
            if ( remove_list.size() > 0 )
            {
                remove = new epp_DomainUpdateAddRemove();
                remove.setNameServers( EPPXMLBase.convertListToStringArray(remove_list) );
            }
        }
	   
	    
    }

}
