package fr.fryscop.probe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.fryscop.probe.monitoring.HeartBeat;

public class Probe {

	private static final Logger logger = LoggerFactory.getLogger(Probe.class);

	private String name;
	private String tld;
	private ProbeType type;
	private ProbeStatus status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTld() {
		return tld;
	}

	public void setTld(String tld) {
		this.tld = tld;
	}

	public ProbeType getType() {
		return type;
	}

	public void setType(ProbeType type) {
		this.type = type;
	}

	public ProbeStatus getStatus() {
		return status;
	}

	public void setStatus(ProbeStatus status) {
		this.status = status;
		// HeartBeat.sendBeat(this);
	}

	public String toString() {
		return this.name + "|" + this.tld + "|" + type.getValue();
	}

	public void stop() {
		logger.info("Stopping " + this.toString());
		this.setStatus(ProbeStatus.Stopped);
		HeartBeat.sendBeat(this);
	}	

}
