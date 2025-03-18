package com.bbv.object;

import com.bbv.knightblock.KnightBlock;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;

public enum ClassType {
    NONE("&7/class","", 0, 0, 0, 0, 0, 0),
    SAT_THU(ChatColor.of(new Color(194, 0, 41, 219)) + "\uD83D\uDD2A Sát Thủ", "", 6, 1, 1, 2, 6, 0),
    DAU_SI(ChatColor.of(new Color(77, 134, 134)) + "\uD83D\uDDE1 Đấu Sĩ", "", 7, 3, 3, 1, 1, 0),
    PHAP_SU(ChatColor.of(new Color(240, 31, 253)) + "\uD83D\uDD25 Pháp Sư", "", 0, 1, 1, 1, 4, 8),
    DO_DON(ChatColor.of(new Color(162, 162, 162)) + "♦ Đỡ đòn", "", 1, 7, 5, 1, 1, 0),
    XA_THU(ChatColor.of(new Color(131, 229, 123)) + "\uD83C\uDFF9 Xạ Thủ", "", 10, 1, 1, 2, 1, 0);

    private final String DisplayName;
    private final String Description;
    private final int damage;
    private final int tank;
    private final int hp;
    private final int speed;
    private final int critRate;
    private final int magicDamage;

    ClassType(String displayName, String description, int damage, int tank, int hp, int speed, int critRate, int magicDamage) {
        this.DisplayName = displayName;
        this.Description = description;
        this.damage = damage;
        this.tank = tank;
        this.hp = hp;
        this.speed = speed;
        this.critRate = critRate;
        this.magicDamage = magicDamage;
    }

    public String getDisplayName() {
        return KnightBlock.toColor(DisplayName);
    }

    public String getDescription() {
        return Description;
    }

    public int getDamage() {
        return damage;
    }

    public int getTank() {
        return tank;
    }

    public int getHp() {
        return hp;
    }

    public int getCritRate() {
        return critRate;
    }

    public int getSpeed() {
        return speed;
    }

    public int getMagicDamage() {
        return magicDamage;
    }
}
