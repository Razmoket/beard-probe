/*
**
** EPP RTK Java
** Copyright (C) 2003, Liberty Registry Management Services, Inc.
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

/*
 * $Header: /cvsroot/epp-rtk/liberty-rtk-addon/java/src/com/liberty/rtk/extension/epprtk/AsiaDomain.java,v 1.2 2007/09/12 18:27:53 asimbirt Exp $
 * $Revision: 1.2 $
 * $Date: 2007/09/12 18:27:53 $
 */

package com.liberty.rtk.extension.epprtk;

import java.io.*;
import java.util.*;
import java.text.*;

import com.tucows.oxrs.epprtk.rtk.*;
import com.tucows.oxrs.epprtk.rtk.xml.*;
import org.openrtk.idl.epprtk.*;
import org.openrtk.idl.epprtk.domain.*;

import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.apache.xerces.parsers.*;
import org.xml.sax.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;


public class AsiaDomain extends EPPXMLBase implements epp_Extension
{
    private String command_;
    private List contacts;
    private String maintainerURL_;
    private String domainRoid_;
    private boolean chg_;

    public AsiaDomain () { }

    public void setCommand(String value) { command_ = value; }
    public String getCommand() { return command_; }

    public void setContacts(List value) { contacts = value; }
    public List getContacts() { return contacts; }

    public void setMaintainerURL(String value) { maintainerURL_ = value; }
    public String getMaintainerURL() { return maintainerURL_; }

    public void setDomainRoid(String value) { domainRoid_ = value; }
    public String getDomainRoid() { return domainRoid_; }

    public void setChg(boolean value) { chg_ = value; }
    public boolean getChg() { return chg_; }

    /**
     * Renders the AsiaDomain extension "extension" for EPP RFC.<br>
     * This qualifies the contact_, iprType_ in 
     * domain:create request and domain:info response.
     * Implemented method from org.openrrc.rtk.epprtk.epp_Unspec interface.
     * @throws org.openrtk.idl.epprtk.epp_XMLException if required data is missing
     * @see org.openrtk.idl.epprtk.epp_Extension
     */
    public String toXML() throws epp_XMLException
    {
        String method_name = "toXML()";
        debug(DEBUG_LEVEL_THREE,method_name,"Entered");

	String command_ = getCommand();

        if ( command_ == null || command_.equals("") )
        {
            throw new epp_XMLException("missing epp command for asia extension");
        }

        Document doc = new DocumentImpl();
        
        Element asia = doc.createElement("asia:"+command_);

        setAttribute(asia);
   
        Element chg_element = null; 
        Element contact; 
 
        if (command_.equals("update")) {
             if (chg_) {  
                chg_element = doc.createElement("asia:chg");

                if (contacts != null) {
                   for (Iterator it = contacts.iterator(); it.hasNext();) {
                      AsiaContact ct = (AsiaContact)it.next();
                      contact = ExtUtils.addXMLElement(doc, chg_element, "asia:contact", ct.getContactValue());
                      contact.setAttribute("type", ct.getType());
                   }
                }
                if (maintainerURL_ != null && maintainerURL_.length() != 0) {
                  ExtUtils.addXMLElement(doc, chg_element, "asia:maintainerUrl", maintainerURL_);
                }
            }
        } else if (command_.equals("create")) {
             if (contacts != null) {
                for (Iterator it = contacts.iterator(); it.hasNext();) {
                    AsiaContact ct = (AsiaContact)it.next();
                    contact = ExtUtils.addXMLElement(doc, asia, "asia:contact", ct.getContactValue());
                    contact.setAttribute("type", ct.getType());
                }
             }
             if (maintainerURL_ != null && maintainerURL_.length() != 0) {
                ExtUtils.addXMLElement(doc, asia, "asia:maintainerUrl", maintainerURL_);
             }
        }
 
        // create <asia:domainRoid>
        if ((command_.equals("info") || command_.equals("update")) && domainRoid_ != null && domainRoid_.length() != 0) {
            ExtUtils.addXMLElement(doc, asia, "asia:domainRoid", domainRoid_);
        } 

        if (chg_element != null) asia.appendChild( chg_element );

        doc.appendChild( asia );
        
        String asia_xml;
        
        try
        {
            asia_xml = createXMLSnippetFromDoc(doc);
        }
        catch (IOException xcp)
        {
            throw new epp_XMLException("IOException in building oxrs:transfer XML ["+xcp.getMessage()+"]");
        }

        debug(DEBUG_LEVEL_THREE,method_name,"The AsiaDomain extension XML is: ["+asia_xml+"]");
        debug(DEBUG_LEVEL_THREE,method_name,"Leaving");

        return asia_xml;
    }

