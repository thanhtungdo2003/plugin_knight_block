package com.bbv.object;

import com.bbv.knightblock.KnightBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ExpDrop {
    private Location location;
    private int exp;
    private LivingEntity owner;
    private Item item;

    public ExpDrop(LivingEntity owner) {
        this.owner = owner;
    }

    public int getExp() {
        return exp;
    }

    public LivingEntity getOwner() {
        return owner;
    }

    public Item getItem() {
        return item;
    }

    public Location getLocation() {
        return location;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void drop(Location location, int amount) {
        this.location = location;
        this.exp = amount;
        ItemStack eitem = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta im = Bukkit.getItemFactory().getItemMeta(eitem.getType());
        im.setDisplayName(KnightBlock.toColor("&f" + amount + " §x§9§E§F§F§A§2E§x§6§9§F§F§7§4X§x§3§5§F§F§4§7P"));
        NamespacedKey key = new NamespacedKey(KnightBlock.pl, "exp_drop");
        PersistentDataContainer data = im.getPersistentDataContainer();
        data.set(key, PersistentDataType.INTEGER, amount);
        eitem.setItemMeta(im);
        Item expItem = location.getWorld().dropItem(location, eitem);
        expItem.setGlowing(true);
        expItem.setCustomNameVisible(true);
        expItem.setCustomName(im.getDisplayName());
        this.item = expItem;
    }
}
