package com.nobleradical.confluence;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;


public class ConfluenceClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Confluence.GLASS_CAULDRON, RenderLayer.getTranslucent());
        BlockEntityRendererFactories.register(Confluence.TABLE_BLOCK_ENTITY, TableBlockEntityRenderer::new);
    }
    
}
