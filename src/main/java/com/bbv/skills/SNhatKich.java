package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.object.DamageType;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SNhatKich extends KyNangHoatDong implements KyNangDacBiet {
    private boolean Active;
    private final LoaiKyNang loaiKyNang;
    private final LivingEntity caster;
    private final BukkitTask task;
    private double damage;
    private DamageType damageType;
    private Vector direction;
    public SNhatKich(LivingEntity uuid) {
        this.loaiKyNang = LoaiKyNang.NHAT_KICH;
        this.caster = uuid;
        this.Active = false;
        this.task = null;
        damage = 1;
        damageType = DamageType.VAT_LY;
        direction = uuid.getEyeLocation().getDirection();
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
        //Skill tại đây
        Location start = e.getEyeLocation();
        List<Location> centers = new ArrayList<>();
        for (int i = 0; i < 50; i += 1) {
            Location center = start.clone().add(direction.clone().multiply(i));
            centers.add(center);
        }
        (new BukkitRunnable() {
            final World world = e.getWorld();

            @Override
            public void run() {
                if (centers.isEmpty()) {
                    this.cancel();
                    disable();
                    return;
                }
                for (double i = 0; i < 2; i += 0.3) {
                    world.spawnParticle(Particle.ELECTRIC_SPARK, centers.get(0).clone().add(direction.clone().multiply(i)), 0);
                }
                world.spawnParticle(Particle.FLAME, centers.get(0).clone(), 10, 0, 0, 0, 0.07);
                for (Entity e : world.getNearbyEntities(centers.get(0), 1, 1, 1)) {
                    if (e instanceof LivingEntity && !e.equals(caster) && !KnightBlockAPI.isNPC(e)) {
                        KnightBlockAPI.damageForLocation(caster, centers.get(0), damageType, damage, 1,1);
                        ((LivingEntity) e).addPotionEffect(PotionEffectType.SLOW.createEffect(60, 10));
                    }
                }
                if (!centers.get(0).getBlock().getType().isAir()) {
                    world.spawnParticle(Particle.EXPLOSION_LARGE, centers.get(0), 1);
                    world.playSound(centers.get(0), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    this.cancel();
                    disable();
                    return;
                }
                centers.remove(0);
            }
        }).runTaskTimer(KnightBlock.pl, 0, 0);
        return true;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
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

    @Override
    public void setDamageType(DamageType type) {
        this.damageType = type;
    }
}
