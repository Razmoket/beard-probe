/*
**
** EPP RTK Java
** Copyright (C) 2001-2003, Liberty Registry Management Services, Inc.
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

package com.liberty.rtk.extension.epprtk;

import java.io.*;
import java.util.*;
import java.text.*;

import com.tucows.oxrs.epprtk.rtk.*;
import com.tucows.oxrs.epprtk.rtk.xml.*;
import org.openrtk.idl.epprtk.*;
import org.openrtk.idl.epprtk.contact.*;

import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.apache.xerces.parsers.*;
import org.xml.sax.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;

public class AsiaCEDContact extends EPPXMLBase implements epp_Extension
{
    private static final String prefix = "asia:";

    private AsiaCEDContactData cd_data_;
    private String cd_cmd_;

    /**
     * Default constructor
     */
    public AsiaCEDContact () {}

    /**
     * Constructor with Asia CED Contact Unspec XML string to automatically parse.
     * @param xml The EPP Contact Info response XML String
     * @throws org.openrtk.idl.epprtk.epp_XMLException if the response XML is not parsable or does not contain the expected data
     * @see #fromXML(String)
     */
    public AsiaCEDContact (String xml) throws epp_XMLException
    {
        String method_name = "AsiaCEDContact(String)";
        debug(DEBUG_LEVEL_TWO,method_name,"xml is ["+xml+"]");
        fromXML(xml);
    }

    public void setAsiaCEDContactData(AsiaCEDContactData data) { cd_data_ = data; }
    /**
     * Accessor method for the AsiaCEDContact data member.
     * @param value java.util.String
     */
    public AsiaCEDContactData getAsiaCEDContactData() { return cd_data_; }

    public void setCommand(String command)  { cd_cmd_ = command; }
    public String getCommand() { return cd_cmd_; }

    /**
     * Converts the AsiaCEDContact data into XML to be put into the extension
     * section of the request.
     * Implemented method from org.openrtk.idl.epprtk.epp_Unspec interface.
     * @throws org.openrtk.idl.epprtk.epp_XMLException if required data is missing
     * @see org.openrtk.idl.epprtk.epp_Extension
     */
    public String toXML() throws epp_XMLException
    {
        String method_name = "toXML()";
        debug(DEBUG_LEVEL_THREE,method_name,"Entered");

        if ( cd_cmd_ == null )
        {
            throw new epp_XMLException("AsiaCEDContact command");
        }

        Document doc = new DocumentImpl();

        Element tmp = doc.createElement(prefix + cd_cmd_);

        tmp.setAttribute("xmlns:asia","urn:afilias:params:xml:ns:asia-1.0");
        tmp.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        tmp.setAttribute("xsi:schemaLocation","urn:afilias:params:xml:ns:asia-1.0 asia-1.0.xsd");

        Element cdChg = doc.createElement(prefix + "chg");
        Element cedData = doc.createElement(prefix + "cedData"); 

        if (cd_data_.getCcLocality() != null )
             ExtUtils.addXMLElement(doc, cedData, prefix + "ccLocality", cd_data_.getCcLocality());
        if (cd_data_.getLocalitySp() != null )
             ExtUtils.addXMLElement(doc, cedData, prefix + "localitySp", cd_data_.getLocalitySp());
        if (cd_data_.getLocalityCity() != null )
             ExtUtils.addXMLElement(doc, cedData, prefix + "localityCity", cd_data_.getLocalityCity());
        if (cd_data_.getLegalEntityType() != null )
             ExtUtils.addXMLElement(doc, cedData, prefix + "legalEntityType", cd_data_.getLegalEntityType());
        if (cd_data_.getIdentForm() != null )
             ExtUtils.addXMLElement(doc, cedData, prefix + "identForm", cd_data_.getIdentForm());
        if (cd_data_.getIdentNumber() != null )
             ExtUtils.addXMLElement(doc, cedData, prefix + "identNumber", cd_data_.getIdentNumber());
        if (cd_data_.getRemark1() != null )
             ExtUtils.addXMLElement(doc, cedData, prefix + "otherLEType", cd_data_.getRemark1());
        if (cd_data_.getRemark2() != null )
             ExtUtils.addXMLElement(doc, cedData, prefix + "otherIdentForm", cd_data_.getRemark2()); 
            
        if (cd_data_.isChg()) {
             cdChg.appendChild(cedData);
             tmp.appendChild(cdChg);
        } else {
            tmp.appendChild(cedData);
        }
            
        doc.appendChild( tmp );

        String contactCED_xml;

        try
        {
            contactCED_xml = createXMLSnippetFromDoc(doc);
        }
        catch (IOException xcp)
        {
            throw new epp_XMLException("IOException in building XML ["+xcp.getMessage()+"]");
        }

        debug(DEBUG_LEVEL_THREE,method_name,"Leaving");

        return contactCED_xml;
    }

    /**
     * Parses an XML String of contactCED data from the extension section of
     * a response from the Registry.
     * Implemented method from org.openrtk.idl.epprtk.epp_Unspec interface.
     * @param A new contactCED Unspec XML String to parse
     * @throws org.openrtk.idl.epprtk.epp_XMLException if the response XML is not parsable or does not contain the expected data
     * @see org.openrtk.idl.epprtk.epp_Action
     */
    public void fromXML(String xml) throws epp_XMLException
    {
        String method_name = "fromXML()";
        debug(DEBUG_LEVEL_THREE,method_name,"Entered");

        xml_ = getInnerXML(xml);

        try
        {
            if ( xml_ == null ||
                 xml_.length() == 0 )
            {
                // no xml string to parse
                return;
            }

            Element extension_node = getDocumentElement();

            if ( extension_node == null )
            {
                throw new epp_XMLException("unparsable or missing extension");
            }

            NodeList detail_node_list = extension_node.getChildNodes();

            if ( detail_node_list.getLength() == 0 )
            {
                // no contact child elements
                throw new epp_XMLException("no contactCED child elements");
            }

            debug(DEBUG_LEVEL_TWO,method_name,"detail_node_list's node count ["+detail_node_list.getLength()+"]");

            for (int count = 0; count < detail_node_list.getLength(); count++)
            {
                Node node_ = detail_node_list.item(count);

                if (node_.getNodeName().equals(prefix + "cedData")) {
                   NodeList cedD = node_.getChildNodes();
                   cd_data_ = new AsiaCEDContactData();
                   for (int count1 = 0; count1 < cedD.getLength(); count1++) {
                      Node a_node = cedD.item(count1);
                       
                      if ( a_node.getNodeName().equals(prefix + "ccLocality") &&  a_node.getFirstChild() != null) 
                      { 
                         cd_data_.setCcLocality( a_node.getFirstChild().getNodeValue() ); 
                      }
                      if ( a_node.getNodeName().equals(prefix + "localitySp") &&  a_node.getFirstChild() != null)
                      {
                         cd_data_.setLocalitySp( a_node.getFirstChild().getNodeValue() );
                      }
                      if ( a_node.getNodeName().equals(prefix + "localityCity") &&  a_node.getFirstChild() != null)
                      {
                         cd_data_.setLocalityCity( a_node.getFirstChild().getNodeValue() );
                      }
                      if ( a_node.getNodeName().equals(prefix + "legalEntityType") &&  a_node.getFirstChild() != null) 
                      { 
                         cd_data_.setLegalEntityType( a_node.getFirstChild().getNodeValue() ); 
                      }
                      if ( a_node.getNodeName().equals(prefix + "identForm") &&  a_node.getFirstChild() != null) 
                      { 
                         cd_data_.setIdentForm( a_node.getFirstChild().getNodeValue() ); 
                      }
                      if ( a_node.getNodeName().equals(prefix + "identNumber") &&  a_node.getFirstChild() != null) 
                      { 
                         cd_data_.setIdentNumber( a_node.getFirstChild().getNodeValue() ); 
                      }
                      if ( a_node.getNodeName().equals(prefix + "otherLEType") &&  a_node.getFirstChild() != null) 
                      { 
                         cd_data_.setRemark1( a_node.getFirstChild().getNodeValue() ); 
                      }
                      if ( a_node.getNodeName().equals(prefix + "otherIdentForm") &&  a_node.getFirstChild() != null)
                      {
                         cd_data_.setRemark2( a_node.getFirstChild().getNodeValue() );
                      }

                  }
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
