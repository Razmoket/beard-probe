package org.openrtk.idl.epp02.contact;


/**
* org/openrtk/idl/epp/contact/epp_ContactCheckReq.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from epp_contact.idl
* Thursday, July 26, 2001 6:26:12 PM EDT
*/


/////////////////////
public class epp_ContactCheckReq implements org.omg.CORBA.portable.IDLEntity
{
  public org.openrtk.idl.epp02.epp_Command m_cmd = null;
  public String m_roids[] = null;

  public epp_ContactCheckReq ()
  {
  } // ctor

  public epp_ContactCheckReq (org.openrtk.idl.epp02.epp_Command _m_cmd, String[] _m_roids)
  {
    m_cmd = _m_cmd;
    m_roids = _m_roids;
  } // ctor

  public void setCmd(org.openrtk.idl.epp02.epp_Command value) { m_cmd = value; }
  public org.openrtk.idl.epp02.epp_Command getCmd() { return m_cmd; }

  public void setRoids(String[] value) { m_roids = value; }
  public String[] getRoids() { return m_roids; }

  public String toString() { return this.getClass().getName() + ": { m_cmd ["+m_cmd+"] m_roids ["+(m_roids != null ? java.util.Arrays.asList(m_roids) : null)+"] }"; }

} // class epp_ContactCheckReq
