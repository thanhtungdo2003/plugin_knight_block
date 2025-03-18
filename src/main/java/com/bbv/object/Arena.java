package com.bbv.object;

import java.io.File;
import java.util.*;

import com.bbv.knightblock.EcoManager;
import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import org.jetbrains.annotations.NotNull;

public class Arena implements Listener {
    static int maxPlayers = 2;
    static int PlayerCount;
    static Inventory GuiArena;
    static HashMap<Player, Boolean> SetGold = new HashMap<>();
    static Double TienCuoc = 0.0D;
    static HashMap<Player, Boolean> SetBalance = new HashMap<>();
    static HashMap<Player, Long> oncdQuit = new HashMap<>();
    static Long onCdJoin;
    static HashMap<Player, Location> locJoin = new HashMap<>();
    static boolean TienCuocTrigger;
    private String SvMsg = "§7[§aServer§7] ";
    public static List<Player> playerJoinArena = new ArrayList<>();
    static boolean actionArena = false;
    public static ArmorStand Line1;
    public static ArmorStand Line2;
    public static ArmorStand Line3;
    static boolean testBug = false;
    static ItemStack OutLineGuiArena;
    static ItemStack Balance;
    static ItemStack Gold;
    private final Location center;
    private Location hologram_center;
    private int Radius = 0;

    static {
        Gold = new ItemStack(Material.GOLD_INGOT);
        ItemMeta im = Gold.getItemMeta();
        im.setDisplayName("§6Money");
        im.setLore(Arrays.asList("§fClick §7để đặt cược bằng §6Money"));
        im.setUnbreakable(true);
        im.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        Gold.setItemMeta(im);
    }

    static {
        Balance = new ItemStack(Material.DIAMOND);
        ItemMeta im = Balance.getItemMeta();
        im.setDisplayName("§aXu (/p me)");
        im.setLore(Arrays.asList("§fClick §7để đặt cược bằng §aXu"));
        im.setUnbreakable(true);
        im.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        Balance.setItemMeta(im);
    }

