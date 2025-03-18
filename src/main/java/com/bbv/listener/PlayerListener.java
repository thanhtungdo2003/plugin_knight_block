package com.bbv.listener;

import com.bbv.blib.ItemStackManager;
import com.bbv.blib.SimpleDatabaseManager;
import com.bbv.gui.CreateGUI;
import com.bbv.gui.GUIType;
import com.bbv.knightblock.EcoManager;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.ParticleManager;
import com.bbv.object.*;
import com.bbv.scheduler.AntiCheat;
import com.bbv.skills.*;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import io.lumine.mythic.bukkit.events.MythicMobLootDropEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerListener extends KyNangBiDong implements Listener, KeySkills {
    private static final HashMap<UUID, String> result = new HashMap<>();
    public static HashMap<UUID, String> diemNoiTai = new HashMap<>();
    public static HashMap<UUID, Double> damageHapThu = new HashMap<>();
    public static HashMap<UUID, String> dauAn_XaThu = new HashMap<>();
    public static HashMap<UUID, Tuple<Integer, Integer>> check_reach_flags = new HashMap<>();
    public static List<UUID> updatings = new ArrayList<>();
    public static HashMap<String, Integer> jailWaits = new HashMap<>();
    public static HashMap<UUID, Material> waiting_ore_selling = new HashMap<>();
    private static final String[] messOnJoinByVIP = new String[14];
    private static String[] messOnJoinBySuperVIP = new String[7];
    private static HashMap<UUID, String> confirmCodePlayers = new HashMap<>();

    static {
        messOnJoinByVIP[0] = "&6Đại Gia &a{player} &bđã quay trở lại!";
        messOnJoinByVIP[1] = "&eĐại Gia &a{player} &eđã tham gia server!";
        messOnJoinByVIP[2] = "&a{player} &6&lgiàu vl đã tham gia!";
        messOnJoinByVIP[3] = "&bVIP &a{player} &cđã quay trở lại!";
        messOnJoinByVIP[4] = "&cQuý Tộc &a{player} &evừa vào game!";
        messOnJoinByVIP[5] = "&bĐại Gia &a{player} &6đã nhập game!";
        messOnJoinByVIP[6] = "&ePhú Ông &a{player} &cđã gia nhập!";
        messOnJoinByVIP[7] = "&6VIP &a{player} &bđã quay lại!";
        messOnJoinByVIP[8] = "&eĐại Gia &a{player} &6đã tham gia!";
        messOnJoinByVIP[9] = "&cQuý Tộc &a{player} &bđã xuất hiện!";
        messOnJoinByVIP[10] = "&6Đại Gia &a{player} &evừa vào!";
        messOnJoinByVIP[11] = "&bPhú Ông &a{player} &cđã vào game!";
        messOnJoinByVIP[12] = "&cVIP &a{player} &6đã trở lại!";
        messOnJoinByVIP[13] = "&eĐại Gia &a{player} &bđã xuất hiện!";
    }

    static {
        messOnJoinBySuperVIP[0] = "&a{player} &6&lgiàu vl đã tham gia!";
        messOnJoinBySuperVIP[1] = "&c&lTrùm &6{player} &c&lQuay trở lại!";
        messOnJoinBySuperVIP[2] = "&eBố của Admin &a{player} &eđã tham gia";
        messOnJoinBySuperVIP[3] = "&bSiêu Đẹp Trai &a{player} &bđã online";
        messOnJoinBySuperVIP[4] = "&dĐấng &a{player} &bđã xuất hiện trở lại";
        messOnJoinBySuperVIP[5] = "&cCẢNH BẢO &a{player} &6Giàu vcl vừa tham gia Server";
        messOnJoinBySuperVIP[6] = "&eNtruong -> &dMừng daika &e{player} &dđã quay lại";
    }

    @EventHandler
    private void onPlayerJoinEvent(PlayerJoinEvent event) {
        event.getPlayer().setAllowFlight(false);
        Player p = event.getPlayer();
        if (p.hasPermission("knightblock.vip1") && !p.hasPermission(KnightBlock.admin_perm) && !p.hasPermission("knightblock.vip7")) {
            Random random = new Random();
            Bukkit.broadcastMessage(KnightBlock.toColor("§x§F§B§D§3§0§8§lV§x§F§B§C§D§3§0§lI§x§F§C§C§6§5§8§lP§x§F§C§C§0§8§0§lJ§x§F§C§B§A§A§8§lO§x§F§D§B§3§D§0§lI§x§F§D§A§D§F§8§lN &7>&a>&7> " + messOnJoinByVIP[random.nextInt(14)].replace("{player}", p.getName())));
        } else if (p.hasPermission("knightblock.vip7") && !p.hasPermission(KnightBlock.admin_perm)) {
            Random random = new Random();
            Bukkit.broadcastMessage(KnightBlock.toColor("§x§F§B§D§3§0§8§lV§x§F§B§C§D§3§0§lI§x§F§C§C§6§5§8§lP§x§F§C§C§0§8§0§lJ§x§F§C§B§A§A§8§lO§x§F§D§B§3§D§0§lI§x§F§D§A§D§F§8§lN &7>&a>&7> " + messOnJoinBySuperVIP[random.nextInt(7)].replace("{player}", p.getName())));
        }
        (new BukkitRunnable() {
            @Override
            public void run() {
                Player p = event.getPlayer();
                KnightBlock.setUpPlayer(p);
                KnightBlock.users.put(p.getUniqueId(), AuraSkillsApi.get().getUser(p.getUniqueId()));
                KnightBlockAPI.setUpPlayerInWorld(event.getPlayer());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "heal " + event.getPlayer().getName());
                p.setResourcePack("https://www.dropbox.com/scl/fi/5rt9u111jt8dpuf0nb2ry/nongko-s_Fantasy_Weapons_v1.14.zip?rlkey=qa6u939zclp3fl3yphc9albrv&st=0v290fvi&dl=1");
            }
        }).runTaskLater(KnightBlock.pl, 5);
//        confirmCodePlayers.put(event.getPlayer().getUniqueId(), KnightBlockAPI.generateRandomString(4));
//        Location joinLoc = event.getPlayer().getLocation();
//        for (int i = 0; i < 200; i++) {
//            if (joinLoc.getBlock().getType().isAir()) {
//                joinLoc.add(0, -1, 0);
//            } else {
//                break;
//            }
//        }
//        joinLoc.add(0, 1, 0);
//        (new BukkitRunnable() {
//            final Player p = event.getPlayer();
//
//            @Override
//            public void run() {
//                if (p.isOnline()) {
//                    if (!confirmCodePlayers.containsKey(p.getUniqueId())) {
//                        this.cancel();
//                        return;
//                    }
//                    p.sendTitle(KnightBlock.toColor("&e&l" + confirmCodePlayers.get(p.getUniqueId())), KnightBlock.toColor("&fNhập chuỗi ký tự trên màn hình để xác nhận là người"), 0, 40, 0);
//                    p.teleport(joinLoc);
//                } else {
//                    this.cancel();
//                    confirmCodePlayers.remove(p.getUniqueId());
//                }
//            }
//        }).runTaskTimer(KnightBlock.pl, 0, 10);
    }


    @EventHandler
    private void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        KnightBlock.khoKhoangSanHashMap.get(p.getUniqueId()).save();
        KnightBlock.removePlayer(p);
    }

    @EventHandler
    private void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        Player p = event.getPlayer();
        if (KnightBlock.getPlayerStats(p) != null && KnightBlock.getPlayerStats(p).getCulture() != null) {
            int levelNext = event.getNewLevel();
            int levelC = KnightBlock.getPlayerStats(p).getCulture().getLevel();
            int needLevel = 0;
            int maxLevel = 0;

            // Giới hạn cấp độ dựa trên nền văn minh của người chơi
            if (levelNext > 300 && levelC < 4) {
                needLevel = 4;
                maxLevel = 300;
            } else if (levelNext > 500 && levelC < 6) {
                needLevel = 6;
                maxLevel = 500;
            } else if (levelNext > 700 && levelC < 8) {
                needLevel = 8;
                maxLevel = 700;
            } else if (levelNext > 900 && levelC < 10) {
                needLevel = 10;
                maxLevel = 900;
            }

            // Nếu cần giới hạn cấp độ
            if (needLevel > 0) {
                // Tính tổng exp tương ứng với cấp độ giới hạn
                int expForMaxLevel = KnightBlockAPI.getTotalExpForLevel(maxLevel);

                // Lấy tổng exp hiện tại của người chơi
                int totalExpCurrent = p.getTotalExperience();

                // Tính lượng exp thừa (exp dư thừa sau khi giới hạn)
                int excessExp = totalExpCurrent - expForMaxLevel;
                if (excessExp < 0) {
                    excessExp = 0; // Đảm bảo không có số âm
                }

                // Đặt lại tổng kinh nghiệm và cấp độ cho người chơi
                p.setLevel(maxLevel); // Đặt lại level cho người chơi
                p.setExp(0); // Xóa thanh exp hiện tại
                p.setTotalExperience(expForMaxLevel); // Đặt lại tổng exp tương ứng với cấp độ

                // Gửi thông báo và hiển thị exp thừa
                KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐã đạt giới hạn level &aexp &cbạn cần nâng cấp độ nền văn minh đến level " + needLevel + " trước"));
                KnightBlock.messager.msg(p, KnightBlock.toColor("&aLượng exp thừa: " + excessExp));
                ExpDrop expDrop = new ExpDrop(p);
                expDrop.drop(p.getLocation(), excessExp);
                KnightBlock.expDropHashMap.put(p.getUniqueId(), expDrop);
                // Bạn có thể lưu biến `excessExp` vào đâu đó hoặc sử dụng nó sau này
                // Ví dụ: Lưu vào stats của người chơi

                KnightBlock.reloadPlayer(p);
            }
        }
    }


    @EventHandler
    private void xuLyStatsSTVLPlayer(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (KnightBlockAPI.isNPC(entity)) {
            return;
        }
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            if (KnightBlockAPI.isPlayerOnSafeZone(p, p.getLocation())) return;
            KnightBlockAPI.setTimeLeft((Player) p, "combat", 15);
            if (!p.hasPermission(KnightBlock.admin_perm) && !p.hasPermission("knightblock.vip8")) {
                p.setFlying(false);
            }
            if (p.getLocation().distance(event.getEntity().getLocation()) > 5) {
                int time_jail_default = 5;
                if (check_reach_flags.containsKey(p.getUniqueId())) {
                    Tuple<Integer, Integer> checks = check_reach_flags.get(p.getUniqueId());
                    if (checks.Left() > 2) {
                        time_jail_default *= checks.Right();
//                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "jail " + p.getName() + " jail " + time_jail_default + "p");
                        Location baitLoc = p.getEyeLocation().add(p.getLocation().getDirection().multiply(-4.5));
                        Entity bait = p.getWorld().spawnEntity(baitLoc, entity.getType());
                        if (entity instanceof Player) {
                            bait = p.getWorld().spawnEntity(baitLoc, EntityType.ZOMBIE);
                        }
                        if (bait instanceof Zombie) {
                            ((Zombie) bait).setBaby();
                        }
                        bait.setCustomName("[BAIT]-[AURA]-" + p.getName());
                        bait.setCustomNameVisible(false);
                        Entity finalBait = bait;
                        (new BukkitRunnable() {
                            int count = 0;

                            @Override
                            public void run() {
                                count++;
                                if (count > 20) {
                                    this.cancel();
                                    finalBait.remove();
                                    return;
                                }
                                Location l = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(-4.5));
                                if (!finalBait.isDead()) {
                                    finalBait.teleport(l);
                                }
                            }
                        }).runTaskTimer(KnightBlock.pl, 0, 1);
                        bait.setGravity(false);
                        if (bait instanceof LivingEntity) {
                            ((LivingEntity) bait).setSilent(true);
                            ((LivingEntity) bait).setAI(false);
                            ((LivingEntity) bait).setNoDamageTicks(9999999);
                        }
                        check_reach_flags.put(p.getUniqueId(), new Tuple<>(0, checks.Right() + 1));
                        return;
                    }
                    check_reach_flags.put(p.getUniqueId(), new Tuple<>(checks.Left() + 1, checks.Right()));
                } else {
                    check_reach_flags.put(p.getUniqueId(), new Tuple<>(1, 1));
                }
            }
            for (int i = 0; i < 5; i++) {
                Location locCheck = p.getEyeLocation().add(p.getLocation().getDirection().multiply(i));
                for (Entity e : p.getWorld().getNearbyEntities(locCheck, 1, 1, 1)) {
                    if (e.getCustomName() != null && e.getCustomName().contains("[BAIT]-[AURA]-")) {
                        if (e.getCustomName().contains(p.getName())) {
                            if (check_reach_flags.containsKey(p.getUniqueId())) {
                                int amountCheck = check_reach_flags.get(p.getUniqueId()).Right();
                                Bukkit.broadcastMessage(KnightBlock.toColor(AntiCheat.nameOfCaster + " &4" + p.getName() + " &enghi vấn đang hack KillAura &7(&f" + amountCheck + "&7)"));
                                if (amountCheck > 4) {
                                    if (!jailWaits.containsKey(p.getName())) jailWaits.put(p.getName(), 1);
                                    Bukkit.broadcastMessage(KnightBlock.toColor("&b> &c&lTÒA ÁN &6&lFICTIONSKY &b<" + " &4" + p.getName() + " &eHệ thống xác nhận có thể đang hack KillAura, Những người chơi đang online có thể sử dụng [/kac ok " + p.getName() + "] để đồng ý nhốt tù người chơi này!"));
                                    check_reach_flags.put(p.getUniqueId(), new Tuple<>(1, 1));
                                }
                                check_reach_flags.put(p.getUniqueId(), new Tuple<>(1, amountCheck + 1));
                                e.remove();
                            }
                        }
                    }
                    break;
                }
            }

            if (entity instanceof Vex) {
                if ((entity.getCustomName() != null) && entity.getCustomName().contains(KnightBlock.toColor("&7[" + p.getName() + "]"))) {
                    event.setCancelled(true);
                }
            }
            if (KnightBlockAPI.isWeaponClass(p, p.getInventory().getItemInMainHand()).equals(CheckWeaponType.FALSE)) {
                event.setCancelled(true);
                KnightBlock.messager.msg(p, KnightBlock.toColor("&7Bạn không thuộc class sử dụng loại vũ khí này!"));
                int heldItem = p.getInventory().getHeldItemSlot();
                for (int i = 0; i < 8; i++) {
                    if (i != heldItem) {
                        p.getInventory().setHeldItemSlot(i);
                        break;
                    }
                }
                return;
            }
            double damage = event.getDamage();
            int critRate = 0;
            PlayerStats stats = KnightBlock.getPlayerStats(p);
            if (stats == null) {
                return;
            }
            if (stats.consumeMana(5)) {
                damage += KnightBlock.getPlayerStats(p).getSTVatLy();
            }
            critRate += (int) (KnightBlock.getPlayerStats(p).getCritRate() * 100);
            if (stats.getClassType().equals(ClassType.SAT_THU)) {
                if (noiTaiSatThu(entity) && KnightBlockAPI.getTimeLeft(p, "crit_satthu")) {
                    KnightBlockAPI.setTimeLeft(p, "crit_satthu", 3);
                    critRate += 3000;
                }
                KyNangDacBiet amSat = KyNangHoatDong.getSkill(p, LoaiKyNang.AM_SAT);
                if (amSat != null && amSat.isActive()) {
                    damage = (damage + damage) * 2;
                    amSat.disable();
                }
            } else if (stats.getClassType().equals(ClassType.DAU_SI)) {
                if (PlayerListener.diemNoiTai.containsKey(p.getUniqueId())) {
                    if (KnightBlockAPI.getTimeLeft(p, "noitai_dausi")) {
                        KnightBlockAPI.setTimeLeft(p, "noitai_dausi", 3);
                        String[] strDiem = diemNoiTai.get(p.getUniqueId()).split("%");
                        double damageScale = (double) (Integer.parseInt(strDiem[0]) * 3) / 100;
                        damage = damage + (damage * damageScale);
                    }
                }
            } else if (stats.getClassType().equals(ClassType.DO_DON)) {
                if (PlayerListener.damageHapThu.containsKey(p.getUniqueId())) {
                    double damageHT = damageHapThu.get(p.getUniqueId());
                    damageHapThu.remove(p.getUniqueId());
                    damage += damageHT;
                }
            } else if (stats.getClassType().equals(ClassType.PHAP_SU)) {
                double stP = stats.getSTPhep();
                if (KnightBlockAPI.getTimeLeft(p, "hut_nang_luong")) {
                    KnightBlockAPI.setTimeLeft(p, "hut_nang_luong", 3);
                    SkillsUser user = AuraSkillsApi.get().getUser(p.getUniqueId());
                    double manaAdd = user.getMana() + (damage * 0.3);
                    user.setMana(Math.min(manaAdd, user.getMaxMana()));
                }
                if (KnightBlockAPI.getRandom(5, 100)) {
                    satThuongChuan(p, entity, 30);
                }
                satThuongPhep(p, entity, stP);
            } else if (stats.getClassType().equals(ClassType.XA_THU)) {
                LivingEntity target = (LivingEntity) entity;
                if (p.getLocation().distance(entity.getLocation()) >= 13) {
                    if (!dauAn_XaThu.containsKey(p.getUniqueId())) {
                        dauAn_XaThu.put(p.getUniqueId(), target.getName() + "%" + target.getUniqueId() + "%" + 1);
                    } else {
                        String[] strDauAn = dauAn_XaThu.get(p.getUniqueId()).split("%");
                        String uuidTarget = strDauAn[1];
                        int dauAn = Integer.parseInt(strDauAn[2]);
                        if (uuidTarget.equals(target.getUniqueId().toString())) {
                            if (dauAn < 30) {
                                dauAn_XaThu.put(p.getUniqueId(), target.getName() + "%" + target.getUniqueId() + "%" + (++dauAn));
                            }
                        } else {
                            dauAn_XaThu.put(p.getUniqueId(), target.getName() + "%" + target.getUniqueId() + "%" + 1);
                        }
                        if (target.getHealth() <= (target.getMaxHealth() * 0.3)) {
                            int scaleInstallKill = (int) (dauAn * 0.5) * 10;
                            if (KnightBlockAPI.getRandom(scaleInstallKill, 1000)) {
                                target.damage(2, p);
                                target.setHealth(0.0D);
                                dauAn_XaThu.remove(p.getUniqueId());
                                KnightBlock.messager.msg(p, KnightBlock.toColor("&eĐã kết liễu &c" + target.getName()));
                            }
                        }
                    }
                } else if (p.getLocation().distance(entity.getLocation()) < 7) {
                    damage = event.getDamage();
                }
            }
            damage = chiMang(p, damage, critRate);
            event.setDamage(damage);
        } else if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                Player p = (Player) projectile.getShooter();
                if (KnightBlockAPI.isNPC(entity)) {
                    return;
                }
                double damage = event.getDamage();
                int critRate = 0;
                PlayerStats stats = KnightBlock.getPlayerStats(p);
                if (stats == null) {
                    return;
                }
                if (stats.consumeMana(5)) {
                    damage += KnightBlock.getPlayerStats(p).getSTVatLy();
                }
                critRate += (int) (KnightBlock.getPlayerStats(p).getCritRate() * 100);
                if (stats.getClassType().equals(ClassType.XA_THU)) {
                    if (entity instanceof LivingEntity) {
                        LivingEntity target = (LivingEntity) entity;
                        if (p.getLocation().distance(entity.getLocation()) >= 13) {
                            if (!dauAn_XaThu.containsKey(p.getUniqueId())) {
                                dauAn_XaThu.put(p.getUniqueId(), target.getName() + "%" + target.getUniqueId() + "%" + 1);
                            } else {
                                String[] strDauAn = dauAn_XaThu.get(p.getUniqueId()).split("%");
                                String uuidTarget = strDauAn[1];
                                int dauAn = Integer.parseInt(strDauAn[2]);
                                if (uuidTarget.equals(target.getUniqueId().toString())) {
                                    if (dauAn < 30) {
                                        dauAn_XaThu.put(p.getUniqueId(), target.getName() + "%" + target.getUniqueId() + "%" + (++dauAn));
                                    }
                                } else {
                                    dauAn_XaThu.put(p.getUniqueId(), target.getName() + "%" + target.getUniqueId() + "%" + 1);
                                }
                                if (target.getHealth() <= (target.getMaxHealth() * 0.3)) {
                                    int scaleInstallKill = (int) (dauAn * 0.5) * 10;
                                    if (KnightBlockAPI.getRandom(scaleInstallKill, 1000)) {
                                        target.damage(2, p);
                                        target.setHealth(0.0D);
                                        dauAn_XaThu.remove(p.getUniqueId());
                                        KnightBlock.messager.msg(p, KnightBlock.toColor("&eĐã kết liễu &c" + target.getName()));
                                    }
                                }
                            }
                        }
                    }
                    damage = chiMang(p, damage, critRate);
                    event.setDamage(damage);
                } else if (stats.getClassType().equals(ClassType.PHAP_SU)) {
                    double stP = stats.getSTPhep();
                    if (KnightBlockAPI.getTimeLeft(p, "hut_nang_luong")) {
                        KnightBlockAPI.setTimeLeft(p, "hut_nang_luong", 3);
                        SkillsUser user = AuraSkillsApi.get().getUser(p.getUniqueId());
                        double manaAdd = user.getMana() + (damage * 0.3);
                        user.setMana(Math.min(manaAdd, user.getMaxMana()));
                    }
                    if (KnightBlockAPI.getRandom(5, 100)) {
                        satThuongChuan(p, entity, 30);
                    }
                    satThuongPhep(p, entity, stP);
                    damage = chiMang(p, damage, critRate);
                    event.setDamage(damage);
                }
            }
        }
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            KnightBlockAPI.setTimeLeft((Player) p, "combat", 15);
            if (!p.hasPermission(KnightBlock.admin_perm) && !p.hasPermission("knightblock.vip8")) {
                p.setFlying(false);
            }
            if (!KnightBlockAPI.getTimeLeft(p, "onJoinArena", "&eChờ &a{time}s &eđể có thể chiến đấu")) {
                event.setCancelled(true);
                return;
            }
            PlayerStats stats = KnightBlock.getPlayerStats(p);
            if (stats == null) {
                return;
            }
            if (stats.getClassType().equals(ClassType.DO_DON)) {
                if (KnightBlockAPI.getRandom(15, 100)) {
                    damageHapThu.put(p.getUniqueId(), event.getDamage());
                    event.setCancelled(true);
                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 1);
                    KnightBlock.messager.msg(p, KnightBlockAPI.toColor("&7Đã hấp thụ &a" + KnightBlockAPI.decimalFormat(event.getDamage(), "#.##") + " &7Sát thương của đối phương"));
                }
            }
            double nhanhNhen = KnightBlock.getPlayerStats(p).getNhanhNhen();
            if (KnightBlockAPI.getRandom((int) nhanhNhen * 100, 10000)) {
                if (stats.consumeMana(10)) {
                    event.setCancelled(true);
                    KnightBlock.messager.msg(event.getDamager(), KnightBlock.toColor("&e&oTrượt!"));
                    KnightBlock.messager.msg(p, KnightBlock.toColor("&7Né!"));
                }
            }
        }
    }

    @EventHandler
    private void onEntityDeathEvent(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player p = event.getEntity().getKiller();
            PlayerStats stats = KnightBlock.getPlayerStats(p);
            if (stats == null) {
                return;
            }
            ClassType classType = stats.getClassType();
            if (classType.equals(ClassType.DAU_SI)) {
                if (!diemNoiTai.containsKey(p.getUniqueId())) {
                    diemNoiTai.put(p.getUniqueId(), 0 + "%" + 1);
                } else {
                    String[] strNoiTai = diemNoiTai.get(p.getUniqueId()).split("%");
                    int p1 = Integer.parseInt(strNoiTai[1]);
                    int p2 = Integer.parseInt(strNoiTai[0]);
                    if (p1 == 10 && p2 < 30) {
                        diemNoiTai.put(p.getUniqueId(), ++p2 + "%" + 1);
                    } else if (p1 < 10) {
                        diemNoiTai.put(p.getUniqueId(), p2 + "%" + ++p1);
                    }
                }
            } else if (classType.equals(ClassType.XA_THU)) {
                if (dauAn_XaThu.containsKey(p.getUniqueId())) {
                    String[] strNoiTai = dauAn_XaThu.get(p.getUniqueId()).split("%");
                    String uuidTarget = strNoiTai[1];
                    if (uuidTarget.equals(event.getEntity().getUniqueId().toString())) {
                        dauAn_XaThu.remove(p.getUniqueId());
                    }
                }
            }
        }
        if (event.getEntity() instanceof Player) {
            diemNoiTai.remove(event.getEntity().getUniqueId());
            dauAn_XaThu.remove(event.getEntity().getUniqueId());
            damageHapThu.remove(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    private void xuLyChongChiuPlayer(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player p = (Player) event.getEntity();
            PlayerStats stats = KnightBlock.getPlayerStats(p);
            if (stats == null) {
                return;
            }
            if (stats.consumeMana(5)) {
                double chongChiu = KnightBlock.getPlayerStats(p).getChongChiu();
                double damage = event.getDamage() - chongChiu;
                if (damage <= 0) {
                    event.setDamage(1);
                } else {
                    event.setDamage(event.getDamage() - chongChiu);
                }
            }
            if (stats.getClassType().equals(ClassType.DO_DON)) {
                if (p.getHealth() <= (p.getMaxHealth() * 0.2)) {
                    if (KnightBlockAPI.getTimeLeft(p, "hoisinh_dodon")) {
                        event.setCancelled(true);
                        KnightBlockAPI.setTimeLeft(p, "hoisinh_dodon", 25);
                        SkillsUser user = AuraSkillsApi.get().getUser(p.getUniqueId());
                        double mana = user.getMana();
                        user.consumeMana(mana);
                        p.setHealth(Math.min(p.getHealth() + mana, p.getMaxHealth()));
                        p.setNoDamageTicks(60);
                        KnightBlock.messager.msg(p, KnightBlockAPI.toColor("&7Đã chuyến hoá năng lượng thành máu"));
                    }
                }
            }
        }
    }

    @EventHandler
    private void playerInteractEvent(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack itemHand = p.getInventory().getItemInMainHand();
        ItemStack itemOffHand = p.getInventory().getItemInOffHand();
        if (itemOffHand.getType().equals(Material.ENCHANTED_BOOK) && itemOffHand.getItemMeta() != null && itemOffHand.getItemMeta().getDisplayName().equals(KnightBlock.toColor("&3Spell Book"))) {
            p.getInventory().setItemInOffHand(null);
        } else if (itemHand.getType().equals(Material.ENCHANTED_BOOK) && itemHand.getItemMeta() != null && itemHand.getItemMeta().getDisplayName().equals(KnightBlock.toColor("&3Spell Book"))) {
            p.getInventory().setItemInMainHand(null);
        }
        if (p.isSneaking() && event.getAction().equals(Action.RIGHT_CLICK_AIR) && p.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
            CreateGUI.openEditFishingRodGUI(p);
        }
        if (KnightBlockAPI.isWeaponClass(p, p.getInventory().getItemInMainHand()).equals(CheckWeaponType.FALSE)) {
            event.setCancelled(true);
            KnightBlock.messager.msg(p, KnightBlock.toColor("&7Bạn không thuộc class sử dụng loại vũ khí này!"));
            int heldItem = p.getInventory().getHeldItemSlot();
            for (int i = 0; i < 8; i++) {
                if (i != heldItem) {
                    p.getInventory().setHeldItemSlot(i);
                    break;
                }
            }
            return;
        }
        Action action = event.getAction();
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
                ItemStack ngoc = p.getInventory().getItemInMainHand();
                if (ngoc.getItemMeta().getDisplayName().equals(KnightBlockAPI.toColor("&7[&dNgọc Chuyển Hoá&7]"))) {
                    event.setCancelled(true);
                    if (KnightBlock.getTreeOfWorld().getTreeOfWorld() != null) {
                        Location tree = KnightBlock.getTreeOfWorld().getTreeOfWorld();
                        if (tree.getWorld().equals(p.getWorld()) && tree.distance(p.getLocation()) <= 14) {
                            if (updatings.contains(p.getUniqueId())) {
                                KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐang nâng cấp"));
                                return;
                            }
                            PlayerStats stats = KnightBlockAPI.getPlayerStats(p);
                            Culture culture = stats.getCulture();
                            if (culture.getLevel() > 9) {
                                KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐã đạt cấp độ cao nhất"));
                                return;
                            }
                            CreateGUI.openChooseUpdateGUI(p, ngoc);
                        } else {
                            KnightBlock.messager.msg(p, KnightBlock.toColor("&7Sử dụng ngọc tại &a&lCây Thế Giới &e/warp caythegioi"));
                        }
                    } else {
                        KnightBlock.messager.msg(p, KnightBlock.toColor("&7Không tìm thấy Cây Thế Giới"));
                    }
                }
            }
        }
    }

    @EventHandler
    private void playerCloseGUI(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player p = (Player) event.getPlayer();
            CreateGUI.needEXP.remove(p.getUniqueId());
            CreateGUI.rateUpCul.remove(p.getUniqueId());
        }
    }

    @EventHandler
    private void onClickGUI(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) {
            return;
        }
        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player) event.getWhoClicked();
            UUID uuid = event.getWhoClicked().getUniqueId();
            if (CreateGUI.guiUsing.containsKey(uuid)) {
                event.setCancelled(true);
                if (event.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
                    event.setCancelled(true);
                    return;
                }
                boolean isBtn = false;
                if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.CHON_VAN_MINH)) {
                    SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
                    switch (event.getSlot()) {
                        case 9:
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "level", 1, "uuid", p.getUniqueId().toString());
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "type", CultureType.AI_CAP.name(), "uuid", p.getUniqueId().toString());
                            isBtn = true;
                            break;
                        case 11:
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "level", 1, "uuid", p.getUniqueId().toString());
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "type", CultureType.BAC_AU.name(), "uuid", p.getUniqueId().toString());
                            isBtn = true;
                            break;
                        case 13:
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "level", 1, "uuid", p.getUniqueId().toString());
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "type", CultureType.VIET_NAM.name(), "uuid", p.getUniqueId().toString());
                            isBtn = true;
                            break;
                        case 15:
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "level", 1, "uuid", p.getUniqueId().toString());
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "type", CultureType.HY_LAP.name(), "uuid", p.getUniqueId().toString());
                            isBtn = true;
                            break;
                        case 17:
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "level", 1, "uuid", p.getUniqueId().toString());
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "type", CultureType.TRUNG_HOA.name(), "uuid", p.getUniqueId().toString());
                            isBtn = true;
                            break;
                    }
                    if (isBtn) {
                        KnightBlock.reloadPlayer(p);
                        event.getWhoClicked().closeInventory();
                        if (Culture.taskChoose.containsKey(uuid)) {
                            Culture.taskChoose.get(uuid).cancel();
                            Culture.taskChoose.remove(uuid);
                        }
                        Culture culture = new Culture(p);
                        ChatColor color = ChatColor.of(new java.awt.Color(37, 225, 158, 218));
                        p.sendTitle(culture.getDisplayName(), KnightBlock.toColor(color + "Cấp độ: " + culture.getRankName()));
                    }
                } else if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.DOI_VAN_MINH)) {
                    SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
                    CultureType cultureType = null;
                    switch (event.getSlot()) {
                        case 9:
                            cultureType = CultureType.AI_CAP;
                            isBtn = true;
                            break;
                        case 11:
                            cultureType = CultureType.BAC_AU;
                            isBtn = true;
                            break;
                        case 13:
                            cultureType = CultureType.VIET_NAM;
                            isBtn = true;
                            break;
                        case 15:
                            cultureType = CultureType.HY_LAP;
                            isBtn = true;
                            break;
                        case 17:
                            cultureType = CultureType.TRUNG_HOA;
                            isBtn = true;
                            break;
                    }
                    if (isBtn) {
                        if (EcoManager.takeMoney(p, 500000)) {
                            databaseManager.updateDataByColum(Culture.CULTURE_TABLE, "type", cultureType.name(), "uuid", p.getUniqueId().toString());
                            KnightBlock.reloadPlayer(p);
                            event.getWhoClicked().closeInventory();
                            if (Culture.taskChoose.containsKey(uuid)) {
                                Culture.taskChoose.get(uuid).cancel();
                                Culture.taskChoose.remove(uuid);
                            }
                            Culture culture = new Culture(p);
                            ChatColor color = ChatColor.of(new java.awt.Color(37, 225, 158, 218));
                            p.sendTitle(culture.getDisplayName(), KnightBlock.toColor(color + "Cấp độ: " + culture.getRankName()));
                        }
                    }
                } else if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.CHON_CLASS)) {
                    SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
                    ClassType classTypeSet = null;
                    switch (event.getSlot()) {
                        case 9:
                            isBtn = true;
                            classTypeSet = ClassType.SAT_THU;
                            break;
                        case 11:
                            isBtn = true;
                            classTypeSet = ClassType.DAU_SI;
                            break;
                        case 13:
                            isBtn = true;
                            classTypeSet = ClassType.DO_DON;
                            break;
                        case 15:
                            isBtn = true;
                            classTypeSet = ClassType.PHAP_SU;
                            break;
                        case 17:
                            classTypeSet = ClassType.XA_THU;
                            isBtn = true;
                            break;
                    }
                    if (isBtn) {
                        if (KnightBlockAPI.getTimeLeft(p, "class_change", "&cBạn cần đợi &4{time}s &cđể chuyển class lần nữa")) {
                            if (!p.hasPermission(KnightBlock.staff_perm)) {
                                KnightBlockAPI.setTimeLeft(p, "class_change", 3600);
                            }
                            databaseManager.updateDataByColum(PlayerStats.STATS_TABLE, "class_type", classTypeSet.name(), "uuid", p.getUniqueId().toString());
                            event.getWhoClicked().closeInventory();
                            PlayerStats playerStats = KnightBlock.getPlayerStats(p);
                            playerStats.setClassType(classTypeSet);
                            ClassType classType = playerStats.getClassType();
                            p.sendTitle(classType.getDisplayName(), "");
                            KnightBlock.reloadPlayer(p);
                        }
                    }
                } else if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.TINH_LINH)) {
                    if (event.getClick().equals(ClickType.LEFT)) {
                        if (event.getSlot() == 0) {
                            isBtn = true;
                            TinhLinh tinhLinh = KnightBlock.getPlayerStats(p).getTinhLinh();
                            tinhLinh.hide(true, false);
                            tinhLinh.getTinhLinhData().disable(tinhLinh.getLoaiTinhLinh());
                        }
                        if (CreateGUI.btnToggleTinhLinhs.containsKey(event.getSlot())) {
                            isBtn = true;
                            LoaiTinhLinh loaiTinhLinh = CreateGUI.btnToggleTinhLinhs.get(event.getSlot());
                            TinhLinh tinhLinh = KnightBlock.getPlayerStats(p).getTinhLinh();
                            if (tinhLinh.getTinhLinhData().hasTinhLinh(loaiTinhLinh)) {
                                if (!tinhLinh.getTinhLinhData().isActive(loaiTinhLinh)) {
                                    tinhLinh.hide(false, false);
                                    KnightBlock.getPlayerStats(p).setTinhLinh(null);
                                    tinhLinh.getTinhLinhData().active(loaiTinhLinh);
                                    new TinhLinh(p);
                                } else {
                                    tinhLinh.getTinhLinhData().disable(loaiTinhLinh);
                                    tinhLinh.hide(true, false);
                                }
                            } else {
                                KnightBlock.messager.msg(p, KnightBlock.toColor("&7Bạn chưa sở hữu &bTinh Linh " + loaiTinhLinh.getDisplayName()));
                            }
                        }
                        if (isBtn) {
                            event.getWhoClicked().closeInventory();
                        }
                    } else if (event.getClick().equals(ClickType.RIGHT)) {
                        if (CreateGUI.btnToggleTinhLinhs.containsKey(event.getSlot())) {
                            LoaiTinhLinh loaiTinhLinh = CreateGUI.btnToggleTinhLinhs.get(event.getSlot());
                            CreateGUI.openChiTietTinhLinh(p, loaiTinhLinh);
                        }
                    }
                } else if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.NANG_LEVEL_CULTURE)) {
                    if (event.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)) {
                        return;
                    }
                    if (event.getSlot() == 43) {
                        PlayerStats stats = KnightBlockAPI.getPlayerStats(p);
                        Culture culture = stats.getCulture();
                        if (culture.getLevel() > 9) {
                            KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐã đạt cấp độ cao nhất"));
                            return;
                        }
                        updatings.add(p.getUniqueId());
                        ParticleManager.castEffectUpdateCul(p, false);
                    } else if (event.getSlot() == 37) {
                        PlayerStats stats = KnightBlockAPI.getPlayerStats(p);
                        Culture culture = stats.getCulture();
                        if (culture.getLevel() > 9) {
                            KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐã đạt cấp độ cao nhất"));
                            return;
                        }
                        PlayerPointsAPI playerPointsAPI = PlayerPoints.getInstance().getAPI();
                        if (playerPointsAPI.take(p.getUniqueId(), 50)) {
                            updatings.add(p.getUniqueId());
                            ParticleManager.castEffectUpdateCul(p, true);
                        } else {
                            KnightBlock.messager.msg(p, KnightBlockAPI.toColor("&cCần 50xu để kích hoạt Bảo vệ cấp"));
                        }
                    }
                } else if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.CHI_TIET_TINH_LINH)) {
                    switch (event.getSlot()) {
                        case 22:
                            CreateGUI.openTinhLinhGUI(p);
                            break;
                        case 12:
                            if (event.getCurrentItem().getType().equals(Material.NAME_TAG)) {
                                Inventory inventoryEvent = event.getClickedInventory();
                                PlayerPointsAPI playerPointsAPI = PlayerPoints.getInstance().getAPI();
                                LoaiTinhLinh loaiTinhLinh = LoaiTinhLinh.valueOf(inventoryEvent.getItem(18).getItemMeta().getDisplayName().replace(KnightBlock.toColor("&fID: "), ""));
                                String costStr = inventoryEvent.getItem(12).getItemMeta().getLore().get(0);
                                String costStr1 = costStr.replace(KnightBlock.toColor("&6Giá: &e"), "");
                                int cost = Integer.parseInt(costStr1.replace("xu", ""));
                                if (playerPointsAPI.take(p.getUniqueId(), cost)) {
                                    KnightBlock.getPlayerStats(p).getTinhLinh().set(loaiTinhLinh);
                                } else {
                                    KnightBlock.messager.msg(p, KnightBlock.toColor("&7Thiếu &e" + (cost - playerPointsAPI.look(p.getUniqueId())) + "xu"));
                                }
                                p.closeInventory();
                            }
                            break;
                    }
                } else if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.CAN_CAU)) {
                    if (event.getClick().isShiftClick()) return;
                    if (event.getClick().isRightClick()) return;
                    if (event.getClick().isKeyboardClick()) return;
                    ItemStack itemClick = event.getCurrentItem();
                    if (itemClick.getItemMeta() == null) {
                        if (KnightBlock.baitsFish.contains(itemClick.getType().name())) {
                            if (event.getSlot() == 13) {
                                event.getInventory().setItem(13, null);
                                p.getInventory().addItem(itemClick);
                                return;
                            }
                            if (event.getInventory().getItem(13) == null) {
                                itemClick.setType(Material.AIR);
                                event.getInventory().setItem(13, itemClick);
                            }
                        } else {
                            KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐây không phải mồi câu &eCầm cần câu Shift + Left Click &cđể xem danh sách mồi câu!"));
                        }
                    } else if (itemClick.getItemMeta().getLore() == null) {
                        if (KnightBlock.baitsFish.contains(itemClick.getType().name())) {
                            if (event.getSlot() == 13) {
                                ItemStack nullItem = new ItemStack(Material.BARRIER);
                                ItemMeta imNull = nullItem.getItemMeta();
                                imNull.setDisplayName(KnightBlock.toColor("&eClick chuột vào mồi câu trong kho đồ"));
                                nullItem.setItemMeta(imNull);
                                event.getInventory().setItem(13, nullItem);
                                p.getInventory().addItem(itemClick);
                                return;
                            }
                            if (event.getInventory().getItem(13) != null && event.getInventory().getItem(13).getType().equals(Material.BARRIER)) {
                                event.getInventory().setItem(13, itemClick);
                                event.setCurrentItem(new ItemStack(Material.AIR));
                            }
                        } else {
                            KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐây không phải mồi câu &eCầm cần câu Shift + Left Click &cđể xem danh sách mồi câu!"));
                        }
                    } else {
                        KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐây không phải mồi câu &eCầm cần câu Shift + Left Click &cđể xem danh sách mồi câu!"));
                    }
                } else if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.KHO_KHOANG_SAN)) {
                    if (event.getSlot() < 27) {
                        KhoKhoangSan khoKhoangSan = KnightBlock.khoKhoangSanHashMap.get(p.getUniqueId());
                        switch (event.getClick()) {
                            case LEFT:
                                khoKhoangSan.withdraw(event.getCurrentItem().getType(), 1, p.getInventory());
                                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 5);
                                break;
                            case RIGHT:
                                khoKhoangSan.withdraw(event.getCurrentItem().getType(), 64, p.getInventory());
                                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 5);
                                break;
                            case SHIFT_LEFT:
                                khoKhoangSan.withdrawAll(event.getCurrentItem().getType(), p.getInventory());
                                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 5);
                                break;
                            case SHIFT_RIGHT:
                                waiting_ore_selling.put(p.getUniqueId(), event.getCurrentItem().getType());
                                (new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (!waiting_ore_selling.containsKey(p.getUniqueId())) {
                                            this.cancel();
                                        }
                                        p.sendTitle(KnightBlock.toColor("&eBán Quặng"), KnightBlock.toColor("&bBán lượng quặng với số được nhập"), 0, 60, 0);
                                    }
                                }).runTaskTimer(KnightBlock.pl, 0, 20);
                                Bukkit.getScheduler().runTaskLater(KnightBlock.pl, p::closeInventory, 0);
                                break;
                        }
                        CreateGUI.openKhoangSanGUI(p);
                    } else if (event.getSlot() == 39) {
                        if (event.getClick().isShiftClick()) {
                            double total = KnightBlock.khoKhoangSanHashMap.get(p.getUniqueId()).sellAll();
                            if (total > 0) {
                                p.closeInventory();
                                p.sendTitle(KnightBlock.toColor("&aBÁN THÀNH CÔNG"), KnightBlock.toColor("&eBạn vừa bán toàn bộ kho và thu về &6" + KnightBlockAPI.decimalFormat(total, "#.##") + "$"));
                                if (total > 100000) {
                                    Bukkit.broadcastMessage(KnightBlock.toColor("&eNgười chơi &a" + p.getName() + "&e vừa trở thành đại gia vì bán toàn bộ Kho Khoáng Sản và thu về &6" + KnightBlockAPI.decimalFormat(total, "#.##") + "$"));
                                }
                            }
                        }
                    }
                } else if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.UP_WEAPON)) {
                    BanCuongHoa banCuongHoa = (BanCuongHoa) CreateGUI.guiUsing.get(uuid).getTag();
                    ItemStack itemClick = event.getCurrentItem();
                    if (event.getSlot() == 15) {
                        if (itemClick.getType() != Material.BARRIER) {
                            banCuongHoa.setUpgradeItem(null);
                            banCuongHoa.setLevelWeapon(-1);
                            event.setCurrentItem(null);
                            event.getInventory().setItem(15, ItemStackManager.createIS("&cÔ đang trống", Material.BARRIER, Arrays.asList("&dClick vào vũ khí hoặc giáp trong kho", "&dđồ để chọn vật phẩm cần nâng cấp")));
                            p.getInventory().addItem(itemClick);
                        }
                        return;
                    } else if (event.getSlot() == 11) {
                        if (itemClick.getType() != Material.BARRIER) {
                            banCuongHoa.setDaCuongHoa(null);
                            banCuongHoa.setLevelDaCuongHoa(0);
                            event.setCurrentItem(null);
                            event.getInventory().setItem(11, ItemStackManager.createIS("&cÔ đang trống", Material.BARRIER, Arrays.asList("&dClick vào đá cường hóa trong kho", "&dđồ để chọn đá nâng cấp")));
                            p.getInventory().addItem(itemClick);
                        }
                        return;
                    }
                    if (event.getSlot() == 38) {
                        if (PlayerPoints.getInstance().getAPI().take(p.getUniqueId(), 30)) {
                            if (banCuongHoa.getLevelDaCuongHoa() > 0 && banCuongHoa.getLevelWeapon() >= 0) {
                                ItemStack itemUp = banCuongHoa.getUpgradeItem();
                                banCuongHoa.setDaCuongHoa(null);
                                banCuongHoa.setUpgradeItem(null);
                                if (KnightBlockAPI.getRandom(banCuongHoa.getScale(), 10000)) {
                                    p.getInventory().addItem(KnightBlockAPI.updateWeapon(p, itemUp));
                                    p.sendTitle(KnightBlock.toColor("&a&lTHÀNH CÔNG!"), KnightBlock.toColor("&dCường hóa vật phẩm thành công"), 0, 60, 10);
                                    if (banCuongHoa.getLevelWeapon() + 1 > 5) {
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bc &eChúc mừng người chơi &a" + p.getName() + " &evừa cường hóa thành công " + itemUp.getItemMeta().getDisplayName() + " &elên &d+" + (banCuongHoa.getLevelWeapon() + 1));
                                    }
                                } else {
                                    p.getInventory().addItem(itemUp);
                                    p.sendTitle(KnightBlock.toColor("&c&lTHẤT BẠI!"), KnightBlock.toColor("&eCường hóa vật phẩm thất bại"), 0, 60, 10);
                                }
                                p.closeInventory();
                            } else {
                                KnightBlock.messager.msg(p, KnightBlock.toColor("&cChưa đủ vật phẩm!"));
                            }
                        } else {
                            KnightBlock.messager.msg(p, KnightBlock.toColor("&cBạn không có đủ xu"));
                        }
                        return;
                    } else if (event.getSlot() == 42) {
                        if (EcoManager.takeMoney(p, 10000)) {
                            if (banCuongHoa.getLevelDaCuongHoa() > 0 && banCuongHoa.getLevelWeapon() >= 0) {
                                ItemStack itemUp = banCuongHoa.getUpgradeItem();
                                banCuongHoa.setDaCuongHoa(null);
                                banCuongHoa.setUpgradeItem(null);
                                if (KnightBlockAPI.getRandom(banCuongHoa.getScale(), 10000)) {
                                    p.getInventory().addItem(KnightBlockAPI.updateWeapon(p, itemUp));
                                    p.sendTitle(KnightBlock.toColor("&a&lTHÀNH CÔNG!"), KnightBlock.toColor("&dCường hóa vật phẩm thành công"), 0, 60, 10);
                                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    if (banCuongHoa.getLevelWeapon() + 1 > 5) {
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bc &eChúc mừng người chơi &a" + p.getName() + " &evừa cường hóa thành công " + itemUp.getItemMeta().getDisplayName() + " &elên &d+" + (banCuongHoa.getLevelWeapon() + 1));
                                    }
                                } else {
                                    p.getInventory().addItem(KnightBlockAPI.breakWeapon(p, itemUp));
                                    p.sendTitle(KnightBlock.toColor("&c&lTHẤT BẠI!"), KnightBlock.toColor("&eCường hóa vật phẩm thất bại"), 0, 60, 10);
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                }
                                p.closeInventory();
                            } else {
                                KnightBlock.messager.msg(p, KnightBlock.toColor("&cChưa đủ vật phẩm!"));
                            }
                        }
                        return;
                    }
                    if (KnightBlockAPI.getLevelWeapon(itemClick) >= 0) {
                        if (!event.getInventory().getItem(15).getType().equals(Material.BARRIER)) return;
                        int level = KnightBlockAPI.getLevelWeapon(itemClick);
                        banCuongHoa.setLevelWeapon(level);
                        banCuongHoa.setUpgradeItem(itemClick);
                        event.setCurrentItem(KnightBlockAPI.removeItem(itemClick, 1));
                        itemClick.setAmount(1);
                        event.getInventory().setItem(15, itemClick);
                    }
                    if (KnightBlockAPI.getLevelDaCuongHoa(itemClick) > 0) {
                        if (!event.getInventory().getItem(11).getType().equals(Material.BARRIER)) return;
                        int level = KnightBlockAPI.getLevelDaCuongHoa(itemClick);
                        banCuongHoa.setDaCuongHoa(itemClick);
                        banCuongHoa.setLevelDaCuongHoa(level);
                        event.setCurrentItem(KnightBlockAPI.removeItem(itemClick, 1));
                        itemClick.setAmount(1);
                        event.getInventory().setItem(11, itemClick);
                    }
                    if (banCuongHoa.getScale() == -1) return;
                    event.getInventory().setItem(40, ItemStackManager.createIS("&eTỷ lệ thành công: &a" + banCuongHoa.getScale() / 100 + "%", Material.YELLOW_STAINED_GLASS_PANE));
                }
            }
        }
    }

    @EventHandler
    private void removeGuiUsing(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (CreateGUI.guiUsing.containsKey(uuid)) {
            if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.CAN_CAU)) {
                ItemStack baitItem = event.getInventory().getItem(13);
                if (baitItem != null && !baitItem.getType().equals(Material.BARRIER)) {
                    int old_amount = (int) CreateGUI.guiUsing.get(uuid).getTag();
                    if (old_amount < 16) {
                        int newAmount = old_amount + baitItem.getAmount();
                        if (newAmount < 16) {
                            KnightBlockAPI.setBaitFishingRod(p, newAmount);
                        } else {
                            KnightBlockAPI.setBaitFishingRod(p, 15);
                            baitItem.setAmount(baitItem.getAmount() - (15 - old_amount));
                            if (baitItem.getAmount() > 0) {
                                p.getInventory().addItem(baitItem);
                            }
                        }
                        KnightBlock.messager.msg(p, KnightBlock.toColor("&aĐã đặt mồi câu thành công!"));
                    } else {
                        KnightBlock.messager.msg(p, KnightBlock.toColor("&cCần câu đã đầy mồi! &e(MAX: 15)"));
                    }
                } else {
                    KnightBlock.messager.msg(p, KnightBlock.toColor("&aKhông có mồi câu nào được sử dụng"));
                }
            } else if (CreateGUI.guiUsing.get(uuid).getType().equals(GUIType.UP_WEAPON)) {
                BanCuongHoa banCuongHoa = (BanCuongHoa) CreateGUI.guiUsing.get(uuid).getTag();
                if (banCuongHoa.getUpgradeItem() != null) {
                    ItemStack item = banCuongHoa.getUpgradeItem();
                    banCuongHoa.setUpgradeItem(null);
                    p.getInventory().addItem(item);
                }
                if (banCuongHoa.getDaCuongHoa() != null) {
                    ItemStack item = banCuongHoa.getDaCuongHoa();
                    banCuongHoa.setDaCuongHoa(null);
                    p.getInventory().addItem(item);
                }
            }
        }
        CreateGUI.guiUsing.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onCancelClickTinhLinh(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player p = event.getPlayer();

        PlayerStats stats = KnightBlock.getPlayerStats(p);
        if (stats == null) {
            return;
        }
        if (entity.getCustomName() != null && entity.getCustomName().contains(KnightBlock.toColor("&l\uD83D\uDC7B Tinh Linh"))) {
            event.setCancelled(true);
            if (entity.getCustomName().contains("[" + p.getName() + "]")) {
                TinhLinh tinhLinh = stats.getTinhLinh();
                if (tinhLinh == null) {
                    return;
                }
                ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
                if (itemInMainHand.getItemMeta() != null) {
                    if (itemInMainHand.getItemMeta().getDisplayName().contains(KnightBlock.toColor("&b&lLinh &f&lThạch &6("))) {
                        tinhLinh.update();
                    }
                }
                Material type = itemInMainHand.getType();
                if (tinhLinh.getFoods().contains(type)) {
                    if (type.name().contains("BUCKET")) {
                        tinhLinh.add(7);
                        itemInMainHand.setType(Material.BUCKET);
                        p.getWorld().playSound(tinhLinh.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1, 1);
                    } else {
                        tinhLinh.add(5);
                        KnightBlockAPI.removeItemInHand(p, 1);
                        p.getWorld().playSound(tinhLinh.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
                    }
                    p.getWorld().spawnParticle(Particle.HEART, tinhLinh.getLocation(), 5, 1, 1, -1, 0);
                }
            }
        }
    }

    @EventHandler
    private void onCancelCancelDamageTinhLinh(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity.getCustomName() != null && entity.getCustomName().contains(KnightBlock.toColor("&l\uD83D\uDC7B Tinh Linh"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerUsingSkill(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Action action = event.getAction();
        if (!player.isSneaking()) {
            return;
        }
        String var1 = result.get(uuid);
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            var1 += LEFT;
            result.put(uuid, var1);
        }
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getHand() == EquipmentSlot.OFF_HAND)
                return;
            var1 += RIGHT;
            result.put(uuid, var1);
        }
        try {
            player.sendTitle("", ChatColor.translateAlternateColorCodes('&',
                    result.get(uuid).substring(0, result.get(uuid).length() - 1)), 0, 100, 0);
        } catch (Exception e) {
            player.sendTitle("", "", 0, 1, 0);
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (event.isSneaking()) {
            result.put(uuid, "");
        } else {
            String skillName = "";
            String skillKey = result.get(uuid);
            if (skillKey.length() > 1) {
                p.sendTitle("", "", 0, 0, 0);
            }
            for (CapDoKyNangChuDong kyNang : CapDoKyNangChuDong.values()) {
                PlayerStats stats = KnightBlock.getPlayerStats(p);
                if (stats == null) return;
                if (kyNang.getKey().equals(skillKey)) {
                    if (stats.getCulture().getLevel() >= kyNang.getLevelCul()) {
                        skillName = KnightBlockAPI.castSkillClass(kyNang.getLevelCul(), true, stats, stats.getClassType());
                        break;
                    } else {
                        KnightBlock.messager.msg(p, KnightBlock.toColor("Chưa đủ Cấp độ để dùng kỹ năng này!"));
                        return;
                    }
                }
            }
            if (!skillName.isEmpty()) {
                for (Entity e : p.getWorld().getNearbyEntities(p.getLocation(), 15, 15, 15)) {
                    if (e instanceof Player) {
                        Player target = (Player) e;
                        target.sendMessage(KnightBlock.toColor("&c✹ &e" + p.getName() + " &bthi triển tuyệt kỹ " + skillName));
                    }
                }
            }
        }
    }

    @EventHandler
    private void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            PlayerStats stats = KnightBlock.getPlayerStats(p);
            if (stats == null) {
                return;
            }
            Entity damager = event.getDamager();
            if (damager instanceof LivingEntity) {
                KyNangTinhLinh.onSkill(p, (LivingEntity) damager);
                stats.getTinhLinh().setTarget((LivingEntity) damager);
            }
        }
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            PlayerStats stats = KnightBlock.getPlayerStats(p);
            if (stats == null) {
                return;
            }
            Entity entity = event.getEntity();
            if (KnightBlockAPI.isNPC(entity)) {
                return;
            }
            if (entity instanceof LivingEntity) {
                if (event.getEntity() instanceof Vex) {
                    if ((entity.getCustomName() != null) && entity.getCustomName().contains(KnightBlock.toColor("&7[" + p.getName() + "]"))) {
                        event.setCancelled(true);
                        return;
                    }
                }
                KyNangTinhLinh.onSkill(p, (LivingEntity) entity);
                stats.getTinhLinh().setTarget((LivingEntity) entity);
            }

        }
    }

    @EventHandler
    private void onPlayerTepeportEvent(PlayerTeleportEvent event) {
        Player p = event.getPlayer();
        PlayerStats playerStats = KnightBlock.getPlayerStats(p);
        if (playerStats == null) {
            return;
        }
        if (playerStats.getTinhLinh() != null && playerStats.getTinhLinh().getEntity() != null) {
            playerStats.getTinhLinh().getEntity().teleport(p.getLocation());
        }
    }

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent event) {
        AntiCheat.playerAFKTime.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() / 1000);
        String lowerCase = event.getMessage().toLowerCase();
        if (lowerCase.contains("/xemkd")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("/xemkd <tên người chơi>");
            String[] args = event.getMessage().split(" ");
            if (args.length > 1) {
                event.getPlayer().performCommand("isee " + args[1]);
            }
        } else if (!event.getPlayer().hasPermission(KnightBlock.admin_perm) && lowerCase.contains("interactivechat")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onChangeWorldEvent(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();
        PlayerStats playerStats = KnightBlock.getPlayerStats(p);
        AntiCheat.playerAFKTime.put(p.getUniqueId(), System.currentTimeMillis() / 1000);
        KnightBlockAPI.setUpPlayerInWorld(p);
        if (KnightBlockAPI.playerIsOnWOV(p)) {
            KnightBlock.messager.msg(p, KnightBlock.toColor("&x&0&8&4&C&F&BB&x&2&0&6&4&F&BO&x&3&7&7&C&F&CO&x&4&F&9&4&F&CS&x&6&6&A&B&F&CT&x&7&E&C&3&F&CE&x&9&5&D&B&F&DR &x&F&F&C&1&1&4M&x&F&F&D&5&2&9I&x&F&F&E&9&3&DN&x&F&F&F&D&5&1E &dSẽ được áp dụng tại khu vực này"));
        }
        if (KnightBlockAPI.playerIsOnDungeon(p)) {
            KnightBlock.messager.msg(p, KnightBlock.toColor("&eThế giới này chết sẽ mất &c5% &ekinh nghiệm !"));
            p.setAllowFlight(false);
        }
        if (playerStats == null) {
            return;
        }
        (new BukkitRunnable() {
            @Override
            public void run() {
                float speed = (float) (0.2 + (playerStats.getNhanhNhen() / 100));
                p.setWalkSpeed(Math.min(speed, 0.7F));
            }
        }).runTaskLater(KnightBlock.pl, 3);
    }

    @EventHandler
    private void onPlayerDeadEvent(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (Wanteds.playerWanteds.containsKey(p.getUniqueId())) {
            if ((p.getKiller() != null) && (p.getKiller() instanceof Player)) {
                Wanteds wanteds = Wanteds.playerWanteds.get(p.getUniqueId());
                Player caster = Bukkit.getPlayer(wanteds.getCaster());
                int bou = wanteds.getBounty();
                wanteds.getTask().cancel();
                Wanteds.playerWanteds.remove(p.getUniqueId());
                Player killer = p.getKiller();
                Bukkit.broadcastMessage(KnightBlock.toColor("&a" + killer.getName() + " &eđã nhận &6&l" + bou + "$ &etừ &f" + caster.getName() + " &ekhi hạ gục &c" + p.getName()));
                EcoManager.addMoney(killer, bou);
            }
        }
        PlayerStats stats = KnightBlock.getPlayerStats(p);
        if (stats == null) {
            return;
        }
        stats.getTinhLinh().hide(true, true);
        if (KnightBlockAPI.playerIsOnDungeon(p)) {
            int expPlayer = p.getTotalExperience();
            int drop_exp = (int) (expPlayer * 0.05);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp take " + p.getName() + " " + drop_exp);
            ExpDrop expDrop = new ExpDrop(p);
            expDrop.drop(event.getEntity().getLocation(), drop_exp);
            KnightBlock.expDropHashMap.put(p.getUniqueId(), expDrop);
            (new BukkitRunnable() {
                @Override
                public void run() {
                    KnightBlock.expDropHashMap.remove(p.getUniqueId());
                    expDrop.getItem().remove();
                }
            }).runTaskLater(KnightBlock.pl, 6000);
            Location lDie = event.getEntity().getLocation();
            KnightBlock.messager.msg(p, KnightBlock.toColor(""));
            KnightBlock.messager.msg(p, KnightBlock.toColor("&eBạn đã chết tại Dungeon và rơi &c5% &eđiểm kinh nghiệm!"));
            KnightBlock.messager.msg(p, KnightBlock.toColor("&fCó thể nhặt lại tại tọa độ &a" + (int) lDie.getX() + " " + (int) lDie.getY() + " " + (int) lDie.getZ()));
            KnightBlock.messager.onSendHover(p, "         &7[ &aCLICK ĐỂ DỊCH CHUYỂN LẠI &7]", "&eYêu cầu &6100$", ClickEvent.Action.RUN_COMMAND, "/bifrost backtoexp ");
            KnightBlock.messager.msg(p, KnightBlock.toColor(""));
        }
    }

    @EventHandler
    private void onBoosterBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        AntiCheat.playerAFKTime.put(player.getUniqueId(), System.currentTimeMillis() / 1000);
        if (!KnightBlockAPI.playerIsOnWOV(player)) return;
        Material type = event.getBlock().getType();
        if (type.name().contains("_ORE")) {
            event.setDropItems(false);
            World world = event.getBlock().getWorld();
            boolean isBooster;
            isBooster = KnightBlock.BOOSTER.containsKey(player.getUniqueId());
            Bukkit.getScheduler().runTaskLater(KnightBlock.pl, () -> {
                if (!event.getBlock().getType().isAir()) return;
                int totalAmount = 1;
                int vipLevel = KnightBlockAPI.getVipLevel(player);
                ItemStack item = null;
                if (vipLevel <= 1) {
                    String displayNameOre = "ERROR ORE - Báo Admin hoặc staff";
                    if (type.equals(Material.COPPER_ORE)) {
                        item = new ItemStack(Material.COPPER_INGOT);
                        displayNameOre = "§x§C§C§9§3§7§1T§x§C§9§8§A§5§Dh§x§C§7§8§0§4§9ỏ§x§C§4§7§7§3§5i §x§A§1§7§2§3§7Đ§x§8§1§7§8§4§Dồ§x§6§1§7§D§6§2n§x§4§1§8§2§7§8g";
                    } else if (type.equals(Material.GOLD_ORE)) {
                        item = new ItemStack(Material.GOLD_INGOT);
                        displayNameOre = "§x§F§D§F§F§A§2T§x§F§D§E§E§8§Ah§x§F§E§D§D§7§3ỏ§x§F§E§C§C§5§Bi §x§F§F§A§A§2§CV§x§F§D§C§6§3§7à§x§F§C§E§3§4§2n§x§F§A§F§F§4§Dg";
                    } else if (type.equals(Material.IRON_ORE)) {
                        item = new ItemStack(Material.IRON_INGOT);
                        displayNameOre = "§x§F§8§F§8§F§8T§x§E§A§E§A§E§Ah§x§D§C§D§C§D§Cỏ§x§C§F§C§F§C§Fi §x§B§3§B§3§B§3S§x§A§0§9§D§9§1ắ§x§8§C§8§7§6§Et";
                    } else if (type.equals(Material.DIAMOND_ORE)) {
                        item = new ItemStack(Material.DIAMOND);
                        displayNameOre = "§x§F§F§7§E§E§CK§x§E§1§9§A§F§1i§x§C§3§B§6§F§6m §x§8§6§E§D§F§FC§x§8§9§E§2§F§Fư§x§8§B§D§8§F§Fơ§x§8§E§C§D§F§Fn§x§9§0§C§2§F§Fg";
                    } else if (type.equals(Material.REDSTONE_ORE)) {
                        item = new ItemStack(Material.REDSTONE);
                        displayNameOre = "§x§F§F§0§0§0§0Đ§x§F§F§2§0§2§0á §x§F§F§5§F§5§FĐ§x§F§F§5§F§5§Fỏ";
                    } else if (type.equals(Material.LAPIS_ORE)) {
                        item = new ItemStack(Material.LAPIS_LAZULI);
                        displayNameOre = "§x§5§5§7§B§F§FN§x§4§4§6§4§F§9g§x§3§3§4§C§F§3ọ§x§2§2§3§5§E§Cc §x§0§0§0§6§E§0L§x§0§0§1§A§E§2ư§x§0§0§2§D§E§3u §x§0§0§5§4§E§6L§x§0§0§6§8§E§8y";
                    } else if (type.equals(Material.EMERALD_ORE)) {
                        item = new ItemStack(Material.EMERALD);
                        displayNameOre = "§x§3§1§D§D§B§4N§x§3§5§D§2§A§0g§x§3§9§C§6§8§Cọ§x§3§D§B§B§7§8c §x§4§5§A§4§4§FL§x§4§9§9§9§3§Bụ§x§4§A§A§A§3§9c §x§4§D§C§B§3§4B§x§4§E§D§C§3§2ả§x§4§F§E§D§3§0o";
                    } else if (type.equals(Material.COAL_ORE)) {
                        item = new ItemStack(Material.COAL);
                        displayNameOre = "§x§5§F§5§F§5§FC§x§5§5§5§5§5§5ụ§x§4§C§4§C§4§Cc §x§3§8§3§8§3§8T§x§2§E§2§E§2§Eh§x§2§5§2§5§2§5a§x§1§B§1§B§1§Bn";
                    }
                    if (item == null) return;
                    ItemMeta im = item.getItemMeta();
                    if (im == null) {
                        im = Bukkit.getItemFactory().getItemMeta(item.getType());
                    }
                    int amountItem = 1;
                    if (isBooster && KnightBlockAPI.getRandom(KnightBlock.BOOSTER.get(player.getUniqueId()).Right(), 100)) {
                        amountItem = KnightBlock.BOOSTER.get(player.getUniqueId()).Left();
                    }
                    im.setDisplayName(KnightBlock.toColor(displayNameOre));
                    im.setLore(Arrays.asList(KnightBlock.toColor("&ePhẩm chất: §x§8§5§A§6§6§4K§x§6§A§A§3§7§3h§x§5§0§9§F§8§2o§x§3§5§9§C§9§1á§x§1§B§9§8§A§0n§x§0§0§9§5§A§Fg §x§0§0§9§5§A§Fs§x§0§0§9§5§A§Fả§x§0§0§9§5§A§Fn"), KnightBlock.toColor("&7- &fCó giá trị trao đổi &7-")));
                    item.setItemMeta(im);
                    item.setAmount(amountItem);
                    if (item.getAmount() > 0) {
                        if (player.getInventory().firstEmpty() == -1) {
                            world.dropItemNaturally(event.getBlock().getLocation(), item);
                        } else {
                            player.getInventory().addItem(item);
                        }
                    }
                    totalAmount = amountItem;
                } else {
                    int totalItem = 0;
                    Map<Integer, Integer> hmBooster = new HashMap<>();
                    for (int i = 2; i <= vipLevel; i++) {
                        int rateOfBooster = 0;
                        int levelBooster = 1;
                        switch (i) {
                            case 2:
                                rateOfBooster = 20;
                                levelBooster = 2;
                                break;
                            case 3:
                                rateOfBooster = 60;
                                levelBooster = 2;
                                break;
                            case 4:
                                rateOfBooster = 30;
                                levelBooster = 3;
                                break;
                            case 5:
                                rateOfBooster = 100;
                                levelBooster = 2;
                                break;
                            case 6:
                                rateOfBooster = 70;
                                levelBooster = 3;
                                break;
                            case 7:
                                rateOfBooster = 100;
                                levelBooster = 3;
                                break;
                            case 8:
                                rateOfBooster = 40;
                                levelBooster = 5;
                                break;
                        }
                        hmBooster.put(levelBooster, rateOfBooster);
                    }
                    for (Integer amountBooster : hmBooster.keySet()) {
                        int amountItem = 1;
                        if (KnightBlockAPI.getRandom(hmBooster.get(amountBooster), 100)) {
                            amountItem = amountItem + amountBooster;
                        }
                        String displayNameOre = "ERROR ORE - Báo Admin hoặc staff";
                        if (type.equals(Material.COPPER_ORE)) {
                            item = new ItemStack(Material.COPPER_INGOT);
                            displayNameOre = "§x§C§C§9§3§7§1T§x§C§9§8§A§5§Dh§x§C§7§8§0§4§9ỏ§x§C§4§7§7§3§5i §x§A§1§7§2§3§7Đ§x§8§1§7§8§4§Dồ§x§6§1§7§D§6§2n§x§4§1§8§2§7§8g";
                        } else if (type.equals(Material.GOLD_ORE)) {
                            item = new ItemStack(Material.GOLD_INGOT);
                            displayNameOre = "§x§F§D§F§F§A§2T§x§F§D§E§E§8§Ah§x§F§E§D§D§7§3ỏ§x§F§E§C§C§5§Bi §x§F§F§A§A§2§CV§x§F§D§C§6§3§7à§x§F§C§E§3§4§2n§x§F§A§F§F§4§Dg";
                        } else if (type.equals(Material.IRON_ORE)) {
                            item = new ItemStack(Material.IRON_INGOT);
                            displayNameOre = "§x§F§8§F§8§F§8T§x§E§A§E§A§E§Ah§x§D§C§D§C§D§Cỏ§x§C§F§C§F§C§Fi §x§B§3§B§3§B§3S§x§A§0§9§D§9§1ắ§x§8§C§8§7§6§Et";
                        } else if (type.equals(Material.DIAMOND_ORE)) {
                            item = new ItemStack(Material.DIAMOND);
                            displayNameOre = "§x§F§F§7§E§E§CK§x§E§1§9§A§F§1i§x§C§3§B§6§F§6m §x§8§6§E§D§F§FC§x§8§9§E§2§F§Fư§x§8§B§D§8§F§Fơ§x§8§E§C§D§F§Fn§x§9§0§C§2§F§Fg";
                        } else if (type.equals(Material.REDSTONE_ORE)) {
                            item = new ItemStack(Material.REDSTONE);
                            displayNameOre = "§x§F§F§0§0§0§0Đ§x§F§F§2§0§2§0á §x§F§F§5§F§5§FĐ§x§F§F§5§F§5§Fỏ";
                        } else if (type.equals(Material.LAPIS_ORE)) {
                            item = new ItemStack(Material.LAPIS_LAZULI);
                            displayNameOre = "§x§5§5§7§B§F§FN§x§4§4§6§4§F§9g§x§3§3§4§C§F§3ọ§x§2§2§3§5§E§Cc §x§0§0§0§6§E§0L§x§0§0§1§A§E§2ư§x§0§0§2§D§E§3u §x§0§0§5§4§E§6L§x§0§0§6§8§E§8y";
                        } else if (type.equals(Material.EMERALD_ORE)) {
                            item = new ItemStack(Material.EMERALD);
                            displayNameOre = "§x§3§1§D§D§B§4N§x§3§5§D§2§A§0g§x§3§9§C§6§8§Cọ§x§3§D§B§B§7§8c §x§4§5§A§4§4§FL§x§4§9§9§9§3§Bụ§x§4§A§A§A§3§9c §x§4§D§C§B§3§4B§x§4§E§D§C§3§2ả§x§4§F§E§D§3§0o";
                        } else if (type.equals(Material.COAL_ORE)) {
                            item = new ItemStack(Material.COAL);
                            displayNameOre = "§x§5§F§5§F§5§FC§x§5§5§5§5§5§5ụ§x§4§C§4§C§4§Cc §x§3§8§3§8§3§8T§x§2§E§2§E§2§Eh§x§2§5§2§5§2§5a§x§1§B§1§B§1§Bn";
                        }
                        ItemMeta im = item.getItemMeta();
                        if (im == null) {
                            im = Bukkit.getItemFactory().getItemMeta(item.getType());
                        }
                        if (isBooster && KnightBlockAPI.getRandom(KnightBlock.BOOSTER.get(player.getUniqueId()).Right(), 100)) {
                            amountItem += KnightBlock.BOOSTER.get(player.getUniqueId()).Left();
                        }
                        im.setDisplayName(KnightBlock.toColor(displayNameOre));
                        im.setLore(Arrays.asList(KnightBlock.toColor("&ePhẩm chất: §x§8§5§A§6§6§4K§x§6§A§A§3§7§3h§x§5§0§9§F§8§2o§x§3§5§9§C§9§1á§x§1§B§9§8§A§0n§x§0§0§9§5§A§Fg §x§0§0§9§5§A§Fs§x§0§0§9§5§A§Fả§x§0§0§9§5§A§Fn"), KnightBlock.toColor("&7- &fCó giá trị trao đổi &7-")));
                        item.setItemMeta(im);
                        item.setAmount(amountItem);
                        if (item.getAmount() > 0) {
                            if (player.getInventory().firstEmpty() == -1) {
                                world.dropItemNaturally(event.getBlock().getLocation(), item);
                            } else {
                                player.getInventory().addItem(item);
                            }
                            totalItem += amountItem;
                        }
                    }
                    if (totalItem > 1) {
                        KnightBlock.messager.msg(player, KnightBlock.toColor("§x§F§F§C§7§0§0V§x§F§C§E§3§3§0I§x§F§8§F§F§5§FP &7- &ax" + totalItem + "&f Item khi đào quặng"));
                    }
                    totalAmount = totalItem;
                }
                if (item != null) {
                    KhoKhoangSan khoKhoangSan = KnightBlock.khoKhoangSanHashMap.get(player.getUniqueId());
                    khoKhoangSan.add(item.getType(), totalAmount);
                    player.sendTitle("", KnightBlock.toColor("&a+" + totalAmount + " &f" + item.getType().name() + " &e/ks &fĐể xem Kho Khoáng Sản"), 0, 20, 0);
                }
            }, 0);
        } else if (type.equals(Material.COBBLESTONE)) {
            event.setDropItems(false);
            KhoKhoangSan khoKhoangSan = KnightBlock.khoKhoangSanHashMap.get(player.getUniqueId());
            khoKhoangSan.add(Material.COBBLESTONE, 1);
            player.sendTitle("", KnightBlock.toColor("&a+" + 1 + " &e" + Material.COBBLESTONE + " &b/ks &fđể xem Kho Khoáng Sản"), 0, 20, 0);
        } else if (type.equals(Material.STONE)) {
            event.setDropItems(false);
            KhoKhoangSan khoKhoangSan = KnightBlock.khoKhoangSanHashMap.get(player.getUniqueId());
            khoKhoangSan.add(Material.COBBLESTONE, 1);
            player.sendTitle("", KnightBlock.toColor("&a+" + 1 + " &e" + Material.COBBLESTONE + " &b/ks &fđể xem Kho Khoáng Sản"), 0, 20, 0);
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.STRING)) {
            event.setCancelled(true);
        } else if (event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.STRING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBoosterDropMythic(MythicMobLootDropEvent event) {
        int moneyDrop = event.getMoney();
        int exp = event.getExp();
        double addChance = 0;
        Entity killer = event.getKiller();
        if (!(killer instanceof Player)) return;
        switch (KnightBlockAPI.getVipLevel((Player) killer)) {
            case 0:
                return;
            case 1:
                addChance = 0.05D;
                break;
            case 2:
                addChance = 0.1D;
                break;
            case 3:
                addChance = 0.15D;
                break;
            case 4:
                addChance = 0.2D;
                break;
            case 5:
                addChance = 0.25D;
                break;
            case 6:
                addChance = 0.3D;
                break;
            case 7:
                addChance = 0.4D;
                break;
            case 8:
                addChance = 0.65D;
                break;
        }
        moneyDrop = (int) (moneyDrop + (moneyDrop * addChance));
        exp = (int) (exp + (exp * addChance));
        KnightBlock.messager.msg(killer, KnightBlock.toColor("§x§F§B§F§F§3§EV§x§F§D§D§7§1§FI§x§F§F§A§E§0§0P &7[&a+&7] &b" + addChance * 100 + "% &e$ &fvà &aexp &ftừ giết quái"));
        event.setMoney(moneyDrop);
        event.setExp(exp);
    }

    @EventHandler
    private void onPlayerPickupEvent(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (item.getItemMeta() != null) {
            if (KnightBlockAPI.hasCustomKey(item.getItemMeta(), KnightBlock.pl, "exp_drop")) {
                event.setCancelled(true);
                KnightBlock.expDropHashMap.remove(event.getPlayer().getUniqueId());
                int exp = KnightBlockAPI.getIntegerByPDT(item.getItemMeta(), "exp_drop");
                KnightBlock.messager.msg(event.getPlayer(), KnightBlock.toColor("&7[&a+&7] &a" + exp + " exp"));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp give " + event.getPlayer().getName() + " " + exp);
                event.getItem().remove();
                event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }
    }

    @EventHandler
    private void onPlayerToggleFly(PlayerToggleFlightEvent event) {
        if (event.isFlying()) {
            Player p = event.getPlayer();
            if (p.hasPermission(KnightBlock.admin_perm)) return;
            if (!KnightBlockAPI.getTimeLeft(p, "combat", "&cBạn đang trong thời gian combat")) {
                p.setFlying(false);
                event.setCancelled(true);
                p.sendTitle(KnightBlock.toColor("&b&lFLY &c&lTẮT"), KnightBlock.toColor("&cKhông thể bay khi đang combat"), 1, 40, 1);
            }
        }
    }

    @EventHandler
    private void onFishing(PlayerFishEvent event) {
        Player p = event.getPlayer();
        if (!p.getInventory().getItemInOffHand().getType().isAir()) {
            event.setCancelled(true);
            KnightBlock.messager.msg(p, KnightBlock.toColor("&cKhông thể câu cá với 1 tay (Tay trái phải trống)!"));
            return;
        }
        if (p.getInventory().getItemInMainHand().getItemMeta() == null) {
            KnightBlock.messager.msg(p, KnightBlock.toColor("&cCần câu của bạn chưa có mồi câu :-) [/warp trade] để mua"));
            event.setCancelled(true);
        } else {
            int bait_amount = KnightBlockAPI.getBaitFishingRod(p, p.getInventory().getItemInMainHand());
            if (bait_amount > 0) {
                if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
                    KnightBlockAPI.setBaitFishingRod(p, bait_amount - 1);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (waiting_ore_selling.containsKey(p.getUniqueId())) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(KnightBlock.pl, () -> {
                try {
                    int amount = Integer.parseInt(event.getMessage());
                    KnightBlock.khoKhoangSanHashMap.get(p.getUniqueId()).sell(waiting_ore_selling.get(p.getUniqueId()), amount);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                } catch (Exception exception) {
                    KnightBlock.messager.msg(p, KnightBlock.toColor("&cGiá trị đầu vào phải là một số nguyên, lớn hơn 0 và nhỏ hơn hoặc bằng " + KnightBlock.khoKhoangSanHashMap.get(p.getUniqueId()).getItems().get(waiting_ore_selling.get(p.getUniqueId()).name()).Left()));
                }
                waiting_ore_selling.remove(p.getUniqueId());
                CreateGUI.openKhoangSanGUI(p);
            }, 0);
        }

        AntiCheat.playerAFKTime.put(p.getUniqueId(), System.currentTimeMillis() / 1000);
        String mess = event.getMessage();
        if (confirmCodePlayers.containsKey(p.getUniqueId())) {
            event.setCancelled(true);
            if (confirmCodePlayers.get(p.getUniqueId()).equals(mess)) {
                confirmCodePlayers.remove(p.getUniqueId());
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                p.sendTitle(KnightBlock.toColor("&aThành công"), KnightBlock.toColor("&eĐã xác nhận thành công =D"));
            }
        }
    }

    @EventHandler
    private void onAntiPortal(PortalCreateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
//        Player player = event.getPlayer();
//        if (!player.hasPermission(KnightBlock.admin_perm)) return;
//        // Kiểm tra nếu block dưới người chơi là không khí
//        if (player.getLocation().add(0, -3, 0).getBlock().getType() == Material.AIR && !player.isFlying()) {
//            // Kiểm tra nếu người chơi đang rơi
//            if (KnightBlockAPI.getTimeLeft(player, "check_cheat_fly")) {
//                KnightBlockAPI.setTimeLeft(player, "check_cheat_fly", 2);
//                if (player.getFallDistance() > 0) {
//                    return;
//                }
//            }
//            // Kiểm tra nếu người chơi đang sử dụng Elytra
//            if (player.isGliding()) {
//                // Người chơi đang bay với Elytra, không kick
//                return;
//            }
//
//            for (Player p : Bukkit.getOnlinePlayers()) {
//                if (p.hasPermission(KnightBlock.admin_perm)) {
//                    p.sendMessage(KnightBlock.toColor(player.getName() + "&c Nghi vấn hack fly"));
//                    break;
//                }
//            }
//        }
    }
}
