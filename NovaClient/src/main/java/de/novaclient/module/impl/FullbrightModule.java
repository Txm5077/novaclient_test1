package de.novaclient.module.impl;

import de.novaclient.module.Category;
import de.novaclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FullbrightModule extends Module {
	public FullbrightModule() {
		super("Fullbright", "Alles hell (visueller Nachtsicht-Effekt)", Category.RENDER);
	}

	@Override
	public void onTick(Minecraft mc) {
		if (mc.player == null) {
			return;
		}
		MobEffectInstance current = mc.player.getEffect(MobEffects.NIGHT_VISION);
		if (current == null || current.getDuration() < 400) {
			mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1000, 0, false, false, false));
		}
	}

	@Override
	protected void onDisable(Minecraft mc) {
		if (mc.player != null) {
			mc.player.removeEffect(MobEffects.NIGHT_VISION);
		}
	}
}
