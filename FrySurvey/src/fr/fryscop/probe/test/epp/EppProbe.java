package fr.fryscop.probe.test.epp;

import fr.fryscop.probe.test.ProbeTest;

/*
 * EPP	--> 5 sondes / toutes les 5 min
 * 	EPP service availability		<---
 * 	EPP session-command RTT
 * 	EPP query-command RTT
 * 	EPP transform-command RTT
 * ==> si RTT 5 fois supérieur au temps SLR alors service unavailable pour la sonde
 */
public class EppProbe implements ProbeTest	{

	@Override
    public void launchTest() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public String getTestResult() {
	    // TODO Auto-generated method stub
	    return null;
    }

	
}
