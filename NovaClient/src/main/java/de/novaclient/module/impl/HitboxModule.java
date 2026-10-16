package de.novaclient.module.impl;

import de.novaclient.module.Category;
import de.novaclient.module.Module;

/** Die eigentliche Darstellung macht HitboxRenderer. */
public class HitboxModule extends Module {
	public HitboxModule() {
		super("Hitboxes", "Zeigt Hitboxen wie F3+B, ohne Tastenkombination", Category.COMBAT);
	}
}
