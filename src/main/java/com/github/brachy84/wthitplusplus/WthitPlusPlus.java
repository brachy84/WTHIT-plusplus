package com.github.brachy84.wthitplusplus;

import com.github.brachy84.wthitplusplus.renderer.Color;
import mcp.mobius.waila.config.HUDTheme;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WthitPlusPlus implements ModInitializer {

    public static final String MODID = "wthitplusplus";
    public static final String NAME = "WTHIT++";

    private static final List<HUDTheme> THEMES = new ArrayList<>();

    @Override
    public void onInitialize() {
        //int grey150 = Color.of(255, 255, 255, 150).asInt();
        //int white = Color.of(255, 255, 255).asInt();
        //registerTheme(new HUDTheme(id("trans"), Color.of(0, 0, 0, 0).asInt(), grey150, grey150, white));
        //registerTheme(new HUDTheme(id("top"), Color.of(102, 198, 227).asInt(), grey150, grey150, white));
    }

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }

    public static void registerTheme(HUDTheme theme) {
        THEMES.add(theme);
    }

    public static List<HUDTheme> getThemes() {
        return Collections.unmodifiableList(THEMES);
    }
}
