package de.novaclient.module.impl;

import de.novaclient.NovaClient;
import de.novaclient.module.Category;
import de.novaclient.module.Module;
import net.minecraft.client.Minecraft;

public class ZoomModule extends Module {
	private static final int ZOOM_FOV = 30;
	private Integer savedFov = null;

	public ZoomModule() {
		super("Zoom", "Halte C zum Reinzoomen", Category.MISC);
	}

	@Override
	public void onTick(Minecraft mc) {
		if (mc.player == null) {
			return;
		}
		boolean held = mc.screen == null && NovaClient.ZOOM_KEY.isDown();
		if (held) {
			if (savedFov == null) {
				savedFov = mc.options.fov().get();
			}
			mc.options.fov().set(ZOOM_FOV);
		} else {
			restore(mc);
		}
	}

	private void restore(Minecraft mc) {
		if (savedFov != null) {
			mc.options.fov().set(savedFov);
			savedFov = null;
		}
	}

	@Override
	protected void onDisable(Minecraft mc) {
		restore(mc);
	}
}
