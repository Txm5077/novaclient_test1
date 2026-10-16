package de.novaclient.module.impl;

import de.novaclient.module.Category;
import de.novaclient.module.Module;
import net.minecraft.client.Minecraft;

public class SprintModule extends Module {
	public SprintModule() {
		super("Toggle Sprint", "Sprintet automatisch beim Vorwaertslaufen", Category.MISC);
	}

	@Override
	public void onTick(Minecraft mc) {
		if (mc.player == null || mc.screen != null) {
			return;
		}
		if (mc.options.keyUp.isDown()
				&& !mc.player.isUsingItem()
				&& !mc.player.horizontalCollision
				&& mc.player.getFoodData().getFoodLevel() > 6) {
			mc.player.setSprinting(true);
		}
	}
}
