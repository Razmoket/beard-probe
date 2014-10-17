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

		eppRequestObject.setCurrent_time(new Date());
		eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
		host_check_request.setCmd(eppRequestObject.getCommand_data());

		List<String> host_list = new ArrayList<String>();
		host_list.add("ns1." + eppRequestObject.getDomain_name());
		host_list.add("ns2." + eppRequestObject.getDomain_name());
		host_check_request.setNames(EPPXMLBase.convertListToStringArray(host_list));

		EPPHostCheck host_check = new EPPHostCheck();
		host_check.setRequestData(host_check_request);

		host_check = (EPPHostCheck) eppRequestObject.getEpp_client().processAction(host_check);

		epp_HostCheckRsp host_check_response = host_check.getResponseData();
		eppRequestObject.setCheck_results(host_check_response.getResults());
		System.out.println("HostCheck results: host [ns1." + eppRequestObject.getDomain_name() + "] avail? ["
		        + EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), "ns1." + eppRequestObject.getDomain_name()) + "]");
		System.out.println("HostCheck results: host [ns2." + eppRequestObject.getDomain_name() + "] avail? ["
		        + EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), "ns2." + eppRequestObject.getDomain_name()) + "]");

		if (EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), "ns1." + eppRequestObject.getDomain_name()) == null
		        || EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), "ns1." + eppRequestObject.getDomain_name()).booleanValue() == true) {
			// ***************************
			// Host Create
			//
			// Host ns1."domain_name" is available, so let's create it.
			//
			// ***************************
			System.out.println("Creating the Host Create command");
			epp_HostCreateReq host_create_request = new epp_HostCreateReq();

			eppRequestObject.setCurrent_time(new Date());
			eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
			host_create_request.setCmd(eppRequestObject.getCommand_data());

			host_create_request.setName("ns1." + eppRequestObject.getDomain_name());

			List<epp_HostAddress> ip_list = new ArrayList<epp_HostAddress>();
			// Some registries restrict the number of IPs per address type to 1,
			// so, we'll only use 1 in this example. Also, some registries
			// restrict the number of times an IP address may be used to 1,
			// so we'll ask the user for a unique value.
			BufferedReader buffed_reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Dear registrant, please enter an IPv4 address for the nameserver " + host_create_request.getName()
			        + "\n(it must not already be used and must not be a restricted address): ");
			String ipAddr = null;
			while (ipAddr == null || ipAddr.length() == 0) {
				ipAddr = buffed_reader.readLine();
			}
			ip_list.add(new epp_HostAddress(epp_HostAddressType.IPV4, ipAddr));
			host_create_request.setAddresses((epp_HostAddress[]) EPPXMLBase.convertListToArray((new epp_HostAddress()).getClass(), ip_list));

			EPPHostCreate host_create = new EPPHostCreate();
			host_create.setRequestData(host_create_request);

			host_create = (EPPHostCreate) eppRequestObject.getEpp_client().processAction(host_create);

			// As long as an exception is not thrown than the host
			// create succeeded.

		}

		if (EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), "ns2." + eppRequestObject.getDomain_name()) == null
		        || EPPXMLBase.getAvailResultFor(eppRequestObject.getCheck_results(), "ns2." + eppRequestObject.getDomain_name()).booleanValue() == true) {
			// ***************************
			// Host Create
			//
			// Host ns2."domain_name" is available, so let's create it.
			//
			// ***************************
			System.out.println("Creating the Host Create command");
			epp_HostCreateReq host_create_request = new epp_HostCreateReq();

			eppRequestObject.setCurrent_time(new Date());
			eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
			host_create_request.setCmd(eppRequestObject.getCommand_data());

			host_create_request.setName("ns2." + eppRequestObject.getDomain_name());

			List<epp_HostAddress> ip_list = new ArrayList<epp_HostAddress>();
			// Like in the creation of the first host, we'll ask the
			// registrant for a valid IPv4 address
			BufferedReader buffed_reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Dear registrant, please enter an IPv4 address for the nameserver " + host_create_request.getName()
			        + "\n(it must not already be used and must not be a restricted address): ");
			String ipAddr = null;
			while (ipAddr == null || ipAddr.length() == 0) {
				ipAddr = buffed_reader.readLine();
			}
			ip_list.add(new epp_HostAddress(epp_HostAddressType.IPV4, ipAddr));
			host_create_request.setAddresses((epp_HostAddress[]) EPPXMLBase.convertListToArray((new epp_HostAddress()).getClass(), ip_list));

			EPPHostCreate host_create = new EPPHostCreate();
			host_create.setRequestData(host_create_request);

			host_create = (EPPHostCreate) eppRequestObject.getEpp_client().processAction(host_create);

			// As long as an exception is not thrown than the host
			// create succeeded.

		}

		// ***************************
		// Let's do an info on one of the hosts to find its owner
		// and its status
		// ***************************
		System.out.println("Creating the Host Info command");
		epp_HostInfoReq host_info_request = new epp_HostInfoReq();

		eppRequestObject.setCurrent_time(new Date());
		eppRequestObject.getCommand_data().setClientTrid("ABC:" + eppRequestObject.getEpp_client_id() + ":" + eppRequestObject.getCurrent_time().getTime());
		host_info_request.setCmd(eppRequestObject.getCommand_data());

		host_info_request.setName("ns2." + eppRequestObject.getDomain_name());

		EPPHostInfo host_info = new EPPHostInfo();
		host_info.setRequestData(host_info_request);

		host_info = (EPPHostInfo) eppRequestObject.getEpp_client().processAction(host_info);

		epp_HostInfoRsp host_info_response = host_info.getResponseData();

		System.out.println("HostInfo results: clID [" + host_info_response.getClientId() + "] crID [" + host_info_response.getCreatedBy() + "]");
		System.out.println("HostInfo results: crDate [" + host_info_response.getCreatedDate() + "] upDate [" + host_info_response.getUpdatedDate() + "]");
		System.out.println("HostInfo results: number of ipaddresses [" + (host_info_response.getAddresses() == null ? 0 : host_info_response.getAddresses().length) + "]");
		for (int i = 0; i < host_info_response.getAddresses().length; i++) {
			System.out.println("\taddress[" + i + "] type [" + EPPHostBase.hostAddressTypeToString(host_info_response.getAddresses()[i].getType()) + "] value ["
			        + host_info_response.getAddresses()[i].getIp() + "]");
		}

		System.out.println("HostInfo Results: status count [" + host_info_response.getStatus().length + "]");
		for (int i = 0; i < host_info_response.getStatus().length; i++) {
			System.out.println("\tstatus[" + i + "] string [" + EPPHostBase.hostStatusToString(host_info_response.getStatus()[i].getType()) + "]");
			System.out.println("\tstatus[" + i + "] note [" + host_info_response.getStatus()[i].getValue() + "]");
		}

		// OK, the domain exists, the hosts exists. Are
		// they already assigned to the domain as nameservers?

		if (eppRequestObject.getDomain_nameservers() == null) {
			// No nameservers serving the domain
			// so let's add the two we created (or which already existed)
			List<String> add_list = new ArrayList<String>();
			System.out.println("adding both ns1 and ns2 to domain");
			add_list.add("ns1." + eppRequestObject.getDomain_name());
			add_list.add("ns2." + eppRequestObject.getDomain_name());
			eppRequestObject.setAdd(new epp_DomainUpdateAddRemove());
			eppRequestObject.getAdd().setNameServers(EPPXMLBase.convertListToStringArray(add_list));
		} else {
			// Already nameservers for this domain,
			// so let's see if the two we want are there and
			// add or remove accordingly.
			int nameserver_count = eppRequestObject.getDomain_nameservers().length;
			List<String> remove_list = new ArrayList<String>();
			List<String> add_list = new ArrayList<String>();
			boolean found_ns1 = false;
			boolean found_ns2 = false;

			for (int index = 0; index < nameserver_count; index++) {
				if (eppRequestObject.getDomain_nameservers()[index].equalsIgnoreCase("ns1." + eppRequestObject.getDomain_name())) {
					System.out.println("removing ns1 from domain");
					remove_list.add("ns1." + eppRequestObject.getDomain_name());
					found_ns1 = true;
				} else if (eppRequestObject.getDomain_nameservers()[index].equalsIgnoreCase("ns2." + eppRequestObject.getDomain_name())) {
					System.out.println("removing ns2 from domain");
					remove_list.add("ns2." + eppRequestObject.getDomain_name());
					found_ns2 = true;
				}

			}

			if (found_ns1 == false) {
				System.out.println("adding ns1 to domain");
				add_list.add("ns1." + eppRequestObject.getDomain_name());
			}
			if (found_ns2 == false) {
				System.out.println("adding ns2 to domain");
				add_list.add("ns2." + eppRequestObject.getDomain_name());
			}

			if (add_list.size() > 0) {
				eppRequestObject.setAdd(new epp_DomainUpdateAddRemove());
				eppRequestObject.getAdd().setNameServers(EPPXMLBase.convertListToStringArray(add_list));
			}
			if (remove_list.size() > 0) {
				eppRequestObject.setRemove(new epp_DomainUpdateAddRemove());
				eppRequestObject.getRemove().setNameServers(EPPXMLBase.convertListToStringArray(remove_list));
			}
		}

	}

}
