package org.openrtk.idl.epp02;


/**
* org/openrtk/idl/epp/epp_Exception.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from epp.idl
* Thursday, July 26, 2001 6:26:04 PM EDT
*/

public class epp_Exception extends org.omg.CORBA.UserException implements org.omg.CORBA.portable.IDLEntity
{
  public org.openrtk.idl.epp02.epp_Result m_details[] = null;

  public epp_Exception ()
  {
  } // ctor

  public epp_Exception (org.openrtk.idl.epp02.epp_Result[] _m_details)
  {
    m_details = _m_details;
  } // ctor

  public void setDetails(org.openrtk.idl.epp02.epp_Result[] value) { m_details = value; }
  public org.openrtk.idl.epp02.epp_Result[] getDetails() { return m_details; }

  public String toString() { return this.getClass().getName() + ": { m_details ["+(m_details != null ? java.util.Arrays.asList(m_details) : null)+"] }"; }

} // class epp_Exception
