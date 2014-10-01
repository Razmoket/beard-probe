/*
**
** EPP RTK Java
** Copyright (C) 2001-2002, Tucows, Inc.
** Copyright (C) 2003, Liberty RMS
**
**
** This library is free software; you can redistribute it and/or
** modify it under the terms of the GNU Lesser General Public
** License as published by the Free Software Foundation; either
** version 2.1 of the License, or (at your option) any later version.
**
** This library is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
** Lesser General Public License for more details.
**
** You should have received a copy of the GNU Lesser General Public
** License along with this library; if not, write to the Free Software
** Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
**
*/

package com.liberty.rtk.extension.epprtk.example.old;

import java.util.*;
import java.io.*;

import com.tucows.oxrs.epprtk.rtk.*;
import com.tucows.oxrs.epprtk.rtk.xml.*;
import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;

import org.openrtk.idl.epprtk.*;
import org.openrtk.idl.epprtk.domain.*;
import org.openrtk.idl.epprtk.host.*;
import org.openrtk.idl.epprtk.contact.*;

import com.liberty.rtk.extension.epprtk.AsiaCEDContact;
import com.liberty.rtk.extension.epprtk.AsiaCEDContactData;
import com.liberty.rtk.extension.epprtk.example.SessionExample;

/**
 * Example code for a typical logical EPP sessions to demonstrate 
 * usage of com.liberty.rtk.extension.epprtk.AsiaCEDContact.
 *
 * @author Anna Simbirtsev 
 * @version $Revision: 1.2 $ $Date: 2007/06/12 15:15:45 $
**/
public class AsiaCEDContactSessionExample extends SessionExample
{
        String contact_id = null;
        String ccLocality_ = null;
        String localitySp_ = null;
        String localityCity_ = null;
        String legalEntityType_ = null;
        String identForm_ = null;
        String identNumber_ = null;
        String remark1_ = null;
        String remark2_ = null;

	public AsiaCEDContactSessionExample(String args[])
	{
        super(args);
        contact_id = nextArgument();
        ccLocality_ = nextArgument();
        localitySp_ = nextArgument();
        localityCity_ = nextArgument();
        legalEntityType_ = nextArgument();
        identForm_ = nextArgument();
        identNumber_ = nextArgument();
        remark1_ = nextArgument(); 
        remark2_ = nextArgument();
        assertNotNull(contact_id);
	}

    public static void main(String args[])
    {
        SessionExample example = new AsiaCEDContactSessionExample(args);
        example.session();
    }

    protected String getUsage()
    {
        return super.getUsage()
            + " contact_id ccLocality localitySp localityCity legalEntityType identForm identNumber otherLEType otherIdentForm";
    }

	protected void process() throws epp_Exception, IOException, epp_XMLException
    {
        if (!checkContact())
        {
			System.out.println("contact " + contact_id + " exists in .asia registry, please choose another contact");
			System.exit(1);
        }

        createContact();
        contactInfo();
        contactUpdate();
    }

	private boolean checkContact() throws epp_Exception, epp_XMLException
	{
		System.out.println("Creating the Contact Check command for ["+contact_id+"]");

		epp_ContactCheckReq contact_check_request = new epp_ContactCheckReq();                                                                                                                
		contact_check_request.setCmd( createEPPCommand() );

		List contact_list = (List)new ArrayList();
		contact_list.add(contact_id);

		contact_check_request.setIds( EPPXMLBase.convertListToStringArray(contact_list) );

		EPPContactCheck contact_check = new EPPContactCheck();
		contact_check.setRequestData(contact_check_request);

		contact_check = (EPPContactCheck) epp_client.processAction(contact_check);

		epp_ContactCheckRsp contact_check_response = contact_check.getResponseData();

		epp_CheckResult[] check_results = contact_check_response.getResults();
		System.out.println("ContactCheck results: contact ["+contact_id+"] available? ["+EPPXMLBase.getAvailResultFor(check_results, contact_id)+"]");

		if ( EPPXMLBase.getAvailResultFor(check_results, contact_id) == null )
            return false;

        return EPPXMLBase.getAvailResultFor(check_results, contact_id).booleanValue();
	}

