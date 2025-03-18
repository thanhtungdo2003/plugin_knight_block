package com.bbv.scheduler;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntiCheat {
    public static final Map<UUID, Long> playerAFKTime = new HashMap<>();
    public static final long AFKTimeThreshold = 300;
    public static final HashMap<UUID, Location> playerLastLocation = new HashMap<>();
    public static String nameOfCaster = KnightBlock.toColor("&b> &c&lThợ Săn Hacker &b< ");

    public static void antiAFK() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(KnightBlock.pl, () -> {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.isOnline()) {
                    if (KnightBlockAPI.playerIsOnBypassAFKWorl(player)) return;
                    if (!playerAFKTime.containsKey(player.getUniqueId())) {
                        playerAFKTime.put(player.getUniqueId(), System.currentTimeMillis() / 1000);
                    } else {
                        long currentTime = System.currentTimeMillis() / 1000;
                        long playerAFKStartTime = playerAFKTime.get(player.getUniqueId());
                        long AFKTime = currentTime - playerAFKStartTime;
                        if (playerLastLocation.get(player.getUniqueId()) != null) {
                            if (player.getWorld().equals(playerLastLocation.get(player.getUniqueId()).getWorld())) {
                                boolean playerMoved = player.getLocation().distance(playerLastLocation.get(player.getUniqueId())) > 5;
                                if (!playerMoved && AFKTime > AFKTimeThreshold && !player.isDead()) {
                                    playerLastLocation.put(player.getUniqueId(), null);
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + player.getName() + " Ngồi lâu ê mông đó!");
                                    playerLastLocation.remove(player.getUniqueId());
                                    playerAFKTime.remove(player.getUniqueId());
                                }
                                if (playerMoved) {
                                    playerLastLocation.put(player.getUniqueId(), player.getLocation());
                                    playerAFKTime.put(player.getUniqueId(), System.currentTimeMillis() / 1000);
                                }
                            } else {
                                playerLastLocation.put(player.getUniqueId(), player.getLocation());
                            }
                        } else {
                            playerLastLocation.put(player.getUniqueId(), player.getLocation());
                        }
                    }
                }
            }
        }, 0L, 40L);
    }

    public static void antiFly() {
        (new BukkitRunnable() {
            final HashMap<UUID, Integer> flags = new HashMap<>();

            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPotionEffect(PotionEffectType.SLOW_FALLING) || p.hasPotionEffect(PotionEffectType.LEVITATION))
                        continue;
                    if (!flags.containsKey(p.getUniqueId())) {
                        flags.put(p.getUniqueId(), 0);
                        continue;
                    }
                    if (p.isSneaking() || p.isGliding() || p.isFlying() || p.isDead() || p.getPing() > 100 || p.getVehicle() != null) {
                        flags.put(p.getUniqueId(), 0);
                        continue;
                    }
                    Block blockCheck1 = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    Material blockCheck2 = blockCheck1.getRelative(BlockFace.SOUTH).getType();
                    Material blockCheck3 = blockCheck1.getRelative(BlockFace.EAST).getType();
                    Material blockCheck4 = blockCheck1.getRelative(BlockFace.NORTH).getType();
                    Material blockCheck5 = blockCheck1.getRelative(BlockFace.WEST).getType();
                    Material blockCheck6 = blockCheck1.getRelative(BlockFace.UP).getType();
                    Material blockCheck7 = blockCheck1.getRelative(BlockFace.NORTH_EAST).getType();
                    Material blockCheck8 = blockCheck1.getRelative(BlockFace.SOUTH_EAST).getType();
                    Material blockCheck9 = blockCheck1.getRelative(BlockFace.SOUTH_WEST).getType();
                    if (blockCheck1.getType().isAir()) {
                        if (blockCheck2.isAir() && blockCheck3.isAir() && blockCheck4.isAir() && blockCheck5.isAir() && blockCheck6.isAir() && blockCheck7.isAir() && blockCheck8.isAir() && blockCheck9.isAir()) {
                            int flag = flags.get(p.getUniqueId());
                            if (p.getFallDistance() < 1) {
                                flags.put(p.getUniqueId(), flag + 1);
                                if (flag > 2) {
                                    flags.put(p.getUniqueId(), 0);
                                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "jail " + p.getName() + " jail 5m");
                                    for (Player admin : Bukkit.getOnlinePlayers()) {
                                        if (admin.hasPermission(KnightBlock.admin_perm)) {
                                            KnightBlock.messager.msg(admin, KnightBlock.toColor(nameOfCaster + " &4" + p.getName() + " &enghi vấn đang hack fly"));
                                        }
                                    }
                                }
                            }
                        } else {
                            flags.put(p.getUniqueId(), 0);
                        }
                    } else {
                        flags.put(p.getUniqueId(), 0);
                    }
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 20);
    }
}
