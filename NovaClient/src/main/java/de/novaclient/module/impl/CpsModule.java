package de.novaclient.module.impl;

import de.novaclient.module.Category;
import de.novaclient.module.Module;
import net.minecraft.client.Minecraft;

import java.util.ArrayDeque;
import java.util.Deque;

public class CpsModule extends Module {
	private final Deque<Long> clicks = new ArrayDeque<>();
	private boolean wasDown = false;

	public CpsModule() {
		super("CPS Counter", "Zaehlt deine Klicks pro Sekunde", Category.MISC);
	}

	@Override
	public void onTick(Minecraft mc) {
		boolean down = mc.options.keyAttack.isDown();
		if (down && !wasDown) {
			clicks.addLast(System.currentTimeMillis());
		}
		wasDown = down;
		prune();
	}

	private void prune() {
		long now = System.currentTimeMillis();
		while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000) {
			clicks.pollFirst();
		}
	}

	public int getCps() {
		prune();
		return clicks.size();
	}
}
