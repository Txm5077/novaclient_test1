package de.novaclient;

import de.novaclient.module.ModuleManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public final class HudRenderer {
	private HudRenderer() {
	}

	public static void render(GuiGraphics g, DeltaTracker delta) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.options.hideGui || !ModuleManager.HUD.isEnabled()) {
			return;
		}
		List<String> lines = new ArrayList<>();
		lines.add("FPS: " + mc.getFps());
		if (ModuleManager.CPS.isEnabled()) {
			lines.add("CPS: " + ModuleManager.CPS.getCps());
		}
		if (ModuleManager.COORDS.isEnabled()) {
			lines.add(String.format("XYZ: %.1f %.1f %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ()));
		}
		int y = 4;
		for (String line : lines) {
			g.drawString(mc.font, line, 4, y, 0xFFFFFFFF, true);
			y += mc.font.lineHeight + 2;
		}
	}
}
