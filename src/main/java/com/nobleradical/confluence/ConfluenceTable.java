package com.nobleradical.confluence;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;

public class ConfluenceTable extends Block implements BlockEntityProvider{

    public static final BooleanProperty POWERED = BooleanProperty.of("powered");
     
    public ConfluenceTable(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    }
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TableBlockEntity(pos, state);
    }
    @Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (world.isClient()) return;
        boolean intPwr = state.get(POWERED);
        boolean extPwr = world.isReceivingRedstonePower(pos);
        if (!intPwr && extPwr) {
            Confluence.LOGGER.debug("Powered");
            world.setBlockState(pos, state.with(POWERED, true));
            Confluence.TABLE_BLOCK_ENTITY.get(world, pos).craft(world, pos);
        } else if (intPwr && !extPwr) {
            Confluence.LOGGER.debug("Unpowered");
            world.setBlockState(pos, state.with(POWERED, false));
        }
	}
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(POWERED, false);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
            BlockEntityType<T> type) {
        if (type != Confluence.TABLE_BLOCK_ENTITY) return null;
        return (world1, pos, state1, be) -> TableBlockEntity.tick(world1, pos, state1, be);
    }
}