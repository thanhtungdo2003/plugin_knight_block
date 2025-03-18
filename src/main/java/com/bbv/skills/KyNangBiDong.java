package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.object.PlayerStats;
import dev.aurelium.auraskills.api.user.SkillsUser;
import io.lumine.mythic.lib.skill.handler.def.vector.Firebolt;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class KyNangBiDong {
    protected boolean noiTaiSatThu(Entity target) {
        if (target instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) target;
            return (livingEntity.getHealth() <= (livingEntity.getMaxHealth() * 0.5));
        }
        return false;
    }

    protected double chiMang(Player p, double eventDamage, int rate) {
        double damage = eventDamage;
        if (KnightBlockAPI.getRandom(rate, 10000)) {
            if (KnightBlock.getPlayerStats(p).consumeMana(10)) {
                damage = eventDamage * 2;
                KnightBlock.messager.msg(p, KnightBlock.toColor("&c&oĐòn chí mạng!"));
                return damage;
            }
        }
        return damage;
    }

    protected static void satThuongChuan(Player p, Entity entity, double damage) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (!livingEntity.isDead()) {
                double healthE = livingEntity.getHealth();
                double healthHasDamage = healthE - damage;
                if (livingEntity.getHealth() > 0) {
                    if (healthHasDamage > 0) {
                        livingEntity.setHealth(healthHasDamage);
                    } else {
                        livingEntity.setHealth(0);
                    }
                }
                livingEntity.getWorld().spawnParticle(Particle.BLOCK_CRACK, livingEntity.getEyeLocation(), 100, Material.REDSTONE_BLOCK.createBlockData());
            }
        }
    }

    protected static void satThuongPhep(Player p, Entity entity, double damage) {
        damage = damage / 1.2;
        PlayerStats stats = KnightBlock.getPlayerStats(p);
        if (stats.consumeMana(5)) {
            if (entity instanceof Player) {
                double chongChiu = stats.getChongChiu() / 2;
                damage -= chongChiu;
            }
            if (damage < 1) {
                damage = 1;
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                if (!livingEntity.isDead()) {
                    double healthE = livingEntity.getHealth();
                    double healthHasDamage = healthE - damage;
                    if (livingEntity.getHealth() > 0) {
                        if (healthHasDamage > 0) {
                            livingEntity.setHealth(healthHasDamage);
                        } else {
                            livingEntity.setHealth(0);
                        }
                    }
                    livingEntity.getWorld().spawnParticle(Particle.SPELL_WITCH, entity.getLocation(), 1);
                }
            }
            stats.setMana(stats.getMana() + (0.3 * damage));
        }
    }
}
