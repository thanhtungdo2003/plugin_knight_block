package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.object.DamageType;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class SAmSat extends KyNangHoatDong implements KyNangDacBiet {
    private boolean Active;
    private final LoaiKyNang loaiKyNang;
    private final UUID caster;
    private BukkitTask task;

    public SAmSat(UUID uuid) {
        this.loaiKyNang = LoaiKyNang.AM_SAT;
        this.caster = uuid;
        this.Active = false;
        this.task = null;
    }

    @Override
    public LoaiKyNang getType() {
        return loaiKyNang;
    }

    @Override
    public boolean cast() {
        Player p = getCaster();
        if (isActive()) {
            return false;
        }
        if (!KnightBlockAPI.getTimeLeftMess(p, loaiKyNang.name())) {
            return false;
        }
        KnightBlockAPI.setTimeLeft(p, loaiKyNang.name(), loaiKyNang.getCooldown());
        if (!KnightBlock.users.get(caster).consumeMana(loaiKyNang.getManaConsume())) {
            return false;
        }
        Active = true;
        addSkillActive(this);
        KnightBlockAPI.castSkillMagicByCommand(p, "cloak", 0, 1000 * 6);
        task = (new BukkitRunnable() {
            @Override
            public void run() {
                Active = false;
                disable();
            }
        }).runTaskLater(KnightBlock.pl, 20 * 5);
        return true;
    }

    @Override
    public Player getCaster() {
        return Bukkit.getPlayer(caster);
    }

    @Override
    public void disable() {
        Active = false;
        removeSkillActive(this);
        if (task != null) {
            task.cancel();
        }
        KnightBlockAPI.castSkillMagicByCommand(getCaster(), "cloak");
    }

    @Override
    public boolean isActive() {
        return Active;
    }

    @Override
    public void setDamage(double damage) {

    }

    @Override
    public void setDamageType(DamageType type) {

    }
}
