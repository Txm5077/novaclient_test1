package de.novaclient.module;

import de.novaclient.module.impl.CpsModule;
import de.novaclient.module.impl.FullbrightModule;
import de.novaclient.module.impl.HitboxModule;
import de.novaclient.module.impl.HudModule;
import de.novaclient.module.impl.SimpleModule;
import de.novaclient.module.impl.SprintModule;
import de.novaclient.module.impl.ZoomModule;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public final class ModuleManager {
	private static final List<Module> MODULES = new ArrayList<>();
	private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("novaclient.properties");

	public static final HitboxModule HITBOXES = new HitboxModule();
	public static final SimpleModule EYE_LINE = new SimpleModule("Eye Line", "Zeigt die Augenhoehe als Linie in der Hitbox", Category.COMBAT);
	public static final SimpleModule TARGET_COLOR = new SimpleModule("Target Color", "Hitbox wird rot, wenn du den Gegner anvisierst", Category.COMBAT);
	public static final SimpleModule PLAYERS_ONLY = new SimpleModule("Players Only", "Hitboxen nur bei Spielern", Category.COMBAT);

	public static final SprintModule SPRINT = new SprintModule();
	public static final ZoomModule ZOOM = new ZoomModule();
	public static final CpsModule CPS = new CpsModule();

	public static final HudModule HUD = new HudModule();
	public static final FullbrightModule FULLBRIGHT = new FullbrightModule();
	public static final SimpleModule COORDS = new SimpleModule("Coordinates", "Koordinaten im HUD", Category.RENDER);

	static {
		Collections.addAll(MODULES, HITBOXES, EYE_LINE, TARGET_COLOR, PLAYERS_ONLY,
				SPRINT, ZOOM, CPS,
				HUD, COORDS, FULLBRIGHT);
	}

	private ModuleManager() {
	}

	public static List<Module> all() {
		return MODULES;
	}

	public static List<Module> byCategory(Category category) {
		List<Module> list = new ArrayList<>();
		for (Module m : MODULES) {
			if (m.category == category) {
				list.add(m);
			}
		}
		return list;
	}

	public static void tick(Minecraft mc) {
		for (Module m : MODULES) {
			if (m.isEnabled()) {
				m.onTick(mc);
			}
		}
	}

	public static void load() {
		Properties props = new Properties();
		if (Files.exists(CONFIG)) {
			try (InputStream in = Files.newInputStream(CONFIG)) {
				props.load(in);
			} catch (IOException ignored) {
			}
		}
		for (Module m : MODULES) {
			String value = props.getProperty(m.name.replace(' ', '_'));
			if (value != null) {
				m.setEnabled(Boolean.parseBoolean(value));
			} else if (m == HITBOXES || m == HUD) {
				m.setEnabled(true);
			}
		}
	}

	public static void save() {
		Properties props = new Properties();
		for (Module m : MODULES) {
			props.setProperty(m.name.replace(' ', '_'), Boolean.toString(m.isEnabled()));
		}
		try (OutputStream out = Files.newOutputStream(CONFIG)) {
			props.store(out, "Nova Client");
		} catch (IOException ignored) {
		}
	}
}