	private void createContact() throws epp_Exception, IOException, epp_XMLException
	{
		System.out.println("Creating the Contact Create command");
		epp_ContactCreateReq contact_create_request = new epp_ContactCreateReq();

		contact_create_request.setCmd( createEPPCommand() );
		contact_create_request.setId( contact_id );

		System.out.print("Dear registrant, please enter a passphrase for the new registrant contact(min 6, max 16): ");
		epp_AuthInfo contact_auth_info = getUserInputAuthInfo();

		contact_create_request.setAuthInfo( contact_auth_info );

		epp_ContactNameAddress[] name_address = new epp_ContactNameAddress[1];
		name_address[0] = new epp_ContactNameAddress();
		name_address[0].setType( epp_ContactPostalInfoType.INT );
		name_address[0].setName( "John Doe" );
		name_address[0].setOrg( "ACME Solutions" );
		epp_ContactAddress address = new epp_ContactAddress();
		address.setStreet1( "100 Centre St" );
		address.setCity( "Townsville" );
		address.setStateProvince( "County Derry" );
		address.setPostalCode( "Z1Z1Z1" );
		address.setCountryCode( "CA" );
		name_address[0].setAddress( address );

		contact_create_request.setAddresses( name_address );
		contact_create_request.setVoice( new epp_ContactPhone("1234", "+1.4165559999") );
		contact_create_request.setFax( new epp_ContactPhone("9876", "+1.4165558888") );
		contact_create_request.setEmail( "john.doe@company.info" );

                AsiaCEDContactData cdata = new AsiaCEDContactData();

                if (ccLocality_ != null) cdata.setCcLocality(ccLocality_);
                else cdata.setCcLocality("cn");
                if (localitySp_ != null) cdata.setLocalitySp(localitySp_);
                else cdata.setLocalitySp("ontario");
                if (localityCity_ != null) cdata.setLocalityCity(localityCity_);
                else cdata.setLocalityCity("toronto");  
                if (legalEntityType_ != null) cdata.setLegalEntityType(legalEntityType_);
                else cdata.setLegalEntityType("corporation");
                if (identForm_ != null) cdata.setIdentForm(identForm_);
                else cdata.setIdentForm("passport");
                if (identNumber_ != null) cdata.setIdentNumber(identNumber_);
                else cdata.setIdentNumber("12345678");
                if (remark1_ != null) cdata.setRemark1(remark1_);
                else cdata.setRemark1("naturalPerson");
                if (remark2_ != null) cdata.setRemark2(remark2_);
                else cdata.setRemark2("certificate");                 

                AsiaCEDContact asia_contact_extension = new AsiaCEDContact();
                asia_contact_extension.setCommand("create");
                asia_contact_extension.setAsiaCEDContactData(cdata);

                epp_Extension[] extensions = {asia_contact_extension};
                contact_create_request.getCmd().setExtensions(extensions);

		EPPContactCreate contact_create = new EPPContactCreate();
		contact_create.setRequestData(contact_create_request);

		contact_create = (EPPContactCreate) epp_client.processAction(contact_create);

		epp_ContactCreateRsp contact_create_response = contact_create.getResponseData();
		System.out.println("ContactCreate results: contact id ["+contact_create_response.getId()+"]");
	}

	private void contactInfo() throws epp_XMLException, epp_Exception
	{
		System.out.println("Creating the Contact Info command");
		epp_ContactInfoReq contact_info_request = new epp_ContactInfoReq();

		contact_info_request.setCmd( createEPPCommand() );

		contact_info_request.setId(contact_id);

                EPPContactInfo contact_info = new EPPContactInfo();
                contact_info.setRequestData(contact_info_request);

		contact_info = (EPPContactInfo) epp_client.processAction(contact_info);

		epp_ContactInfoRsp contact_info_response = contact_info.getResponseData();

		for ( int i = 0; i < contact_info_response.getStatus().length; i++ )
		{
			System.out.println("\tstatus["+i+"] string ["+EPPContactBase.contactStatusToString( contact_info_response.getStatus()[i].getType() )+"]");
			System.out.println("\tstatus["+i+"] note ["+contact_info_response.getStatus()[i].getValue()+"]");
		}

		System.out.println("ContactInfo extension:");
		epp_Response response = contact_info_response.getRsp();
		String[] extensionStrings = response.getExtensionStrings();
		if ( extensionStrings != null ) {
			AsiaCEDContact asia_contact_extension1 = new AsiaCEDContact();
			asia_contact_extension1.fromXML(extensionStrings[0]);
			System.out.println("Contact CED data [" + asia_contact_extension1.getAsiaCEDContactData() +"]");
		} else {
			System.out.println("Contact Info response contained no extension!!!");
		}
	}

    private void contactUpdate() throws epp_XMLException, epp_Exception
    {
        System.out.println("Creating the Contact Update command");
        epp_ContactUpdateReq contact_update_request = new epp_ContactUpdateReq();

        contact_update_request.setCmd( createEPPCommand() );
        contact_update_request.setId( contact_id );

        AsiaCEDContactData cdata = new AsiaCEDContactData();
  
        cdata.setChg(true);

        if (ccLocality_ != null) cdata.setCcLocality(ccLocality_);
        else cdata.setCcLocality("cn");
        if (localitySp_ != null) cdata.setLocalitySp(localitySp_);
        else cdata.setLocalitySp("ontario");
        if (localityCity_ != null) cdata.setLocalityCity(localityCity_);
        else cdata.setLocalityCity("toronto");
        if (legalEntityType_ != null) cdata.setLegalEntityType(legalEntityType_);
        else cdata.setLegalEntityType("naturalPerson");
        if (identForm_ != null) cdata.setIdentForm(identForm_);
        else cdata.setIdentForm("passport");
        if (identNumber_ != null) cdata.setIdentNumber(identNumber_);
        else cdata.setIdentNumber("12345678");
        if (remark1_ != null) cdata.setRemark1(remark1_);
        else cdata.setRemark1("naturalPerson");
        if (remark2_ != null) cdata.setRemark2(remark2_);
        else cdata.setRemark2("certificate");

        AsiaCEDContact asia_contact_extension = new AsiaCEDContact();
        asia_contact_extension.setCommand("update");
 
        asia_contact_extension.setAsiaCEDContactData(cdata);

        contact_update_request.getCmd().setExtension(asia_contact_extension);

        EPPContactUpdate contact_update = new EPPContactUpdate();
        contact_update.setRequestData(contact_update_request);

        contact_update = (EPPContactUpdate) epp_client.processAction(contact_update);

        epp_ContactUpdateRsp contact_update_response = contact_update.getResponseData();
        epp_Response response = contact_update_response.m_rsp;
        epp_Result[] results = response.m_results;
        System.out.println("ContactUpdate results: ["+results[0].m_code+"] ["+results[0].m_msg+"]");
    }
}
