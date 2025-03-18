package com.bbv.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GUI {
    private final Inventory Inv;
    private final GUIType Type;
    private Object tag;
    public GUI(GUIType type){
        this.Inv = Bukkit.createInventory(null, type.getSize(),type.getTitle());
        this.Type = type;
    }
    public void show(Player p){
        p.openInventory(this.Inv);
        CreateGUI.guiUsing.put(p.getUniqueId(), this);
    }
    public GUIType getType() {
        return Type;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Inventory getInv() {
        return Inv;
    }
}
