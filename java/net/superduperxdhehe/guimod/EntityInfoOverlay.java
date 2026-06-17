package net.superduperxdhehe.guimod;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class EntityInfoOverlay {

    private static String entityName = null;
    private static float health = 0;
    private static float maxHealth = 0;
    private static float armor = 0;

    private static Identifier HEART_ICON = Identifier.ofVanilla("hud/heart/full");
    private static Identifier ARMOR_ICON = Identifier.ofVanilla("hud/armor_full");

    public static void setEntity(LivingEntity entity) {
        if (entity == null) {
            entityName = null;
            return;
        }
        entityName = entity instanceof PlayerEntity playerEntity ? playerEntity.getStringifiedName() : entity.getName().getString();
        health = entity.getHealth();
        maxHealth = entity.getMaxHealth();
        armor = entity.getArmor();
    }

    public static void register() {
        HudRenderCallback.EVENT.register((context, renderTickCounter) -> {
            if (entityName == null) return;

            MinecraftClient client = MinecraftClient.getInstance();
            TextRenderer textRenderer = client.textRenderer;

            String healthText = (Math.round(health * 2) / 2.0f) + " / " + maxHealth;
            String armorText = String.valueOf(armor);

            int screenWidth = client.getWindow().getScaledWidth();

            int boxHeight = 46;
            int margin = 10;
            int text_margin = margin + 13;

            int nameWidth = textRenderer.getWidth(entityName) + (margin * 2);
            int healthWidth = textRenderer.getWidth(healthText) + text_margin + margin;
            int armorWidth = textRenderer.getWidth(armorText) + text_margin + margin;

            int boxWidth = Math.max(margin + margin, Math.max(nameWidth, Math.max(healthWidth, armorWidth)));

            int x = (screenWidth - boxWidth) / 2;
            int y = 12;

            context.fill(x, y, x + boxWidth, y + boxHeight, 0xAA000000);
            context.drawText(textRenderer, entityName, x + margin, y + 8, 0xFFFFFFFF, true);

            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, HEART_ICON, x + margin, y + 20, 9, 9);
            context.drawText(textRenderer, healthText, x + text_margin, y + 20, 0xFFD3D3D3, true);

            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ARMOR_ICON, x + margin, y + 32, 9, 9);
            context.drawText(textRenderer, armorText, x + text_margin, y + 32, 0xFFD3D3D3, true);
        });
    }
}
