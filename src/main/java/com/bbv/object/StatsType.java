package com.bbv.object;

import com.bbv.knightblock.KnightBlock;

public enum StatsType {
    DAMAGE_BASE("",""),
    DAMAGE_MAGIC("",""),
    TANK("",""),
    HP("",""),
    THE_LUC("",""),
    CRIT_RATE("",""),
    NHANH_NHEN("","");
    private final String DisplayName;
    private final String Description;
    private StatsType(String displayName, String description){
        this.Description = description;
        this.DisplayName = displayName;
    }

    public String getDisplayName() {
        return KnightBlock.toColor(DisplayName);
    }

    public String getDescription() {
        return KnightBlock.toColor(Description);
    }
}
