package com.nobleradical.confluence;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.fluid.Fluid;
/**
 * An empty cauldron block.
 */
public class GlassCauldron
extends AbstractCauldronBlock {
    public GlassCauldron(AbstractBlock.Settings settings) {
        super(settings, CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR);
    }

    @Override
    public boolean isFull(BlockState state) {
        return false;
    }

    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return false;
    }
}

