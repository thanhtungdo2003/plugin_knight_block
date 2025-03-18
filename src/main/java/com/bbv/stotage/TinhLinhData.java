package com.bbv.stotage;


import com.bbv.object.LoaiTinhLinh;
import org.bukkit.entity.Player;

public interface TinhLinhData {
    void active(LoaiTinhLinh loaiTinhLinh);
    void disable(LoaiTinhLinh loaiTinhLinh);
    void give(LoaiTinhLinh loaiTinhLinh);
    void remove(LoaiTinhLinh loaiTinhLinh);
    void setLevel(LoaiTinhLinh loaiTinhLinh, int level);
    void setMana(LoaiTinhLinh loaiTinhLinh, int mana);
    int getLevel(LoaiTinhLinh loaiTinhLinh);
    int getMana(LoaiTinhLinh loaiTinhLinh);
    boolean hasTinhLinh(LoaiTinhLinh loaiTinhLinh);
    boolean isActive(LoaiTinhLinh loaiTinhLinh);
    LoaiTinhLinh getTypeActive();
    Player getOwner();
}
