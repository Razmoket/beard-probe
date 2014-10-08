package fr.fryscop.probe;

public enum ProbeStatus {

	
    Ok("ok","1"),
    Ko("ko","2"),
    Error("error","3"),
    Unavailable("unavailable","4"),
    Stopped("stopped","5");

    private final String value;
    private final String desc;

    private ProbeStatus(String description, String value) {
        this.value = value;
        this.desc = description;
    }

    public String getValue() {
        return this.value;
    }
    public String getDescription() {
        return this.desc;
    }

}
