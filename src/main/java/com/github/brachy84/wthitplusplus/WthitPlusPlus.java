package com.github.brachy84.wthitplusplus;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class WthitPlusPlus implements ModInitializer {

    public static final String MODID = "wthitplusplus";
    public static final String NAME = "WTHIT++";

    @Override
    public void onInitialize() {

    }

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
