package com.github.brachy84.wthitplusplus.accessors;

public interface AbstractBlockAccess {

    SettingsAcces getSettings();

    interface SettingsAcces {
        boolean doesRequiresTool();
        float getHardness();
    }
}
