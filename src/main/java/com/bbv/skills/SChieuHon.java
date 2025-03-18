package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.ParticleManager;
import com.bbv.object.DamageType;
import com.bbv.object.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SChieuHon extends KyNangHoatDong implements KyNangDacBiet {
    private boolean Active;
    private final LoaiKyNang loaiKyNang;
    private final UUID caster;
    private BukkitTask task;

    public SChieuHon(UUID uuid) {
        this.loaiKyNang = LoaiKyNang.CHIEU_HON;
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
        PlayerStats stats = KnightBlockAPI.getPlayerStats(p);
        for (int i = 0; i < 20; i++) {
            Location loc = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(i));
            loc.getWorld().spawnParticle(Particle.GLOW, loc, 10, 0.1, 0.1, -0.1, 0.1);
            for (Entity target : loc.getWorld().getNearbyEntities(loc, 2, 2, 2)) {
                if (!target.equals(p) && target instanceof LivingEntity) {
                    if (stats.getTinhLinh() != null && stats.getTinhLinh().isShowing() && stats.getTinhLinh().getEntity().equals(target)) {
                        continue;
                    }
                    for (Location l : ParticleManager.SphereLocation(loc, 2, 0.6, 0.6, 0.6)) {
                        p.getWorld().spawnParticle(Particle.SOUL, l, 0);
                    }
                    List<Vex> vexs = new ArrayList<>();
                    for (int j = 0; j < 8; j++) {
                        Vex vex = (Vex) p.getWorld().spawnEntity(KnightBlockAPI.createRandomLocation(loc, 3), EntityType.VEX);
                        vex.setCustomName(KnightBlock.toColor("&7Linh hồn Hộ vệ&7[" + p.getName() + "]"));
                        vex.setCustomNameVisible(true);
                        vex.setNoDamageTicks(60);
                        vex.setTarget((LivingEntity) target);
                        vex.setLifeTicks(500);
                        vex.getEquipment().setItemInMainHand(null);
                        vexs.add(vex);
                    }
                    (new BukkitRunnable() {
                        int count = 0;

                        @Override
                        public void run() {
                            count++;
                            if (count > 10) {
                                this.cancel();
                                for (Vex vex : vexs) {
                                    vex.remove();
                                }
                            }
                            for (Vex vex : vexs) {
                                vex.setTarget((LivingEntity) target);
                                vex.setCharging(true);
                                for (Entity e : vex.getNearbyEntities(1, 1, 1)) {
                                    if (e instanceof LivingEntity && !e.equals(p) && !e.equals(vex)) {
                                        if (e.getCustomName() != null && e.getCustomName().contains("["+p.getName()+"]"))continue;
                                        ((LivingEntity) e).damage(5, p);
                                        break;
                                    }
                                }
                            }
                        }
                    }).runTaskTimer(KnightBlock.pl, 0, 20);
                    return true;
                }
            }
        }
        return false;
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
