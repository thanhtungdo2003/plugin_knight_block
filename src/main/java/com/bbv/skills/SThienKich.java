package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.ParticleManager;
import com.bbv.object.DamageType;
import com.bbv.object.PlayerStats;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SThienKich extends KyNangHoatDong implements KyNangDacBiet {
    private boolean Active;
    private final LoaiKyNang loaiKyNang;
    private final LivingEntity caster;
    private BukkitTask task;
    private double damage;

    public SThienKich(LivingEntity uuid) {
        this.loaiKyNang = LoaiKyNang.THIEN_KICH;
        this.caster = uuid;
        this.Active = false;
        this.task = null;
        this.damage = 3;
    }

    @Override
    public LoaiKyNang getType() {
        return loaiKyNang;
    }

    @Override
    public boolean cast() {
        LivingEntity e = getCaster();
        boolean isPlayer = false;
        if (isActive()) {
            return false;
        }
        if (e instanceof Player) {
            isPlayer = true;
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
        for (int i = 0; i < 20; i++) {
            Location loc = e.getEyeLocation().add(e.getEyeLocation().getDirection().multiply(i));
            loc.getWorld().spawnParticle(Particle.WAX_OFF, loc, 0);
            for (Entity target : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if (!target.equals(e) && target instanceof LivingEntity) {
                    if (isPlayer) {
                        PlayerStats stats = KnightBlockAPI.getPlayerStats((Player) caster);
                        if (stats.getTinhLinh() != null && stats.getTinhLinh().isShowing() && stats.getTinhLinh().getEntity().equals(target)) {
                            continue;
                        }
                    }
                    (new BukkitRunnable() {
                        int count = 0;

                        @Override
                        public void run() {
                            Location center = target.getLocation().add(0, 10, 0);
                            count++;
                            if (count > 100) {
                                this.cancel();
                                disable();
                            }
                            ParticleManager.particleThienKich(e, center.clone(), damage);
                        }
                    }).runTaskTimer(KnightBlock.pl, 0, 2);
                    return true;
                }
            }
        }
        return false;
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

    }
}
