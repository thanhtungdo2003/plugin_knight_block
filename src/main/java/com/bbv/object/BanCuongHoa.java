package com.bbv.object;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BanCuongHoa {
    private ItemStack upgradeItem;
    private ItemStack daCuongHoa;
    private int levelDaCuongHoa;
    private int levelWeapon;
    private int scale;
    private Player viewer;

    public BanCuongHoa(Player p) {
        this.viewer = p;
        this.daCuongHoa = null;
        this.upgradeItem = null;
        this.levelWeapon = -1;
        this.levelDaCuongHoa = 0;
    }

    public void setDaCuongHoa(ItemStack daCuongHoa) {
        this.daCuongHoa = daCuongHoa;
    }

    public void setLevelDaCuongHoa(int levelDaCuongHoa) {
        this.levelDaCuongHoa = levelDaCuongHoa;
    }

    public void setLevelWeapon(int levelWeapon) {
        this.levelWeapon = levelWeapon;
    }

    public void setUpgradeItem(ItemStack upgradeItem) {
        this.upgradeItem = upgradeItem;
    }

    public int getLevelDaCuongHoa() {
        return levelDaCuongHoa;
    }

    public int getScale() {
        double scale = 8000;
        if (levelDaCuongHoa == 0) return -1;
        if (levelWeapon == -1) return -1;
        for (int i = 0; i < 21; i++) {
            if (i == levelWeapon) break;
            scale = scale / 1.40;
        }
        scale *= levelDaCuongHoa;
        if (scale > 10000) return 9000;
        return (int) scale;
    }

    public int getLevelWeapon() {
        return levelWeapon;
    }

    public ItemStack getDaCuongHoa() {
        return daCuongHoa;
    }

    public ItemStack getUpgradeItem() {
        return upgradeItem;
    }

    public Player getViewer() {
        return viewer;
    }
}
