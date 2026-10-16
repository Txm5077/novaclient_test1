package de.novaclient.gui;

import de.novaclient.module.Category;
import de.novaclient.module.Module;
import de.novaclient.module.ModuleManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Locale;

/** Menue im Spaltenlayout. Oeffnen und Schliessen mit Rechts-Shift. */
public class MenuScreen extends Screen {
	private static final int PANEL_W = 130;
	private static final int GAP = 10;
	private static final int HEADER_H = 20;
	private static final int ROW_H = 16;

	private static final int COL_PANEL = 0xF0080808;
	private static final int COL_BORDER = 0xFFE8E8E8;
	private static final int COL_TEXT = 0xFFFFFFFF;
	private static final int COL_DIM = 0xFF9A9A9A;
	private static final int COL_ON = 0xFF3D7BFF;
	private static final int COL_OFF = 0xFF3A3A3A;
	private static final int COL_HOVER = 0x30FFFFFF;

	public MenuScreen() {
		super(Component.literal("Nova Client"));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private int panelX(int index) {
		int count = Category.values().length;
		int total = count * PANEL_W + (count - 1) * GAP;
		return (this.width - total) / 2 + index * (PANEL_W + GAP);
	}

	private int panelY() {
		return this.height / 2 - 90;
	}

	@Override
	public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
		super.render(g, mouseX, mouseY, delta);

		String hover = null;
		Category[] cats = Category.values();
		for (int i = 0; i < cats.length; i++) {
			List<Module> modules = ModuleManager.byCategory(cats[i]);
			int x = panelX(i);
			int y = panelY();
			int h = HEADER_H + modules.size() * ROW_H + 6;

			g.fill(x - 1, y - 1, x + PANEL_W + 1, y + h + 1, COL_BORDER);
			g.fill(x, y, x + PANEL_W, y + h, COL_PANEL);
			g.drawString(this.font, cats[i].label, x + 8, y + 7, COL_TEXT, true);
			g.fill(x + 6, y + HEADER_H - 3, x + PANEL_W - 6, y + HEADER_H - 2, COL_BORDER);

			int ry = y + HEADER_H;
			for (Module m : modules) {
				boolean over = mouseX >= x && mouseX < x + PANEL_W && mouseY >= ry && mouseY < ry + ROW_H;
				if (over) {
					g.fill(x + 2, ry, x + PANEL_W - 2, ry + ROW_H, COL_HOVER);
					hover = m.description;
				}
				g.drawString(this.font, m.name.toUpperCase(Locale.ROOT), x + 8, ry + 4,
						m.isEnabled() ? COL_TEXT : COL_DIM, true);

				// Schalter rechts: kleine Pille mit Punkt
				int sx = x + PANEL_W - 24;
				int sy = ry + 4;
				g.fill(sx, sy, sx + 16, sy + 8, m.isEnabled() ? COL_ON : COL_OFF);
				int kx = m.isEnabled() ? sx + 9 : sx + 1;
				g.fill(kx, sy + 1, kx + 6, sy + 7, COL_TEXT);
				ry += ROW_H;
			}
		}

		if (hover != null) {
			int w = this.font.width(hover);
			g.drawString(this.font, hover, (this.width - w) / 2, panelY() + 190, COL_DIM, true);
		}
		String hint = "Rechts-Shift: Menue schliessen";
		g.drawString(this.font, hint, (this.width - this.font.width(hint)) / 2, this.height - 16, COL_DIM, true);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == 0) {
			double mx = event.x();
			double my = event.y();
			Category[] cats = Category.values();
			for (int i = 0; i < cats.length; i++) {
				int x = panelX(i);
				int ry = panelY() + HEADER_H;
				for (Module m : ModuleManager.byCategory(cats[i])) {
					if (mx >= x && mx < x + PANEL_W && my >= ry && my < ry + ROW_H) {
						m.toggle();
						ModuleManager.save();
						return true;
					}
					ry += ROW_H;
				}
			}
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (event.key() == GLFW.GLFW_KEY_RIGHT_SHIFT) {
			this.onClose();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	public void onClose() {
		ModuleManager.save();
		super.onClose();
	}
}
