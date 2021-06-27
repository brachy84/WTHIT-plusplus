package com.github.brachy84.wthitplusplus.mixin;

import com.github.brachy84.wthitplusplus.accessors.ClientInteractionAccess;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin implements ClientInteractionAccess {

    @Shadow private float currentBreakingProgress;

    @Override
    public float getBreakProgress() {
        return currentBreakingProgress;
    }
}
