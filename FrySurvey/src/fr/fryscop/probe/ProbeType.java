package fr.fryscop.probe;

public enum ProbeType {

	
	Dns("DNS"),
	Epp("EPP"),
	Rdds("RDDS");
	
	private final String value;

    private ProbeType(String description) {
        this.value = description;
    }

    public String getValue() {
        return this.value;
    }


}
