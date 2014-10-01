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

import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.apache.xerces.parsers.*;
import org.xml.sax.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;

public class AsiaCEDContactData
{
    private String cd_ccLocality_;
    private String cd_localitySp_;
    private String cd_localityCity_;
    private String cd_legalEntityType_;
    private String cd_identForm_;
    private String cd_identNumber_;
    private String cd_remark1_;
    private String cd_remark2_;

    private boolean chg = false; 

    public void setCcLocality(String value) { cd_ccLocality_ = value; }
    public String getCcLocality() { return cd_ccLocality_; }

    public void setLocalitySp(String value) { cd_localitySp_ = value; }
    public String getLocalitySp() { return cd_localitySp_; }

    public void setLocalityCity(String value) { cd_localityCity_ = value; }
    public String getLocalityCity() { return cd_localityCity_; }

    public void setLegalEntityType(String value) { cd_legalEntityType_ = value; }
    public String getLegalEntityType() { return cd_legalEntityType_; }

    public void setIdentForm(String value) { cd_identForm_ = value; }
    public String getIdentForm() { return cd_identForm_; }

    public void setIdentNumber(String value) { cd_identNumber_ = value; }
    public String getIdentNumber() { return cd_identNumber_; }

    public void setRemark1(String value) { cd_remark1_ = value; }
    public String getRemark1() { return cd_remark1_; }

    public void setRemark2(String value) { cd_remark2_ = value; }
    public String getRemark2() { return cd_remark2_; }

    public void setChg(boolean chg) { this.chg = chg; }
    public boolean isChg() { return chg; }

    public String toString()
    {
        return "[chg:" + chg + "|ccLocality:" + cd_ccLocality_ + "|localitySp:" + cd_localitySp_ + "|localityCity:" + cd_localityCity_ + "|legalEntityType:" + cd_legalEntityType_ + "|identForm:" + cd_identForm_ + "|identNumber:" + cd_identNumber_ + "|otherLEType:" + cd_remark1_ + "|otherIdentForm:" + cd_remark2_ + "]";
    }
}