    static {
        OutLineGuiArena = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta im = OutLineGuiArena.getItemMeta();
        im.setDisplayName(" ");
        im.setUnbreakable(true);
        im.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        OutLineGuiArena.setItemMeta(im);
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && block.getType().name().contains("WALL_SIGN")) {
            if (!(block.getState() instanceof Sign)) return;
            Sign sign = (Sign) block.getState();
            if (sign.getLine(0).equals("[kbarena]") && sign.getLine(1).equals("join")) {
                event.setCancelled(true);
                sign.setLine(0, "§a§l[ARENA]");
                sign.setLine(1, "[Join]");
                sign.update();
            }
            if (sign.getLine(0).equals("[kbarena]") && sign.getLine(1).equals("quit")) {
                event.setCancelled(true);
                sign.setLine(0, "§a§l[ARENA]");
                sign.setLine(1, "[Thoát]");
                sign.update();
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType().name().contains("WALL_SIGN")) {
            if (!(block.getState() instanceof Sign)) return;
            Sign sign = (Sign) block.getState();
            if (sign.getLine(0).equals("§a§l[ARENA]")) {
                event.setCancelled(true);
                if (sign.getLine(1).equals("[Join]")) {
                    double timelJoin = getTimeLeftJoin();
                    if (timelJoin == 0.0D) {
                        if (PlayerCount < maxPlayers) {
                            if (!playerJoinArena.contains(player)) {
                                if (!actionArena) {
                                    player.openInventory(GuiArena);
                                    locJoin.put(player, player.getLocation());
                                } else {
                                    event.setCancelled(true);
                                    if (!TienCuocTrigger) {
                                        double GoldPlayer = EcoManager.getMoney(player);
                                        if (GoldPlayer > TienCuoc) {
                                            locJoin.put(player, player.getLocation());
                                            playerJoinArena.add(player);
                                            onTeleportToTheArena(player);
                                            EcoManager.takeMoney(player, TienCuoc);
                                            Bukkit.broadcastMessage(
                                                    "§7§l§m----------------------------------------------------------");
                                            Bukkit.broadcastMessage(
                                                    "§6" + player.getDisplayName() + "§e Đã tham gia §aARENA§e");
                                            if (playerJoinArena.size() > 1) {
                                                Bukkit.broadcastMessage(
                                                        "§aARENA §eđã đủ 2 người §a[" + playerJoinArena.get(0).getName()
                                                                + ", " + playerJoinArena.get(1).getName() + "]");
                                            }
                                            Bukkit.broadcastMessage(
                                                    "§7§l§m----------------------------------------------------------");
                                            oncdQuit.put(player, System.currentTimeMillis() + (long) (600 * 70));
                                        } else {
                                            player.sendMessage(SvMsg + "§cBạn không đủ §6" + TienCuoc
                                                    + "$ §cđể tham gia thách đấu");
                                        }
                                    } else {
                                        double MoneyPlayer = PlayerPoints.getInstance().getAPI().look(player.getUniqueId());
                                        if (MoneyPlayer > TienCuoc) {
                                            locJoin.put(player, player.getLocation());
                                            playerJoinArena.add(player);
                                            onTeleportToTheArena(player);
                                            PlayerPoints.getInstance().getAPI().take(player.getUniqueId(), TienCuoc.intValue());
                                            Bukkit.broadcastMessage(
                                                    "§7§l§m----------------------------------------------------------");
                                            Bukkit.broadcastMessage(
                                                    "§6" + player.getDisplayName() + "§e Đã tham gia §aARENA§e");
                                            if (playerJoinArena.size() > 1) {
                                                Bukkit.broadcastMessage(
                                                        "§aARENA §eđã đủ 2 người §a[" + playerJoinArena.get(0).getName()
                                                                + ", " + playerJoinArena.get(1).getName() + "]");
                                            }
                                            Bukkit.broadcastMessage(
                                                    "§7§l§m----------------------------------------------------------");
                                            oncdQuit.put(player, System.currentTimeMillis() + (long) (600 * 70));
                                        } else {
                                            player.sendMessage(SvMsg + "§cBạn không đủ §a" + TienCuoc
                                                    + "Xu §cđể tham gia thách đấu");
                                        }
                                    }
                                }
                            }
                        } else {
                            player.sendMessage("§7[§aServer§7]§c Đấu trường đã đầy!");

                        }
                    } else {
                        player.sendMessage("§7[§aServer§7]§c Chờ " + (int) (timelJoin / 48) + "s để sử dụng bảng này");
                    }
                }
                event.setCancelled(true);
                if (sign.getLine(1).equals("[Thoát]")) {
                    if (playerJoinArena.contains(player)) {
                        double timel = getTimeLeftQuit(player);
                        if (timel == 0.0D) {
                            if (locJoin.get(player) != null) {
                                if (KnightBlockAPI.getTimeLeft(player, "combat")) {
                                    player.teleport(locJoin.get(player), TeleportCause.PLUGIN);
                                    playerJoinArena.remove(player);
                                    if (!TienCuocTrigger) {
                                        player.sendMessage(SvMsg + "§eĐã thoát §aArena §evà nhận lại §6" + TienCuoc + "$");
                                        EcoManager.addMoney(player, TienCuoc);
                                    } else {
                                        player.sendMessage(SvMsg + "§eĐã thoát §aArena §evà nhận lại §a" + TienCuoc + "xu");
                                        PlayerPoints.getInstance().getAPI().give(player.getUniqueId(), TienCuoc.intValue());
                                    }
                                    TienCuoc = 0.0D;
                                    if (!playerJoinArena.isEmpty()) {
                                        Player p2 = getPlayer();
                                        p2.teleport(locJoin.get(p2), TeleportCause.PLUGIN);
                                        playerJoinArena.remove(p2);
                                    }
                                } else {
                                    player.sendMessage(SvMsg + "§cBạn chưa thoát Combat");
                                }
                            }
                        } else {
                            player.sendMessage(SvMsg + "§cCòn §7" + (int) (timel / 48) + "s§c để thoát khỏi §aArena");
                        }
                    } else {
                        player.sendMessage(SvMsg + "§cBạn không trong ARENA");
                    }
                }
            }
        }
    }

