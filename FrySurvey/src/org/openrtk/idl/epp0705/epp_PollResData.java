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

package org.openrtk.idl.epp0705;


/**
 * IDL interface which is used to denote classes that can contain poll response data.</p>
 * Poll responses data can vary from registry to registry, but typically includes object
 * transfer notifications, renewal notifications, etc...  essentially data pertaining
 * to object that belong to a registry but which were changed without the registrar's
 * direct action.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0705/epp_PollResData.java,v 1.1 2003/03/20 22:42:19 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/20 22:42:19 $<br>
 * @see org.openrtk.idl.epp0705.domain.epp_DomainTrnData
 * @see org.openrtk.idl.epp0705.contact.epp_ContactTrnData
 */
public interface epp_PollResData extends epp_PollResDataOperations, org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity 
{
} // interface epp_PollResData
