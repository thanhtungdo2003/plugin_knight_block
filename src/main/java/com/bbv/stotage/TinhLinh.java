package com.bbv.stotage;

import com.bbv.blib.ConfigFile;
import com.bbv.knightblock.KnightBlock;
import com.bbv.object.LoaiTinhLinh;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TinhLinh implements TinhLinhData {
    private final ConfigFile configFile;
    private final UUID uuid;

    public TinhLinh(Player p) {
        this.configFile = new ConfigFile(KnightBlock.pl, "TinhLinh/" + p.getUniqueId() + ".yml");
        this.uuid = p.getUniqueId();
        HashMap<String, Object> dataSets = new HashMap<>();
        for (LoaiTinhLinh loaiTinhLinh : LoaiTinhLinh.values()) {
            dataSets.put(loaiTinhLinh.name() + ".level", 0);
            dataSets.put(loaiTinhLinh.name() + ".mana", 0);
            dataSets.put(loaiTinhLinh.name() + ".so_huu", false);
            dataSets.put(loaiTinhLinh.name() + ".active", false);
        }
        configFile.setDefaultConfigurationFile(dataSets);
    }

    @Override
    public void active(LoaiTinhLinh loaiTinhLinh) {
        Configuration configuration = configFile.getConfigFile();
        boolean soHuu = configuration.getBoolean(loaiTinhLinh.name() + ".so_huu");
        if (soHuu) {
            for (LoaiTinhLinh typeData : LoaiTinhLinh.values()) {
                configFile.setConfigurationFile(loaiTinhLinh.name() + ".active", true);
                if (loaiTinhLinh != typeData) {
                    configFile.setConfigurationFile(typeData.name() + ".active", false);
                }
            }
            KnightBlock.messager.msg(getOwner(), KnightBlock.toColor("&7Đã triệu hồi &bTinh Linh " + loaiTinhLinh.getDisplayName()));
        }
    }

    @Override
    public void disable(LoaiTinhLinh loaiTinhLinh) {
        configFile.setConfigurationFile(loaiTinhLinh.name() + ".active", false);
        KnightBlock.reloadPlayer(getOwner());
    }

    @Override
    public void give(LoaiTinhLinh loaiTinhLinh) {
        configFile.setConfigurationFile(loaiTinhLinh.name() + ".so_huu", true);
        configFile.setConfigurationFile(loaiTinhLinh.name() + ".level", 1);
        configFile.setConfigurationFile(loaiTinhLinh.name() + ".mana", loaiTinhLinh.getMaxMana());
    }

    @Override
    public void remove(LoaiTinhLinh loaiTinhLinh) {
        configFile.setConfigurationFile(loaiTinhLinh.name() + ".so_huu", false);
        configFile.setConfigurationFile(loaiTinhLinh.name() + ".level", 0);
        configFile.setConfigurationFile(loaiTinhLinh.name() + ".mana", 0);
    }

    @Override
    public void setLevel(LoaiTinhLinh loaiTinhLinh, int level) {
        configFile.setConfigurationFile(loaiTinhLinh.name() + ".level", level);
    }

    @Override
    public void setMana(LoaiTinhLinh loaiTinhLinh, int mana) {
        configFile.setConfigurationFile(loaiTinhLinh.name() + ".mana", mana);
    }

    @Override
    public int getLevel(LoaiTinhLinh loaiTinhLinh) {
        Configuration configuration = configFile.getConfigFile();
        return configuration.getInt(loaiTinhLinh.name() + ".level");
    }

    @Override
    public int getMana(LoaiTinhLinh loaiTinhLinh) {
        Configuration configuration = configFile.getConfigFile();
        return configuration.getInt(loaiTinhLinh.name() + ".mana");
    }

    @Override
    public boolean hasTinhLinh(LoaiTinhLinh loaiTinhLinh) {
        Configuration configuration = configFile.getConfigFile();
        return configuration.getBoolean(loaiTinhLinh.name() + ".so_huu");
    }

    @Override
    public boolean isActive(LoaiTinhLinh loaiTinhLinh) {
        Configuration configuration = configFile.getConfigFile();
        return configuration.getBoolean(loaiTinhLinh.name() + ".active");
    }

    @Override
    public LoaiTinhLinh getTypeActive() {
        Configuration configuration = configFile.getConfigFile();
        for (String KEY : configuration.getKeys(false)) {
            if (configuration.getBoolean(KEY + ".active")) {
                return LoaiTinhLinh.valueOf(KEY);
            }
        }
        return null;
    }


    @Override
    public Player getOwner() {
        return Bukkit.getPlayer(uuid);
    }
}
