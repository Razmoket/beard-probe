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

/*
 * $Header: /cvsroot/epp-rtk/liberty-rtk-addon/java/src/com/liberty/rtk/extension/epprtk/AsiaContact.java,v 1.1 2007/05/18 15:34:16 asimbirt Exp $
 * $Revision: 1.1 $
 * $Date: 2007/05/18 15:34:16 $
 */

package com.liberty.rtk.extension.epprtk;

import java.io.*;
import java.util.*;
import java.text.*;

import com.tucows.oxrs.epprtk.rtk.*;
import com.tucows.oxrs.epprtk.rtk.xml.*;
import org.openrtk.idl.epprtk.*;

import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.apache.xerces.parsers.*;
import org.xml.sax.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;

public class AsiaContact 
{
    private String contactValue_;
    private String type_;

    private boolean remove = false;
    private boolean add = false;  

    public AsiaContact () {}

    public AsiaContact (String v, String t) {
       contactValue_ = v;
       type_ = t;
    }

    public void setContactValue(String value) { contactValue_ = value; }
    public String getContactValue() { return contactValue_; }

    public void setType(String value) { type_ = value; }
    public String getType() { return type_; }

    public void setRemove(boolean remove) { this.remove = remove; }
    public boolean isRemove() { return remove; }

    public void setAdd(boolean add) { this.add = add; }
    public boolean isAdd() { return add; }

    public String toString()
    {
        return "[contact:" + contactValue_ + "|type:" + type_;
    }
}
