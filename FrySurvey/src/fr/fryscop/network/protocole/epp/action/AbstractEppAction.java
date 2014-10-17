package fr.fryscop.network.protocole.epp.action;

import java.io.IOException;

import org.openrtk.idl.epprtk.epp_Exception;
import org.openrtk.idl.epprtk.epp_XMLException;

import com.tucows.oxrs.epprtk.rtk.transport.EPPTransportException;

import fr.fryscop.network.protocole.epp.EppRequestObject;

public abstract class AbstractEppAction {

	public AbstractEppAction() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract void doAction(EppRequestObject eppRequestObject) throws epp_Exception, epp_XMLException, IOException, EPPTransportException;

}
