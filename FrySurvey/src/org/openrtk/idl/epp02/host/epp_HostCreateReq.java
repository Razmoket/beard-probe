package org.openrtk.idl.epp02.host;


/**
* org/openrtk/idl/epp/host/epp_HostCreateReq.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from epp_host.idl
* Thursday, July 26, 2001 6:26:10 PM EDT
*/


///////////////////
public class epp_HostCreateReq implements org.omg.CORBA.portable.IDLEntity
{
  public org.openrtk.idl.epp02.epp_Command m_cmd = null;
  public String m_name = null;
  public org.openrtk.idl.epp02.host.epp_HostAddress m_addresses[] = null;

  public epp_HostCreateReq ()
  {
  } // ctor

  public epp_HostCreateReq (org.openrtk.idl.epp02.epp_Command _m_cmd, String _m_name, org.openrtk.idl.epp02.host.epp_HostAddress[] _m_addresses)
  {
    m_cmd = _m_cmd;
    m_name = _m_name;
    m_addresses = _m_addresses;
  } // ctor

  public void setCmd(org.openrtk.idl.epp02.epp_Command value) { m_cmd = value; }
  public org.openrtk.idl.epp02.epp_Command getCmd() { return m_cmd; }

  public void setName(String value) { m_name = value; }
  public String getName() { return m_name; }

  public void setAddresses(org.openrtk.idl.epp02.host.epp_HostAddress[] value) { m_addresses = value; }
  public org.openrtk.idl.epp02.host.epp_HostAddress[] getAddresses() { return m_addresses; }

  public String toString() { return this.getClass().getName() + ": { m_cmd ["+m_cmd+"] m_name ["+m_name+"] m_addresses ["+(m_addresses != null ? java.util.Arrays.asList(m_addresses) : null)+"] }"; }

} // class epp_HostCreateReq
