package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.object.DamageType;
import com.bbv.object.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.UUID;

public class SBocPha extends KyNangHoatDong implements KyNangDacBiet {
    private boolean Active;
    private final LoaiKyNang loaiKyNang;
    private final UUID caster;
    private BukkitTask task;

    public SBocPha(UUID uuid) {
        this.loaiKyNang = LoaiKyNang.BOC_PHA;
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
        PlayerStats stats = KnightBlockAPI.getPlayerStats(p);
        Active = true;
        addSkillActive(this);
        (new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                count++;
                p.setNoDamageTicks(20);
                p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 100);
                for (int i = 0; i < 50; i++) {
                    Location randomLoc = KnightBlockAPI.createRandomLocation(p.getLocation(), 2);
                    p.getWorld().spawnParticle(Particle.SMOKE_LARGE, randomLoc, 0);
                    p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, randomLoc, 0);
                }
                if (count > 20) {
                    this.cancel();
                    for (Entity e : p.getWorld().getNearbyEntities(p.getLocation(), 9, 9, 9)) {
                        if (e instanceof LivingEntity && !e.equals(p) && !KnightBlockAPI.isNPC(e)) {
                            double distance = e.getLocation().distance(p.getLocation());
                            if (distance <= 2) {
                                ((LivingEntity) e).damage(stats.getSTVatLy() * 3, p);
                            } else if (distance <= 3) {
                                ((LivingEntity) e).damage(stats.getSTVatLy() * 2, p);
                            } else if (distance <= 4) {
                                ((LivingEntity) e).damage(stats.getSTVatLy(), p);
                            } else if (distance > 4){
                                ((LivingEntity) e).damage(stats.getSTVatLy() / 2, p);
                            }
                            Vector v = p.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
                            e.setVelocity(v.multiply(-4).setY(0.8));
                        }
                    }
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1,1);
                    p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, p.getLocation(), 1);
                    p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p.getLocation(), 100, 10,10,-10, 1);
                    p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p.getLocation(), 100, 10,10,-10, 1);
                    p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation(), 100, 10,10,-10, 1);
                    disable();
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 5);
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
