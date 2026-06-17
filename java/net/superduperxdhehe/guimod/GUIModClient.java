package net.superduperxdhehe.guimod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class GUIModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityInfoOverlay.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            Entity looked = getLookedAtEntity(client.player, 10.0);
            if (looked instanceof LivingEntity living) {
                EntityInfoOverlay.setEntity(living);
            } else {
                EntityInfoOverlay.setEntity(null);
            }
        });
    }

    private Entity getLookedAtEntity(ClientPlayerEntity player, double range) {
        Vec3d start = player.getEyePos();
        Vec3d look = player.getRotationVec(1.0f);
        Vec3d end = start.add(look.x * range, look.y * range, look.z * range);

        Box searchBox = player.getBoundingBox().stretch(look.multiply(range)).expand(1.0);

        EntityHitResult hit = ProjectileUtil.raycast(
                player,
                start,
                end,
                searchBox,
                entity -> !entity.isSpectator() && entity != player,
                range * range
        );

        return hit != null ? hit.getEntity() : null;
    }
}
