package com.nobleradical.confluence;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class SmolScreenHandler extends ScreenHandler {
    public SmolScreenHandler() {
        super(null, 0);
    }
    
    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity var1, int var2) {
        return ItemStack.EMPTY;
    }
}
