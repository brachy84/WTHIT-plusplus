package com.github.brachy84.wthitplusplus.mixin;

import com.github.brachy84.wthitplusplus.WthitPlusPlus;
import com.github.brachy84.wthitplusplus.renderer.Color;
import mcp.mobius.waila.config.HUDTheme;
import mcp.mobius.waila.config.WailaConfig;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(WailaConfig.ConfigOverlay.ConfigOverlayColor.class)
public class WailaConfigMixin {

    @Shadow private Map<Identifier, HUDTheme> themes;

    @Inject(method = "<init>()V", at = @At("TAIL"))
    public void construct(CallbackInfo ci) {
        for(HUDTheme theme : WthitPlusPlus.getThemes()) {
            themes.put(theme.getId(), theme);
        }
        int grey150 = Color.of(255, 255, 255, 150).asInt();
        int white = Color.of(255, 255, 255).asInt();
        HUDTheme trans = new HUDTheme(WthitPlusPlus.id("trans"), Color.of(0, 0, 0, 0).asInt(), grey150, grey150, white);
        HUDTheme top = new HUDTheme(WthitPlusPlus.id("top"), Color.of(102, 198, 227).asInt(), grey150, grey150, white);
        themes.put(trans.getId(), trans);
        themes.put(top.getId(), top);
    }
}
