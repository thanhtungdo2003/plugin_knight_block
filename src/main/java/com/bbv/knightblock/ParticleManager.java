package com.bbv.knightblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.bbv.blib.SimpleDatabaseManager;
import com.bbv.gui.CreateGUI;
import com.bbv.listener.PlayerListener;
import com.bbv.object.Culture;
import com.bbv.object.DamageType;
import com.bbv.object.PlayerStats;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleManager {
    static HashMap<Player, List<Location>> listLocation = new HashMap<>();

    public static void onParticleDustColor(Location loc, Color Color) {
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, new Particle.DustOptions(Color, 1));
    }

    public static void spawnCircleParticles(Location start, int particleCount, double radius, int particleType) {
        World world = start.getWorld();
        Location center = start.getBlock().getLocation();
        Particle particle = Particle.values()[particleType];
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY();
            double z = center.getZ() + radius * Math.sin(angle);
            world.spawnParticle(particle, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    public static void spawnCircleParticles(Location start, int particleCount, double radius, Color color) {
        World world = start.getWorld();
        Location center = start.getBlock().getLocation();
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY();
            double z = center.getZ() + radius * Math.sin(angle);
            world.spawnParticle(Particle.REDSTONE, x, y, z, 1, new Particle.DustOptions(Color.RED, 1));
        }
    }


    public static void spawnCircleParticlesY(Location center, Particle particle, int particleAmount, double multiply, double radius) {
        World world = center.getWorld();
        final Vector circleDir = center.getDirection().clone().setY(0);
        final Vector distanceFromPlayer = circleDir.clone().multiply(multiply);
        final double increments = 2 * Math.PI / particleAmount;
        for (double angle = 0; angle < 2 * Math.PI; angle += increments) {
            Vector rotatedVector = new Vector(0, 1, 0)
                    .multiply(radius)
                    .rotateAroundAxis(circleDir, angle);
            Location loc = rotatedVector
                    .toLocation(world)
                    .add(center.clone())
                    .add(distanceFromPlayer);
            world.spawnParticle(particle, loc, 0);
        }
    }

    public static void spawnCircleParticlesY(Location center, Particle particle, int particleAmount, double multiply, double radius, double stepFill) {
        World world = center.getWorld();
        final Vector circleDir = center.getDirection().clone().setY(0);
        final Vector distanceFromPlayer = circleDir.clone().multiply(multiply);
        final double increments = 2 * Math.PI / particleAmount;
        for (double i = 0; i < radius; i += stepFill) {
            for (double angle = 0; angle < 2 * Math.PI; angle += increments) {
                Vector rotatedVector = new Vector(0, 1, 0)
                        .multiply(radius)
                        .rotateAroundAxis(circleDir, angle);
                Location loc = rotatedVector
                        .toLocation(world)
                        .add(center.clone())
                        .add(distanceFromPlayer);
                world.spawnParticle(particle, loc, 0);
            }
        }
    }

    public static void particleThienKich(LivingEntity e, Location center, double damage) {
        World world = center.getWorld();
        for (int i = 0; i < 40; i++) {
            double angle = 2 * Math.PI * i / 40;
            double x = center.clone().getX() + 5 * Math.cos(angle);
            double y = center.clone().getY();
            double z = center.clone().getZ() + 5 * Math.sin(angle);
            world.spawnParticle(Particle.FLAME, x, y, z, 5, 0.1, 0.1, -0.1, 0);
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                double angle = 2 * Math.PI * j / 10;
                double x = center.getX() + i * Math.cos(angle);
                double y = center.getY();
                double z = center.getZ() + i * Math.sin(angle);
                Location loc = new Location(world, x, y, z);
                if (i == 4) {
                    world.spawnParticle(Particle.FLAME, loc, 0);
                }
                if (KnightBlockAPI.getRandom(2, 100)) {
                    Location locArrow = loc.clone().setDirection(new Vector(0, -3, 0));
                    ParticleManager.spawnCircleParticles(loc.clone(), 20, 0.5, Color.RED);
                    Arrow arrow = center.getWorld().spawnArrow(locArrow, locArrow.getDirection(), 1, -16);
                    arrow.setGlowing(true);
                    arrow.setShooter(e);
                    arrow.setDamage(damage);
                    arrow.setCustomName("[ARROWSKILL]%" + e.getName() + "%");
                }
            }
        }
    }

    public static void spawnCircleParticles(Entity damager, Location start, int particleCount, double radius, int particleType, DamageType damageType, double damage, double radiusDamange) {
        World world = start.getWorld();
        Location center = start.getBlock().getLocation();
        Particle particle = Particle.values()[particleType];
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY();
            double z = center.getZ() + radius * Math.sin(angle);
            Location loc = new Location(world, x, y, z);
            world.spawnParticle(particle, loc, 1, 0, 0, 0, 0);
            KnightBlockAPI.damageForLocation((LivingEntity) damager, loc,damageType, damage, radiusDamange, 0);
        }
    }

    public static List<Location> SphereLocation(Location l, double range, double step, double stepSize, double height) {
        List<Location> locs = new ArrayList<>();
        for (double p = 0.0; p <= Math.PI; p += step) {
            for (double d = 0.0; d < Math.PI * 2; d += stepSize) {
                double r = 1.0;
                double x = r * Math.cos(d) * Math.sin(p) * range;
                double y = r * Math.cos(p) * range + height;
                double z = r * Math.sin(d) * Math.sin(p) * range;
                Location loc = l.clone().add(x, y, z);
                locs.add(loc);
            }
        }
        return locs;
    }

    public static void createWall(Player player, Material block) {
        Location playerLoc = player.getLocation().add(0, -4, 0);
        List<Location> listLoc = new ArrayList<>();
        int wallX = playerLoc.getBlockX() + playerLoc.getDirection().getBlockX() * 1;
        int wallY = playerLoc.getBlockY() + 1;
        int wallZ = playerLoc.getBlockZ() + playerLoc.getDirection().getBlockZ() * 1;

        for (int x = wallX - 5; x <= wallX + 6; x++) {
            for (int y = wallY; y < wallY + 12; y++) {
                for (int z = wallZ - 5; z <= wallZ + 6; z++) {
                    Location loc = new Location(player.getWorld(), x, y, z);
                    if (x == wallX - 5 || x == wallX + 6 || z == wallZ - 5 || z == wallZ + 6) {
                        if (loc.getBlock().getType() == Material.AIR) {
                            loc.getBlock().setType(block);
                            listLoc.add(loc);
                        }
                    }
                }
            }
        }
        listLocation.put(player, listLoc);
    }

    public static void createStair(Player player, Material block) {
        Location playerLoc = player.getLocation().add(0, -4, 0);
        List<Location> listLoc = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            playerLoc.add(player.getLocation().getDirection().multiply(2));
            int wallX = playerLoc.getBlockX() + playerLoc.getDirection().getBlockX();
            int wallY = playerLoc.getBlockY() + 1;
            int wallZ = playerLoc.getBlockZ() + playerLoc.getDirection().getBlockZ();

            // Tạo bức tường hình vuông rộng 12 block
            for (int x = wallX - 3; x <= wallX + 4; x++) {
                for (int y = wallY; y < wallY + 2; y++) {
                    for (int z = wallZ - 3; z <= wallZ + 2; z++) {
                        Location loc = new Location(player.getWorld(), x, y, z);
                        if (!(x == wallX - 3 || x == wallX + 4 || z == wallZ - 3 || z == wallZ + 4)) {
                            if (loc.getBlock().getType() == Material.AIR) {
                                loc.getBlock().setType(block);
                                listLoc.add(loc);
                            }
                        }
                    }
                }
            }
        }
        listLocation.put(player, listLoc);
    }

    public static void flowingEffect(LivingEntity liveE, Location start, Particle particle, long period) {
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
                        world.spawnParticle(particle, start, 10, 0.1, 0.1, -0.1, 0);
                        Vector direction = start.toVector().subtract(liveE.getLocation().add(0, 0.4, 0).toVector()).normalize().multiply(-0.45);
                        start.add(direction);
                        for (Entity e : world.getNearbyEntities(start, 0.1, 0.1, 0.1)) {
                            if (e.equals(liveE)) {
                                this.cancel();
                                break;
                            }
                        }
                    }
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, period);
    }

    public static void flowingEffect(LivingEntity liveE, Location start, Particle particle, long period, int rangeRandom) {
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
                        world.spawnParticle(particle, start, 10, 0.1, 0.1, -0.1, 0);
                        Vector direction = start.toVector().subtract(KnightBlockAPI.createRandomLocation(liveE.getLocation(), rangeRandom).toVector()).normalize().multiply(-0.45);
                        start.add(direction);
                        for (Entity e : world.getNearbyEntities(start, 0.1, 0.1, 0.1)) {
                            if (e.equals(liveE)) {
                                this.cancel();
                                break;
                            }
                        }
                    }
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, period);
    }

    public static void castEffectUpdateCul(Player p, boolean baoVeCap) {
        UUID uuid = p.getUniqueId();
        int needExp = CreateGUI.needEXP.get(uuid);
        double rate = CreateGUI.rateUpCul.get(uuid);
        CreateGUI.rateUpCul.remove(uuid);
        CreateGUI.needEXP.remove(uuid);
        KnightBlockAPI.removeItemInHand(p, 1);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp take " + p.getName() + " " + needExp);
        p.closeInventory();
        (new BukkitRunnable() {
            int progress = 1;

            @Override
            public void run() {
                if (!KnightBlock.getTreeOfWorld().getLocOfBeams().isEmpty()) {
                    for (Location locOfBeam : KnightBlock.getTreeOfWorld().getLocOfBeams()) {
                        if (baoVeCap) {
                            flowingEffect(p, locOfBeam.clone(), Particle.VILLAGER_HAPPY, 1);
                        } else {
                            flowingEffect(p, locOfBeam.clone(), Particle.ELECTRIC_SPARK, 1);
                        }
                    }
                }
                p.setNoDamageTicks(60);
                progress++;
                if (progress >= 10) {
                    this.cancel();
                    PlayerListener.updatings.remove(uuid);
                    if (!p.isOnline()) {
                        return;
                    }
                    PlayerStats stats = KnightBlockAPI.getPlayerStats(p);
                    Culture culture = stats.getCulture();
                    if (!baoVeCap) {
                        if (KnightBlockAPI.getRandom((int) (rate * 100), 10000)) {
                            if (culture.getLevel() < 10) {
                                SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
                                databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "level", culture.getLevel() + 1, "uuid", p.getUniqueId().toString());
                                culture.setLevel(culture.getLevel() + 1);
                                p.sendTitle(KnightBlockAPI.toColor("&a&lNâng cấp thành công"), KnightBlock.toColor("&aCấp độ: " + culture.getRankName()));
                                KnightBlock.reloadPlayer(p);
                                Bukkit.broadcastMessage(KnightBlock.toColor("&eChúc mừng người chơi &a" + p.getName() + " &echuyển hoá kinh nghiệm thành công: &6Thăng cấp: " + culture.getRankName()));
                                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            }
                        } else {
                            if (culture.getLevel() > 1) {
                                SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
                                databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "level", culture.getLevel() - 1, "uuid", p.getUniqueId().toString());
                                culture.setLevel(culture.getLevel() - 1);
                                p.sendTitle(KnightBlockAPI.toColor("&4&lNâng cấp Thất bại"), KnightBlock.toColor("&c(-1 level) &cCấp độ: " + culture.getRankName()));
                                KnightBlock.reloadPlayer(p);
                                return;
                            }
                            p.sendTitle(KnightBlockAPI.toColor("&4&lNâng cấp Thất bại"), KnightBlock.toColor("&cCấp độ: " + culture.getRankName()));
                        }
                    } else {
                        if (KnightBlockAPI.getRandom((int) (rate * 100), 10000)) {
                            if (culture.getLevel() < 10) {
                                SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
                                databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "level", culture.getLevel() + 1, "uuid", p.getUniqueId().toString());
                                culture.setLevel(culture.getLevel() + 1);
                                p.sendTitle(KnightBlockAPI.toColor("&a&lNâng cấp thành công"), KnightBlock.toColor("&aCấp độ: " + culture.getRankName()));
                                KnightBlock.reloadPlayer(p);
                                Bukkit.broadcastMessage(KnightBlock.toColor("&eChúc mừng người chơi &a" + p.getName() + " &echuyển hoá kinh nghiệm thành công: &6Thăng cấp: " + culture.getRankName()));
                                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            }
                        } else {
                            p.sendTitle(KnightBlockAPI.toColor("&4&lNâng cấp Thất bại"), KnightBlock.toColor("&cCấp độ: " + culture.getRankName()));
                        }
                    }
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 10);
    }
}
