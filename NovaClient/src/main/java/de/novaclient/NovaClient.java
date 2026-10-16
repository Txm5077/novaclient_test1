package de.novaclient;

import com.mojang.blaze3d.platform.InputConstants;
import de.novaclient.gui.MenuScreen;
import de.novaclient.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class NovaClient implements ClientModInitializer {
	public static final KeyMapping.Category CATEGORY =
			KeyMapping.Category.register(Identifier.fromNamespaceAndPath("novaclient", "main"));

	public static final KeyMapping MENU_KEY = KeyBindingHelper.registerKeyBinding(
			new KeyMapping("key.novaclient.menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, CATEGORY));

	public static final KeyMapping ZOOM_KEY = KeyBindingHelper.registerKeyBinding(
			new KeyMapping("key.novaclient.zoom", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY));

	@Override
	public void onInitializeClient() {
		ModuleManager.load();

		ClientTickEvents.END_CLIENT_TICK.register(mc -> {
			while (MENU_KEY.consumeClick()) {
				if (mc.screen == null) {
					mc.setScreen(new MenuScreen());
				}
			}
			ModuleManager.tick(mc);
		});

		WorldRenderEvents.BEFORE_TRANSLUCENT.register(HitboxRenderer::render);
		HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("novaclient", "hud"), HudRenderer::render);

		ClientLifecycleEvents.CLIENT_STOPPING.register(mc -> ModuleManager.save());
	}
}
