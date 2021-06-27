package com.github.brachy84.wthitplusplus;

import com.github.brachy84.wthitplusplus.plugins.Mineable;
import com.github.brachy84.wthitplusplus.renderer.Icon;
import com.github.brachy84.wthitplusplus.renderer.ProgressBar;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;

import java.util.ArrayList;
import java.util.List;

public class WthitPlugin implements IWailaPlugin {

    private static final List<Feature> features = new ArrayList<>();

    @Override
    public void register(IRegistrar registrar) {
        registrar.addRenderer(ProgressBar.ID, new ProgressBar());
        registrar.addRenderer(Icon.ID, new Icon());
        features.add(new Mineable());

        features.forEach(feature -> feature.initialize(registrar));
    }
}
