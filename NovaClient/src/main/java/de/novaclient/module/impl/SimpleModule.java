package de.novaclient.module.impl;

import de.novaclient.module.Category;
import de.novaclient.module.Module;

/** Modul ohne eigene Logik. Andere Module/Renderer lesen nur isEnabled(). */
public class SimpleModule extends Module {
	public SimpleModule(String name, String description, Category category) {
		super(name, description, category);
	}
}
