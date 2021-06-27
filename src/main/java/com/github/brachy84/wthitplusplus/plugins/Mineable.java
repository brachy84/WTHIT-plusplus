package com.github.brachy84.wthitplusplus.plugins;

import com.github.brachy84.wthitplusplus.*;
import com.github.brachy84.wthitplusplus.accessors.AbstractBlockAccess;
import com.github.brachy84.wthitplusplus.accessors.ClientInteractionAccess;
import com.github.brachy84.wthitplusplus.renderer.Color;
import com.github.brachy84.wthitplusplus.renderer.Icon;
import com.github.brachy84.wthitplusplus.renderer.ProgressBar;
import com.google.common.collect.Lists;
import mcp.mobius.waila.api.*;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class Mineable extends Feature implements IBlockComponentProvider {

    public static Identifier TOOL_INFO = WthitPlusPlus.id("block_tool_info");
    public static Identifier BREAK_PROGRESS = WthitPlusPlus.id("block_break_progress");;

    public Mineable() {
    }

    private static final Map<String, Tag<Item>> TOOL_TAGS = Map.of(
            "pickaxe", FabricToolTags.PICKAXES,
            "axe", FabricToolTags.AXES,
            "shovel", FabricToolTags.SHOVELS,
            "sword", FabricToolTags.SWORDS,
            "hoe", FabricToolTags.HOES,
            "shear", FabricToolTags.SHEARS,
            "wrench", TagRegistry.item(new Identifier("fabric", "wrenches"))
    );

    private static final List<Tag<Item>> toolTags = Lists.newArrayList(
            FabricToolTags.AXES,
            FabricToolTags.HOES,
            FabricToolTags.PICKAXES,
            FabricToolTags.SHEARS,
            FabricToolTags.SHOVELS,
            FabricToolTags.SWORDS,
            TagRegistry.item(new Identifier("fabric", "wrenches"))
    );

    @Override
    public void initialize(IRegistrar registrar) {
        registrar.addConfig(TOOL_INFO, true);
        registrar.addConfig(BREAK_PROGRESS, true);
        registrar.addComponent(this, TooltipPosition.BODY, Block.class);
    }

    @Override
    public void appendBody(List<Text> tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if(config.get(TOOL_INFO)) {
            ItemStack item = accessor.getPlayer().getStackInHand(Hand.MAIN_HAND);

            boolean requiresTool = ((AbstractBlockAccess) accessor.getBlock()).getSettings().doesRequiresTool();
            boolean unbreakable = ((AbstractBlockAccess) accessor.getBlock()).getSettings().getHardness() < 0;
            boolean correctTool = false;
            String required = "wthitplusplus.blockinfo.requires";
            String toolKey = "wthitplusplus.blockinfo.";
            Map.Entry<String, Tag<Item>> entry = getToolTag(item.getItem());
            if(entry != null) {
                Tag<Item> tag = entry.getValue();
                correctTool = ToolManagerImpl.entry(accessor.getBlock()).getMiningLevel(tag) > 0;
                if (!correctTool)
                    correctTool = tag == getRequiredTool(accessor.getBlock());
                toolKey += entry.getKey();
            } else {
                toolKey += "none";
            }

            if (!unbreakable && (!requiresTool || item.getItem().isSuitableFor(accessor.getBlockState()))) {
                //IDrawableText checkmark = Icon.createText(16, 16, WthitPlusPlus.id("textures/gui/icons.png"), 0f, 0.5f, 0.5f, 1f);
                //tooltip.add(checkmark.append(new TranslatableText("wthitplusplus.blockinfo.mineable")));
                tooltip.add(new LiteralText("§a§lx§r §a" + I18n.translate("wthitplusplus.blockinfo.mineable")));
            } else {
                //IDrawableText cross = Icon.createText(16, 16, WthitPlusPlus.id("textures/gui/icons.png"), 0.5f, 0.5f, 1f, 1f);
                //tooltip.add(cross.append(new TranslatableText("wthitplusplus.blockinfo.not_mineable")));
                tooltip.add(new LiteralText("§4§lx§r §4" + I18n.translate("wthitplusplus.blockinfo.not_mineable")));
            }

            if (correctTool) {
                //IDrawableText checkmark = Icon.createText(8, 8, WthitPlusPlus.id("textures/gui/icons.png"), 0f, 0.5f, 0.5f, 1f);
                tooltip.add(new LiteralText("§a" + I18n.translate(required) + ": " + I18n.translate(toolKey)));
            } else {
                //IDrawableText cross = Icon.createText(8, 8, WthitPlusPlus.id("textures/gui/icons.png"), 0.5f, 0.5f, 1f, 1f);
                tooltip.add(new LiteralText("§4" + I18n.translate(required) + ": " + I18n.translate(toolKey)));
            }
        }

        if(config.get(BREAK_PROGRESS)) {
            float progress = ((ClientInteractionAccess)MinecraftClient.getInstance().interactionManager).getBreakProgress();
            if(progress > 0) {
                tooltip.add(ProgressBar.createText(progress, 70, 9, Color.of(100, 200, 100, 150).asInt()));
            }
        }
    }

    public Tag<Item> getRequiredTool(Block block) {
        if (BlockTags.PICKAXE_MINEABLE.contains(block)) return FabricToolTags.PICKAXES;
        if (BlockTags.SHOVEL_MINEABLE.contains(block)) return FabricToolTags.SHOVELS;
        if (BlockTags.AXE_MINEABLE.contains(block)) return FabricToolTags.AXES;
        if (BlockTags.HOE_MINEABLE.contains(block)) return FabricToolTags.HOES;
        return null;
    }

    public Map.Entry<String, Tag<Item>> getToolTag(Item item) {
        for (Map.Entry<String, Tag<Item>> entry : TOOL_TAGS.entrySet()) {
            if (entry.getValue().contains(item)) {
                return entry;
            }
        }
        return null;
    }
}
