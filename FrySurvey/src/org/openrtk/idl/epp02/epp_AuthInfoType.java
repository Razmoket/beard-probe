package org.openrtk.idl.epp02;


/**
* org/openrtk/idl/epp/epp_AuthInfoType.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from epp.idl
* Thursday, July 26, 2001 6:26:03 PM EDT
*/


//
public class epp_AuthInfoType implements org.omg.CORBA.portable.IDLEntity
{
  private        int __value;
  private static int __size = 1;
  private static org.openrtk.idl.epp02.epp_AuthInfoType[] __array = new org.openrtk.idl.epp02.epp_AuthInfoType [__size];

  public static final int _PW = 0;
  public static final org.openrtk.idl.epp02.epp_AuthInfoType PW = new org.openrtk.idl.epp02.epp_AuthInfoType(_PW);

  public int value ()
  {
    return __value;
  }

  public static org.openrtk.idl.epp02.epp_AuthInfoType from_int (int value)
  {
    if (value >= 0 && value < __size)
      return __array[value];
    else
      throw new org.omg.CORBA.BAD_PARAM ();
  }

  protected epp_AuthInfoType (int value)
  {
    __value = value;
    __array[__value] = this;
  }
} // class epp_AuthInfoType
