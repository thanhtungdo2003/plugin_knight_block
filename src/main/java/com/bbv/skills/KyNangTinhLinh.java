package com.bbv.skills;

import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.ParticleManager;
import com.bbv.object.PlayerStats;
import com.bbv.object.TinhLinh;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KyNangTinhLinh {
    public static void wind(Entity damager, Vector direction, Location start, double damage) {
        (new BukkitRunnable() {
            int count = 0;
            final World world = start.getWorld();

            @Override
            public void run() {
                count++;
                if (count > 20) {
                    this.cancel();
                } else {
                    Location l = start.add(direction.multiply(1));
                    world.spawnParticle(Particle.SWEEP_ATTACK, l, 3);
                    for (Entity e : world.getNearbyEntities(start, 1.5, 1.5, 1.5)) {
                        if (e instanceof LivingEntity && !e.equals(damager)) {
                            ((LivingEntity) e).damage(damage, damager);
                            ((LivingEntity) e).addPotionEffect(PotionEffectType.SLOW.createEffect(5 * 20, 3));
                            break;
                        }
                    }
                    world.playSound(l, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 3);
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 0);
    }

    public static void land(Entity damager, Location start, double damage) {
        Block block = start.getBlock();
        List<Block> gai = new ArrayList<>();
        if (block.getRelative(BlockFace.DOWN).getType().equals(Material.POINTED_DRIPSTONE)) {
            return;
        }
        start.getWorld().spawnParticle(Particle.GLOW, start, 10);
        if (block.getType().isAir()) {
            block.setType(Material.POINTED_DRIPSTONE);
            gai.add(block);
        }
        (new BukkitRunnable() {
            final World world = start.getWorld();
            int count = 0;

            @Override
            public void run() {
                count++;
                if (count == 1) {
                    if (block.getRelative(BlockFace.UP).getType().isAir()) {
                        block.getRelative(BlockFace.UP).setType(Material.POINTED_DRIPSTONE);
                        gai.add(block.getRelative(BlockFace.UP));
                    }
                } else if (count == 2) {
                    if (block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType().isAir()) {
                        block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(Material.POINTED_DRIPSTONE);
                        gai.add(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP));
                    }
                    this.cancel();
                    for (Entity e : world.getNearbyEntities(start, 1.5, 1.5, 1.5)) {
                        if (e instanceof LivingEntity && !e.equals(damager)) {
                            ((LivingEntity) e).damage(damage, damager);
                            e.setVelocity(e.getLocation().toVector().zero().setY(1));
                        }
                    }
                    (new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (Block block1 : gai) {
                                block1.setType(Material.AIR);
                                world.spawnParticle(Particle.BLOCK_CRACK, block1.getLocation(), 10, Material.STONE.createBlockData());
                            }
                        }
                    }).runTaskLater(KnightBlock.pl, 20);
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 0);
    }

    public static void fireStom(Entity damager, LivingEntity liveE, Location start, double damage) {
        (new BukkitRunnable() {
            int count = 0;
            final World world = start.getWorld();

            @Override
            public void run() {
                count++;
                if (count > 100) {
                    this.cancel();
                } else {
                    if (liveE != null && liveE.isDead()) {
                        this.cancel();
                        return;
                    }
                    if (liveE.getWorld().equals(world)) {
                        for (int i = 0; i < 5; i++) {
                            world.spawnParticle(Particle.FLAME, start, 3, 0.15, 0.15, -0.15, 0);
                        }
                        Vector direction = start.toVector().subtract(liveE.getEyeLocation().toVector()).normalize().multiply(-0.45);
                        start.add(direction);
                        for (Entity e : world.getNearbyEntities(start, 0.1, 0.1, 0.1)) {
                            if (e.equals(liveE)) {
                                this.cancel();
                                liveE.setFireTicks(220);
                                liveE.damage(damage, damager);
                                break;
                            }
                        }
                    }
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 0);
    }

    public static void tiaSet(Player p, Entity tinhLinh, Location start, Vector direction, double damage, int range, int radius) {
        World world = p.getWorld();
        world.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 10);
        line:
        for (int i = 0; i < range; i++) {
            Location l = start.add(direction.normalize().multiply(0.5));
            world.spawnParticle(Particle.ELECTRIC_SPARK, l, 0);
            for (Entity e : world.getNearbyEntities(l, 0.5, 0.5, 0.5)) {
                if (!e.equals(p) && e instanceof LivingEntity) {
                    ((LivingEntity) e).damage(damage, p);
                    int count = 0;
                    for (Entity eOther : world.getNearbyEntities(l, radius, radius, radius)) {
                        if (!eOther.equals(p) && eOther instanceof LivingEntity && !(eOther instanceof ArmorStand) && !eOther.equals(e) && !eOther.equals(tinhLinh)) {
                            count++;
                            if (count > 4) {
                                break;
                            }
                            line2:
                            for (double j = 0; j < 15; j += 0.5) {
                                l = l.add(((LivingEntity) eOther).getEyeLocation().toVector().subtract(l.toVector()).normalize().multiply(0.5));
                                world.spawnParticle(Particle.WAX_ON, l, 0);
                                world.spawnParticle(Particle.ELECTRIC_SPARK, l, 0);
                                for (Entity target : world.getNearbyEntities(l, 0.5, 0.5, 0.5)) {
                                    if (!target.equals(p) && target.equals(eOther)) {
                                        ((LivingEntity) eOther).damage(damage, p);
                                        break line2;
                                    }
                                }
                            }
                        }
                    }
                    break line;
                }
            }
        }

    }

    private static void lighting(Player p, LivingEntity tinhLinh, Location start, LivingEntity target, double damage) {
        p.getWorld().spawnParticle(Particle.GLOW, start, 10);
        Vector direction = target.getEyeLocation().toVector().subtract(start.toVector()).normalize();
        Location loc = start.clone();
        Location[] points = new Location[11];
        points[0] = start;
        for (int i = 1; i < 11; i++) {
            Location point = KnightBlockAPI.createRandomLocation(loc.add(direction.multiply(1)), 1);
            points[i] = point;
        }
        points[10] = target.getEyeLocation();
        for (int i = 0; i < 10; i++) {
            Location l1 = points[i];
            Location l2 = points[i + 1];
            Vector d = l2.toVector().subtract(l1.toVector()).normalize();
            Location l = l1.clone();
            for (double j = 0; j < 8; j++) {
                l.add(d.clone().multiply(0.3));
                l.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, l, 0);
                for (Entity e : start.getWorld().getNearbyEntities(l, 1, 1, 1)) {
                    if (e instanceof LivingEntity && !e.equals(p) && !e.equals(tinhLinh)) {
                        tiaSet(p, tinhLinh, l, target.getEyeLocation().toVector().subtract(l.toVector()).normalize(), damage, 6, 15);
                        break;
                    }
                }
            }
        }
    }

    public static void tiaNuoc(Player p, Entity tinhLinh, LivingEntity target, double damage, double range) {
        (new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                count++;
                if (count > 2) {
                    this.cancel();
                }
                Location start = KnightBlockAPI.createRandomLocation(tinhLinh.getLocation(), 1);
                Vector direction = target.getEyeLocation().toVector().subtract(tinhLinh.getLocation().toVector()).normalize();
                p.getWorld().spawnParticle(Particle.GLOW, start, 10);
                for (double i = 0; i < range; i += 0.5) {
                    Location l = start.add(direction.normalize().multiply(0.5));
                    for (int j = 0; j < 10; j++) {
                        p.getWorld().spawnParticle(Particle.WATER_BUBBLE, l, 1);
                        p.getWorld().spawnParticle(Particle.WATER_WAKE, l, 0);
                    }
                    for (Entity e : p.getWorld().getNearbyEntities(l, 0.2, 0.2, 0.2)) {
                        if (!e.equals(p) && e instanceof LivingEntity) {
                            ((LivingEntity) e).damage(damage, p);
                            ((LivingEntity) e).addPotionEffect(PotionEffectType.DARKNESS.createEffect(60, 10));
                            ((LivingEntity) e).addPotionEffect(PotionEffectType.SLOW_DIGGING.createEffect(20, 10));
                        }
                    }
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 10);
    }

    private static void skillTinhLinhHacAm(Player p, LivingEntity tinhLinh, LivingEntity target, double damage) {
        tinhLinh.getWorld().spawnParticle(Particle.GLOW, tinhLinh.getLocation(), 10);
        for (Location l : ParticleManager.SphereLocation(target.getEyeLocation(), 2, 0.6, 0.6, 0.6)) {
            p.getWorld().spawnParticle(Particle.SOUL, l, 0);
        }
        List<Vex> vexs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Vex vex = (Vex) p.getWorld().spawnEntity(KnightBlockAPI.createRandomLocation(target.getEyeLocation(), 3), EntityType.VEX);
            vex.setCustomName(KnightBlock.toColor("&7Linh hồn &8&lHắc Ám &7[" + p.getName() + "]"));
            vex.setCustomNameVisible(true);
            vex.setNoDamageTicks(60);
            vex.setTarget(target);
            vex.setLifeTicks(500);
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
                    vex.setTarget(target);
                    vex.setCharging(true);
                    for (Entity e : vex.getNearbyEntities(1, 1, 1)) {
                        if (e instanceof LivingEntity && !e.equals(p) && !e.equals(vex)) {
                            if (e.getCustomName() != null && e.getCustomName().contains("[" + p.getName() + "]"))
                                continue;
                            ((LivingEntity) e).damage(damage, p);
                            break;
                        }
                    }
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 20);
    }

    public static void onSkill(Player p, LivingEntity target) {
        PlayerStats stats = KnightBlock.getPlayerStats(p);
        if (stats == null) {
            return;
        }
        if (KnightBlockAPI.getTimeLeft(p, "tinhlinhskill")) {
            TinhLinh tinhLinh = stats.getTinhLinh();
            if (tinhLinh != null && tinhLinh.isShowing()) {
                if (tinhLinh.getEntity().equals(target)) {
                    return;
                }
                tinhLinh.consumeMana(1);
                int levelTinhLinh = tinhLinh.getLevel();
                int timeLeft = 11;
                double damage = 0;
                Random random = new Random();
                switch (levelTinhLinh) {
                    case 1:
                    case 2:
                        timeLeft = 7;
                        damage = random.nextInt(10);
                        break;
                    case 3:
                    case 4:
                        timeLeft = 6;
                        damage = random.nextInt(18);
                        break;
                    case 5:
                        timeLeft = 5;
                        damage = random.nextInt(23);
                        break;
                    case 6:
                        timeLeft = 4;
                        damage = random.nextInt(28);
                        break;
                    case 7:
                        timeLeft = 3;
                        damage = random.nextInt(35);
                        break;
                    case 8:
                    case 9:
                        timeLeft = 3;
                        damage = random.nextInt(40);
                        break;
                    case 10:
                        timeLeft = 2;
                        damage = random.nextInt(47);
                        break;
                }
                KnightBlockAPI.setTimeLeft(p, "tinhlinhskill", timeLeft);
                if (tinhLinh.getLocation() != null) {
                    switch (tinhLinh.getLoaiTinhLinh()) {
                        case LUA:
                            KyNangTinhLinh.fireStom(p, target, tinhLinh.getLocation(), damage);
                            p.getWorld().playSound(tinhLinh.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
                            break;
                        case GIO:
                            KyNangTinhLinh.wind(p, target.getEyeLocation().toVector().subtract(tinhLinh.getLocation().toVector()).normalize(), tinhLinh.getLocation(), damage - (damage * 0.25));
                            break;
                        case DAT:
                            KyNangTinhLinh.land(p, target.getLocation(), damage + 2);
                            p.getWorld().playSound(tinhLinh.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 10);
                            break;
                        case NUOC:
                            tiaNuoc(p, tinhLinh.getEntity(), target, damage - 5, 30);
                            break;
                        case SET:
                            lighting(p, (LivingEntity) tinhLinh.getEntity(), tinhLinh.getLocation(), target, damage);
                            p.getWorld().playSound(tinhLinh.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 10);
                            break;
                        case HACAM:
                            skillTinhLinhHacAm(p, (LivingEntity) tinhLinh.getEntity(), target, damage);
                            break;
                    }
                }
            }
        }
    }
}
