package fr.fryscop.monitoring;

public class Probe {
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
	}
	
	
}