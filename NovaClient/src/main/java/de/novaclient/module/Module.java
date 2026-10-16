package de.novaclient.module;

import net.minecraft.client.Minecraft;

public abstract class Module {
	public final String name;
	public final String description;
	public final Category category;
	private boolean enabled;

	protected Module(String name, String description, Category category) {
		this.name = name;
		this.description = description;
		this.category = category;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean value) {
		if (this.enabled == value) {
			return;
		}
		this.enabled = value;
		Minecraft mc = Minecraft.getInstance();
		if (value) {
			onEnable(mc);
		} else {
			onDisable(mc);
		}
	}

	public void toggle() {
		setEnabled(!enabled);
	}

	/** Wird jeden Client-Tick aufgerufen, solange das Modul an ist. */
	public void onTick(Minecraft mc) {
	}

	protected void onEnable(Minecraft mc) {
	}

	protected void onDisable(Minecraft mc) {
	}
}
