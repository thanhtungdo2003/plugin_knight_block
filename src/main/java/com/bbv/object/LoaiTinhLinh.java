package com.bbv.object;

import com.bbv.knightblock.KnightBlock;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.EntityType;

import java.awt.*;

public enum LoaiTinhLinh {
    NONE("&7/tl","",0, null, false,0),
    LUA(ChatColor.of(new Color(255, 0, 0)) + "&lLửa", "",70, EntityType.MAGMA_CUBE, true, 50),
    GIO(ChatColor.of(new Color(255, 255, 255, 255)) + "&lGió", "",40, EntityType.ALLAY,false, 0),
    DAT(ChatColor.of(new Color(66, 143, 68)) + "&lĐất", "",50, EntityType.ZOGLIN,false, 0),
    NUOC(ChatColor.of(new Color(3, 112, 238)) + "&lNước", "",40, EntityType.ALLAY,false, 0),
    HACAM(ChatColor.of(new Color(82, 0, 0)) + "&lHắc Ám", "",90, EntityType.ALLAY,true, 100),
    SET(ChatColor.of(new Color(255, 249, 128)) + "&lSÉT", "",85, EntityType.ALLAY,true, 90);
    private final String DisplayName;
    private final String Description;
    private final int MaxMana;
    private final EntityType entityType;
    private final int PointsCost;
    private final boolean isSell;
    private LoaiTinhLinh(String displayName, String description, int maxMana, EntityType entityType, boolean isSell, int pointsCost){
        this.DisplayName = displayName;
        this.Description = description;
        this.MaxMana = maxMana;
        this.entityType = entityType;
        this.isSell = isSell;
        this.PointsCost = pointsCost;
    }

    public String getDescription() {
        return  KnightBlock.toColor(Description);
    }

    public String getDisplayName() {
        return KnightBlock.toColor(DisplayName);
    }

    public int getMaxMana() {
        return MaxMana;
    }

    public int getPointsCost() {
        return PointsCost;
    }

    public boolean isSell() {
        return isSell;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