    public void fromXML(String xml) throws epp_XMLException
	{
        String method_name = "fromXML()";
        debug(DEBUG_LEVEL_THREE,method_name,"Entered");

        xml_ = getInnerXML(xml);
        
        try
        {
            command_ = null;
            contacts = null; 
            maintainerURL_ = null;

            if ( xml_ == null || xml_.length() == 0 )
            {
                // no xml string to parse
                debug(DEBUG_LEVEL_THREE,method_name,"No XML to parse");
                debug(DEBUG_LEVEL_THREE,method_name,"Leaving");
                return;
            }

            Element asia_node = getDocumentElement();

            if ( asia_node == null )
            {
                return;
            }

            NodeList asia_node_list = asia_node.getChildNodes();
			
            if ( asia_node_list.getLength() == 0 )
            {
                return;
        	}

            debug(DEBUG_LEVEL_TWO,method_name,"asia_node_list's node count ["+asia_node_list.getLength()+"]");

            contacts = (List)new ArrayList();

            for (int count = 0; count < asia_node_list.getLength(); count++)
            {
                Node a_node = asia_node_list.item(count);

                if ( a_node.getNodeName().equals("asia:contact") ) 
		{
                    AsiaContact contact_ = new AsiaContact();
                    contact_.setContactValue(a_node.getFirstChild().getNodeValue());
                    contact_.setType(((Element)a_node).getAttribute("type"));

                    contacts.add(contact_); 
                }

                if ( a_node.getNodeName().equals("asia:domainRoid") )
                {
                    domainRoid_ = a_node.getFirstChild().getNodeValue();
                }

                if ( a_node.getNodeName().equals("asia:maintainerUrl") )
                {
                    maintainerURL_ = a_node.getFirstChild().getNodeValue();
                }
            }

        }
        catch (SAXException xcp)
        {
            debug(DEBUG_LEVEL_ONE,method_name,xcp);
            throw new epp_XMLException("unable to parse xml ["+xcp.getClass().getName()+"] ["+xcp.getMessage()+"]");
        }
        catch (IOException xcp)
        {
            debug(DEBUG_LEVEL_ONE,method_name,xcp);
            throw new epp_XMLException("unable to parse xml ["+xcp.getClass().getName()+"] ["+xcp.getMessage()+"]");
        }

        debug(DEBUG_LEVEL_THREE,method_name,"Leaving");
    }

	protected void setAttribute(Element asia)
	{
           asia.setAttribute("xmlns:asia", "urn:afilias:params:xml:ns:asia-1.0");
           asia.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
           asia.setAttribute("xsi:schemaLocation", "urn:afilias:params:xml:ns:asia-1.0 asia-1.0.xsd");
	}

	protected String getInnerXML(String xml)
	{
		if ( xml == null || xml.length() == 0 )
			return xml;

        int indexOfStart = xml.indexOf("<asia:");
        xml = xml.substring(indexOfStart);
        int indexOfEnd = xml.lastIndexOf("</asia:");
        int realIndexOfEnd = xml.indexOf(">", indexOfEnd);
        xml = xml.substring(0, realIndexOfEnd+1);

	return xml;
	}
}
