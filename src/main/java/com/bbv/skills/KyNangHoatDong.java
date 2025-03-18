package com.bbv.skills;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class KyNangHoatDong {
    private static final HashMap<UUID, List<KyNangDacBiet>> skills_active = new HashMap<>();

    protected static void addSkillActive(KyNangDacBiet kyNangDacBiet) {
        UUID uuidCaster = kyNangDacBiet.getCaster().getUniqueId();
        List<KyNangDacBiet> kyNangDacBiets;
        if (!skills_active.containsKey(uuidCaster)) {
            kyNangDacBiets = new ArrayList<>();
        } else {
            kyNangDacBiets = skills_active.get(uuidCaster);
        }
        kyNangDacBiets.add(kyNangDacBiet);
        skills_active.put(uuidCaster, kyNangDacBiets);
    }
    protected static void removeSkillActive(KyNangDacBiet kyNangDacBiet) {
        UUID uuidCaster = kyNangDacBiet.getCaster().getUniqueId();
        List<KyNangDacBiet> kyNangDacBiets = skills_active.get(uuidCaster);
        kyNangDacBiets.remove(kyNangDacBiet);
        skills_active.put(uuidCaster, kyNangDacBiets);
    }
    public static KyNangDacBiet getSkill(Player caster, LoaiKyNang loaiKyNang){
        UUID uuidCaster = caster.getUniqueId();
        if (skills_active.containsKey(uuidCaster)){
            List<KyNangDacBiet> kyNangDacBietList = skills_active.get(uuidCaster);
            for (KyNangDacBiet kyNangDacBiet: kyNangDacBietList){
                if (kyNangDacBiet.getType().equals(loaiKyNang)){
                    return kyNangDacBiet;
                }
            }
        }
        return null;
    }
}
