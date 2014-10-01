package fr.fryscop.probe;

public enum ProbeStatus {

	
    Ok("1"),
    Ko("2"),
    Error("3"),
    Unavailable("4"),
    Stopped("5");

    private final String value;

    private ProbeStatus(String description) {
        this.value = description;
    }

    public String getValue() {
        return this.value;
    }

}
