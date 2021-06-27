package com.github.brachy84.wthitplusplus.mixin;

import com.github.brachy84.wthitplusplus.accessors.AbstractBlockAccess;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin implements AbstractBlockAccess {

    @Shadow
    @Final
    protected AbstractBlock.Settings settings;

    @Override
    public SettingsAcces getSettings() {
        return (SettingsAcces) settings;
    }

    @Mixin(AbstractBlock.Settings.class)
    public static class SettingsMixin implements SettingsAcces {

        @Shadow
        boolean toolRequired;

        @Shadow
        float hardness;

        @Override
        public boolean doesRequiresTool() {
            return toolRequired;
        }

        @Override
        public float getHardness() {
            return hardness;
        }
    }
}
