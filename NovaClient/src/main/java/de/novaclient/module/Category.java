package de.novaclient.module;

public enum Category {
	COMBAT("COMBAT"),
	MISC("MISC"),
	RENDER("RENDER");

	public final String label;

	Category(String label) {
		this.label = label;
	}
}
