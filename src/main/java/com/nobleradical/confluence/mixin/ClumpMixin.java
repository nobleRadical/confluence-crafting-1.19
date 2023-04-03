package com.nobleradical.confluence.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.ItemEntity;


@Mixin(ItemEntity.class)
public class ClumpMixin {

    @Inject(at = @At("HEAD"), method = "canMerge()Z", cancellable = true)
	private void init(CallbackInfoReturnable<Boolean> info) {
        if (((ItemEntity) (Object) this).getScoreboardTags().contains("doNotClump")) {
            info.setReturnValue(false);
        }
	}
}
