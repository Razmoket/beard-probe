package fr.fryscop.monitoring;

public class HeartBeat {
	//appel la page de gestion des heartbeat en donnant l'état de la sonde
	// http://www.frysurvey.fr/monitoring/probes/beat.php?name=test&tld=fr&type=dns&status=1
	private final static String defaultBeatUrl="http://www.frysurvey.fr/monitoring/probes/beat.php?";
	
	private static HeartBeat instance = null;
	
	private HeartBeat(){}
	
	public HeartBeat getInstance(){
		if(instance == null){
			instance = new HeartBeat();
		}
		return instance;
	}
	
	public void sendBeat(Probe probe){
		String urlToCall = defaultBeatUrl 
				+ HeartBeatParameter.Name.getValue() + "=" + probe.getName()
				+ HeartBeatParameter.Tld.getValue() + "=" + probe.getTld()
				+ HeartBeatParameter.Type.getValue() + "=" + probe.getType().getValue()
				+ HeartBeatParameter.Status.getValue() + "=" + probe.getStatus().getValue();
		System.out.println(urlToCall);
	}
}
