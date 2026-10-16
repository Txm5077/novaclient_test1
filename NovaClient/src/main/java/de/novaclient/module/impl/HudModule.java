package de.novaclient.module.impl;

import de.novaclient.module.Category;
import de.novaclient.module.Module;

/** Die eigentliche Darstellung macht HudRenderer. */
public class HudModule extends Module {
	public HudModule() {
		super("HUD", "FPS und CPS im Overlay", Category.RENDER);
	}
}
