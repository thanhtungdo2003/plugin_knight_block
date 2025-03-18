package com.bbv.knightblock;

import com.bbv.blib.ItemStackManager;
import com.bbv.object.*;
import com.bbv.skills.*;
import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.*;

public class KnightBlockAPI {
    private static final HashMap<String, Long> onCd = new HashMap<>();
    private static final List<UUID> playerInWWVPs = new ArrayList<>();
    private static final List<UUID> playerInDungeons = new ArrayList<>();
    private static final List<UUID> playerInBypassAFKWorld = new ArrayList<>();
    private static final Map<Character, Integer> romanMap = new HashMap<>();

    static {
        romanMap.put('I', 1);
        romanMap.put('V', 5);
        romanMap.put('X', 10);
        romanMap.put('L', 50);
        romanMap.put('C', 100);
        romanMap.put('D', 500);
        romanMap.put('M', 1000);
    }

    public static String intToRoman(int num) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                result.append(symbols[i]);
            }
        }
        return result.toString();
    }

    public static int romanToInt(String s) {
        int result = 0;

        for (int i = 0; i < s.length(); i++) {
            int currentVal = romanMap.get(s.charAt(i));
            int nextVal = (i + 1 < s.length()) ? romanMap.get(s.charAt(i + 1)) : 0;

            if (currentVal < nextVal) {
                result += (nextVal - currentVal);
                i++;
            } else {
                result += currentVal;
            }
        }

        return result;
    }

    public static void castSkillMagicByCommand(Player p, String skillName) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "castp " + p.getName() + " " + skillName);
    }

    public static void castSkillMagicByCommand(Player p, String skillName, int cooldown) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "castp " + p.getName() + " " + skillName + " cooldown " + cooldown);
    }

    public static void castSkillMagicByCommand(Player p, String skillName, int cooldown, long duration) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "castp " + p.getName() + " " + skillName + " cooldown " + cooldown + " duration " + duration);
    }

    public static String getComboSkillChuDong(int level) {
        for (CapDoKyNangChuDong capDo : CapDoKyNangChuDong.values()) {
            if (capDo.getLevelCul() == level) {
                return capDo.getKey();
            }
        }
        return null; // Trả về null nếu không tìm thấy level tương ứng
    }

    public static String castSkillClass(int levelSkill, boolean cast, PlayerStats stats, ClassType classType) {
        KyNangDacBiet skill;
        switch (classType) {
            case SAT_THU:
                if (levelSkill == 1) {
                    skill = new SAmSat(stats.getUuid());
                    if (!cast) {
                        return skill.getType().getDisplayName();
                    }
                    if (skill.cast()) {
                        return skill.getType().getDisplayName();
                    }
                } else if (levelSkill == 3) {
                    skill = new SPhanThan(stats.getUuid());
                    if (!cast) {
                        return skill.getType().getDisplayName();
                    }
                    if (skill.cast()) {
                        return skill.getType().getDisplayName();
                    }
                }
                break;
            case DAU_SI:
                if (levelSkill == 1) {
                    skill = new SCanQuet(stats.getPlayer());
                    if (!cast) {
                        return skill.getType().getDisplayName();
                    }
                    skill.setDamage(stats.getSTVatLy());
                    if (skill.cast()) {
                        return skill.getType().getDisplayName();
                    }
                } else if (levelSkill == 3) {
                    skill = new SBaoKiem(stats.getPlayer());
                    if (!cast) {
                        return skill.getType().getDisplayName();
                    }
                    skill.setDamage(stats.getSTVatLy());
                    if (skill.cast()) {
                        return skill.getType().getDisplayName();
                    }
                }
                break;
            case DO_DON:
                if (levelSkill == 1) {
                    skill = new SThietThe(stats.getUuid());
                    if (!cast) {
                        return skill.getType().getDisplayName();
                    }
                    if (skill.cast()) {
                        return skill.getType().getDisplayName();
                    }
                } else if (levelSkill == 3) {
                    skill = new SBocPha(stats.getUuid());
                    if (!cast) {
                        return skill.getType().getDisplayName();
                    }
                    if (skill.cast()) {
                        return skill.getType().getDisplayName();
                    }
                }
                break;
            case PHAP_SU:
                if (levelSkill == 1) {
                    skill = new SChieuHon(stats.getUuid());
                    if (!cast) {
                        return skill.getType().getDisplayName();
                    }
                    if (skill.cast()) {
                        return skill.getType().getDisplayName();
                    }
                } else if (levelSkill == 3) {
                    skill = new SLinhTran(stats.getUuid());
                    if (!cast) {
                        return skill.getType().getDisplayName();
                    }
                    if (skill.cast()) {
                        return skill.getType().getDisplayName();
                    }
                }
                break;
            case XA_THU:
                if (levelSkill == 1) {
                    skill = new SThienKich(stats.getPlayer());
                    if (!cast) {
                        return skill.getType().getDisplayName();
                    }
                    if (skill.cast()) {
                        return skill.getType().getDisplayName();
                    }
                }
                if (levelSkill == 3) {
                    skill = new SNhatKich(stats.getPlayer());
                    if (!cast) {
                        return skill.getType().getDisplayName();
                    }
                    skill.setDamage(50);
                    skill.setDamageType(DamageType.CHUAN);
                    if (skill.cast()) {
                        return skill.getType().getDisplayName();
                    }
                }
                break;
        }
        return "";
    }

    public static ItemStack updateWeapon(Player player, ItemStack itemStack) {
        if (itemStack.getItemMeta() != null) {
            ItemMeta im = itemStack.getItemMeta();
            NBTItem nbtItem = NBTItem.get(itemStack);
            if (nbtItem.get("MMOITEMS_ATTACK_DAMAGE") == null) return itemStack;
            if (!im.getDisplayName().contains(KnightBlock.updateCaseLeft)) {
                im.setDisplayName(im.getDisplayName() + KnightBlock.updateCaseLeft + 1 + KnightBlock.updateCaseRight);
            } else {
                String[] nameSplit = im.getDisplayName().split("ᶱ");
                String levelRaw = nameSplit[1].replace(KnightBlock.toColor(" §x§F§F§E§C§A§F§l[§x§F§F§5§3§5§3§l+"), "");
                int newDisplayLevel = Integer.parseInt(levelRaw.replace(KnightBlock.updateCaseRight, "")) + 1;
                im.setDisplayName(KnightBlock.toColor(nameSplit[0] + "ᶱ" + " §x§F§F§E§C§A§F§l[§x§F§F§5§3§5§3§l+" + newDisplayLevel + KnightBlock.updateCaseRight));
            }
            double dataValue = nbtItem.getDouble("MMOITEMS_ATTACK_DAMAGE");
            double valueChance = (dataValue * 1.15);
            for (int i = 0; i < im.getLore().size(); i++) {
                if (im.getLore().get(i).contains("⚔") && im.getLore().get(i).contains("S") && im.getLore().get(i).contains("á")) {
                    List<String> lores = im.getLore();
                    if (!lores.get(i).contains(KnightBlock.toColor(" &7ᶱ &f[&e -> "))) {
                        lores.set(i, KnightBlockAPI.toColor(lores.get(i) + " &7ᶱ &f[&e -> " + KnightBlockAPI.decimalFormat((valueChance), "#.##") + "&f]"));
                    } else {
                        String[] loreSplit = lores.get(i).split("ᶱ");
                        lores.set(i, KnightBlock.toColor(loreSplit[0] + "ᶱ &f[&e -> " + KnightBlockAPI.decimalFormat((valueChance), "#.##") + "&f]"));
                    }
                    im.setLore(lores);
                    break;
                }
            }
            itemStack.setItemMeta(im);
            nbtItem = NBTItem.get(itemStack);
            double value = nbtItem.getDouble("MMOITEMS_ATTACK_DAMAGE");
            double newValue = (value * 1.15);
            nbtItem.addTag(new ItemTag("MMOITEMS_ATTACK_DAMAGE", newValue));
            return nbtItem.toItem();
        }
        return itemStack;
    }

    public static ItemStack breakWeapon(Player player, ItemStack itemStack) {
        if (itemStack.getItemMeta() != null) {
            NBTItem nbtItem = NBTItem.get(itemStack);
            if (nbtItem.get("MMOITEMS_ATTACK_DAMAGE") == null) return itemStack;
            double value = 0D;
            double valueChance = 0D;
            ItemMeta im = itemStack.getItemMeta();
            if (im.getDisplayName().contains(KnightBlock.updateCaseLeft)) {
                String[] nameSplit = im.getDisplayName().split("ᶱ");
                String levelRaw = nameSplit[1].replace(KnightBlock.toColor(" §x§F§F§E§C§A§F§l[§x§F§F§5§3§5§3§l+"), "");
                int newDisplayLevel = Integer.parseInt(levelRaw.replace(KnightBlock.updateCaseRight, "")) - 1;
                if (newDisplayLevel == 0) {
                    value = (nbtItem.getDouble("MMOITEMS_ATTACK_DAMAGE") / 1.15);
                    valueChance = value;
                    im.setDisplayName(im.getDisplayName().replace(KnightBlock.toColor(" §x§9§F§9§F§9§F§lᶱ" + nameSplit[1]), ""));
                } else {
                    double dataValue = nbtItem.getDouble("MMOITEMS_ATTACK_DAMAGE");
                    value = (dataValue / 1.15);
                    valueChance = value;
                    im.setDisplayName(KnightBlock.toColor(nameSplit[0] + "ᶱ" + " §x§F§F§E§C§A§F§l[§x§F§F§5§3§5§3§l+" + newDisplayLevel + KnightBlock.updateCaseRight));
                }
            } else {
                value = nbtItem.getDouble("MMOITEMS_ATTACK_DAMAGE");
                valueChance = value;
            }
            for (int i = 0; i < im.getLore().size(); i++) {
                if (im.getLore().get(i).contains("⚔") && im.getLore().get(i).contains("S") && im.getLore().get(i).contains("á")) {
                    List<String> lores = im.getLore();
                    if (!lores.get(i).contains(KnightBlock.toColor(" &7ᶱ &f[&e -> "))) {
                        lores.set(i, KnightBlockAPI.toColor(lores.get(i) + " &7ᶱ &f[&e -> " + KnightBlockAPI.decimalFormat((valueChance), "#.##") + "&f]"));
                    } else {
                        String[] loreSplit = lores.get(i).split("ᶱ");
                        lores.set(i, KnightBlock.toColor(loreSplit[0] + "ᶱ &f[&e -> " + KnightBlockAPI.decimalFormat((valueChance), "#.##") + "&f]"));
                    }
                    im.setLore(lores);
                    break;
                }
            }
            itemStack.setItemMeta(im);
            nbtItem = NBTItem.get(itemStack);
            nbtItem.addTag(new ItemTag("MMOITEMS_ATTACK_DAMAGE", value));
            return nbtItem.toItem();
        }
        return itemStack;
    }

    public static CheckWeaponType isWeaponClass(Player p, ItemStack itemStack) {
        Type type = MMOItems.getType(itemStack);
        PlayerStats stats = KnightBlock.getPlayerStats(p);
        if (type != null) {
            if (type.isWeapon()) {
                if (stats.getWeaponTypes() != null && KnightBlock.getPlayerStats(p).getWeaponTypes().contains(type)) {
                    return CheckWeaponType.TRUE;
                } else {
                    return CheckWeaponType.FALSE;
                }
            }
        }
        return CheckWeaponType.NOT;
    }

    // Hàm này tính tổng exp cần thiết để đạt một cấp độ cụ thể
    public static int getTotalExpForLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
    }


    public static PlayerStats getPlayerStats(Player p) {
        if (p != null && p.isOnline()) {
            UUID uuid = p.getUniqueId();
            if (KnightBlock.playerStats.containsKey(uuid)) {
                return KnightBlock.playerStats.get(uuid);
            }
            PlayerStats playerStats1 = new PlayerStats(p);
            KnightBlock.playerStats.put(uuid, playerStats1);
            return playerStats1;
        }
        return null;
    }

    public static String createScaleBar(char Char, ChatColor colorProgress, ChatColor colorBar, int Progress, int Max, int length) {
        double scale = Math.min((double) Progress / (double) (Max) * 100, 100.0);
        StringBuilder expBar = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if ((double) i / length * 100 <= scale) {
                expBar.append(colorProgress).append(Char);
            } else {
                expBar.append(colorBar).append(Char);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', expBar + " " + colorProgress + Progress + "&8/&7" + Max);
    }

    public static String createScaleBar(char Char, ChatColor colorProgress, ChatColor colorBar, double Progress, double Max, double length) {
        double scale = Math.min((double) Progress / (double) (Max) * 100, 100.0);
        StringBuilder expBar = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if ((double) i / length * 100 <= scale) {
                expBar.append(colorProgress).append(Char);
            } else {
                expBar.append(colorBar).append(Char);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', expBar + " " + colorProgress + Progress + "&8/&7" + Max);
    }

    public static String createScaleBarHideValue(char Char, ChatColor colorProgress, ChatColor colorBar, int Progress, int Max, int length) {
        double scale = Math.min((double) Progress / (double) (Max) * 100, 100.0);
        StringBuilder expBar = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if ((double) i / length * 100 <= scale) {
                expBar.append(colorProgress).append(Char);
            } else {
                expBar.append(colorBar).append(Char);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', expBar + " ");
    }

    public static String toColor(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String decimalFormat(double value, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(value);
    }

    public static boolean getRandom(int Rate, int Scale) {
        Random random = new Random();
        int var1 = random.nextInt(Scale);
        return var1 < Rate;
    }


    public static void removePlayer(Player p) {
        getPlayerStats(p).getTinhLinh().hide(false, false);
        KnightBlock.playerStats.remove(p.getUniqueId());
        KnightBlock.users.remove(p.getUniqueId());
    }

    public static Location createRandomLocation(Location baseLocation, int range) {
        double randomX = baseLocation.getX() + (Math.random() * (2 * range)) - range;
        double randomY = baseLocation.getY() + (Math.random() * (2 * range)) - range;
        double randomZ = baseLocation.getZ() + (Math.random() * (2 * range)) - range;
        return new Location(baseLocation.getWorld(), randomX, randomY, randomZ);
    }

    public static boolean getTimeLeftMess(LivingEntity p, String nameSkill) {
        double timel = TimeLeftSkill(p.getName() + nameSkill);
        if (timel < 1) {
            onCd.remove(p.getName() + nameSkill);
            return true;
        } else {
            KnightBlock.messager.msg(p, toColor("§cChưa hồi chiêu, còn §e" + (int) (timel) + "s"));
            return false;
        }
    }

    public static int getVipLevel(Player player) {
        int vipLevel = 0;
        for (int i = 1; i < 9; i++) {
            if (player.hasPermission("knightblock.vip" + i)) {
                vipLevel = i;
            }
        }
        return vipLevel;
    }

    public static void setUpPlayerInWorld(Player player) {
        String worldNameOfPlayer = player.getWorld().getName();
        if (KnightBlock.nameOfWhitelistWorldVIPPerm.contains(worldNameOfPlayer)) {
            if (!playerInWWVPs.contains(player.getUniqueId())) {
                playerInWWVPs.add(player.getUniqueId());
            }
        } else {
            playerInWWVPs.remove(player.getUniqueId());
        }
        if (KnightBlock.nameDungeonWorlds.contains(worldNameOfPlayer)) {
            if (!playerInDungeons.contains(player.getUniqueId())) {
                playerInDungeons.add(player.getUniqueId());
            }
        } else {
            playerInDungeons.remove(player.getUniqueId());
        }
        if (KnightBlock.nameByPassAntiAFKWorlds.contains(worldNameOfPlayer)) {
            if (!playerInBypassAFKWorld.contains(player.getUniqueId())) {
                playerInBypassAFKWorld.add(player.getUniqueId());
            }
        } else {
            playerInBypassAFKWorld.remove(player.getUniqueId());
        }
    }

    public static Boolean playerIsOnWOV(Player player) {
        return playerInWWVPs.contains(player.getUniqueId());
    }

    public static Boolean playerIsOnDungeon(Player player) {
        return playerInDungeons.contains(player.getUniqueId());
    }

    public static Boolean playerIsOnBypassAFKWorl(Player player) {
        return playerInBypassAFKWorld.contains(player.getUniqueId());
    }

    public static boolean getTimeLeft(Player p, String nameSkill, String message) {
        double timel = TimeLeftSkill(p.getName() + nameSkill);
        if (timel < 1) {
            onCd.remove(p.getName() + nameSkill);
            return true;
        } else {
            KnightBlock.messager.msg(p, toColor(message.replace("{time}", (int) (timel) + "")));
            return false;
        }
    }

    public static boolean getTimeLeft(Player p, String nameSkill) {
        double timel = TimeLeftSkill(p.getName() + nameSkill);
        return timel < 1;
    }

    public static void setTimeLeft(LivingEntity p, String nameSkill, long Timel) {
        onCd.put(p.getName() + nameSkill, System.currentTimeMillis() + (1000L * Timel));
    }

    private static long TimeLeftSkill(String s) {
        if (!onCd.containsKey(s)) {
            return 0;
        } else {
            long now = System.currentTimeMillis();
            long cd = onCd.get(s);
            if (now > cd) {
                onCd.remove(s);
                return 0;
            } else {
                return (cd - now) / 1000;
            }
        }
    }

    public static void removeItemInHand(Player p, int amount) {
        ItemStack i = p.getInventory().getItemInMainHand();
        int am = i.getAmount() - amount;
        if (am <= 0) {
            p.getInventory().setItemInMainHand(null);
        } else {
            i.setAmount(am);
            p.getInventory().setItemInMainHand(i);
        }
    }
    public static ItemStack removeItem(ItemStack i, int amount) {
        int am = i.getAmount() - amount;
        if (am <= 0) {
            return null;
        } else {
            i.setAmount(am);
            return i;
        }
    }

    public static void removeItemInOffHand(Player p, int amount) {
        ItemStack i = p.getInventory().getItemInOffHand();
        int am = i.getAmount() - amount;
        if (am <= 0) {
            p.getInventory().setItemInOffHand(null);
        } else {
            i.setAmount(am);
            p.getInventory().setItemInOffHand(i);
        }
    }

    public static void damageForLocation(LivingEntity damager, Location loc, DamageType damageType, double damage, double PhamVi, int fireTick) {
        for (Entity e : loc.getWorld().getNearbyEntities(loc, PhamVi, PhamVi, PhamVi)) {
            if (e instanceof LivingEntity && !e.equals(damager)) {
                switch (damageType) {
                    case VAT_LY:
                        ((LivingEntity) e).damage(damage, damager);
                        break;
                    case MAGIC:
                        satThuongPhep(damager, e, damage);
                        break;
                    case CHUAN:
                        satThuongChuan(damager, e, damage);
                        break;
                }
                e.setFireTicks(fireTick);
            }
        }
    }

    protected static void satThuongChuan(LivingEntity damager, Entity entity, double damage) {
        if (entity instanceof LivingEntity) {
            if (entity instanceof Player) {
                if (((Player) entity).getGameMode().equals(GameMode.CREATIVE)) return;
            }
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

    protected static void satThuongPhep(LivingEntity damager, Entity entity, double damage) {
        damage = damage / 1.2;
        if (entity instanceof Player) {
            PlayerStats stats = KnightBlockAPI.getPlayerStats((Player) entity);
            double chongChiu = stats.getChongChiu() / 2;
            damage -= chongChiu;
            stats.setMana(stats.getMana() + (0.3 * damage));
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
    }

    public static boolean isNPC(Entity e) {
        if (e.hasMetadata("NPC")) {
            return true;
        }
        return ShopkeepersAPI.getShopkeeperRegistry().isShopkeeper(e);
    }

    public static LivingEntity spawnMythicMob(String mobName, Location location) {
        MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(mobName).orElse(null);
        if (mob != null) {
            ActiveMob knight = mob.spawn(BukkitAdapter.adapt(location), 1);
            if (knight.getEntity().getBukkitEntity() instanceof LivingEntity) {
                return (LivingEntity) knight.getEntity().getBukkitEntity();
            }
        }
        return null;
    }

    public static String TimeFormat(Player p) {
        if (KnightBlock.TIMELEFT_BOOSTER.containsKey(p.getUniqueId())) {
            long seconds = KnightBlock.TIMELEFT_BOOSTER.get(p.getUniqueId()) / 20;
            long minutes = (seconds) / 60;
            long remainingSeconds = seconds % 60;
            return MessagerType.TIME_FORMAT.getContent().replace("{min}", minutes + "").replace("{sec}", remainingSeconds + "");
        }
        return "";
    }

    public static String TimeFormat(int time) {
        long seconds = time / 20;
        long minutes = (seconds) / 60;
        long remainingSeconds = seconds % 60;
        return MessagerType.TIME_FORMAT.getContent().replace("{min}", minutes + "").replace("{sec}", remainingSeconds + "");
    }

    public static String TimeLeftFormat(long value) {
        long seconds = value / 20;
        long minutes = (seconds) / 60;
        long remainingSeconds = seconds % 60;
        return MessagerType.TIME_FORMAT.getContent().replace("{min}", minutes + "").replace("{sec}", remainingSeconds + "");
    }

    public static int getIntegerByPDT(ItemMeta meta, String str_key) {
        NamespacedKey key = new NamespacedKey(KnightBlock.pl, str_key);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (data.has(key, PersistentDataType.INTEGER)) {
            return data.get(key, PersistentDataType.INTEGER);
        }
        return 0;
    }

    public static boolean hasCustomKey(ItemMeta meta, Plugin plugin, String str_key) {
        if (meta != null) {
            NamespacedKey key = new NamespacedKey(plugin, str_key);
            PersistentDataContainer data = meta.getPersistentDataContainer();
            return data.has(key, PersistentDataType.INTEGER);
        }
        return false;
    }

    public static int getLevelWeapon(ItemStack itemStack) {
        if (MMOItems.getType(itemStack) == null) return -1;
        NBTItem nbtItem = NBTItem.get(itemStack);
        if (nbtItem.get("MMOITEMS_ATTACK_DAMAGE") == null) return -1;
        if (itemStack.getItemMeta() != null) {
            String displayName = itemStack.getItemMeta().getDisplayName();
            if (displayName.contains(KnightBlock.updateCaseLeft)) {
                String[] displayNameSpit = itemStack.getItemMeta().getDisplayName().split(KnightBlock.toColor("ᶱ"));
                String raw1 = displayNameSpit[1].replace(KnightBlock.toColor(" §x§F§F§E§C§A§F§l[§x§F§F§5§3§5§3§l+"), "");
                String raw2 = raw1.replace(KnightBlock.updateCaseRight, "").replace(" ", "");
                return Integer.parseInt(raw2);
            }
            return 0;
        }
        return 0;
    }

    public static ItemStack daCuongHoa(int level) {
        return ItemStackManager.createIS("§x§F§F§A§D§1§AĐ§x§F§B§A§4§2§FÁ §x§F§4§9§3§5§8C§x§F§1§8§B§6§DƯ§x§E§D§8§2§8§2Ờ§x§E§A§7§A§9§7N§x§E§6§7§1§A§CG §x§D§F§6§0§D§5H§x§D§C§5§8§E§AÓ§x§D§8§4§F§F§FA &7[&eLevel &d" + level + "&7]", Material.AMETHYST_SHARD, Arrays.asList("&6Sử dụng để cường hóa vũ khí", "&6Sử dụng tại &a/kb cuonghoa"));
    }

    public static int getLevelDaCuongHoa(ItemStack itemStack) {
        if (!itemStack.getType().equals(Material.AMETHYST_SHARD)) return -1;
        if (itemStack.getItemMeta() == null) return -1;
        if (itemStack.getItemMeta().getDisplayName().contains(KnightBlock.toColor("§x§F§F§A§D§1§AĐ§x§F§B§A§4§2§FÁ §x§F§4§9§3§5§8C§x§F§1§8§B§6§DƯ§x§E§D§8§2§8§2Ờ§x§E§A§7§A§9§7N§x§E§6§7§1§A§CG §x§D§F§6§0§D§5H§x§D§C§5§8§E§AÓ§x§D§8§4§F§F§FA &7[&eLevel &d"))) {
            String raw = itemStack.getItemMeta().getDisplayName().replace(KnightBlock.toColor("§x§F§F§A§D§1§AĐ§x§F§B§A§4§2§FÁ §x§F§4§9§3§5§8C§x§F§1§8§B§6§DƯ§x§E§D§8§2§8§2Ờ§x§E§A§7§A§9§7N§x§E§6§7§1§A§CG §x§D§F§6§0§D§5H§x§D§C§5§8§E§AÓ§x§D§8§4§F§F§FA &7[&eLevel &d"), "");
            return Integer.parseInt(raw.replace(KnightBlock.toColor("&7]"), ""));
        }
        return -1;
    }

    public static int getBaitFishingRod(Player p, ItemStack itemStack) {
        if (itemStack.getItemMeta() != null) {
            String displayName = itemStack.getItemMeta().getDisplayName();
            if (displayName.contains(KnightBlock.nameCaseBaitFishsingRod)) {
                String[] displayNameSpit = itemStack.getItemMeta().getDisplayName().split(KnightBlock.toColor("Օ"));
                String raw1 = displayNameSpit[1].replace(KnightBlock.toColor("&8 &7[&9Mồi Cầu: &a"), "");
                String raw2 = raw1.replace(KnightBlock.toColor("&7]"), "").replace(" ", "");
                int baitAmount = Integer.parseInt(raw2);
                if (baitAmount == 0) {
                    KnightBlock.messager.msg(p, KnightBlock.toColor("&cCần câu của bạn chưa có mồi câu :-) &eCầm cần câu &a[Shift + Click phải] &eđể xem"));
                    return 0;
                }
                return baitAmount;
            }
            KnightBlock.messager.msg(p, KnightBlock.toColor("&cCần câu của bạn chưa có mồi câu :-) &eCầm cần câu &a[Shift + Click phải] &eđể xem"));
            return 0;
        }
        KnightBlock.messager.msg(p, KnightBlock.toColor("&cCần câu của bạn chưa có mồi câu :-) &eCầm cần câu &a[Shift + Click phải] &eđể xem"));
        return 0;
    }

    public static void setBaitFishingRod(Player p, int amount) {
        ItemStack itemStack = p.getInventory().getItemInMainHand();
        if (itemStack.getItemMeta().getDisplayName().contains(KnightBlock.nameCaseBaitFishsingRod)) {
            String[] displayName = itemStack.getItemMeta().getDisplayName().split(KnightBlock.toColor("Օ"));
            String newAmount = KnightBlock.toColor(KnightBlock.nameCaseBaitFishsingRod + amount + "&7]");
            ItemMeta im = itemStack.getItemMeta();
            im.setDisplayName(displayName[0] + newAmount);
            itemStack.setItemMeta(im);
        } else {
            String newAmount = KnightBlock.toColor(KnightBlock.nameCaseBaitFishsingRod + amount + "&7]");
            ItemMeta im = itemStack.getItemMeta();
            im.setDisplayName(im.getDisplayName() + newAmount);
            itemStack.setItemMeta(im);
        }
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public static boolean isPlayerOnSafeZone(Player player, Location locCheck) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(locCheck));
        return (set.queryState(localPlayer, Flags.PVP) == StateFlag.State.DENY);
    }
}
