package com.bbv.listener;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.object.DamageType;
import com.bbv.object.PlayerStats;
import com.bbv.skills.*;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import net.Indyuce.mmoitems.api.event.ItemDropEvent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityListener implements Listener {
    private static List<Material> blackListMateria = new ArrayList<>();

    static {
        blackListMateria.add(Material.COBBLESTONE);
        blackListMateria.add(Material.RAW_GOLD);
        blackListMateria.add(Material.RAW_COPPER);
        blackListMateria.add(Material.RAW_IRON);
        blackListMateria.add(Material.COAL);
        blackListMateria.add(Material.REDSTONE);
        blackListMateria.add(Material.LAPIS_LAZULI);
        blackListMateria.add(Material.DIAMOND);
        blackListMateria.add(Material.EMERALD);
    }

    @EventHandler
    private void onTargetEvent(EntityTargetLivingEntityEvent event) {
        LivingEntity target = event.getTarget();
        Entity entity = event.getEntity();
        if (target instanceof Player) {
            if (entity instanceof LivingEntity) {
                Player p = (Player) target;
                if (entity instanceof Vex) {
                    if (entity.getCustomName() != null && entity.getCustomName().contains(KnightBlock.toColor("&7[" + p.getName() + "]"))) {
                        event.setCancelled(true);
                        return;
                    }
                }
                KyNangTinhLinh.onSkill(p, (LivingEntity) entity);
                PlayerStats stats = KnightBlock.getPlayerStats(p);
                if (stats != null && stats.getTinhLinh() != null) {
                    stats.getTinhLinh().setTarget((LivingEntity) entity);
                }
            }
            Player p = (Player) target;
            PlayerStats stats = KnightBlock.getPlayerStats(p);
            if (stats == null || stats.getTinhLinh() == null) return;
            if (stats.getTinhLinh().getEntity() != null && stats.getTinhLinh().getEntity().equals(entity)) {
                event.setCancelled(true);
            }
        }
        if ((target != null) && (target.getCustomName() != null) && target.getCustomName().contains(KnightBlock.toColor("&l\uD83D\uDC7B Tinh Linh"))) {
            event.setCancelled(true);
        }
        if ((entity.getCustomName() != null) && entity.getCustomName().contains(KnightBlock.toColor("&l\uD83D\uDC7B Tinh Linh"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onTinhLinhDamageEntityEvent(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if ((entity.getCustomName() != null) && entity.getCustomName().contains(KnightBlock.toColor("&l\uD83D\uDC7B Tinh Linh"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onProjectileHitEvent(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        if (entity.getCustomName() != null && entity.getCustomName().contains("[ARROWSKILL]")) {
            if (entity instanceof Arrow) {
                Arrow arrow = (Arrow) entity;
                if (event.getHitEntity() instanceof Player) {
                    Player p = (Player) event.getHitEntity();
                    if (arrow.getCustomName().contains("%" + p.getName() + "%")) {
                        event.setCancelled(true);
                    }
                }
                entity.getWorld().spawnParticle(Particle.GLOW, entity.getLocation(), 10);
                entity.remove();
            }
        }
    }

    @EventHandler
    private void onAddCustomSkillMythicMob(MythicMobSpawnEvent event) {
        if (event.getMobType().getInternalName().equalsIgnoreCase("talinh_daituong")) {
            (new BukkitRunnable() {
                @Override
                public void run() {
                    if (event.getMob().getEntity().isDead()) this.cancel();
                    if (event.getMob().isDead()) this.cancel();
                    Entity caster = event.getEntity();
                    if (caster instanceof Monster) {
                        if (((Monster) caster).getTarget() == null) return;
                    }
                    KyNangDacBiet skill;
                    switch (new Random().nextInt(2)) {
                        case 0:
                            for (Entity e : caster.getNearbyEntities(50, 50, 50)) {
                                e.sendMessage(KnightBlock.toColor(caster.getName() + " &cThi triển tuyệt kỹ &fKì Bão Kiếm"));
                            }
                            skill = new SBaoKiem((LivingEntity) caster);
                            skill.setDamage(90);
                            skill.setDamageType(DamageType.CHUAN);
                            skill.cast();
                            break;
                        case 1:
                            for (Entity e : caster.getNearbyEntities(50, 50, 50)) {
                                e.sendMessage(KnightBlock.toColor(caster.getName() + " &cThi triển tuyệt kỹ &cLưỡi Kiếm Địa Ngục"));
                            }
                            skill = new SCanQuet((LivingEntity) caster);
                            skill.setDamage(500);
                            skill.setDamageType(DamageType.VAT_LY);
                            skill.cast();
                            break;
                    }

                }
            }).runTaskTimer(KnightBlock.pl, 0, 130);
        } else if (event.getMobType().getInternalName().equalsIgnoreCase("xathu")) {
            (new BukkitRunnable() {
                @Override
                public void run() {
                    if (event.getMob().getEntity().isDead()) this.cancel();
                    if (event.getMob().isDead()) this.cancel();
                    Entity caster = event.getEntity();
                    if (caster instanceof Monster) {
                        if (((Monster) caster).getTarget() == null) return;
                    }
                    KyNangDacBiet skill;
                    switch (new Random().nextInt(2)) {
                        case 0:
                            for (Entity e : caster.getNearbyEntities(50, 50, 50)) {
                                e.sendMessage(KnightBlock.toColor(caster.getName() + " &cThi triển tuyệt kỹ &eThiên Kích"));
                            }
                            skill = new SThienKich((LivingEntity) caster);
                            skill.setDamage(100);
                            skill.cast();
                            break;
                        case 1:
                            for (Entity e : caster.getNearbyEntities(50, 50, 50)) {
                                e.sendMessage(KnightBlock.toColor(caster.getName() + " &cThi triển tuyệt kỹ &d&lTử Kích"));
                            }
                            caster.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, caster.getLocation(), 2);
                            caster.getWorld().spawnParticle(Particle.SMOKE_NORMAL, caster.getLocation(), 50, 0, 0, 0, 0.5);
                            caster.setVelocity(new Vector(0, 1, 0));

                            (new BukkitRunnable() {
                                int count = 0;

                                @Override
                                public void run() {
                                    count++;
                                    if (count > 10) {
                                        ((LivingEntity) caster).removePotionEffect(PotionEffectType.LEVITATION);
                                        this.cancel();
                                    }
                                    Entity finalTarget = null;
                                    if (((Mob) caster).getTarget() != null)
                                        finalTarget = ((Mob) caster).getTarget();
                                    SNhatKich skill = new SNhatKich((LivingEntity) caster);
                                    skill.setDamage(80);
                                    skill.setDamageType(DamageType.CHUAN);
                                    if (finalTarget != null) {
                                        skill.setDirection(finalTarget.getLocation().toVector().subtract(KnightBlockAPI.createRandomLocation(caster.getLocation(), 3).toVector()).normalize());
                                    } else {
                                        skill.setDirection(caster.getLocation().getDirection());
                                    }
                                    skill.cast();
                                    ((LivingEntity) caster).addPotionEffect(PotionEffectType.LEVITATION.createEffect(20, -1));
                                }
                            }).runTaskTimer(KnightBlock.pl, 5, 10);
                            break;
                    }

                }
            }).runTaskTimer(KnightBlock.pl, 0, 100);
        }
    }

    @EventHandler
    private void onKillNormalItem(ItemSpawnEvent event) {
        Item item = event.getEntity();
        Material type = item.getItemStack().getType();
        if (blackListMateria.contains(type)) {
            if (item.getItemStack().getItemMeta() != null) {
                if (!item.getItemStack().getItemMeta().hasDisplayName()) {
                    event.setCancelled(true);
                    item.remove();
                }
            } else {
                event.setCancelled(true);
                item.remove();
            }
        }
    }
}
