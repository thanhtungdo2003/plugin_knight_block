package com.bbv.knightblock;

import com.bbv.blib.ConfigFile;
import com.bbv.blib.ItemStackManager;
import com.bbv.blib.Messager;
import com.bbv.blib.SimpleDatabaseManager;
import com.bbv.gui.CreateGUI;
import com.bbv.listener.EntityListener;
import com.bbv.listener.PlayerListener;
import com.bbv.object.*;
import com.bbv.object.TinhLinh;
import com.bbv.scheduler.AntiCheat;
import com.bbv.skills.SPhanThan;
import com.bbv.stotage.*;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.SharedStat;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.StatMap;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class KnightBlock extends JavaPlugin {
    public static final String admin_perm = "knightblock.admin";
    public static final String staff_perm = "knightblock.staff";
    public static final String updateCaseLeft = KnightBlock.toColor(" §x§9§F§9§F§9§F§lᶱ §x§F§F§E§C§A§F§l[§x§F§F§5§3§5§3§l+");
    public static final String updateCaseRight = KnightBlock.toColor("§x§C§7§F§F§F§C§l]");
    public static Plugin pl;
    public static Messager messager;
    public static final String DATABASE_NAME = "knightData";
    public static HashMap<UUID, PlayerStats> playerStats = new HashMap<>();
    public static HashMap<UUID, SkillsUser> users = new HashMap<>();
    private static TreeOfWorld treeOfWorld;
    private static final List<Location> effectLocOfBeams = new ArrayList<>();
    public static ConfigurationFile config;
    public static ConfigFile arenaData;
    public static ConfigFile configData;
    public static ConfigFile invincibleData;
    public static MessagerDataFile messageConfig;
    public static String prefixMessage;
    public static List<String> nameOfWhitelistWorldVIPPerm = new ArrayList<>();
    public static List<String> nameDungeonWorlds = new ArrayList<>();
    public static List<String> nameByPassAntiAFKWorlds = new ArrayList<>();
    public static HashMap<String, SoulSpawner> soulSpawners = new HashMap<>();
    public static ConfigFile soulSpawnerData;
    public static HashMap<UUID, Tuple<Integer, Integer>> BOOSTER = new HashMap<>();
    public static HashMap<UUID, BukkitTask> TASK_BOOSTER = new HashMap<>();
    public static HashMap<UUID, Integer> TIMELEFT_BOOSTER = new HashMap<>();
    public static HashMap<UUID, ExpDrop> expDropHashMap = new HashMap<>();
    public static HashMap<UUID, String> hasVotes = new HashMap<>();
    public static HashMap<UUID, KhoKhoangSan> khoKhoangSanHashMap = new HashMap<>();
    static List<String> VICINBLE = new ArrayList<>();
    static int AMOUNT_ITEM_INVICINBLE = 1;
    static long TIME_INVICINBLE = 1;
    static int COOLDOWN = 1;
    static ItemStack ITEM_INVICINBLE;
    private HashMap<UUID, Boolean> vipFlyPlayers = new HashMap<>();
    public static String nameCaseBaitFishsingRod = KnightBlock.toColor("&bՕ&8 &7[&9Mồi Cầu: &a");
    public static List<String> baitsFish = new ArrayList<>();

    @Override
    public void onEnable() {
        pl = this;
        Bukkit.getScheduler().runTaskLater(pl, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sk lang vi");
        }, 20 * 60 * 5);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), pl);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), pl);
        onRandomBroadcast();
        (new BukkitRunnable() {
            @Override
            public void run() {
                soulSpawnerData = new ConfigFile(pl, "SoulSpawner.yml");
                arenaData = new ConfigFile(pl, "Arena.yml");
                Bukkit.getPluginManager().registerEvents(new Arena(), pl);
                configData = new ConfigFile(pl, "Config.yml");
                invincibleData = new ConfigFile(pl, "invincible.yml");
                createDefaultConfigFile(invincibleData);
                ITEM_INVICINBLE = invincibleData.getConfigFile().getItemStack("invicinble.item_cost");
                TIME_INVICINBLE = invincibleData.getConfigFile().getLong("invicinble.time");
                COOLDOWN = invincibleData.getConfigFile().getInt("cooldown_invicinble");
                AMOUNT_ITEM_INVICINBLE = invincibleData.getConfigFile().getInt("invicinble.amount");
                treeOfWorld = new KnightBlockConfig();
                config = new KnightBlockConfig();
                messageConfig = new KnightBlockMessage();
                prefixMessage = config.getPrefixMessage();
                messager = new Messager(ChatColor.translateAlternateColorCodes('&', prefixMessage));
                nameOfWhitelistWorldVIPPerm = configData.getConfigFile().getStringList("WhitelistWorldVipPerm");
                nameDungeonWorlds = configData.getConfigFile().getStringList("DungeonWorld");
                nameByPassAntiAFKWorlds = configData.getConfigFile().getStringList("BlackListWorldAntiAFK");
                baitsFish = configData.getConfigFile().getStringList("BaitFish");
                for (String id : soulSpawnerData.getConfigFile().getKeys(false)) {
                    SoulSpawner soulSpawner = new SoulSpawner(id);
                    soulSpawners.put(id, soulSpawner);
                    soulSpawner.active();
                    Bukkit.getPluginManager().registerEvents(soulSpawner, pl);
                }
                Culture.createData();
                PlayerStats.createData();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.hasPermission(KnightBlock.admin_perm) && !p.hasPermission("knightblock.vip8")) {
                        p.setAllowFlight(false);
                    }
                    setUpPlayer(p);
                    users.put(p.getUniqueId(), AuraSkillsApi.get().getUser(p.getUniqueId()));
                }
                new PlaceholderKB().register();
                EffectTreeOfWorld();
                AntiCheat.antiFly();
            }
        }).runTaskLater(pl, 20);
    }

    private static void createDefaultConfigFile(ConfigFile file) {
        HashMap<String, Object> keySet = new HashMap<>();
        keySet.put("invicinble.item_cost", new ItemStack(Material.DIRT));
        keySet.put("invicinble.amount", 64);
        keySet.put("invicinble.time", 6000L);
        keySet.put("cooldown_invicinble", 300);
        file.setDefaultConfigurationFile(keySet);
        file.Save();
    }

    private static void sendCredit(CommandSender sender) {
        String[] msg = new String[]{
                "", "&f&lKNIGHT&2&lBLOCK" + 1.0,
                "&9Plugin bởi _BBV_",
                "&3Link FB Author: &b&ohttps://web.facebook.com/thanhtung.bbv", ""};
        for (int i = 0; i < msg.length; i++) {
            msg[i] = KnightBlockAPI.toColor(msg[i]);
        }
        sender.sendMessage(msg);
    }

    public static TreeOfWorld getTreeOfWorld() {
        return treeOfWorld;
    }

    private static void EffectTreeOfWorld() {
        if (treeOfWorld.getTreeOfWorld() != null) {
            if (treeOfWorld.getLocOfBeams() != null && !treeOfWorld.getLocOfBeams().isEmpty()) {
                for (Location locOfBeam : treeOfWorld.getLocOfBeams()) {
                    effectLocOfBeams.addAll(ParticleManager.SphereLocation(locOfBeam, 1, 0.5, 0.5, 1));
                }
            }
            (new BukkitRunnable() {
                final World world = treeOfWorld.getTreeOfWorld().getWorld();

                @Override
                public void run() {
                    if (world != null) {
                        boolean hasPlayer = false;
                        for (Entity e : world.getNearbyEntities(treeOfWorld.getTreeOfWorld(), 15, 15, 15)) {
                            if (e instanceof Player) {
                                hasPlayer = true;
                                break;
                            }
                        }
                        if (hasPlayer) {
                            world.spawnParticle(Particle.TOTEM, treeOfWorld.getTreeOfWorld(), 50, 10, 10, -10, 0);
                            world.spawnParticle(Particle.VILLAGER_HAPPY, treeOfWorld.getTreeOfWorld(), 50, 10, 10, -10, 0);
                            for (Location locEffect : effectLocOfBeams) {
                                world.spawnParticle(Particle.FIREWORKS_SPARK, locEffect, 0);
                                ParticleManager.onParticleDustColor(locEffect, Color.fromBGR(255, 62, 255));
                            }
                        }
                    }
                }
            }).runTaskTimer(pl, 0, 10);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bifrost")) {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "backtoexp":
                        if (expDropHashMap.containsKey(((Player) sender).getUniqueId())) {
                            if (EcoManager.takeMoney(((Player) sender), 500)) {
                                ((Player) sender).teleport(expDropHashMap.get(((Player) sender).getUniqueId()).getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                            }
                        }
                        break;
                }
            }
        } else if (command.getName().equals("knightblock")) {
            if (args.length > 0) {
                if (sender.hasPermission(admin_perm)) {
                    switch (args[0].toLowerCase()) {
                        case "setlevel":
                            int levelSet = Integer.parseInt(args[1]);
                            SimpleDatabaseManager database = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
                            database.updateDataByColum(Culture.CULTURE_TABLE, "level", levelSet, "uuid", ((Player)sender).getUniqueId().toString());
                            getPlayerStats((Player) sender).getCulture().setLevel(levelSet);
                            break;
                        case "getlevelweapon":
                            sender.sendMessage("" + KnightBlockAPI.getLevelWeapon(((Player) sender).getInventory().getItemInMainHand()));
                            break;
                        case "getdacuonghoa":
                            if (args.length > 1) {
                                int level = Integer.parseInt(args[1]);
                                ((Player) sender).getInventory().addItem(KnightBlockAPI.daCuongHoa(level));
                            }
                            break;
                        case "itemupgrade":
                            ((Player) sender).getInventory().setItemInMainHand(KnightBlockAPI.updateWeapon((Player) sender, ((Player) sender).getInventory().getItemInMainHand()));
                            break;
                        case "breakweapon":
                            ((Player) sender).getInventory().setItemInMainHand(KnightBlockAPI.breakWeapon((Player) sender, ((Player) sender).getInventory().getItemInMainHand()));
                            break;
                        case "baitfish":
                            if (args.length > 1) {
                                Player p = (Player) sender;
                                if (p.getInventory().getItemInMainHand().getType().isAir()) {
                                    sender.sendMessage(toColor("&aCần cầm một vật phẩm trên tay"));
                                    return false;
                                }
                                List<String> list = configData.getConfigFile().getStringList("BaitFish");
                                if (args[1].equalsIgnoreCase("add")) {
                                    list.add(p.getInventory().getItemInMainHand().getType().name());
                                } else if (args[1].equalsIgnoreCase("remove")) {
                                    list.remove(p.getInventory().getItemInMainHand().getType().name());
                                }
                                configData.setConfigurationFile("BaitFish", list);
                                sender.sendMessage(toColor("&aĐã cập nhật danh sách mồi câu"));
                                sender.sendMessage(toColor("&a" + configData.getConfigFile().getStringList("BaitFish")));
                                baitsFish = configData.getConfigFile().getStringList("BaitFish");
                            }
                            break;
                        case "bypassafkworld":
                            if (args.length > 2) {
                                String worldName = args[2];
                                List<String> listWorld = configData.getConfigFile().getStringList("BlackListWorldAntiAFK");
                                if (args[1].equalsIgnoreCase("add")) {
                                    listWorld.add(worldName);
                                } else if (args[1].equalsIgnoreCase("remove")) {
                                    listWorld.remove(worldName);
                                }
                                configData.setConfigurationFile("BlackListWorldAntiAFK", listWorld);
                                sender.sendMessage(toColor("&aĐã cập nhật danh sách thế giới không chặn AFK"));
                                sender.sendMessage(toColor("&a" + configData.getConfigFile().getStringList("BlackListWorldAntiAFK")));
                                nameByPassAntiAFKWorlds = configData.getConfigFile().getStringList("BlackListWorldAntiAFK");
                            }
                            break;
                        case "dungeon":
                            if (args.length > 2) {
                                String worldName = args[2];
                                List<String> listWorld = configData.getConfigFile().getStringList("DungeonWorld");
                                if (args[1].equalsIgnoreCase("add")) {
                                    listWorld.add(worldName);
                                } else if (args[1].equalsIgnoreCase("remove")) {
                                    listWorld.remove(worldName);
                                }
                                configData.setConfigurationFile("DungeonWorld", listWorld);
                                sender.sendMessage(toColor("&aĐã cập nhật danh sách thế giới dungeon"));
                                sender.sendMessage(toColor("&a" + configData.getConfigFile().getStringList("DungeonWorld")));
                                nameDungeonWorlds = configData.getConfigFile().getStringList("DungeonWorld");
                            }
                            break;
                        case "booster":
                            if (args.length <= 3) {
                                sender.sendMessage(org.bukkit.ChatColor.YELLOW + "/skm booster <player> <amount> <time> <rate>");
                                sender.sendMessage(org.bukkit.ChatColor.YELLOW + "/skm booster ** <amount> <time> <rate>" + org.bukkit.ChatColor.GRAY + "Cấp cho toàn bộ người chơi đang online");
                                sender.sendMessage(org.bukkit.ChatColor.YELLOW + "'* time: 20 = 1s'");
                            } else {
                                if (args[1].equals("**")) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        int amount = Integer.parseInt(args[2]);
                                        int time = Integer.parseInt(args[3]);
                                        int rate = Integer.parseInt(args[4]);
                                        if (BOOSTER.containsKey(p.getUniqueId()) && BOOSTER.get(p.getUniqueId()).Left() > amount) {
                                            messager.msg(p, MessagerType.HAS_LEVEL_BOOSTER.getContent().replace("{amount}", amount + ""));
                                        } else {
                                            boosterMine(p, amount, time, rate);
                                        }
                                    }
                                } else {
                                    Player p = Bukkit.getPlayer(args[1]);
                                    int amount = Integer.parseInt(args[2]);
                                    int time = Integer.parseInt(args[3]);
                                    int rate = Integer.parseInt(args[4]);
                                    if (rate > 100) {
                                        rate = 100;
                                    }
                                    assert p != null;
                                    if (BOOSTER.containsKey(p.getUniqueId()) && BOOSTER.get(p.getUniqueId()).Left() > amount) {
                                        messager.msg(p, MessagerType.HAS_LEVEL_BOOSTER.getContent().replace("{amount}", amount + ""));
                                    } else {
                                        if (!TASK_BOOSTER.containsKey(p.getUniqueId())) {
                                            TIMELEFT_BOOSTER.put(p.getUniqueId(), time);
                                            BukkitTask task = onBooster(time, p.getUniqueId());
                                            TASK_BOOSTER.put(p.getUniqueId(), task);
                                            BOOSTER.put(p.getUniqueId(), new Tuple<>(amount, rate));
                                            messager.msg(p, MessagerType.GIVE_BOOST.getContent().replace("{amount}", amount + "").replace("{time}", KnightBlockAPI.TimeFormat(p)).replace("{type}", "Mine").replace("{rate}", rate + ""));
                                        } else {
                                            if (amount > BOOSTER.get(p.getUniqueId()).Left()) {
                                                TIMELEFT_BOOSTER.put(p.getUniqueId(), time);
                                                TASK_BOOSTER.get(p.getUniqueId()).cancel();
                                                BukkitTask task = onBooster(time, p.getUniqueId());
                                                TASK_BOOSTER.put(p.getUniqueId(), task);
                                                BOOSTER.put(p.getUniqueId(), new Tuple<>(amount, rate));
                                                messager.msg(p, MessagerType.GIVE_BOOST.getContent().replace("{amount}", amount + "").replace("{time}", KnightBlockAPI.TimeFormat(p)).replace("{type}", "Mine").replace("{rate}", rate + ""));
                                            } else if (amount == BOOSTER.get(p.getUniqueId()).Left()) {
                                                int timeAdd = time + TIMELEFT_BOOSTER.get(p.getUniqueId());
                                                TIMELEFT_BOOSTER.put(p.getUniqueId(), timeAdd);
                                                TASK_BOOSTER.get(p.getUniqueId()).cancel();
                                                BukkitTask task = onBooster(timeAdd, p.getUniqueId());
                                                TASK_BOOSTER.put(p.getUniqueId(), task);
                                                BOOSTER.put(p.getUniqueId(), new Tuple<>(amount, rate));
                                                messager.msg(p, MessagerType.GIVE_BOOST.getContent().replace("{amount}", amount + "").replace("{time}", KnightBlockAPI.TimeFormat(p)).replace("{type}", "Mine").replace("{rate}", rate + ""));
                                                // System.out.println(ChatColor.YELLOW + "Người chơi " + p.getName() + " Đã sử dụng booster level " + amount + " thời gian: " + TimeFormat(p));
//                                                    for (Player admin : Bukkit.getOnlinePlayers()) {
//                                                        if (admin.hasPermission(admin_perm)) {
//                                                            admin.sendMessage(ChatColor.YELLOW + "Người chơi " + p.getName() + " Đã sử dụng booster level " + amount + " thời gian: " + TimeFormat(p));
//                                                        }
//                                                    }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case "whitelistworldvip":
                            if (args.length > 2) {
                                String worldName = args[2];
                                List<String> listWorld = configData.getConfigFile().getStringList("WhitelistWorldVipPerm");
                                if (args[1].equalsIgnoreCase("add")) {
                                    listWorld.add(worldName);
                                } else if (args[1].equalsIgnoreCase("remove")) {
                                    listWorld.remove(worldName);
                                }
                                configData.setConfigurationFile("WhitelistWorldVipPerm", listWorld);
                                sender.sendMessage(toColor("&aĐã cập nhật danh sách thế giới áp dụng vip"));
                                sender.sendMessage(toColor("&a" + configData.getConfigFile().getStringList("WhitelistWorldVipPerm")));
                                nameOfWhitelistWorldVIPPerm = configData.getConfigFile().getStringList("WhitelistWorldVipPerm");
                            }
                            break;
                        case "reload":
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                removePlayer(p);
                            }
                            for (World world : Bukkit.getWorlds()) {
                                for (Entity e : world.getEntities()) {
                                    if (e.getCustomName() != null && e.getCustomName().contains(KnightBlock.toColor("&l\uD83D\uDC7B Tinh Linh"))) {
                                        e.remove();
                                    }
                                }
                            }
                            Bukkit.getScheduler().cancelTasks(pl);
                            treeOfWorld = new KnightBlockConfig();
                            config = new KnightBlockConfig();
                            messageConfig = new KnightBlockMessage();
                            prefixMessage = config.getPrefixMessage();
                            messager = new Messager(ChatColor.translateAlternateColorCodes('&', prefixMessage));
                            new PlaceholderKB().register();
                            Culture.createData();
                            PlayerStats.createData();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                setUpPlayer(p);
                                users.put(p.getUniqueId(), AuraSkillsApi.get().getUser(p.getUniqueId()));
                                reloadPlayer(p);
                            }
                            EffectTreeOfWorld();
                            break;
                        case "treeofworld":
                            Player p = (Player) sender;
                            if (args.length == 1) {
                                treeOfWorld.setTreOfWorld(p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5));
                                messager.msg(p, toColor("&7Đã đặt toạ độ Cây Thế Giới"));
                            } else if (args.length > 2) {
                                if (args[1].equalsIgnoreCase("beam")) {
                                    if (args[2].equalsIgnoreCase("add")) {
                                        if (treeOfWorld.getLocOfBeams() != null) {
                                            List<Location> locOfBeam = treeOfWorld.getLocOfBeams();
                                            locOfBeam.add(p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5));
                                            treeOfWorld.setLocOfBeams(locOfBeam);
                                        } else {
                                            List<Location> locOfBeam = new ArrayList<>();
                                            locOfBeam.add(p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5));
                                            treeOfWorld.setLocOfBeams(locOfBeam);
                                        }
                                        messager.msg(p, toColor("&7Đã thêm toạ độ Beam"));
                                    } else if (args[2].equalsIgnoreCase("clear")) {
                                        List<Location> locOfBeam = new ArrayList<>();
                                        treeOfWorld.setLocOfBeams(locOfBeam);
                                        messager.msg(p, toColor("&7Đã clear toạ độ Beam"));
                                    }
                                }
                            }
                            break;
                        case "stats":
                            try {
                                if (args[1].equalsIgnoreCase("up")) {
                                    if (args[2].equalsIgnoreCase("damage")) {
                                        KnightBlockAPI.getPlayerStats((Player) sender).update(StatsType.DAMAGE_BASE);
                                    } else if (args[2].equalsIgnoreCase("tank")) {
                                        KnightBlockAPI.getPlayerStats((Player) sender).update(StatsType.TANK);
                                    } else if (args[2].equalsIgnoreCase("hp")) {
                                        KnightBlockAPI.getPlayerStats((Player) sender).update(StatsType.HP);
                                    } else if (args[2].equalsIgnoreCase("mana")) {
                                        KnightBlockAPI.getPlayerStats((Player) sender).update(StatsType.THE_LUC);
                                    } else if (args[2].equalsIgnoreCase("speed")) {
                                        KnightBlockAPI.getPlayerStats((Player) sender).update(StatsType.NHANH_NHEN);
                                    } else if (args[2].equalsIgnoreCase("crit")) {
                                        KnightBlockAPI.getPlayerStats((Player) sender).update(StatsType.CRIT_RATE);
                                    }
                                } else if (args[1].equalsIgnoreCase("set")) {
                                    Player target = Bukkit.getPlayer(args[3]);
                                    if (target == null) {
                                        return false;
                                    }
                                    if (args[2].equalsIgnoreCase("damage")) {
                                        KnightBlockAPI.getPlayerStats(target).set(StatsType.DAMAGE_BASE, Double.parseDouble(args[4]));
                                    } else if (args[2].equalsIgnoreCase("tank")) {
                                        KnightBlockAPI.getPlayerStats(target).set(StatsType.TANK, Double.parseDouble(args[4]));
                                    } else if (args[2].equalsIgnoreCase("hp")) {
                                        KnightBlockAPI.getPlayerStats(target).set(StatsType.HP, Double.parseDouble(args[4]));
                                    } else if (args[2].equalsIgnoreCase("mana")) {
                                        KnightBlockAPI.getPlayerStats(target).set(StatsType.THE_LUC, Double.parseDouble(args[4]));
                                    } else if (args[2].equalsIgnoreCase("speed")) {
                                        KnightBlockAPI.getPlayerStats(target).set(StatsType.NHANH_NHEN, Double.parseDouble(args[4]));
                                    } else if (args[2].equalsIgnoreCase("crit")) {
                                        KnightBlockAPI.getPlayerStats(target).set(StatsType.CRIT_RATE, Double.parseDouble(args[4]));
                                    }
                                }
                            } catch (Exception ex) {
                                sender.sendMessage(KnightBlockAPI.toColor("&e/kb stats <up/set> <type> <player>"), KnightBlockAPI.toColor("&6type: &adamage, tank, hp, mana, speed, crit"));
                            }
                            break;
                        case "dropdata":
                            SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
                            databaseManager.dropData(Culture.CULTURE_TABLE);
                            databaseManager.dropData(PlayerStats.STATS_TABLE);
                            break;
                        case "invincibleitem":
                            ItemStack itemInHand = ((Player) sender).getInventory().getItemInMainHand();
                            if (itemInHand.getType().isAir()) {
                                messager.msg((Player) sender, "Bạn đang cầm không khí!");
                                return false;
                            } else {
                                invincibleData.setConfigurationFile("invicinble.item_cost", itemInHand);
                                ITEM_INVICINBLE = itemInHand;
                                messager.msg((Player) sender, "Thiết lập item bất khả chiến bại thành công!");
                            }
                            break;
                        case "linhthach":
                            if (args.length == 1) {
                                sender.sendMessage(KnightBlockAPI.toColor("&e/kb linhthach <level> <rate>"), KnightBlockAPI.toColor("&7Ví dụ: &a/kb linhthach 5 100"));
                            }
                            if (args.length > 2) {
                                int rate = Integer.parseInt(args[2]);
                                ItemStack linhThach = ItemStackManager.createIS(KnightBlockAPI.toColor("&b&lLinh &f&lThạch &6(cấp " + args[1] + ")"), Material.ECHO_SHARD, Arrays.asList("", ("&9Tỷ lệ thành công: &a" + rate + "%"), "", ("&eCách sử dụng: &7Cầm trên tay"), ("&7Click lên Tinh Linh của mình")));
                                ItemMeta im = linhThach.getItemMeta();
                                im.setCustomModelData(rate);
                                im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
                                im.addEnchant(Enchantment.THORNS, 1, true);
                                linhThach.setItemMeta(im);
                                ((Player) sender).getInventory().addItem(linhThach);
                            }
                            break;
                        case "ngocchuyenhoa":
                            if (args.length == 1) {
                                sender.sendMessage(KnightBlockAPI.toColor("&e/kb ngocchuyenhoa <level>"), KnightBlockAPI.toColor("&7Ví dụ: &a/kb ngocchuyenhoa 4"));
                            }
                            if (args.length > 1) {
                                int level = Integer.parseInt(args[1]);
                                ItemStack ngoc = ItemStackManager.createIS(KnightBlockAPI.toColor("&7[&dNgọc Chuyển Hoá&7]"), Material.ENDER_PEARL, Arrays.asList("", ("&b»»» &6Cấp: &a" + level), "", "&7_______--------------&do&7--------------_______", ("&b➥ &aLoại ngọc chuyển hoá EXP để tăng cấp"), ("&ađộ nền văn minh, tăng cấp thất bại"), ("&asẽ giảm 1 cấp"), "", ("&b➥ &eCách sử dụng: &7Đứng dưới &a&lCây Thế Giới"), ("&f/warp caythegioi&7, cầm ngọc tên tay chính"), ("&7và click chuột phải"), "&7------_____________&do&7_____________------"));
                                ItemMeta im = ngoc.getItemMeta();
                                im.setCustomModelData(level);
                                im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
                                im.addEnchant(Enchantment.THORNS, 1, true);
                                ngoc.setItemMeta(im);
                                ((Player) sender).getInventory().addItem(ngoc);
                            }
                            break;
                    }
                }
                switch (args[0].toLowerCase()) {
                    case "cuonghoa":
                        CreateGUI.openCuongHoaGUI((Player) sender);
                        break;
                    case "help":
                        sendCredit(sender);
                        if (sender.hasPermission(admin_perm)) {
                            sender.sendMessage("§e/kb stats <up/set> <type> <player>",
                                    "§e/kb linhthach <level> <rate>",
                                    "§e/kb ngocchuyenhoa <level>",
                                    KnightBlockAPI.toColor("&e/kb help"));
                        }
                        sender.sendMessage(KnightBlockAPI.toColor("&e<> &6Bắt buộc &7; &e() &6Không bắt buộc"));
                        break;
                }
            }
        } else if (command.getName().equals("tinhlinh")) {
            if (args.length == 0) {
                CreateGUI.openTinhLinhGUI((Player) sender);
            }
            if (args.length > 0) {
                PlayerStats stats = getPlayerStats((Player) sender);
                switch (args[0].toLowerCase()) {
                    case "toggle":
                        if (KnightBlockAPI.getTimeLeft((Player) sender, "toggle_tinhlinh_command", "&7Đợi &a{time}s")) {
                            KnightBlockAPI.setTimeLeft((Player) sender, "toggle_tinhlinh_command", 6);
                            if (stats.getTinhLinh().getTinhLinhData().isActive(stats.getTinhLinh().getLoaiTinhLinh())) {
                                if (stats.getTinhLinh().isShowing()) {
                                    stats.getTinhLinh().hide(true, true);
                                } else {
                                    stats.getTinhLinh().show();
                                    messager.msg((Player) sender, toColor("&aTinh Linh đã được hoạt động"));
                                }
                            } else {
                                messager.msg((Player) sender, toColor("&7Bạn chưa triệu hồi Tinh Linh nào &e/tinhlinh &7(/tl)"));
                            }
                        }
                        break;
                    case "help":
                        sendCredit(sender);
                        if (sender.hasPermission(admin_perm)) {
                            sender.sendMessage("§e/tl set <type> (player) (level)");
                        }
                        sender.sendMessage(KnightBlockAPI.toColor("&e/tl help"),
                                toColor("&e/tl &7Mở giao diện Tinh Linh"));
                        sender.sendMessage(KnightBlockAPI.toColor("&e<> &6Bắt buộc &7; &e() &6Không bắt buộc"));
                        break;
                    case "set":
                        if (args.length <= 2) {
                            Player p = (Player) sender;
                            TinhLinh tinhLinh = KnightBlockAPI.getPlayerStats(p).getTinhLinh();
                            if (args[1].equalsIgnoreCase("lua")) {
                                tinhLinh.set(LoaiTinhLinh.LUA);
                            } else if (args[1].equalsIgnoreCase("set")) {
                                tinhLinh.set(LoaiTinhLinh.SET);
                            } else if (args[1].equalsIgnoreCase("gio")) {
                                tinhLinh.set(LoaiTinhLinh.GIO);
                            } else if (args[1].equalsIgnoreCase("dat")) {
                                tinhLinh.set(LoaiTinhLinh.DAT);
                            } else if (args[1].equalsIgnoreCase("nuoc")) {
                                tinhLinh.set(LoaiTinhLinh.NUOC);
                            } else if (args[1].equalsIgnoreCase("hac_am")) {
                                tinhLinh.set(LoaiTinhLinh.HACAM);
                            }
                            break;
                        } else {
                            try {
                                Player target = Bukkit.getPlayer(args[2]);
                                TinhLinh tinhLinh = KnightBlockAPI.getPlayerStats(target).getTinhLinh();
                                if (target == null) {
                                    messager.msg((Player) sender, toColor("&7Không tồn tại người chơi"));
                                    return false;
                                }
                                if (args[1].equalsIgnoreCase("lua")) {
                                    tinhLinh.set(LoaiTinhLinh.LUA);
                                } else if (args[1].equalsIgnoreCase("set")) {
                                    tinhLinh.set(LoaiTinhLinh.SET);
                                } else if (args[1].equalsIgnoreCase("gio")) {
                                    tinhLinh.set(LoaiTinhLinh.GIO);
                                } else if (args[1].equalsIgnoreCase("dat")) {
                                    tinhLinh.set(LoaiTinhLinh.DAT);
                                } else if (args[1].equalsIgnoreCase("nuoc")) {
                                    tinhLinh.set(LoaiTinhLinh.NUOC);
                                } else if (args[1].equalsIgnoreCase("hac_am")) {
                                    tinhLinh.set(LoaiTinhLinh.HACAM);
                                }
                                break;
                            } catch (Exception ex) {
                                KnightBlock.messager.msg((Player) sender, KnightBlock.toColor("&cKhông tồn tại người chơi đó"));
                                break;
                            }
                        }
                }
            }
        } else if (command.getName().equals("class")) {
            ClassType classType = getPlayerStats((Player) sender).getClassType();
            CreateGUI.openChooseClassGUI((Player) sender);
        } else if (command.getName().equals("thuoctinh")) {
            ClassType classType = getPlayerStats((Player) sender).getClassType();
            CreateGUI.openStatsGUI((Player) sender);
        } else if (command.getName().equals("soulspawner")) {
            if (sender.hasPermission(staff_perm)) {
                if (args.length > 0) {
                    switch (args[0].toLowerCase()) {
                        case "create":
                            if (args.length > 4) {
                                String id = args[1];
                                if (soulSpawnerData.getConfigFile().getKeys(false).contains(id)) {
                                    KnightBlock.messager.msg((Player) sender, toColor("&cid " + id + " đã tồn tại! &e[/sspawner edit " + id + "]"));
                                    return false;
                                }
                                String mobName = args[2];
                                String childName = args[3];
                                int childAmount = Integer.parseInt(args[4]);
                                if (MythicBukkit.inst().getMobManager().getMythicMob(mobName).get().getDisplayName() != null) {
                                    soulSpawnerData.setConfigurationFile(id + ".Title", "&6Điểm triệu hồi &a" + MythicBukkit.inst().getMobManager().getMythicMob(mobName).get().getDisplayName());
                                } else {
                                    soulSpawnerData.setConfigurationFile(id + ".Title", "&6Điểm triệu hồi &a" + MythicBukkit.inst().getMobManager().getMythicMob(mobName).get().getInternalName());

                                }
                                soulSpawnerData.setConfigurationFile(id + ".SpawnPoint", ((Player) sender).getLocation());
                                soulSpawnerData.setConfigurationFile(id + ".MobName", mobName);
                                soulSpawnerData.setConfigurationFile(id + ".Bandit.Name", childName);
                                soulSpawnerData.setConfigurationFile(id + ".Bandit.Amount", childAmount);
                                SoulSpawner newSoulSpawner = new SoulSpawner(id);
                                soulSpawners.put(id, newSoulSpawner);
                                newSoulSpawner.active();
                                KnightBlock.messager.msg((Player) sender, toColor("&aĐã tạo thành công điểm triệu hồi &e" + id));
                            }
                            break;
                        case "edit":
                            if (args.length >= 3) {
                                String id = args[1];
                                if (!soulSpawnerData.getConfigFile().getKeys(false).contains(id)) {
                                    KnightBlock.messager.msg((Player) sender, toColor("&cid không tồn tại!"));
                                    KnightBlock.messager.msg((Player) sender, toColor("&aDanh sách id SoulSpawner đã tạo: &e" + soulSpawnerData.getConfigFile().getKeys(false)));
                                    return false;
                                }
                                switch (args[2].toLowerCase()) {
                                    case "title":
                                        StringBuilder value = new StringBuilder();
                                        for (int i = 3; i < args.length; i++) {
                                            value.append(toColor(args[i])).append(" ");
                                        }
                                        soulSpawnerData.setConfigurationFile(id + ".Title", value.toString());
                                        soulSpawners.get(id).setTitle(value.toString());
                                        break;
                                    case "location":
                                        soulSpawnerData.setConfigurationFile(id + ".SpawnPoint", ((Player) sender).getLocation());
                                        soulSpawners.get(id).setSpawnPoint(((Player) sender).getLocation());
                                        break;
                                    case "mobspawn":
                                        soulSpawnerData.setConfigurationFile(id + ".MobName", args[3]);
                                        soulSpawners.get(id).setMobName(args[3]);
                                        break;
                                    case "child":
                                        try {
                                            soulSpawnerData.setConfigurationFile(id + ".Bandit.Name", args[3]);
                                            soulSpawnerData.setConfigurationFile(id + ".Bandit.Amount", Integer.parseInt(args[4]));
                                            soulSpawners.get(id).setChildAmount(new Tuple<>(args[3], Integer.parseInt(args[4])));
                                        } catch (Exception ex) {
                                            KnightBlock.messager.msg((Player) sender, toColor("&e/sspawner edit child <mythicmob_name> <amount>"));
                                        }
                                        break;

                                }
                            } else {
                                KnightBlock.messager.msg((Player) sender, toColor("&e/sspawner edit <id> <title/location/mobspawn/child> <value>"));
                            }
                            KnightBlock.messager.msg((Player) sender, toColor("&aThiết lập thành công!"));

                            break;
                        case "remove":
                            if (args.length >= 2) {
                                String id = args[1];
                                if (!soulSpawnerData.getConfigFile().getKeys(false).contains(id)) {
                                    KnightBlock.messager.msg((Player) sender, toColor("&cid không tồn tại!"));
                                    for (String idExisted : soulSpawners.keySet()) {
                                        sender.sendMessage("+ [/sspawner remove " + idExisted + "]");
                                    }
                                    return false;
                                }
                                soulSpawners.get(id).getTask().cancel();
                                soulSpawners.get(id).getAmStatus().remove();
                                soulSpawners.get(id).getAmTitle().remove();
                                soulSpawners.get(id).getAmStatusMob().remove();
                                soulSpawners.remove(id);
                                soulSpawnerData.setConfigurationFile(id, null);
                                KnightBlock.messager.msg((Player) sender, toColor("&eĐã xóa thành công!"));
                            }
                            break;
                        case "move":
                            break;
                    }
                }
            }
        } else if (command.getName().equals("knightblockarena")) {
            if (sender.hasPermission(admin_perm)) {
                Player p = (Player) sender;
                if (args.length > 0 && args[0].toLowerCase().equals("setarena")) {
                    if (args.length == 1) {
                        p.sendMessage("§c/eventor setarena <radisus>");
                    }
                    if (args.length > 1) {
                        if (Integer.parseInt(args[1]) > 0) {
                            arenaData.setConfigurationFile("Arena.Center", p.getLocation());
                            arenaData.setConfigurationFile("Arena.Radius", Integer.parseInt(args[1]));
                            p.sendMessage(new String[]{
                                    "§aĐã đặt Center của Arena tại đây!, với phạm vi: §e" + args[1] + "Block (Bán kính)"});
                        } else {
                            p.sendMessage(new String[]{"§cSố được nhập phải lớn hơn 0"});
                        }
                    }
                }
                if (args.length > 0 && args[0].toLowerCase().equals("setarenahologram")) {
                    if (Arena.Line3 != null) {
                        Arena.Line1.remove();
                        Arena.Line2.remove();
                        Arena.Line3.remove();
                        Arena.Line1 = null;
                        Arena.Line2 = null;
                        Arena.Line3 = null;
                    }
                    arenaData.setConfigurationFile("Arena.Hologram", p.getLocation());
                    p.sendMessage(new String[]{
                            "§aĐã đặt Hologram của Arena tại đây!"});

                }
                if (args.length > 0 && args[0].toLowerCase().equals("showarena")) {
                    p.sendMessage("§a" + Arena.playerJoinArena.toString());
                }
            }
        } else if (command.getName().equalsIgnoreCase("antoan")) {
            try {
                Player p = (((Player) sender).getPlayer());
                if (KnightBlockAPI.getTimeLeft(p, "invicible", "Chưa kết thúc lượt cũ")) {
                    if (!VICINBLE.contains(p.getName())) {
                        if (hasEnoughItems(p, AMOUNT_ITEM_INVICINBLE, ITEM_INVICINBLE)) {
                            VICINBLE.add(p.getName());
                            invincible(TIME_INVICINBLE, p.getName());
                            messager.msg(p, MessagerType.GIVE_INVICINBLE.getContent().replace("{time}", TIME_INVICINBLE / 20 + ""));

                        } else {
                            if (ITEM_INVICINBLE.getItemMeta() != null) {
                                messager.msg(p, MessagerType.NO_ENOUGH_ITEMS.getContent().replace("{type}", ITEM_INVICINBLE.getItemMeta().getDisplayName()));
                            } else {
                                messager.msg(p, MessagerType.NO_ENOUGH_ITEMS.getContent().replace("{type}", ITEM_INVICINBLE.getType().name()));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                messager.msg((Player) sender, "&cKhông tồn tại người chơi này!");
            }
        } else if (command.getName().equalsIgnoreCase("vipfly")) {
            Player p = (Player) sender;
            if (!KnightBlockAPI.playerIsOnDungeon(p)) {
                messager.msg(p, toColor("&cBạn không thể sử dụng lệnh này tại thế giới này"));
                return false;
            }
            int vipLevel = KnightBlockAPI.getVipLevel(p);
            if (vipLevel < 5) {
                messager.msg(p, toColor("&cBạn cần nâng cấp rank để sử dụng lệnh này! [/rank]"));
                return false;
            } else if (KnightBlockAPI.getTimeLeft(p, "combat", toColor("&cKhông thể sử dụng lệnh này khi đang combat"))) {
                if (KnightBlockAPI.getVipLevel(p) == 5) {
                    vipFly(p, 5 * 60, 15 * 60);
                } else if (KnightBlockAPI.getVipLevel(p) == 6) {
                    vipFly(p, 15 * 60, 10 * 60);
                } else if (KnightBlockAPI.getVipLevel(p) == 7) {
                    vipFly(p, -1, 0);
                } else if (KnightBlockAPI.getVipLevel(p) == 8) {
                    messager.msg(p, toColor("&eBạn đang sở hữu rank không giới hạn bay, sử dụng [/fly]"));
                }
            }
        } else if (command.getName().equalsIgnoreCase("tuyetky")) {
            messager.msg((Player) sender, toColor("&eTính năng đang được cập nhật!"));
            CreateGUI.openTuyetKyGUI((Player) sender);
        } else if (command.getName().equalsIgnoreCase("truyna")) {
            messager.msg((Player) sender, toColor("&eTính năng đang được cập nhật!"));
            Player psend = (Player) sender;
            if (args.length > 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) return false;
                int bounty = 0;
                try {
                    bounty = Integer.parseInt(args[1]);
                } catch (Exception ex) {
                    messager.msg(psend, toColor("&cPhải nhập một số tiền!"));
                }
                if (bounty > 100) {
                    if (args.length > 2 && args[2] != null) {
                        int count = 0;
                        StringBuilder resons = new StringBuilder();
                        for (String reson : args) {
                            count++;
                            if (count > 2) {
                                resons.append(" ").append(reson);
                            }
                        }
                        new Wanteds(psend, target, bounty, resons.toString()).start();
                    } else {
                        new Wanteds(psend, target, bounty, null).start();
                    }
                } else {
                    messager.msg(psend, toColor("&cSố tiền thưởng phải lớn hơn &6100$"));
                }
            } else {
                messager.msg(psend, toColor("&e/truyna <tên> <số tiền> (lý do)"));
            }
        } else if (command.getName().equalsIgnoreCase("khokhoangsan")) {
            if (args.length == 0) {
                CreateGUI.openKhoangSanGUI(((Player) sender));
            } else {
                if (args.length > 1) {
                    if (args[0].equalsIgnoreCase("show")) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            Player other = Bukkit.getPlayer(args[1]);
                            CreateGUI.openKhoKhoanSanOther(((Player) sender), other);
                        }
                    }
                }
            }
        } else if (command.getName().equalsIgnoreCase("knightblockanticheat")) {
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("ok")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (PlayerListener.jailWaits.containsKey(target.getName())) {
                            if (hasVotes.containsKey(((Player) sender).getUniqueId())) {
                                if (hasVotes.get(((Player) sender).getUniqueId()).equals(target.getName()))
                                    return false;
                            }
                            int voteAmount = PlayerListener.jailWaits.get(target.getName());
                            Bukkit.broadcastMessage(toColor("&b> &c&lTÒA ÁN &6&lFICTIONSKY &b< &eĐã có &a" + voteAmount + " &eNgười chơi bỏ phiếu xử phạt &c" + target.getName() + " [/kac ok " + target.getName() + "] &eđể bỏ phiếu phạt"));
                            hasVotes.put(((Player) sender).getUniqueId(), target.getName());
                            if (voteAmount > 4) {
                                PlayerListener.jailWaits.remove(pl.getName());
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "jail " + target.getName() + " jail " + 10 + "m");
                                Bukkit.broadcastMessage(KnightBlock.toColor("&b> &c&lTÒA ÁN &6&lFICTIONSKY &b< &eĐã tổng cổ &4" + target.getName() + " &evào nhà tù 10 phút vì sử dụng hack theo biểu quyết của member"));
                                hasVotes.remove(((Player) sender).getUniqueId());
                                PlayerListener.jailWaits.remove(target.getName());
                            }
                            PlayerListener.jailWaits.put(target.getName(), voteAmount + 1);
                        }
                    }
                }
            }
        } else if (command.getName().equalsIgnoreCase("nen_van_hoa")) {
            CreateGUI.openChangeCultureGUI(((Player) sender));
        }
        return super.onCommand(sender, command, label, args);
    }

    private void vipFly(Player p, int sec, int delay) {
        if (sec == -1) {
            p.setAllowFlight(true);
            messager.msg(p, toColor(MessagerType.ON_VIP_FLY.getContent().replace("{time}", "∞")));
            return;
        }
        if (vipFlyPlayers.containsKey(p.getUniqueId())) {
            messager.msg(p, toColor("&cChưa kết thúc lượt cũ"));
            return;
        }
        if (KnightBlockAPI.getTimeLeft(p, "on_vipfly", toColor("&cCần đợi &4{time}s &cđể sử dụng lệnh này"))) {
            p.setAllowFlight(true);
            vipFlyPlayers.put(p.getUniqueId(), true);
            Bukkit.getScheduler().runTaskLater(pl, () -> {
                KnightBlockAPI.setTimeLeft(p, "on_vipfly", delay);
                p.setAllowFlight(false);
                p.setFlying(false);
                messager.msg(p, toColor(MessagerType.CANCEL_VIP_FLY.getContent()));
                vipFlyPlayers.remove(p.getUniqueId());
            }, sec * 20L);
            messager.msg(p, toColor(MessagerType.ON_VIP_FLY.getContent().replace("{time}", KnightBlockAPI.TimeLeftFormat(sec * 20L))));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("tinhlinh")) {
            if (args.length == 1) {
                if (sender.hasPermission(admin_perm)) {
                    completions.add("set");
                }
                completions.add("toggle");
                completions.add("help");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
                if (!sender.hasPermission(admin_perm)) return null;
                completions.add("SET");
                completions.add("GIO");
                completions.add("LUA");
                completions.add("DAT");
                completions.add("NUOC");
                completions.add("HAC_AM");
            }
            return completions;
        } else if (command.getName().equalsIgnoreCase("knightblock")) {
            if (sender.hasPermission(admin_perm)) {
                if (args.length == 1) {
                    completions.add("ngocchuyenhoa");
                    completions.add("invincibleitem");
                    completions.add("linhthach");
                    completions.add("stats");
                    completions.add("treeofworld");
                    completions.add("reload");
                    completions.add("whitelistworldvip");
                    completions.add("dungeon");
                    completions.add("bypassafkworld");
                    completions.add("booster");
                    completions.add("baitfish");
                    completions.add("help");
                } else if (args.length == 2 && args[0].equalsIgnoreCase("treeofworld")) {
                    completions.add("beam");
                } else if (args.length == 3 && args[1].equalsIgnoreCase("beam")) {
                    completions.add("add");
                    completions.add("clear");
                } else if (args.length == 2 && args[0].equalsIgnoreCase("whitelistworldvip")) {
                    completions.add("add");
                    completions.add("remove");
                } else if (args.length == 2 && args[0].equalsIgnoreCase("dungeon")) {
                    completions.add("add");
                    completions.add("remove");
                } else if (args.length == 3 && args[0].equalsIgnoreCase("whitelistworldvip")) {
                    for (World world : Bukkit.getWorlds()) {
                        completions.add(world.getName());
                    }
                } else if (args.length == 3 && args[0].equalsIgnoreCase("dungeon")) {
                    for (World world : Bukkit.getWorlds()) {
                        completions.add(world.getName());
                    }
                } else if (args.length == 3 && args[0].equalsIgnoreCase("bypassafkworld")) {
                    for (World world : Bukkit.getWorlds()) {
                        completions.add(world.getName());
                    }
                }
                return completions;
            } else {
                if (args.length == 1) {
                    completions.add("cuonghoa");
                }
                return completions;
            }
        } else if (command.getName().equalsIgnoreCase("soulspawner")) {
            if (sender.hasPermission(admin_perm)) {
                if (args.length == 1) {
                    if (sender.hasPermission(staff_perm)) {
                        completions.add("create");
                        completions.add("edit");
                        completions.add("remove");
                        completions.add("move");
                    }
                } else if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
                    if (sender.hasPermission(staff_perm)) {
                        for (MythicMob mythicMob : MythicBukkit.inst().getMobManager().getMobTypes()) {
                            completions.add(mythicMob.getInternalName());
                        }
                    }
                } else if (args.length == 4 && args[0].equalsIgnoreCase("create")) {
                    if (sender.hasPermission(staff_perm)) {
                        for (MythicMob mythicMob : MythicBukkit.inst().getMobManager().getMobTypes()) {
                            completions.add(mythicMob.getInternalName());
                        }
                    }
                } else if (args.length == 3 && args[0].equalsIgnoreCase("edit")) {
                    completions.add("title");
                    completions.add("mobspawn");
                    completions.add("location");
                    completions.add("child");
                } else if (args.length == 3 && args[2].equalsIgnoreCase("mobspawn") && args[0].equalsIgnoreCase("edit")) {
                    if (sender.hasPermission(staff_perm)) {
                        for (MythicMob mythicMob : MythicBukkit.inst().getMobManager().getMobTypes()) {
                            completions.add(mythicMob.getInternalName());
                        }
                    }
                } else if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {
                    if (sender.hasPermission(staff_perm)) {
                        completions.addAll(soulSpawnerData.getConfigFile().getKeys(false));
                    }
                } else if (args.length == 4 && args[2].equalsIgnoreCase("child") && args[0].equalsIgnoreCase("edit")) {
                    if (sender.hasPermission(staff_perm)) {
                        for (MythicMob mythicMob : MythicBukkit.inst().getMobManager().getMobTypes()) {
                            completions.add(mythicMob.getInternalName());
                        }
                    }
                } else if (args.length == 5 && args[2].equalsIgnoreCase("child") && args[0].equalsIgnoreCase("edit")) {
                    completions.add("1");
                    completions.add("10");
                    completions.add("100");
                    completions.add("1000");
                } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                    if (sender.hasPermission(staff_perm)) {
                        completions.addAll(soulSpawnerData.getConfigFile().getKeys(false));
                    }
                }
                return completions;
            }
        } else if (command.getName().equalsIgnoreCase("knightblockarena")) {
            if (sender.hasPermission(admin_perm)) {
                if (args.length == 1) {
                    completions.add("setarena");
                    completions.add("setarenahologram");
                    completions.add("showarena");
                }
            }
            return completions;
        } else if (command.getName().equalsIgnoreCase("khokhoangsan")) {
            if (args.length == 1) {
                completions.add("show");
            } else if (args.length > 1 && args[0].equalsIgnoreCase("show")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            }
            return completions;
        } else if (command.getName().equalsIgnoreCase("truyna")) {
            if (args.length == 1) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            }
            return completions;
        }
        return null;
    }

    @Override
    public void onDisable() {
        for (NPC npc : SPhanThan.npcs) {
            if (npc == null) continue;
            npc.destroy();
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (khoKhoangSanHashMap.containsKey(p.getUniqueId())) {
                khoKhoangSanHashMap.get(p.getUniqueId()).save();
            }
            removePlayer(p);
        }
        for (World world : Bukkit.getWorlds()) {
            for (Entity e : world.getEntities()) {
                if (e.getCustomName() != null && e.getCustomName().contains(KnightBlock.toColor("&l\uD83D\uDC7B Tinh Linh"))) {
                    e.remove();
                }
            }
        }
        for (SoulSpawner soulSpawner : soulSpawners.values()) {
            if (soulSpawner.getAmStatus() != null) soulSpawner.getAmStatus().remove();
            if (soulSpawner.getAmTitle() != null) soulSpawner.getAmTitle().remove();
            if (soulSpawner.getAmStatusMob() != null) soulSpawner.getAmStatusMob().remove();
        }
        Arena.Line1.remove();
        Arena.Line2.remove();
        Arena.Line3.remove();
    }

    public static PlayerStats getPlayerStats(Player p) {
        if (p != null && p.isOnline()) {
            UUID uuid = p.getUniqueId();
            if (playerStats.containsKey(uuid)) {
                return playerStats.get(uuid);
            }
            PlayerStats playerStats1 = new PlayerStats(p);
            playerStats.put(uuid, playerStats1);
            return playerStats1;
        }
        return null;
    }


    public static String toColor(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void setUpPlayer(Player p) {
        PlayerStats playerStats = new PlayerStats(p);
        users.put(p.getUniqueId(), AuraSkillsApi.get().getUser(p.getUniqueId()));
        KnightBlock.playerStats.put(p.getUniqueId(), playerStats);
        Culture culture = new Culture(p);
        ChatColor color = ChatColor.of(new java.awt.Color(37, 225, 158, 218));
        new TinhLinh(p);
        p.sendTitle(culture.getDisplayName(), toColor(color + "Cấp độ: " + culture.getRankName()));
        p.setMaxHealth(20 + playerStats.getHP());
        float speed = (float) (0.2 + (playerStats.getNhanhNhen() / 100));
        p.setWalkSpeed(Math.min(speed, 0.7F));
        MMOPlayerData mmodata = MMOPlayerData.get(p);
        StatMap stats = mmodata.getStatMap();
        StatInstance instance = stats.getInstance(SharedStat.MAX_HEALTH);
        instance.remove("mmocoreClass");
        stats.update("MAX_HEALTH");
        mmodata.updatePlayer(p);
        KnightBlockAPI.setUpPlayerInWorld(p);
        KnightBlock.khoKhoangSanHashMap.put(p.getUniqueId(), new KhoKhoangSan(p));
    }

    public static void reloadPlayer(Player p) {
        PlayerStats stats = getPlayerStats(p);
        if (stats == null) return;
        TinhLinhData tinhLinhData = new com.bbv.stotage.TinhLinh(p);

        if (stats.getTinhLinh() != null && stats.isActivedTinhLinh()) {
            stats.getTinhLinh().hide(false, false);
            tinhLinhData.setMana(stats.getTinhLinh().getLoaiTinhLinh(), stats.getTinhLinh().getMana());
        }
        stats.setTinhLinh(null);
        stats.setCulture(null);
        KnightBlock.playerStats.remove(p.getUniqueId());
        users.remove(p.getUniqueId());
        PlayerStats playerStats = new PlayerStats(p);
        users.put(p.getUniqueId(), AuraSkillsApi.get().getUser(p.getUniqueId()));
        KnightBlock.playerStats.put(p.getUniqueId(), playerStats);
        new Culture(p);
        new TinhLinh(p);
        p.setMaxHealth(20 + playerStats.getHP());
        float speed = (float) (0.2 + (playerStats.getNhanhNhen() / 100));
        p.setWalkSpeed(Math.min(speed, 0.7F));
    }

    public static void removePlayer(Player p) {
        if (getPlayerStats(p).getTinhLinh() != null) {
            getPlayerStats(p).getTinhLinh().hide(false, false);
        }
        playerStats.remove(p.getUniqueId());
        users.remove(p.getUniqueId());
        CreateGUI.rateUpCul.remove(p.getUniqueId());
        CreateGUI.needEXP.remove(p.getUniqueId());
        PlayerListener.updatings.remove(p.getUniqueId());
        AntiCheat.playerLastLocation.remove(p.getUniqueId());
        AntiCheat.playerAFKTime.remove(p.getUniqueId());
    }

    public static void boosterMine(Player p, int amount, int time, int rate) {
        if (rate > 100) {
            rate = 100;
        }
        if (BOOSTER.containsKey(p.getUniqueId()) && BOOSTER.get(p.getUniqueId()).Left() > amount) {
            messager.msg(p, MessagerType.HAS_LEVEL_BOOSTER.getContent().replace("{amount}", amount + ""));
        } else {
            if (!TASK_BOOSTER.containsKey(p.getUniqueId())) {
                TIMELEFT_BOOSTER.put(p.getUniqueId(), time);
                BukkitTask task = onBooster(time, p.getUniqueId());
                TASK_BOOSTER.put(p.getUniqueId(), task);
                BOOSTER.put(p.getUniqueId(), new Tuple<>(amount, rate));
                messager.msg(p, MessagerType.GIVE_BOOST.getContent().replace("{amount}", amount + "").replace("{time}", KnightBlockAPI.TimeFormat(p)).replace("{type}", "Mine").replace("{rate}", rate + ""));
            } else {
                if (amount > BOOSTER.get(p.getUniqueId()).Left()) {
                    TIMELEFT_BOOSTER.put(p.getUniqueId(), time);
                    TASK_BOOSTER.get(p.getUniqueId()).cancel();
                    BukkitTask task = onBooster(time, p.getUniqueId());
                    TASK_BOOSTER.put(p.getUniqueId(), task);
                    BOOSTER.put(p.getUniqueId(), new Tuple<>(amount, rate));
                    messager.msg(p, MessagerType.GIVE_BOOST.getContent().replace("{amount}", amount + "").replace("{time}", KnightBlockAPI.TimeFormat(p)).replace("{type}", "Mine").replace("{rate}", rate + ""));
                } else if (amount == BOOSTER.get(p.getUniqueId()).Left()) {
                    int timeAdd = time + TIMELEFT_BOOSTER.get(p.getUniqueId());
                    TIMELEFT_BOOSTER.put(p.getUniqueId(), timeAdd);
                    TASK_BOOSTER.get(p.getUniqueId()).cancel();
                    BukkitTask task = onBooster(timeAdd, p.getUniqueId());
                    TASK_BOOSTER.put(p.getUniqueId(), task);
                    BOOSTER.put(p.getUniqueId(), new Tuple<>(amount, rate));
                    messager.msg(p, MessagerType.GIVE_BOOST.getContent().replace("{amount}", amount + "").replace("{time}", KnightBlockAPI.TimeFormat(p)).replace("{type}", "Mine").replace("{rate}", rate + ""));
                    //  System.out.println(ChatColor.YELLOW + "Người chơi " + p.getName() + " Đã sử dụng booster toàn server level " + amount + " thời gian: " + KnightBlockAPI.TimeFormat(p));
//                        for (Player admin : Bukkit.getOnlinePlayers()) {
//                            if (admin.hasPermission(admin_perm)) {
//                                admin.sendMessage(ChatColor.YELLOW + "Người chơi " + p.getName() + " Đã sử dụng booster toàn server level " + amount + " thời gian: " + KnightBlockAPI.TimeFormat(p));
//                            }
//                        }
                }
            }
        }
    }

    static BukkitTask onBooster(int time, UUID IDPlayer) {
        return (new BukkitRunnable() {
            int dem = time;

            @Override
            public void run() {
                dem--;
                TIMELEFT_BOOSTER.put(IDPlayer, dem);
                if (dem <= 0) {
                    this.cancel();
                    String bost = BOOSTER.get(IDPlayer).toString();
                    BOOSTER.remove(IDPlayer);
                    TASK_BOOSTER.remove(IDPlayer);
                    TIMELEFT_BOOSTER.remove(IDPlayer);
                    Player p = Bukkit.getPlayer(IDPlayer);
                    if (p != null) {
                        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 10);
                        messager.msg(p, MessagerType.CANCEL_BOOST.getContent().replace("{type}", "Mine"));
                    }
                }
            }
        }).runTaskTimer(pl, 0, 0);
    }

    @EventHandler
    public void onInvicible(EntityDamageByEntityEvent e) {
//        if (e.getEntity().getName().equalsIgnoreCase("TESSSTB")) {
//            e.setCancelled(true);
//            if (e.getDamager() instanceof LivingEntity) {
//                ((LivingEntity) e.getDamager()).damage(e.getDamage(), e.getEntity());
//            }
//            e.getDamager().sendMessage(e.getEntity().getName() + ": Anh zai bình tĩnh đừng chém eim :3");
//        }
        if (e.getDamager() instanceof Player) {
            Player pDamager = (Player) e.getDamager();
            if (e.getEntity() instanceof Player) {
                if (VICINBLE.contains(pDamager.getName())) {
                    e.setCancelled(true);
                    messager.msg(pDamager, toColor("&eBạn đang bật tính năng &a/antoan, không thể gây sát thương lên người chơi"));
                }
            } else {
                if (VICINBLE.contains(pDamager.getName())) {
                    e.setDamage(e.getDamage() * 0.5);
                }
            }
        } else if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (VICINBLE.contains(p.getName())) {
                e.setCancelled(true);
                if (e.getDamager() instanceof Player) {
                    messager.msg(e.getDamager(), toColor("&eĐối phương đang bật tính năng &a/antoan"));
                }
            }
        }
    }

    @EventHandler
    private void onInvincible(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (VICINBLE.contains(event.getEntity().getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onIvincibleByBow(ProjectileHitEvent e) {
        Entity eDamage = e.getHitEntity();
        if (eDamage == null) return;
        if (eDamage instanceof Player) {
            Player p = (Player) eDamage;
            if (VICINBLE.contains(p.getName())) {
                e.setCancelled(true);
                eDamage.getWorld().spawnParticle(Particle.CRIT_MAGIC, e.getEntity().getLocation(), 15, 0, 0, 0, 0);
                e.getEntity().remove();
                ProjectileSource Shooter = e.getEntity().getShooter();
                if (Shooter != null) {
                    if (Shooter instanceof Player) {
                        messager.msg((Player) Shooter, toColor("&eĐối phương đang bật tính năng &a/antoan"));
                    }
                }
            }
        }
    }

    public static boolean hasEnoughItems(Player player, int amount, ItemStack cost) {
        PlayerInventory playerInventory = player.getInventory();
        int totalAmountFound = 0;

        // Lặp qua kho đồ của người chơi để đếm tổng số lượng item cần kiểm tra
        for (ItemStack item : playerInventory.getContents()) {
            if (item != null && item.isSimilar(cost)) {
                totalAmountFound += item.getAmount();
            }
        }

        // Nếu đủ số lượng item, thực hiện xoá các item đã đủ
        if (totalAmountFound >= amount) {
            int remainingAmount = amount;

            // Lặp qua lại kho đồ để xoá các item đã đủ
            for (int i = 0; i < playerInventory.getSize(); i++) {
                ItemStack item = playerInventory.getItem(i);

                if (item != null && item.isSimilar(cost)) {
                    int itemAmount = item.getAmount();

                    if (itemAmount <= remainingAmount) {
                        // Xoá toàn bộ item nếu số lượng còn lại ít hơn hoặc bằng số item trong slot
                        playerInventory.setItem(i, null);
                        remainingAmount -= itemAmount;
                    } else {
                        // Giảm số lượng item nếu số item trong slot lớn hơn số lượng còn lại
                        item.setAmount(itemAmount - remainingAmount);
                        break;
                    }
                }
            }
            return true; // Trả về true nếu đủ item và đã xoá các item đủ
        }
        return false; // Trả về false nếu không đủ item
    }

    public static void invincible(long time, String playerName) {
        (new BukkitRunnable() {
            @Override
            public void run() {
                this.cancel();
                VICINBLE.remove(playerName);
                Player p = Bukkit.getPlayer(playerName);
                assert p != null;
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 10);
                messager.msg(p, toColor("&eĐã hết thời gian bảo vệ PVP"));
                KnightBlockAPI.setTimeLeft(p, "invicible", COOLDOWN);
            }
        }).runTaskTimer(pl, time, 0);
    }

    public static void onRandomBroadcast() {
        // TODO Auto-generated method stub
        (new BukkitRunnable() {
            public void run() {
                Random random = new Random();
                switch (random.nextInt(9)) {
                    case 0:
                        Bukkit.broadcastMessage("§7");
                        Bukkit.broadcastMessage("§f/thuoctinh (/tt) để xem thuộc tính của bản thân");
                        Bukkit.broadcastMessage("§7");
                        break;
                    case 1:
                        Bukkit.broadcastMessage("§7");
                        Bukkit.broadcastMessage("§fBạn bị săn? sử dụng [/antoan] để kích hoạt bảo vệ");
                        Bukkit.broadcastMessage("§fNhưng sẽ bị giảm 50% sát thương, và không thể tấn công người chơi");
                        Bukkit.broadcastMessage("§7");
                        break;
                    case 2:
                        Bukkit.broadcastMessage("§7");
                        Bukkit.broadcastMessage("§eCó thể kiếm tiền thông qua /warp afk");
                        Bukkit.broadcastMessage("§eHoặc /taixiu cuoc để đặt cược");
                        Bukkit.broadcastMessage("§eSử dụng /cf <số tiền cược> để tạo một game CoinFlip");
                        Bukkit.broadcastMessage("§7");
                        break;
                    case 3:
                        Bukkit.broadcastMessage("§7---");
                        Bukkit.broadcastMessage("§eSử dụng [/tuyetky] để tìm hiểu về hệ thống skill đặc biệt");
                        Bukkit.broadcastMessage("§7---");
                        break;
                    case 4:
                        Bukkit.broadcastMessage("§7");
                        Bukkit.broadcastMessage("§fKhuyến cáo người chơi nên sử dụng bản 1.20.4 để có trải nghiệm tốt nhất");
                        Bukkit.broadcastMessage("§7");
                        break;
                    case 5:
                        Bukkit.broadcastMessage("§7");
                        Bukkit.broadcastMessage("§fBạn đang giàu mà không biết tiêu tiền vào đâu?");
                        Bukkit.broadcastMessage("§fsử dụng [/truyna <name> <money>] để đặt lệnh truy nã người khác");
                        Bukkit.broadcastMessage("§7");
                        break;
                    case 6:
                        Bukkit.broadcastMessage("§7");
                        Bukkit.broadcastMessage("§fServer cung cấp tính năng tự chế tạo [/autocraft] hoặc [/atc]");
                        Bukkit.broadcastMessage("§fcần mua công thức tại [/warp khoidau]");
                        Bukkit.broadcastMessage("§7");
                        break;
                    case 7:
                        Bukkit.broadcastMessage("§7");
                        Bukkit.broadcastMessage("§fServer cung cấp tính năng kho khoáng sản [/kks] hoặc [/ks]");
                        Bukkit.broadcastMessage("§fQuặng hoặc đá sẽ ở trong kho này khi đào");
                        Bukkit.broadcastMessage("§7");
                        break;

                    default:
                        break;
                }
            }
        }).runTaskTimer(pl, 300, 6700);
    }
}
