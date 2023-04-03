package com.nobleradical.confluence;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Environment(EnvType.CLIENT)
public class TableBlockEntityRenderer implements BlockEntityRenderer<TableBlockEntity> {
    private static ItemStack stack = new ItemStack(Items.CRAFTING_TABLE, 1);
    
    public TableBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(TableBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        // Move the item
        matrices.translate(0.5, 0.69, 0.5);
        // Rotate the item
        //matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((blockEntity.getWorld().getTime() + tickDelta) * 4));
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
        matrices.pop();
    }

}
