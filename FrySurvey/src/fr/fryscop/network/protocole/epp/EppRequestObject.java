package fr.fryscop.network.protocole.epp;

import java.util.Date;

import org.openrtk.idl.epprtk.epp_AuthInfo;
import org.openrtk.idl.epprtk.epp_CheckResult;
import org.openrtk.idl.epprtk.epp_Command;
import org.openrtk.idl.epprtk.epp_TransferRequest;
import org.openrtk.idl.epprtk.domain.epp_DomainInfoRsp;
import org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove;

import com.tucows.oxrs.epprtk.rtk.EPPClient;

public class EppRequestObject {

	
	Date domain_exp_date = null;
    epp_AuthInfo domain_auth_info = null;
    epp_AuthInfo contact1_auth_info = null;
    epp_AuthInfo contact2_auth_info = null;
    // This date will be used in the client trid
    // because the .biz registry requires unique
    // trid's per client session.
    Date current_time = new Date();
	
	String epp_host_name;
    String epp_host_port_string;
    String epp_client_id;
    String epp_password;
    String domain_name;
    String contact_id1 = null;
    String contact_id2 = null;
    int epp_host_port;
    
    
    String[] domain_nameservers = null;
    epp_Command command_data = null;
    epp_TransferRequest transfer_request = null;
    epp_CheckResult[] check_results = null;
    
    EPPClient epp_client;
    
    epp_DomainUpdateAddRemove add = null;
    epp_DomainUpdateAddRemove remove = null;
    epp_DomainInfoRsp domain_info_response = null;
	
	public EppRequestObject() {
		
	}
	
	
	public void init(String args[]){
		if (args.length < 5)
        {
            //System.err.println(USAGE);
            System.exit(1);
        }

        epp_host_name = args[0];
        epp_host_port_string = args[1];
        epp_client_id = args[2];
        epp_password  = args[3];
        domain_name  = args[4];
        if ( args.length > 5 )
        {
            contact_id1 = args[5];
        }
        if ( args.length > 6 )
        {
            contact_id2 = args[6];
        }

        if ( contact_id1 == null ) contact_id1 = epp_client_id + "001";
        if ( contact_id2 == null ) contact_id2 = epp_client_id + "002";
        
        epp_host_port = Integer.parseInt(epp_host_port_string);

        epp_client = new EPPClient(epp_host_name,
                                             epp_host_port,
                                             epp_client_id,
                                             epp_password);

        epp_client.setLang("en");
        String [] extensions =  {"urn:ietf:params:xml:ns:rgp-1.0"};
        epp_client.setEPPServiceExtensions(extensions);
	}


	public Date getDomain_exp_date() {
		return domain_exp_date;
	}


	public void setDomain_exp_date(Date domain_exp_date) {
		this.domain_exp_date = domain_exp_date;
	}


	public epp_AuthInfo getDomain_auth_info() {
		return domain_auth_info;
	}


	public void setDomain_auth_info(epp_AuthInfo domain_auth_info) {
		this.domain_auth_info = domain_auth_info;
	}


	public epp_AuthInfo getContact1_auth_info() {
		return contact1_auth_info;
	}


	public void setContact1_auth_info(epp_AuthInfo contact1_auth_info) {
		this.contact1_auth_info = contact1_auth_info;
	}


	public epp_AuthInfo getContact2_auth_info() {
		return contact2_auth_info;
	}


	public void setContact2_auth_info(epp_AuthInfo contact2_auth_info) {
		this.contact2_auth_info = contact2_auth_info;
	}


	public Date getCurrent_time() {
		return current_time;
	}


	public void setCurrent_time(Date current_time) {
		this.current_time = current_time;
	}


	public String getEpp_host_name() {
		return epp_host_name;
	}


	public void setEpp_host_name(String epp_host_name) {
		this.epp_host_name = epp_host_name;
	}


	public String getEpp_host_port_string() {
		return epp_host_port_string;
	}


	public void setEpp_host_port_string(String epp_host_port_string) {
		this.epp_host_port_string = epp_host_port_string;
	}


	public String getEpp_client_id() {
		return epp_client_id;
	}


	public void setEpp_client_id(String epp_client_id) {
		this.epp_client_id = epp_client_id;
	}


	public String getEpp_password() {
		return epp_password;
	}


	public void setEpp_password(String epp_password) {
		this.epp_password = epp_password;
	}


	public String getDomain_name() {
		return domain_name;
	}


	public void setDomain_name(String domain_name) {
		this.domain_name = domain_name;
	}


	public String getContact_id1() {
		return contact_id1;
	}


	public void setContact_id1(String contact_id1) {
		this.contact_id1 = contact_id1;
	}


	public String getContact_id2() {
		return contact_id2;
	}


	public void setContact_id2(String contact_id2) {
		this.contact_id2 = contact_id2;
	}


	public int getEpp_host_port() {
		return epp_host_port;
	}


	public void setEpp_host_port(int epp_host_port) {
		this.epp_host_port = epp_host_port;
	}


	public String[] getDomain_nameservers() {
		return domain_nameservers;
	}


	public void setDomain_nameservers(String[] domain_nameservers) {
		this.domain_nameservers = domain_nameservers;
	}


	public epp_Command getCommand_data() {
		return command_data;
	}


	public void setCommand_data(epp_Command command_data) {
		this.command_data = command_data;
	}


	public epp_TransferRequest getTransfer_request() {
		return transfer_request;
	}


	public void setTransfer_request(epp_TransferRequest transfer_request) {
		this.transfer_request = transfer_request;
	}


	public epp_CheckResult[] getCheck_results() {
		return check_results;
	}


	public void setCheck_results(epp_CheckResult[] check_results) {
		this.check_results = check_results;
	}


	public EPPClient getEpp_client() {
		return epp_client;
	}


	public void setEpp_client(EPPClient epp_client) {
		this.epp_client = epp_client;
	}


	public epp_DomainUpdateAddRemove getAdd() {
		return add;
	}


	public void setAdd(epp_DomainUpdateAddRemove add) {
		this.add = add;
	}


	public epp_DomainUpdateAddRemove getRemove() {
		return remove;
	}


	public void setRemove(epp_DomainUpdateAddRemove remove) {
		this.remove = remove;
	}


	public epp_DomainInfoRsp getDomain_info_response() {
		return domain_info_response;
	}


	public void setDomain_info_response(epp_DomainInfoRsp domain_info_response) {
		this.domain_info_response = domain_info_response;
	}

}
