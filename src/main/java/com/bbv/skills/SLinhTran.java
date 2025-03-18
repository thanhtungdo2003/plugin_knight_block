package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.ParticleManager;
import com.bbv.object.DamageType;
import com.bbv.object.PlayerStats;
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

public class SLinhTran extends KyNangHoatDong implements KyNangDacBiet {
    private boolean Active;
    private final LoaiKyNang loaiKyNang;
    private final UUID caster;
    private final BukkitTask task;

    public SLinhTran(UUID uuid) {
        this.loaiKyNang = LoaiKyNang.LINH_TRAN;
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
        PlayerStats stats = KnightBlock.getPlayerStats(p);
        World world = p.getWorld();
        List<Location> soulLocs = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            soulLocs.add(KnightBlockAPI.createRandomLocation(p.getLocation().add(0, 5, 0), 7));
            world.spawnParticle(Particle.TOTEM, p.getEyeLocation(), 40);
        }
        world.spawnParticle(Particle.SONIC_BOOM, p.getEyeLocation(), 1);
        (new BukkitRunnable() {
            int count1 = 0;
            @Override
            public void run() {
                count1++;
                if (count1 > 70){
                    this.cancel();
                }
                if (soulLocs.isEmpty()) {this.cancel();return;}
                for (Entity e : world.getNearbyEntities(soulLocs.get(0), 20, 20, 20)) {
                    if (e instanceof LivingEntity && !e.equals(p) && !KnightBlockAPI.isNPC(e)) {
                        if (e instanceof Player && ((Player)e).getGameMode().equals(GameMode.CREATIVE)){
                            continue;
                        }
                        if ((stats.getTinhLinh() != null && stats.getTinhLinh().getEntity() != null && e.equals(stats.getTinhLinh().getEntity())))
                            continue;
                        Location start = soulLocs.get(0);
                        Vector v = e.getLocation().toVector().subtract(start.toVector()).normalize();
                        world.spawnParticle(Particle.SONIC_BOOM, start, 1);

                        soulLocs.remove(0);
                        (new BukkitRunnable() {
                            int count = 0;
                            @Override
                            public void run() {
                                count++;
                                if (count > 50){
                                    this.cancel();
                                }
                                Location newLoc = start.add(v);
                                world.spawnParticle(Particle.SOUL_FIRE_FLAME, newLoc, 10, 0.1,0.1,-0.1, 0);
                                world.spawnParticle(Particle.END_ROD, newLoc, 0);
                                for (Entity target: world.getNearbyEntities(newLoc, 1.5,1.5,1.5)) {
                                    if (target instanceof LivingEntity && !target.equals(p) && !KnightBlockAPI.isNPC(target)) {
                                        this.cancel();
                                        world.spawnParticle(Particle.EXPLOSION_LARGE, newLoc, 1);
                                        world.playSound(newLoc, Sound.ENTITY_GENERIC_EXPLODE, 1,10);
                                        ((LivingEntity)target).damage(10, p);
                                        if (target instanceof Player && KnightBlockAPI.isPlayerOnSafeZone((Player) target, target.getLocation())) {
                                            return;
                                        }
                                        KyNangBiDong.satThuongPhep(p, target, stats.getSTPhep());
                                        break;
                                    }
                                }
                            }
                        }).runTaskTimer(KnightBlock.pl,0,0);
                        break;
                    }
                }
                for (Location l : soulLocs) {
                    world.spawnParticle(Particle.END_ROD, l, 20, 0.1, 0.1, -0.1, 0);
                    world.spawnParticle(Particle.SOUL_FIRE_FLAME, l, 10, 0.1,0.1,-0.1, 0);
                    world.spawnParticle(Particle.SMOKE_NORMAL, l, 5, 0.5,0.5,-0.5, 0);
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 15);
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
