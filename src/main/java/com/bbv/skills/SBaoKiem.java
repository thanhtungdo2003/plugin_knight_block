package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.ParticleManager;
import com.bbv.object.DamageType;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SBaoKiem extends KyNangHoatDong implements KyNangDacBiet {
    private boolean Active;
    private final LoaiKyNang loaiKyNang;
    private final LivingEntity caster;
    private final BukkitTask task;
    private double damage = 0;
    private DamageType damageType = DamageType.VAT_LY;

    public SBaoKiem(LivingEntity entity) {
        this.loaiKyNang = LoaiKyNang.BAO_KIEM;
        this.caster = entity;
        this.Active = false;
        this.task = null;
    }

    @Override
    public LoaiKyNang getType() {
        return loaiKyNang;
    }

    @Override
    public boolean cast() {
        LivingEntity p = getCaster();
        if (isActive()) {
            return false;
        }
        if (p instanceof Player) {
            if (!KnightBlockAPI.getTimeLeftMess(p, loaiKyNang.name())) {
                return false;
            }
            KnightBlockAPI.setTimeLeft(p, loaiKyNang.name(), loaiKyNang.getCooldown());
            if (!KnightBlock.users.get(caster.getUniqueId()).consumeMana(loaiKyNang.getManaConsume())) {
                return false;
            }
        }
        Active = true;
        addSkillActive(this);
        //Skill tại đây
        Location start = p.getEyeLocation();
        p.setVelocity(p.getLocation().getDirection().multiply(2));
        List<Location> centers = new ArrayList<>();
        for (double i = 0; i < 15; i += 0.5) {
            Location center = KnightBlockAPI.createRandomLocation(start.clone().add(p.getLocation().getDirection().multiply(i)), 4);
            centers.add(center);
        }
        (new BukkitRunnable() {
            int count = 0;
            final World world = p.getWorld();

            @Override
            public void run() {
                count++;
                if (count > 1) {
                    this.cancel();
                    for (Location center : centers) {
                        Location randomLoc = KnightBlockAPI.createRandomLocation(center, 3);
                        Vector v = center.toVector().subtract(randomLoc.toVector()).normalize().multiply(1);
                        for (double i = 0; i < 5; i += 0.3) {
                            Location lEffect = center.clone().add(v.clone().multiply(i));
                            world.spawnParticle(Particle.SOUL_FIRE_FLAME, lEffect, 10, 0.01, 0.01, -0.01, 0);
                            world.spawnParticle(Particle.FIREWORKS_SPARK, lEffect, 10, 0.01, 0.01, -0.01, 0);
                        }
                        KnightBlockAPI.damageForLocation(p, center, damageType, damage, 5, 20);
                    }
                }
                for (Location center : centers) {
                    world.spawnParticle(Particle.SONIC_BOOM, center, 1);
                    world.playSound(center, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 20);
        return true;
    }

    @Override
    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
    }


    @Override
    public LivingEntity getCaster() {
        return caster;
    }

    @Override
    public void disable() {
        Active = false;
        removeSkillActive(this);
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public boolean isActive() {
        return Active;
    }

    @Override
    public void setDamage(double damage) {
        this.damage = damage;
    }
}
