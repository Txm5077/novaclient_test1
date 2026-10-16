package de.novaclient;

import com.mojang.blaze3d.vertex.VertexConsumer;
import de.novaclient.module.ModuleManager;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.List;

/** Zeichnet Hitboxen als Drahtgitter, unabhaengig von F3+B. */
public final class HitboxRenderer {
	private static final double RANGE = 64.0;
	private static final double LINE = 0.012;

	private HitboxRenderer() {
	}

	public static void render(WorldRenderContext context) {
		if (!ModuleManager.HITBOXES.isEnabled()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null) {
			return;
		}

		float partial = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
		Vec3 camera = mc.gameRenderer.getMainCamera().position();
		boolean playersOnly = ModuleManager.PLAYERS_ONLY.isEnabled();
		boolean targetColor = ModuleManager.TARGET_COLOR.isEnabled();
		boolean eyeLine = ModuleManager.EYE_LINE.isEnabled();

		List<Entity> entities = mc.level.getEntities(mc.player, mc.player.getBoundingBox().inflate(RANGE), e -> true);
		if (entities.isEmpty()) {
			return;
		}

		context.matrices().pushPose();
		context.matrices().translate(-camera.x, -camera.y, -camera.z);
		VertexConsumer buffer = context.consumers().getBuffer(RenderTypes.debugFilledBox());
		Matrix4f matrix = context.matrices().last().pose();

		for (Entity entity : entities) {
			if (playersOnly && !(entity instanceof Player)) {
				continue;
			}
			// Position zwischen zwei Ticks glaetten, damit die Box nicht ruckelt
			Vec3 offset = new Vec3(
					Mth.lerp(partial, entity.xOld, entity.getX()) - entity.getX(),
					Mth.lerp(partial, entity.yOld, entity.getY()) - entity.getY(),
					Mth.lerp(partial, entity.zOld, entity.getZ()) - entity.getZ());
			AABB box = entity.getBoundingBox().move(offset);

			int color = ARGB.colorFromFloat(1f, 1f, 1f, 1f);
			if (targetColor && mc.crosshairPickEntity == entity) {
				color = ARGB.colorFromFloat(1f, 1f, 0.1f, 0.1f);
			}
			wireframe(buffer, matrix, box, color);

			if (eyeLine && entity instanceof LivingEntity) {
				double y = box.minY + entity.getEyeHeight();
				AABB line = new AABB(box.minX, y - LINE, box.minZ, box.maxX, y + LINE, box.maxZ);
				filled(buffer, matrix, line, ARGB.colorFromFloat(1f, 1f, 0f, 0f));
			}
		}
		context.matrices().popPose();
	}

	private static void wireframe(VertexConsumer b, Matrix4f m, AABB box, int color) {
		double[] xs = {box.minX, box.maxX};
		double[] ys = {box.minY, box.maxY};
		double[] zs = {box.minZ, box.maxZ};
		double t = LINE;
		for (double y : ys) {
			for (double z : zs) {
				filled(b, m, new AABB(box.minX - t, y - t, z - t, box.maxX + t, y + t, z + t), color);
			}
		}
		for (double x : xs) {
			for (double z : zs) {
				filled(b, m, new AABB(x - t, box.minY - t, z - t, x + t, box.maxY + t, z + t), color);
			}
		}
		for (double x : xs) {
			for (double y : ys) {
				filled(b, m, new AABB(x - t, y - t, box.minZ - t, x + t, y + t, box.maxZ + t), color);
			}
		}
	}

	/** Gefuellte Box, gleiche Vertex-Reihenfolge wie im Fabric-API-Testmod. */
	private static void filled(VertexConsumer v, Matrix4f m, AABB box, int color) {
		float x0 = (float) box.minX, y0 = (float) box.minY, z0 = (float) box.minZ;
		float x1 = (float) box.maxX, y1 = (float) box.maxY, z1 = (float) box.maxZ;
		// Front
		v.addVertex(m, x0, y0, z0).setColor(color);
		v.addVertex(m, x1, y0, z0).setColor(color);
		v.addVertex(m, x1, y1, z0).setColor(color);
		v.addVertex(m, x0, y1, z0).setColor(color);
		// Back
		v.addVertex(m, x1, y0, z1).setColor(color);
		v.addVertex(m, x0, y0, z1).setColor(color);
		v.addVertex(m, x0, y1, z1).setColor(color);
		v.addVertex(m, x1, y1, z1).setColor(color);
		// Left
		v.addVertex(m, x0, y0, z1).setColor(color);
		v.addVertex(m, x0, y0, z0).setColor(color);
		v.addVertex(m, x0, y1, z0).setColor(color);
		v.addVertex(m, x0, y1, z1).setColor(color);
		// Right
		v.addVertex(m, x1, y0, z0).setColor(color);
		v.addVertex(m, x1, y0, z1).setColor(color);
		v.addVertex(m, x1, y1, z1).setColor(color);
		v.addVertex(m, x1, y1, z0).setColor(color);
		// Top
		v.addVertex(m, x0, y1, z0).setColor(color);
		v.addVertex(m, x1, y1, z0).setColor(color);
		v.addVertex(m, x1, y1, z1).setColor(color);
		v.addVertex(m, x0, y1, z1).setColor(color);
		// Bottom
		v.addVertex(m, x0, y0, z1).setColor(color);
		v.addVertex(m, x1, y0, z1).setColor(color);
		v.addVertex(m, x1, y0, z0).setColor(color);
		v.addVertex(m, x0, y0, z0).setColor(color);
	}
}
