/*
**
** EPP RTK Java
** Copyright (C) 2001, Tucows, Inc.
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

package org.openrtk.idl.epp0604.host;


/**
 * Master external interface for the implementor of the EPP Host Delete command.</p>
 * The interface brings together epp_HostDeleteOperations, epp_Action and standard
 * IDL classes.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0604/host/epp_HostDelete.java,v 1.2 2003/09/10 21:29:58 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:58 $<br>
 * @see com.tucows.oxrs.epp0604.rtk.xml.EPPHostDelete
 */
public interface epp_HostDelete extends epp_HostDeleteOperations, org.openrtk.idl.epp0604.epp_Action, org.omg.CORBA.portable.IDLEntity 
{
} // interface epp_HostDelete