    @NotNull
    private Player getPlayer() {
        Player p2 = playerJoinArena.get(0);
        if (!TienCuocTrigger) {
            p2.sendMessage(SvMsg + "§cLỗi, đối thủ đã thoát §aArena§c nhận lại §6"
                    + TienCuoc + "g");
        } else {
            p2.sendMessage(SvMsg + "§cLỗi, đối thủ đã thoát §aArena§c nhận lại §a"
                    + TienCuoc + "Coints");
        }
        return p2;
    }

    @EventHandler
    public void onQuitSvInArena(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (playerJoinArena.contains(p)) {
            if (!TienCuocTrigger) {
                playerJoinArena.remove(p);
            } else {
                playerJoinArena.remove(p);
            }
            if (!playerJoinArena.isEmpty()) {
                Player p2 = playerJoinArena.get(0);
                if (!TienCuocTrigger) {
                    p2.sendMessage(SvMsg + "§cLỗi, đối thủ đã thoát §aArena§c nhận lại §6" + TienCuoc + "g");
                    p2.teleport(locJoin.get(p2), TeleportCause.PLUGIN);
                    playerJoinArena.remove(p2);
                } else {
                    p2.sendMessage(SvMsg + "§cLỗi, đối thủ đã thoát §aArena§c nhận lại §a" + TienCuoc + "Coints");
                    p2.teleport(locJoin.get(p2), TeleportCause.PLUGIN);
                    playerJoinArena.remove(p2);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDieInArena(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (playerJoinArena.contains(p)) {
            if (playerJoinArena.size() == 2) {
                playerJoinArena.remove(p);
                Player p2 = playerJoinArena.get(0);
                if (!TienCuocTrigger) {
                    onCdJoin = System.currentTimeMillis() + (long) (600 * 13);
                    EcoManager.addMoney(p2, TienCuoc * 2);
                    Bukkit.broadcastMessage(SvMsg + "§e" + p2.getName() + " đã chiến thắng trong §aARENA §evà nhận §6"
                            + (TienCuoc * 2) + "$");
                    TienCuoc = 0.0D;
                    p2.teleport(locJoin.get(p2), TeleportCause.PLUGIN);
                    playerJoinArena.remove(p2);
                } else {
                    onCdJoin = System.currentTimeMillis() + (long) (600 * 13);
                    PlayerPoints.getInstance().getAPI().give(p2.getUniqueId(), TienCuoc.intValue() * 2);
                    Bukkit.broadcastMessage(SvMsg + "§e" + p2.getName() + " đã chiến thắng trong §aARENA §evà nhận §a"
                            + (TienCuoc * 2) + "Xu");
                    TienCuoc = 0.0D;
                    p2.teleport(locJoin.get(p2), TeleportCause.PLUGIN);
                    playerJoinArena.remove(p2);
                }
            }
            playerJoinArena.remove(p);
        }
    }

    @EventHandler
    public void onChatInArena(PlayerCommandPreprocessEvent event) {
        if (playerJoinArena.contains(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(SvMsg + "§cKhông thể sử dụng lệnh khi đang trong §aArena");
        }
    }

    static {
        GuiArena = Bukkit.createInventory(null, 27, "§a§lThe ARENA §7| §2Chọn loại tiền muốn cược");
        for (int i = 0; i < 9; i++) {
            GuiArena.setItem(i, OutLineGuiArena);
        }
        for (int i = 18; i < 27; i++) {
            GuiArena.setItem(i, OutLineGuiArena);
        }
        GuiArena.setItem(12, Gold);
        GuiArena.setItem(14, Balance);
    }

    @EventHandler
    public void onClickGui(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        if (event.getInventory().equals(GuiArena)) {
            if (event.getCurrentItem() != null) {
                if (SetGold.get(p) == null || !SetGold.get(p)) {
                    if (event.getCurrentItem().equals(Gold)) {
                        SetBalance.put(p, false);
                        p.sendMessage(SvMsg + "§6Cược Tiền §7- §eHãy nhập một số nguyên lớn 0, để thoát nhấn 'exit'");
                        SetGold.put(p, true);
                        p.closeInventory();
                    }
                    event.setCancelled(true);
                } else {
                    p.sendMessage(SvMsg + "§cLỗi, bạn chưa thoát lượt trước");
                    p.closeInventory();
                }
                if (SetBalance.get(p) == null || !SetBalance.get(p)) {
                    if (event.getCurrentItem().equals(Balance)) {
                        SetGold.put(p, false);
                        p.sendMessage(SvMsg + "§aCược Xu §7- §eHãy nhập một số nguyên lớn 0, để thoát nhấn 'exit'");
                        SetBalance.put(p, true);
                        p.closeInventory();
                    }
                    event.setCancelled(true);
                } else {
                    p.sendMessage(SvMsg + "§cLỗi, bạn chưa thoát lượt trước");
                    p.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onSetReward(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (SetGold.get(p) != null) {
            if (SetGold.get(p)) {
                event.setCancelled(true);
                if (event.getMessage().toLowerCase().equals("exit")) {
                    p.sendMessage("§7[§aArena§7] §7Đã thoát");
                    SetGold.put(p, false);
                    return;
                }
                try {
                    Integer.valueOf(event.getMessage());
                } catch (NumberFormatException e) {
                    event.getPlayer().sendMessage("§7[§aServer§7] §cHãy nhập một số lớn hơn 0, hoặc 'exit' để thoát");
                    return;
                }
                double Gold = Integer.parseInt(event.getMessage());
                if (Gold > 0) {
                    double GoldPlayer = EcoManager.getMoney(p);
                    if (GoldPlayer > Gold) {
                        if (!actionArena) {
                            SetGold.put(p, false);
                            TienCuoc = Gold;
                            onTeleportToTheArena(p);
                            EcoManager.takeMoney(p, TienCuoc);
                            Bukkit.broadcastMessage("§7§l§m----------------------------------------------------------");
                            Bukkit.broadcastMessage(
                                    "§6" + p.getDisplayName() + "§e Đã tham gia §aARENA§e với §6" + Gold + "$");
                            Bukkit.broadcastMessage(
                                    "§eĐể tham gia, sử dụng [/warp pvp] §eđể tìm hiểu");
                            Bukkit.broadcastMessage("§eNgười tham gia sẽ mất§6 " + Gold + "$");
                            Bukkit.broadcastMessage("§eChiến thắng để nhận§6 " + (Gold * 2) + "$");
                            Bukkit.broadcastMessage("§7§l§m----------------------------------------------------------");
                            TienCuocTrigger = false;
                            oncdQuit.put(p, System.currentTimeMillis() + (long) (600 * 70));
                            playerJoinArena.add(p);
                        }
                    } else {
                        p.sendMessage("§7[§aArena§7] §cBạn không đủ số Money để đặt cược: §6" + Gold + "xu");
                        SetGold.put(p, false);
                    }
                }
            }
        } if (SetBalance.get(p) != null) {
            if (SetBalance.get(p)) {
                event.setCancelled(true);
                if (event.getMessage().toLowerCase().equals("exit")) {
                    p.sendMessage("§7[§aArena§7] §7Đã thoát");
                    SetBalance.put(p, false);
                    return;
                }
                try {
                    Integer.valueOf(event.getMessage());
                } catch (NumberFormatException e) {
                    event.getPlayer().sendMessage("§7[§aServer§7] §cHãy nhập một số lớn hơn 0, hoặc 'exit' để thoát");
                    return;
                }

                int Money = Integer.parseInt(event.getMessage());
                if (Money > 0) {
                    PlayerPointsAPI api = PlayerPoints.getInstance().getAPI();
                    int MoneyPlayer = api.look(p.getUniqueId());
                    if (MoneyPlayer > Money) {
                        if (!actionArena) {
                            SetBalance.put(p, false);
                            TienCuoc = (double) Money;
                            Bukkit.getScheduler().runTaskLater(KnightBlock.pl, () -> {
                                api.take(p.getUniqueId(), Money);
                            }, 1);
                            onTeleportToTheArena(p);
                            Bukkit.broadcastMessage("§7§l§m----------------------------------------------------------");
                            Bukkit.broadcastMessage(
                                    "§6" + p.getDisplayName() + "§e Đã tham gia §aARENA§e với §a" + Money + "xu");
                            Bukkit.broadcastMessage(
                                    "§eĐể tham gia, sử dụng [/warp pvp] để tìm hiểu");
                            Bukkit.broadcastMessage("§eNgười tham gia sẽ mất§a " + Money + "xu");
                            Bukkit.broadcastMessage("§eChiến thắng để nhận§a " + (Money * 2) + "xu");
                            Bukkit.broadcastMessage("§7§l§m----------------------------------------------------------");
                            TienCuocTrigger = true;
                            playerJoinArena.add(p);
                            oncdQuit.put(p, System.currentTimeMillis() + (long) (600 * 70));
                        }
                    } else {
                        p.sendMessage("§7[§aArena§7] §cBạn không đủ số Xu để đặt cược: §a" + Money + "$");
                        SetBalance.put(p, false);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public void onTeleportToTheArena(Player p) {
        Bukkit.getScheduler().runTaskLater(KnightBlock.pl, () -> {
            KnightBlockAPI.setTimeLeft(p, "onJoinArena", 10);
            p.teleport(center.clone().add(0, 1, 0), TeleportCause.PLUGIN);
            p.playSound(center, Sound.ENTITY_PLAYER_LEVELUP, 1, 5);
        }, 0);
    }

    public Arena() {
        // TODO Auto-generated method stub
        FileConfiguration data = KnightBlock.arenaData.getConfigFile();
        center = data.getLocation("Arena.Center");
        Radius = data.getInt("Arena.Radius");
        hologram_center = data.getLocation("Arena.Hologram");
        if (center == null || Radius == 0 || hologram_center == null) return;
        (new BukkitRunnable() {
            public void run() {
                try {
                    List<Player> listPlayer = new ArrayList<>();
                    if (center.getWorld().getNearbyEntities(center, Radius, Radius, Radius).isEmpty()) {
                        PlayerCount = 0;
                    }
                    for (Entity e : center.getWorld().getNearbyEntities(center, Radius, Radius, Radius)) {
                        if (!e.isDead()) {
                            if (e instanceof Player) {
                                listPlayer.add((Player) e);
                                PlayerCount = listPlayer.size();
                                if (!playerJoinArena.contains((Player) e)) {
                                    Vector v1 = center.toVector();
                                    Vector v2 = e.getLocation().toVector();
                                    Vector v = v1.subtract(v2).normalize().multiply(-0.5);
                                    e.setVelocity(v);
                                    e.sendMessage("§7[§aServer§7] §cKhông thể vào §aARENA");
                                }
                            } else {
                                if (listPlayer.isEmpty()) {
                                    PlayerCount = 0;
                                    actionArena = false;
                                }
                            }
                        }
                    }
                    if (PlayerCount >= 1) {
                        actionArena = true;
                    }
                    if (PlayerCount == 0) {
                        actionArena = false;
                    }
                    for (Player pArena : playerJoinArena) {
                        if (!listPlayer.contains(pArena)) {
                            onTeleportToTheArena(pArena);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Line1 == null || Line1.isDead()) {
                    Entity holo = hologram_center.getWorld().spawnEntity(hologram_center.add(0, 0.9, 0), EntityType.ARMOR_STAND);
                    if (holo instanceof ArmorStand) {
                        ((ArmorStand) holo).setVisible(false);
                        ((ArmorStand) holo).setGravity(false);
                        ((ArmorStand) holo).setMarker(true);
                        holo.setCustomName("§7[§aTHE ARENA§7]");
                        holo.setCustomNameVisible(true);
                    }
                    Line1 = (ArmorStand) holo;
                }
                if (Line2 == null || Line2.isDead()) {
                    Entity holo = hologram_center.getWorld().spawnEntity(hologram_center.add(0, -0.4, 0), EntityType.ARMOR_STAND);
                    if (holo instanceof ArmorStand) {
                        ((ArmorStand) holo).setVisible(false);
                        ((ArmorStand) holo).setGravity(false);
                        ((ArmorStand) holo).setMarker(true);
                        holo.setCustomNameVisible(true);
                    }
                    Line2 = (ArmorStand) holo;
                }
                if (Line3 == null || Line3.isDead()) {
                    Entity holo = hologram_center.getWorld().spawnEntity(hologram_center.add(0, -0.25, 0), EntityType.ARMOR_STAND);
                    if (holo instanceof ArmorStand) {
                        ((ArmorStand) holo).setVisible(false);
                        ((ArmorStand) holo).setGravity(false);
                        ((ArmorStand) holo).setMarker(true);
                        holo.setCustomNameVisible(true);
                    }
                    Line3 = (ArmorStand) holo;
                    onHologram(Line2, Line3);
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 3);
    }

    public static void onHologram(ArmorStand line1, ArmorStand line2) {
        // TODO Auto-generated method stub
        (new BukkitRunnable() {
            public void run() {
                if (line1 != null && !line1.isDead()) {
                    if (playerJoinArena.size() > 1) {
                        line1.setCustomName("§7" + playerJoinArena.get(0).getName() + ": §c§l❤§e"
                                + (int) playerJoinArena.get(0).getHealth() + "§7, " + playerJoinArena.get(1).getName()
                                + ": §c§l❤§e" + (int) playerJoinArena.get(1).getHealth());
                        line1.setCustomNameVisible(true);
                    }
                    if (playerJoinArena.size() == 1) {
                        line1.setCustomName("§7" + playerJoinArena.get(0).getName() + ": §c§l❤§e"
                                + (int) playerJoinArena.get(0).getHealth());
                        line1.setCustomNameVisible(true);
                    }
                    if (playerJoinArena.isEmpty()) {
                        line1.setCustomName("§cTrống");
                        line1.setCustomNameVisible(true);
                    }
                }
                if (line2 != null && !line2.isDead()) {
                    if (TienCuoc == 0) {
                        line2.setCustomName("§7Tiền cược: §cTrống");
                    }
                    if (TienCuoc > 0) {
                        if (!TienCuocTrigger) {
                            line2.setCustomName("§7Tiền cược:§6 " + TienCuoc + "$");
                            line2.setCustomNameVisible(true);
                        } else {
                            line2.setCustomName("§7Tiền cược:§a " + TienCuoc + "Coints");
                            line2.setCustomNameVisible(true);
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 20);
    }

    static double getTimeLeftQuit(Player e) {
        if (!oncdQuit.containsKey(e)) {
            return 0.0D;
        } else {
            long now = System.currentTimeMillis();
            long cd = oncdQuit.get(e);
            if (now > cd) {
                return 0.0D;
            } else {
                double d = (double) (cd - now) / 20;
                return d;
            }
        }
    }

    static double getTimeLeftJoin() {
        if (onCdJoin == null) {
            return 0.0D;
        } else {
            long now = System.currentTimeMillis();
            long cd = onCdJoin;
            if (now > cd) {
                return 0.0D;
            } else {
                double d = (double) (cd - now) / 20;
                return d;
            }
        }
    }
}

