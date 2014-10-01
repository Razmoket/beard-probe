package org.openrtk.idl.epp02;


/**
* org/openrtk/idl/epp/epp_TransferStatusType.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from epp.idl
* Thursday, July 26, 2001 6:26:03 PM EDT
*/


//
public class epp_TransferStatusType implements org.omg.CORBA.portable.IDLEntity
{
  private        int __value;
  private static int __size = 6;
  private static org.openrtk.idl.epp02.epp_TransferStatusType[] __array = new org.openrtk.idl.epp02.epp_TransferStatusType [__size];

  public static final int _PENDING = 0;
  public static final org.openrtk.idl.epp02.epp_TransferStatusType PENDING = new org.openrtk.idl.epp02.epp_TransferStatusType(_PENDING);
  public static final int _APPROVED = 1;
  public static final org.openrtk.idl.epp02.epp_TransferStatusType APPROVED = new org.openrtk.idl.epp02.epp_TransferStatusType(_APPROVED);
  public static final int _CANCELLED = 2;
  public static final org.openrtk.idl.epp02.epp_TransferStatusType CANCELLED = new org.openrtk.idl.epp02.epp_TransferStatusType(_CANCELLED);
  public static final int _REJECTED = 3;
  public static final org.openrtk.idl.epp02.epp_TransferStatusType REJECTED = new org.openrtk.idl.epp02.epp_TransferStatusType(_REJECTED);
  public static final int _AUTO_APPROVED = 4;
  public static final org.openrtk.idl.epp02.epp_TransferStatusType AUTO_APPROVED = new org.openrtk.idl.epp02.epp_TransferStatusType(_AUTO_APPROVED);
  public static final int _AUTO_CANCELLED = 5;
  public static final org.openrtk.idl.epp02.epp_TransferStatusType AUTO_CANCELLED = new org.openrtk.idl.epp02.epp_TransferStatusType(_AUTO_CANCELLED);

  public int value ()
  {
    return __value;
  }

  public static org.openrtk.idl.epp02.epp_TransferStatusType from_int (int value)
  {
    if (value >= 0 && value < __size)
      return __array[value];
    else
      throw new org.omg.CORBA.BAD_PARAM ();
  }

  protected epp_TransferStatusType (int value)
  {
    __value = value;
    __array[__value] = this;
  }
} // class epp_TransferStatusType
