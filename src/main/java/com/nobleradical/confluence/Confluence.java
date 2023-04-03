package com.nobleradical.confluence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Confluence implements ModInitializer {
	
    public static final Logger LOGGER = LoggerFactory.getLogger("confluence");
	public static final Block CONFLUENCE_TABLE = new ConfluenceTable(FabricBlockSettings.copy(Blocks.ENCHANTING_TABLE));
	public static final Block GLASS_CAULDRON = new GlassCauldron(FabricBlockSettings.of(Material.GLASS).requiresTool().strength(2.0f).nonOpaque());
    public static BlockEntityType<TableBlockEntity> TABLE_BLOCK_ENTITY;

	@Override
	public void onInitialize() {
        //TODO stop items from coalesing in cauldrons
        //2. Do not take the whole stack when making a recipe, only take one. (Return crafting inventory both times)
		Registry.register(Registry.ITEM, new Identifier("confluence", "confluence_table"), new BlockItem(CONFLUENCE_TABLE, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
		Registry.register(Registry.BLOCK, new Identifier("confluence", "confluence_table"), CONFLUENCE_TABLE);
        Registry.register(Registry.ITEM, new Identifier("confluence", "glass_cauldron"), new BlockItem(GLASS_CAULDRON, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        Registry.register(Registry.BLOCK, new Identifier("confluence", "glass_cauldron"), GLASS_CAULDRON);
        TABLE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "confluence:confluence_table", FabricBlockEntityTypeBuilder.create(TableBlockEntity::new, CONFLUENCE_TABLE).build(null));

		LOGGER.info("Confluence Initialized.");
	}
}
