package com.github.brachy84.wthitplusplus.plugins;

import com.github.brachy84.wthitplusplus.Feature;
import com.github.brachy84.wthitplusplus.WthitPlusPlus;
import com.github.brachy84.wthitplusplus.accessors.AbstractBlockAccess;
import com.github.brachy84.wthitplusplus.accessors.ClientInteractionAccess;
import com.github.brachy84.wthitplusplus.renderer.Color;
import com.github.brachy84.wthitplusplus.renderer.IconText;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class Mineable extends Feature implements IBlockComponentProvider {

    public static Identifier TOOL_INFO = WthitPlusPlus.id("block_tool_info");
    public static Identifier BREAK_PROGRESS = WthitPlusPlus.id("block_break_progress");
    ;

    public Mineable() {
    }

    private static final Map<Tag<Item>, String> TOOL_TAGS = Map.of(
            FabricToolTags.PICKAXES, "pickaxe",
            FabricToolTags.AXES, "axe",
            FabricToolTags.SHOVELS, "shovel",
            FabricToolTags.SWORDS, "sword",
            FabricToolTags.HOES, "hoe",
            FabricToolTags.SHEARS, "shear",
            TagRegistry.item(new Identifier("fabric", "wrenches")), "wrench"
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
        if (config.get(TOOL_INFO)) {
            ItemStack item = accessor.getPlayer().getStackInHand(Hand.MAIN_HAND);

            boolean requiresTool = ((AbstractBlockAccess) accessor.getBlock()).getSettings().doesRequiresTool();
            boolean unbreakable = ((AbstractBlockAccess) accessor.getBlock()).getSettings().getHardness() < 0;
            boolean correctTool = true;
            String required = "wthitplusplus.blockinfo.requires";
            String toolKey = "wthitplusplus.blockinfo.";

            Tag<Item> requiredTool = getRequiredTool(accessor.getBlock());
            Tag<Item> toolTag = getToolTag(item.getItem(), accessor.getBlock());

            if (requiredTool != null && toolTag != null) {
                correctTool = requiredTool == toolTag;
            }
            if (requiredTool != null) {
                toolKey += TOOL_TAGS.get(requiredTool);
                if (toolTag == null) correctTool = false;
            } else {
                toolKey += "none";
            }

            if (!unbreakable && (!requiresTool || item.getItem().isSuitableFor(accessor.getBlockState()))) {
                NbtCompound tag = IconText.createTag(16, 16, WthitPlusPlus.id("textures/gui/icons.png"), 0f, 0.5f, 0.5f, 1f, "§a" + I18n.translate("wthitplusplus.blockinfo.mineable"));
                tooltip.add(IDrawableText.of(IconText.ID, tag));
            } else {
                NbtCompound tag = IconText.createTag(16, 16, WthitPlusPlus.id("textures/gui/icons.png"), 0.5f, 0.5f, 1f, 1f, "§4" + I18n.translate("wthitplusplus.blockinfo.not_mineable"));
                tooltip.add(IDrawableText.of(IconText.ID, tag));
            }

            if (correctTool) {
                NbtCompound tag = IconText.createTag(16, 16, WthitPlusPlus.id("textures/gui/icons.png"), 0f, 0.5f, 0.5f, 1f, I18n.translate(required) + ": " + I18n.translate(toolKey), 0.65f);
                tooltip.add(IDrawableText.of(IconText.ID, tag));
            } else {
                NbtCompound tag = IconText.createTag(16, 16, WthitPlusPlus.id("textures/gui/icons.png"), 0.5f, 0.5f, 1f, 1f, I18n.translate(required) + ": " + I18n.translate(toolKey), 0.65f);
                tooltip.add(IDrawableText.of(IconText.ID, tag));
            }
        }

        if (config.get(BREAK_PROGRESS)) {
            float progress = ((ClientInteractionAccess) MinecraftClient.getInstance().interactionManager).getBreakProgress();
            if (progress > 0) {
                tooltip.add(ProgressBar.createText(progress, 70, 9, Color.of(100, 200, 100, 150).asInt()));
            }
        }
    }

    public Tag<Item> getRequiredTool(Block block) {
        for (Tag<Item> tag : TOOL_TAGS.keySet()) {
            if (ToolManagerImpl.entry(block).getMiningLevel(tag) > 0) {
                return tag;
            }
        }
        if (BlockTags.PICKAXE_MINEABLE.contains(block)) return FabricToolTags.PICKAXES;
        if (BlockTags.SHOVEL_MINEABLE.contains(block)) return FabricToolTags.SHOVELS;
        if (BlockTags.AXE_MINEABLE.contains(block)) return FabricToolTags.AXES;
        if (BlockTags.HOE_MINEABLE.contains(block)) return FabricToolTags.HOES;
        return null;
    }

    public Tag<Item> getToolTag(Item item, Block block) {
        for (Tag<Item> tag : TOOL_TAGS.keySet()) {
            if (ToolManagerImpl.entry(block).getMiningLevel(tag) > 0) {
                return tag;
            }
        }
        for (Tag<Item> tag : TOOL_TAGS.keySet()) {
            if (tag.contains(item)) {
                return tag;
            }
        }
        return null;
    }
}
