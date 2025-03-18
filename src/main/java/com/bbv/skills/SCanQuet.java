package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.ParticleManager;
import com.bbv.object.DamageType;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SCanQuet extends KyNangHoatDong implements KyNangDacBiet {
    private boolean Active;
    private final LoaiKyNang loaiKyNang;
    private final LivingEntity caster;
    private final BukkitTask task;
    private double damage = 0;
    private DamageType damageType = DamageType.VAT_LY;

    public SCanQuet(LivingEntity uuid) {
        this.loaiKyNang = LoaiKyNang.CAN_QUET;
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
        LivingEntity e = getCaster();
        if (isActive()) {
            return false;
        }
        if (e instanceof Player) {
            if (!KnightBlockAPI.getTimeLeftMess(e, loaiKyNang.name())) {
                return false;
            }
            KnightBlockAPI.setTimeLeft(e, loaiKyNang.name(), loaiKyNang.getCooldown());
            if (!KnightBlock.users.get(caster.getUniqueId()).consumeMana(loaiKyNang.getManaConsume())) {
                return false;
            }
        }
        Active = true;
        addSkillActive(this);
        e.setVelocity(e.getLocation().getDirection().setY(0.05).multiply(-3));
        e.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, e.getLocation(), 1);
        e.getWorld().playSound(e.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP,1,4);
          (new BukkitRunnable() {
            @Override
            public void run() {
                e.setVelocity(e.getLocation().getDirection().setY(0.05).multiply(4));
                e.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, e.getLocation(), 1);
                e.getWorld().playSound(e.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP,1,4);
                (new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, e.getLocation(), 10,0.1,0.1,-0.1,0.5);
                        ParticleManager.spawnCircleParticles(e, e.getEyeLocation(),30,2,Particle.SWEEP_ATTACK.ordinal(), damageType, damage,2);
                        e.getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP,1,0);
                        e.teleport(e.getLocation());
                        disable();
                    }
                }).runTaskLater(KnightBlock.pl, 6);
            }
        }).runTaskLater(KnightBlock.pl, 6);
        return true;
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

    }

    @Override
    public void setDamageType(DamageType type) {

    }
}
