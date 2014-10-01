package fr.fryscop.monitoring;

public enum HeartBeatParameter {
// liste des parametres fournit à la page beat.php
	// name=test&tld=fr&type=dns&status=1
	Name("name"),
	Tld("tld"),
	Type("type"),
	Status("status");
	
	
	private final String value;

    private HeartBeatParameter(String description) {
        this.value = description;
    }

    public String getValue() {
        return this.value;
    }

}
