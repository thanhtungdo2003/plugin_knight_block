package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.object.DamageType;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import java.util.UUID;

public class SThietThe extends KyNangHoatDong implements KyNangDacBiet {
    private final LoaiKyNang loaiKyNang;
    private final UUID caster;

    public SThietThe(UUID uuid) {
        this.loaiKyNang = LoaiKyNang.THIET_THE;
        this.caster = uuid;
    }

    @Override
    public LoaiKyNang getType() {
        return loaiKyNang;
    }

    @Override
    public boolean cast() {
        Player p = getCaster();
        if (!KnightBlockAPI.getTimeLeftMess(p, loaiKyNang.name())) {
            return false;
        }
        KnightBlockAPI.setTimeLeft(p, loaiKyNang.name(), loaiKyNang.getCooldown());
        if (!KnightBlock.users.get(caster).consumeMana(loaiKyNang.getManaConsume())) {
            return false;
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "castp "+p.getName()+" invincible cooldown 0");
        return true;
    }

    @Override
    public Player getCaster() {
        return Bukkit.getPlayer(caster);
    }


    @Override
    public void disable() {
        removeSkillActive(this);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setDamage(double damage) {

    }

    @Override
    public void setDamageType(DamageType type) {

    }
}
