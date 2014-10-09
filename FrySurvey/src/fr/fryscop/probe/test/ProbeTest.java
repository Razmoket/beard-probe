package fr.fryscop.probe.test;

import fr.fryscop.probe.Probe;

public interface ProbeTest extends Runnable{

	public void launchTest() ;
	
	public String getTestResult() ;
	
	public Probe getProbe();
	
	
}
