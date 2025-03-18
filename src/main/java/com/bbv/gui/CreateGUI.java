package com.bbv.gui;

import com.bbv.blib.ItemStackManager;
import com.bbv.knightblock.EcoManager;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.KnightBlock;
import com.bbv.object.*;
import com.bbv.stotage.TinhLinhData;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CreateGUI {
    public static HashMap<UUID, GUI> guiUsing = new HashMap<>();
    public static HashMap<UUID, Double> rateUpCul = new HashMap<>();
    public static HashMap<UUID, Integer> needEXP = new HashMap<>();
    public static HashMap<Integer, LoaiTinhLinh> btnToggleTinhLinhs = new HashMap<>();


    public static void openChooseCultureGUI(Player p) {
        GUI gui = new GUI(GUIType.CHON_VAN_MINH);
        ItemStack backGround = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta imOutline = backGround.getItemMeta();
        imOutline.setDisplayName("§f ");
        backGround.setItemMeta(imOutline);
        for (int i = 0; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        CultureType cultureAC = CultureType.AI_CAP;
        CultureType cultureBA = CultureType.BAC_AU;
        CultureType cultureVN = CultureType.VIET_NAM;
        CultureType cultureCN = CultureType.TRUNG_HOA;
        CultureType cultureHL = CultureType.HY_LAP;
        gui.getInv().setItem(9, ItemStackManager.createIS("&eNền văn hoá &9&lAi Cập &e&o-click để chọn", Material.CHISELED_SANDSTONE,
                writeLoreStatsCulture(cultureAC.getDamage(), cultureAC.getTank(), cultureAC.getHP(), cultureAC.getSpeed())));
        gui.getInv().setItem(11, ItemStackManager.createIS("&eNền văn hoá &f&lBắc Âu &e&o-click để chọn", Material.CHISELED_STONE_BRICKS,
                writeLoreStatsCulture(cultureBA.getDamage(), cultureBA.getTank(), cultureBA.getHP(), cultureBA.getSpeed())));
        gui.getInv().setItem(13, ItemStackManager.createIS("&eNền văn hoá &6&lÂu Lạc (Việt cổ) &e&o-click để chọn", Material.HAY_BLOCK,
                writeLoreStatsCulture(cultureVN.getDamage(), cultureVN.getTank(), cultureVN.getHP(), cultureVN.getSpeed())));
        gui.getInv().setItem(15, ItemStackManager.createIS("&eNền văn hoá &e&lHy Lạp &e&o-click để chọn", Material.GOLD_BLOCK,
                writeLoreStatsCulture(cultureHL.getDamage(), cultureHL.getTank(), cultureHL.getHP(), cultureHL.getSpeed())));
        gui.getInv().setItem(17, ItemStackManager.createIS("&eNền văn hoá &c&lTrung Hoa &e&o-click để chọn", Material.SPRUCE_PLANKS,
                writeLoreStatsCulture(cultureCN.getDamage(), cultureCN.getTank(), cultureCN.getHP(), cultureCN.getSpeed())));
        gui.show(p);
    }
    public static void openChangeCultureGUI(Player p) {
        GUI gui = new GUI(GUIType.DOI_VAN_MINH);
        ItemStack backGround = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta imOutline = backGround.getItemMeta();
        imOutline.setDisplayName("§f ");
        backGround.setItemMeta(imOutline);
        for (int i = 0; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        CultureType cultureAC = CultureType.AI_CAP;
        CultureType cultureBA = CultureType.BAC_AU;
        CultureType cultureVN = CultureType.VIET_NAM;
        CultureType cultureCN = CultureType.TRUNG_HOA;
        CultureType cultureHL = CultureType.HY_LAP;
        gui.getInv().setItem(9, ItemStackManager.createIS("&eNền văn hoá &9&lAi Cập &e&o-click để chọn", Material.CHISELED_SANDSTONE,
                writeLoreStatsCulture(cultureAC.getDamage(), cultureAC.getTank(), cultureAC.getHP(), cultureAC.getSpeed())));
        gui.getInv().setItem(11, ItemStackManager.createIS("&eNền văn hoá &f&lBắc Âu &e&o-click để chọn", Material.CHISELED_STONE_BRICKS,
                writeLoreStatsCulture(cultureBA.getDamage(), cultureBA.getTank(), cultureBA.getHP(), cultureBA.getSpeed())));
        gui.getInv().setItem(13, ItemStackManager.createIS("&eNền văn hoá &6&lÂu Lạc (Việt cổ) &e&o-click để chọn", Material.HAY_BLOCK,
                writeLoreStatsCulture(cultureVN.getDamage(), cultureVN.getTank(), cultureVN.getHP(), cultureVN.getSpeed())));
        gui.getInv().setItem(15, ItemStackManager.createIS("&eNền văn hoá &e&lHy Lạp &e&o-click để chọn", Material.GOLD_BLOCK,
                writeLoreStatsCulture(cultureHL.getDamage(), cultureHL.getTank(), cultureHL.getHP(), cultureHL.getSpeed())));
        gui.getInv().setItem(17, ItemStackManager.createIS("&eNền văn hoá &c&lTrung Hoa &e&o-click để chọn", Material.SPRUCE_PLANKS,
                writeLoreStatsCulture(cultureCN.getDamage(), cultureCN.getTank(), cultureCN.getHP(), cultureCN.getSpeed())));
        gui.show(p);
    }
    public static void openEditFishingRodGUI(Player p) {
        GUI gui = new GUI(GUIType.CAN_CAU);
        ItemStack backGround = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta imOutline = backGround.getItemMeta();
        imOutline.setDisplayName("§f ");
        backGround.setItemMeta(imOutline);
        ItemStack fence = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta fenceMeta = fence.getItemMeta();
        fenceMeta.setDisplayName("§6Mồi câu §e->");
        fence.setItemMeta(fenceMeta);
        ItemStack borderBait = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta borderBaitMeta = borderBait.getItemMeta();
        borderBaitMeta.setDisplayName("§aNơi đặt mồi câu");
        borderBait.setItemMeta(borderBaitMeta);
        for (int i = 0; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        int[] slotFences = {5, 14, 23};
        int[] bait_slots = {6, 7, 8, 15, 16, 17, 24, 25, 26};
        int[] border_bait = {3, 4, 5, 12, 14, 21, 22, 23};
        for (int slotFence : slotFences) {
            gui.getInv().setItem(slotFence, fence);
        }
        for (int slot : bait_slots) {
            gui.getInv().setItem(slot, null);
        }
        for (int slot : border_bait) {
            gui.getInv().setItem(slot, borderBait);
        }
        for (String type : KnightBlock.baitsFish) {
            ItemStack bait = new ItemStack(Objects.requireNonNull(Material.getMaterial(type)));
            ItemMeta im = bait.getItemMeta();
            im.setLore(Arrays.asList(KnightBlock.toColor("&eMồi câu"), KnightBlock.toColor("&eCá rất thích loại này")));
            bait.setItemMeta(im);
            gui.getInv().addItem(bait);
        }
        ItemStack fishsing_rod = new ItemStack(Material.FISHING_ROD);
        ItemMeta im = fishsing_rod.getItemMeta();
        im.setDisplayName(KnightBlock.toColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()));
        int baitAmount = KnightBlockAPI.getBaitFishingRod(p, p.getInventory().getItemInMainHand());
        im.setLore(Arrays.asList(KnightBlock.toColor("&eMồi câu đang có: &6" + baitAmount), KnightBlock.toColor("&eGiới hạn mồi câu: &c15")));
        fishsing_rod.setItemMeta(im);
        gui.getInv().setItem(10, fishsing_rod);
        ItemStack nullItem = new ItemStack(Material.BARRIER);
        ItemMeta imNull = nullItem.getItemMeta();
        imNull.setDisplayName(KnightBlock.toColor("&eClick chuột vào mồi câu trong kho đồ"));
        nullItem.setItemMeta(imNull);
        gui.getInv().setItem(13, nullItem);
        gui.setTag(baitAmount);
        gui.show(p);
    }

    public static void openTinhLinhGUI(Player p) {
        GUI gui = new GUI(GUIType.TINH_LINH);
        TinhLinhData data = KnightBlockAPI.getPlayerStats(p).getTinhLinh().getTinhLinhData();
        TinhLinh tinhLinh = KnightBlock.getPlayerStats(p).getTinhLinh();
        for (int i = 0; i < LoaiTinhLinh.values().length; i++) {
            LoaiTinhLinh loaiTinhLinh = LoaiTinhLinh.values()[i];
            String soHuu = "&cBạn chưa sở hữu Tinh Linh này";
            Material material = Material.ENDERMAN_SPAWN_EGG;
            if (loaiTinhLinh != LoaiTinhLinh.NONE) {
                if (data.hasTinhLinh(loaiTinhLinh)) {
                    material = Material.POLAR_BEAR_SPAWN_EGG;
                    soHuu = "&eTriệu Hồi &7(&aChuột trái&7)";
                    if (tinhLinh.getLoaiTinhLinh().equals(loaiTinhLinh)) {
                        if (data.isActive(loaiTinhLinh)) {
                            material = Material.ALLAY_SPAWN_EGG;
                            soHuu = "&6Thu Hồi &7(&aChuột trái&7)";
                        }
                    }
                }
                gui.getInv().setItem(0, ItemStackManager.createIS("&6Thu Hồi",
                        Material.BARRIER, Arrays.asList("&eThu hồi Tinh Linh đang hoạt động", "&7(&aClick&7)")));
                gui.getInv().setItem(i, ItemStackManager.createIS("&bTinh Linh " + loaiTinhLinh.getDisplayName(),
                        material, Arrays.asList("&7Level: &a" + data.getLevel(loaiTinhLinh), soHuu, "&6Xem chi tiết &7(&aChuột phải&7)")));
                btnToggleTinhLinhs.put(i, loaiTinhLinh);
            }
        }
        gui.show(p);
    }

    public static void openTuyetKyGUI(Player p) {
        GUI gui = new GUI(GUIType.TUYET_KY);
        ItemStack backGround = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta imOutline = backGround.getItemMeta();
        imOutline.setDisplayName("§f ");
        backGround.setItemMeta(imOutline);
        for (int i = 0; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        PlayerStats stats = KnightBlock.getPlayerStats(p);
        int level = stats.getCulture().getLevel();
        int[] levelLine = {0, 9, 18, 19, 20, 11, 2, 3, 4, 13, 22, 23, 24, 15, 6, 7, 8, 17, 26};
        int[] levelPoints = {0, 18, 20, 2, 4, 22, 24, 6, 8, 26};
        ItemStack nonLevelItem = new ItemStack(Material.BARRIER);
        ItemMeta nonLevelItemMeta = nonLevelItem.getItemMeta();
        nonLevelItemMeta.setDisplayName("§cChưa mở khóa");
        nonLevelItem.setItemMeta(nonLevelItemMeta);
        ItemStack line = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta lineMeta = line.getItemMeta();
        lineMeta.setDisplayName("§e-Kỹ Lộ-");
        line.setItemMeta(lineMeta);

        for (int j : levelLine) {
            gui.getInv().setItem(j, line);
        }
        int levels = 1;
        for (int i = 0; i < levelPoints.length; i++) {
            if (i <= level) {
                if ((levels % 2 != 0)) {
                    gui.getInv().setItem(levelPoints[i], ItemStackManager.createIS("&dCấp nền văn minh &e" + stats.getCulture().getLevelDisplay(levels), Material.LIME_STAINED_GLASS_PANE, Arrays.asList("&7- Kỹ năng chủ động: " + KnightBlockAPI.castSkillClass(levels, false, stats, stats.getClassType()), "&7- Combo (kích hoạt): " + KnightBlockAPI.getComboSkillChuDong(levels))));
                }
                levels++;
                continue;
            }
            gui.getInv().setItem(levelPoints[i], nonLevelItem);
        }
        gui.show(p);
    }

    public static void openCuongHoaGUI(Player p) {
        GUI gui = new GUI(GUIType.UP_WEAPON);
        ItemStack backGround = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta imOutline = backGround.getItemMeta();
        imOutline.setDisplayName("§f ");
        backGround.setItemMeta(imOutline);
        for (int i = 0; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        int[] gemSlots = {1, 2, 3, 10, 12, 19, 20, 21};
        int[] weaponSlots = {5, 6, 7, 14, 16, 23, 24, 25};
        ItemStack borderGem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta borderGemMeta = borderGem.getItemMeta();
        borderGemMeta.setDisplayName("§cNơi đặt đá cường hóa");
        borderGem.setItemMeta(borderGemMeta);
        ItemStack borderWeapon = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta borderWeaponMeta = borderWeapon.getItemMeta();
        borderWeaponMeta.setDisplayName("§aNơi đặt vũ khí");
        borderWeapon.setItemMeta(borderWeaponMeta);
        for (int j : gemSlots) {
            gui.getInv().setItem(j, borderGem);
        }
        for (int j : weaponSlots) {
            gui.getInv().setItem(j, borderWeapon);
        }
        gui.getInv().setItem(15, ItemStackManager.createIS("&cÔ đang trống", Material.BARRIER, Arrays.asList("&dClick vào vũ khí hoặc giáp trong kho", "&dđồ để chọn vật phẩm cần nâng cấp")));
        gui.getInv().setItem(11, ItemStackManager.createIS("&cÔ đang trống", Material.BARRIER, Arrays.asList("&dClick vào đá cường hóa trong kho", "&dđồ để chọn đá nâng cấp")));
        gui.getInv().setItem(40, ItemStackManager.createIS("&eTỷ lệ thành công: &a", Material.YELLOW_STAINED_GLASS_PANE));
        gui.getInv().setItem(38, ItemStackManager.createIS("&eCường hóa có bảo vệ", Material.BLUE_STAINED_GLASS_PANE, Arrays.asList("&6Giá &d30xu", "&aKhi thất bại sẽ không giảm cấp")));
        gui.getInv().setItem(42, ItemStackManager.createIS("&eCường hóa", Material.LIME_STAINED_GLASS_PANE, Arrays.asList("&6Giá &610.000$", "&cKhi thất bại sẽ giảm 1 cấp")));
        gui.setTag(new BanCuongHoa(p));
        gui.show(p);
    }


    public static void openKhoangSanGUI(Player p) {
        GUI gui = new GUI(GUIType.KHO_KHOANG_SAN);
        KhoKhoangSan khoKhoangSan = KnightBlock.khoKhoangSanHashMap.get(p.getUniqueId());
        ItemStack backGround = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta imOutline = backGround.getItemMeta();
        imOutline.setDisplayName("§f ");
        backGround.setItemMeta(imOutline);
        for (int i = 27; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        for (String type : khoKhoangSan.getItems().keySet()) {
            if (Material.getMaterial(type) != null) {
                gui.getInv().addItem(ItemStackManager.createIS("&e" + type, Material.getMaterial(type), Arrays.asList("&fĐang có &a" + khoKhoangSan.getItems().get(type).Left() + "&f/30000", "&fGiá bán &6" + khoKhoangSan.getItems().get(type).Right() + "$&e/1 cái", "", "&8(&aClick Trái&8) &7Để rút &a1", "&8(&aClick Phải&8) &7Để rút &a64", "&8(&aShift Click Trái&8) &7Để rút đầy kho đồ", "&8(&aShift Click Phải&8) &cĐể nhập số lượng muốn bán")));
            }
        }
        gui.getInv().setItem(37, ItemStackManager.createIS("&eGiới thiệu", Material.PAPER, Arrays.asList("&fLà tính năng thay thế cho rương", "&fMặc định sẽ có đá cuội, bạn", "&fCó thể đào các quặng khác để ", "&fHệ thống tự động thêm ô chứa")));
        gui.getInv().setItem(39, ItemStackManager.createIS("&aBán toàn bộ", Material.CAULDRON, Collections.singletonList("&8(&aShift + Chuột Trái&8)&7 Để xác nhận bán toàn bộ vật phẩm trong kho")));
        gui.show(p);
    }

    public static void openKhoKhoanSanOther(Player p, Player other) {
        GUI gui = new GUI(GUIType.KHO_KHOANG_SAN_OTHER);
        KhoKhoangSan khoKhoangSan = KnightBlock.khoKhoangSanHashMap.get(other.getUniqueId());
        ItemStack backGround = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta imOutline = backGround.getItemMeta();
        imOutline.setDisplayName("§f ");
        backGround.setItemMeta(imOutline);
        for (int i = 27; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        for (String type : khoKhoangSan.getItems().keySet()) {
            if (Material.getMaterial(type) != null) {
                gui.getInv().addItem(ItemStackManager.createIS("&e" + type, Material.getMaterial(type), Collections.singletonList("&fĐang có &a" + khoKhoangSan.getItems().get(type).Left() + "&f/30000")));
            }
        }
        gui.getInv().setItem(37, ItemStackManager.createIS("&eKho Khoáng Sản của " + other.getName(), Material.PLAYER_HEAD));

        gui.show(p);
    }

    public static void openChiTietTinhLinh(Player p, LoaiTinhLinh loaiTinhLinh) {
        GUI gui = new GUI(GUIType.CHI_TIET_TINH_LINH);
        TinhLinh tinhLinh = KnightBlock.getPlayerStats(p).getTinhLinh();
        TinhLinhData data = KnightBlockAPI.getPlayerStats(p).getTinhLinh().getTinhLinhData();
        int[] bolder = {5, 14, 23};
        ItemStack backGround = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta imOutline = backGround.getItemMeta();
        imOutline.setDisplayName("§f ");
        List<String> lores = new ArrayList<>();
        imOutline.setLore(lores);
        backGround.setItemMeta(imOutline);
        for (int i = 0; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        int[] contains = {6, 7, 8, 15, 16, 17, 24, 25, 26};
        for (int slotFoods : contains) {
            gui.getInv().setItem(slotFoods, null);
        }
        ItemStack backGround2 = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta imOutline2 = backGround2.getItemMeta();
        imOutline2.setDisplayName("§f ");
        List<String> lores2 = new ArrayList<>();
        lores2.add(KnightBlock.toColor("&7Thức ăn &e-->"));
        imOutline2.setLore(lores2);
        backGround2.setItemMeta(imOutline2);
        for (int slotBolder : bolder) {
            gui.getInv().setItem(slotBolder, backGround2);
        }
        List<String> loresTinhLinh = new ArrayList<>();
        switch (loaiTinhLinh) {
            case SET:
                gui.getInv().addItem(new ItemStack(Material.REDSTONE));
                gui.getInv().addItem(new ItemStack(Material.REDSTONE_TORCH));
                gui.getInv().addItem(new ItemStack(Material.GOLD_NUGGET));
                loresTinhLinh.add("&e⚡ &7Tinh Linh phóng một tia sét vào kẻ thù");
                loresTinhLinh.add(" &7gây sát thương, đồng thời lan đến 5 kẻ thù xung quanh");
                break;
            case HACAM:
                gui.getInv().addItem(new ItemStack(Material.SOUL_SAND));
                gui.getInv().addItem(new ItemStack(Material.SOUL_SOIL));
                gui.getInv().addItem(new ItemStack(Material.MAGMA_CREAM));
                loresTinhLinh.add("&e⚡ &7Tinh Linh triệu hồi 2-3 linh hồn Hắc Ám");
                loresTinhLinh.add(" &7Linh hồn nhắm và gây sát thương lên kẻ thù");
                break;
            case LUA:
                gui.getInv().addItem(new ItemStack(Material.LAVA_BUCKET));
                gui.getInv().addItem(new ItemStack(Material.FIRE_CHARGE));
                gui.getInv().addItem(new ItemStack(Material.TORCH));
                loresTinhLinh.add("&e⚡ &7Tinh Linh phóng một ngọn lửa bay theo mục tiêu");
                loresTinhLinh.add(" &7gây sát thương và cháy khi trúng mục tiêu");
                break;
            case DAT:
                gui.getInv().addItem(new ItemStack(Material.DIRT));
                gui.getInv().addItem(new ItemStack(Material.STONE));
                gui.getInv().addItem(new ItemStack(Material.COBBLESTONE));
                loresTinhLinh.add("&e⚡ &7Tinh Linh tạo một cột đá nhọn từ dưới");
                loresTinhLinh.add(" &7đất lên mục tiêu đồng thời hất tung");
                break;
            case NUOC:
                gui.getInv().addItem(new ItemStack(Material.WATER_BUCKET));
                gui.getInv().addItem(new ItemStack(Material.SWEET_BERRIES));
                loresTinhLinh.add("&e⚡ &7Tinh Linh phóng liên tiếp các tia nước");
                loresTinhLinh.add(" &7vào mục tiêu và gây sát thương lên chúng");
                break;
            case GIO:
                loresTinhLinh.add("&e⚡ &7Tinh Linh tạo một cơn gió mạnh lên kẻ thù");
                loresTinhLinh.add(" &7Gây sát thương đồng thời làm choáng đối phương");
                break;
        }
        loresTinhLinh.add("&bMana: &a" + data.getMana(loaiTinhLinh) + "/" + loaiTinhLinh.getMaxMana());
        gui.getInv().setItem(10, ItemStackManager.createIS("&bTinh Linh " + loaiTinhLinh.getDisplayName(),
                Material.ALLAY_SPAWN_EGG, loresTinhLinh));
        gui.getInv().setItem(18, ItemStackManager.createIS("&fID: " + loaiTinhLinh.name(),
                Material.GRAY_STAINED_GLASS_PANE));
        gui.getInv().setItem(22, ItemStackManager.createIS("&6Quay lại",
                Material.RED_STAINED_GLASS_PANE));
        if (data.hasTinhLinh(loaiTinhLinh)) {
            gui.getInv().setItem(12, ItemStackManager.createIS("&aĐã sở hữu",
                    Material.SLIME_BALL, Collections.singletonList("&7Level: &e" + data.getLevel(loaiTinhLinh))));
        } else {
            if (loaiTinhLinh.isSell()) {
                gui.getInv().setItem(12, ItemStackManager.createIS("&aNhấn để mua",
                        Material.NAME_TAG, Collections.singletonList("&6Giá: &e" + loaiTinhLinh.getPointsCost() + "xu")));
            } else {
                gui.getInv().setItem(12, ItemStackManager.createIS("&cKhông thể mua",
                        Material.BARRIER, Arrays.asList("&eCó thể kiếm được từ sự kiện", "&eHoặc làm nhiệm vụ")));
            }
        }
        gui.show(p);
    }

    public static void openStatsGUI(Player p) {
        GUI gui = new GUI(GUIType.STATS_VIEW);
        ItemStack backGround = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta imOutline = backGround.getItemMeta();
        imOutline.setDisplayName("§f ");
        backGround.setItemMeta(imOutline);
        for (int i = 0; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        PlayerStats stats = KnightBlock.getPlayerStats(p);
        gui.getInv().setItem(13, ItemStackManager.createIS("&e" + p.getName(), Material.PLAYER_HEAD,
                writeLoreStatsPlayer(stats, p.getLevel(), stats.getSTVatLy(), stats.getChongChiu(), stats.getHP(), stats.getNhanhNhen(), stats.getSTPhep(), stats.getCritRate())));
        gui.getInv().setItem(14, ItemStackManager.createIS("&f&lSức chống chịu", Material.WHITE_STAINED_GLASS_PANE,
                Arrays.asList("&f♦ Giá trị: &9" + KnightBlockAPI.decimalFormat(stats.getChongChiu(), "#.##"),
                        "", "&e● &7Tăng sức chịu sát thương của người chơi", " &7Có thể giảm sát thương vật lý và sát thương phép", "&e● &7Tiêu hao 5 thể lực")));
        gui.getInv().setItem(15, ItemStackManager.createIS("&c&lMáu", Material.PINK_STAINED_GLASS_PANE,
                Arrays.asList("&c❤ Giá trị: &9" + KnightBlockAPI.decimalFormat(stats.getHP(), "#.##"),
                        "", "&e● &7Tăng máu cho người chơi")));
        gui.getInv().setItem(16, ItemStackManager.createIS("&e&lNhanh nhẹn", Material.YELLOW_STAINED_GLASS_PANE,
                Arrays.asList("&c⚡ Giá trị: &9" + KnightBlockAPI.decimalFormat(stats.getNhanhNhen(), "#.##") + "%",
                        "", "&e● &7Tăng tốc độ cho người chơi, giới hạn 50%,", " &7Ngoài ra sự Nhanh Nhẹn cũng tăng tỷ lệ né đòn", " &7VD: giá trị = 5 => có 5% né đòn", "&e● &7Tiêu hao 10 thể lực")));
        gui.getInv().setItem(12, ItemStackManager.createIS("&6&lTỷ lệ siêu chí mạng", Material.ORANGE_STAINED_GLASS_PANE,
                Arrays.asList("&6× Giá trị: &9" + KnightBlockAPI.decimalFormat(stats.getCritRate(), "#.##") + "%",
                        "", "&e● &7Tỷ lệ gây siêu chí mạng (gấp đôi sát thương),", " &7Giới hạn 50%, &7Áp dụng trên cả sát thương phép", "&e● &7Tiêu hao 10 thể lực")));
        gui.getInv().setItem(11, ItemStackManager.createIS("&d&lSát thương phép", Material.MAGENTA_STAINED_GLASS_PANE,
                Arrays.asList("&d\uD83D\uDD25 Giá trị: &9" + KnightBlockAPI.decimalFormat(stats.getSTPhep(), "#.##"),
                        "", "&e● &7Loại sát thương dành cho pháp sư, hoặc vũ khí", " &7Thuộc loại phép, Sức chống chịu có thể giảm", " &7sát thuương phép, nhưng không nhiều")));
        gui.getInv().setItem(10, ItemStackManager.createIS("&c&lSát thương", Material.RED_STAINED_GLASS_PANE,
                Arrays.asList("&c⚔ Giá trị: &9" + KnightBlockAPI.decimalFormat(stats.getSTVatLy(), "#.##"),
                        "", "&e● &7Loại sát thương cơ bản, gia tăng cho mọi đòn đánh", " &7Ngoại trừ đòn từ vũ khí phép", "&e● &7Tiêu hao 5 thể lực")));
        gui.show(p);
    }

    public static void openChooseClassGUI(Player p) {
        GUI gui = new GUI(GUIType.CHON_CLASS);
        ItemStack backGround = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta imOutline = backGround.getItemMeta();
        imOutline.setDisplayName("§f ");
        backGround.setItemMeta(imOutline);
        for (int i = 0; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        ClassType classST = ClassType.SAT_THU;
        ClassType classDD = ClassType.DO_DON;
        ClassType classDS = ClassType.DAU_SI;
        ClassType classPS = ClassType.PHAP_SU;
        ClassType classXT = ClassType.XA_THU;
        HashMap<Enchantment, Integer> enchantmentIntegerHashMap = new HashMap<>();
        enchantmentIntegerHashMap.put(Enchantment.THORNS, 1);
        gui.getInv().setItem(9, ItemStackManager.createIS("&c&lSát thủ &e&o-click để chọn", Material.GOLDEN_SWORD,
                Arrays.asList("", "&a&l☣ &bNội tại &cCuồng Sát &a&l☣", "&e◆ &7Máu kẻ địch dưới 50% sẽ có 30% gây siêu chí mạng", "&e➥ &7Hồi chiêu: &a3s"
                        , "", "&e● &fSử dụng:", " &e- &7Dao găm, Kiếm, Súng, Katana, Lưỡi Hái"), 1, enchantmentIntegerHashMap, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS}));
        gui.getInv().setItem(11, ItemStackManager.createIS("&f&lĐấu Sĩ &e&o-click để chọn", Material.IRON_SWORD,
                Arrays.asList("", "&a&l☣ &bNội tại &eAnh Dũng &a&l☣", "&e◆ &7Tiêu diệt bất kì một sinh vật, đủ 10 mạng"
                        , " &7sẽ nhận 1 điểm nội tại, với mỗi điểm sẽ cộng", " &73% sát thương tay, giới hạn 30 điểm", "&e➥ &7Hồi chiêu: &a3s",
                        "", "&e● &fSử dụng:", " &e- &7Rìu, Giáo, Thương, Roi, Kích, Kiếm Dài"), 1, enchantmentIntegerHashMap, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS}));
        gui.getInv().setItem(13, ItemStackManager.createIS("&7&lĐỡ đòn &e&o-click để chọn", Material.IRON_AXE,
                Arrays.asList("", "&a&l☣ &bNội tại &cBất Tử &a&l☣", "&e◆ &7Có 15% chặn toàn bộ đòn tấn công của đối phương"
                        , " &7Sau khi chặn đòn, sẽ hấp thụ sát thương", " &7đòn đánh đó thành của mình, khi ra đòn", " &7Sẽ gây thêm sát thương đó một lần",
                        "&e◆ &7Khi máu dưới 20%, sẽ chuyển hoá toàn bộ năng", " &7lượng đang có thành số máu và bất tử 3s", "&e➥ &7Hồi chiêu: &a25s"
                        , "", "&e● &fSử dụng:", " &e- &7Khiên, Búa, Giáo, Kiếm Dài, Rìu"), 1, enchantmentIntegerHashMap, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS}));
        gui.getInv().setItem(15, ItemStackManager.createIS("&d&lPháp sư &e&o-click để chọn", Material.BLAZE_ROD,
                Arrays.asList("", "&a&l☣ &bNội tại &dBừng Tỉnh &a&l☣", "&e◆ &7Chuyển 30% sát thương gây lên kẻ địch thành"
                        , " &7Năng lượng của bản thân", "&e➥ &7Hồi chiêu: &a3s", "&e◆ &7Có 5% gây 30 sát thương chuẩn"
                        , "", "&e● &fSử dụng:", " &e- &7Staff, Wand, Găng Tay, Đàn, Các loại vũ khí phép"), 1, enchantmentIntegerHashMap, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS}));
        gui.getInv().setItem(17, ItemStackManager.createIS("&a&lXạ thủ &e&o-click để chọn", Material.BOW,
                Arrays.asList("", "&a&l☣ &bNội tại &cĐịnh Vị &a&l☣", "&e◆ &7Với mỗi lần gây sát thương cho kẻ địch"
                        , " &7từ xa sẽ khắc dấu ấn lên kẻ địch đó", " &7Cộng 0.5% kết liễu kẻ địch từ xa với mỗi dấu ấn", " &7Giới hạn 30 dấu ấn",
                        " &7Khi hạ gục kẻ định hoặc chuyển đối phương", " &7Sẽ mất các dấu ấn đã khắc", "&c*&7 Lưu ý: chỉ kết liễu được kẻ địch có máu dưới 30%", "&c* &7Phạm vi kích hoạt nội tại từ xa: &a13 block",
                        "&a* &7Các thuộc tính cơ bản sẽ áp dụng cho vật bắn", "&e➥ &7Hồi chiêu: &a120s"
                        , "", "&e● &fSử dụng:", " &e- &7Súng, Nỏ, Cung"), 1, enchantmentIntegerHashMap, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS}));
        gui.getInv().setItem(18, ItemStackManager.createIS("&eThuộc tính ban đầu: " + classST.getDisplayName(), Material.YELLOW_STAINED_GLASS_PANE,
                writeLoreStatsClass(classST.getDamage(), classST.getTank(), classST.getHp(), classST.getSpeed(), classST.getMagicDamage(), classST.getCritRate())));

        gui.getInv().setItem(20, ItemStackManager.createIS("&eThuộc tính ban đầu: " + classDS.getDisplayName(), Material.YELLOW_STAINED_GLASS_PANE,
                writeLoreStatsClass(classDS.getDamage(), classDS.getTank(), classDS.getHp(), classDS.getSpeed(), classDS.getMagicDamage(), classDS.getCritRate())));

        gui.getInv().setItem(22, ItemStackManager.createIS("&eThuộc tính ban đầu: " + classDD.getDisplayName(), Material.YELLOW_STAINED_GLASS_PANE,
                writeLoreStatsClass(classDD.getDamage(), classDD.getTank(), classDD.getHp(), classDD.getSpeed(), classDD.getMagicDamage(), classDD.getCritRate())));

        gui.getInv().setItem(24, ItemStackManager.createIS("&eThuộc tính ban đầu: " + classPS.getDisplayName(), Material.YELLOW_STAINED_GLASS_PANE,
                writeLoreStatsClass(classPS.getDamage(), classPS.getTank(), classPS.getHp(), classPS.getSpeed(), classPS.getMagicDamage(), classPS.getCritRate())));

        gui.getInv().setItem(26, ItemStackManager.createIS("&eThuộc tính ban đầu: " + classXT.getDisplayName(), Material.YELLOW_STAINED_GLASS_PANE,
                writeLoreStatsClass(classXT.getDamage(), classXT.getTank(), classXT.getHp(), classXT.getSpeed(), classXT.getMagicDamage(), classXT.getCritRate())));
        gui.show(p);
    }

    public static void openChooseUpdateGUI(Player p, ItemStack ngocNangCap) {
        GUI gui = new GUI(GUIType.NANG_LEVEL_CULTURE);
        ItemStack backGround = ItemStackManager.createIS("&f ", Material.GRAY_STAINED_GLASS_PANE);
        ItemStack borderItemEXP = ItemStackManager.createIS("&aĐiểm Kinh nghiệm", Material.GREEN_STAINED_GLASS_PANE);
        ItemStack borderItemNGOC = ItemStackManager.createIS("&dNgọc Chuyển Hoá", Material.MAGENTA_STAINED_GLASS_PANE);
        ItemStack borderItemRESULT = ItemStackManager.createIS("&eKết quả thành công", Material.YELLOW_STAINED_GLASS_PANE);
        for (int i = 0; i < gui.getInv().getSize(); i++) {
            gui.getInv().setItem(i, backGround);
        }
        int[] bordersEXP = {1, 2, 3, 10, 12, 19, 20, 21};
        int[] bordersNGOC = {5, 6, 7, 14, 16, 23, 24, 25};
        int[] bordersRESULT = {30, 31, 32, 39, 41, 48, 49, 50};
        for (int slot : bordersEXP) {
            gui.getInv().setItem(slot, borderItemEXP);
        }
        for (int slot : bordersNGOC) {
            gui.getInv().setItem(slot, borderItemNGOC);
        }
        for (int slot : bordersRESULT) {
            gui.getInv().setItem(slot, borderItemRESULT);
        }
        PlayerStats stats = KnightBlockAPI.getPlayerStats(p);
        int levelCul = stats.getCulture().getLevel();
        int needExp = 0;
        switch (levelCul) {
            case 1:
                needExp = 1000;
                break;
            case 2:
                needExp = 5500;
                break;
            case 3:
                needExp = 13000;
                break;
            case 4:
                needExp = 35000;
                break;
            case 5:
                needExp = 53000;
                break;
            case 6:
                needExp = 77000;
                break;
            case 7:
                needExp = 90000;
                break;
            case 8:
                needExp = 120000;
                break;
            case 9:
                needExp = 200000;
                break;
        }
        String xuLyLoreNangCap = ngocNangCap.getItemMeta().getLore().get(1).replace(KnightBlockAPI.toColor("&b»»» &6Cấp: &a"), "");
        int levelNgoc = Integer.parseInt(xuLyLoreNangCap);
        gui.getInv().setItem(15, ngocNangCap);
        gui.getInv().setItem(40, ItemStackManager.createIS(KnightBlockAPI.toColor("&e&lCấp tiếp theo"), Material.PLAYER_HEAD, writeLoreStatsCultureNextLevel(p)));
        gui.getInv().setItem(11, ItemStackManager.createIS(KnightBlockAPI.toColor("&aEXP sử dụng: &e" + EcoManager.formatNumber(needExp) + " &aexp"), Material.EXPERIENCE_BOTTLE));
        double successRate = calculateSuccessRate(levelCul, levelNgoc) * 100;
        if (p.getTotalExperience() >= needExp) {
            gui.getInv().setItem(43, ItemStackManager.createIS("&a&lXác nhận chuyển hoá", Material.LIME_STAINED_GLASS_PANE, Arrays.asList(KnightBlockAPI.toColor("&eTỷ lệ thành công:&a " + KnightBlockAPI.decimalFormat(successRate, "#.##") + "%"), KnightBlockAPI.toColor("&eChuyển hoá thất bại sẽ giảm 1 level"))));
            gui.getInv().setItem(37, ItemStackManager.createIS("&a&lChuyển hoá Bảo vệ cấp", Material.ORANGE_STAINED_GLASS_PANE, Arrays.asList(KnightBlockAPI.toColor("&eTỷ lệ thành công:&a " + KnightBlockAPI.decimalFormat(successRate, "#.##") + "%"), KnightBlockAPI.toColor("&eChi phí: &a50xu"))));
            needEXP.put(p.getUniqueId(), needExp);
        } else {
            gui.getInv().setItem(43, ItemStackManager.createIS("&a&lXác nhận chuyển hoá", Material.RED_STAINED_GLASS_PANE, Collections.singletonList(KnightBlockAPI.toColor("&cKhông đủ EXP &e(thiếu " + KnightBlockAPI.decimalFormat((needExp - p.getExp()), "#.##") + " exp)"))));
            gui.getInv().setItem(37, ItemStackManager.createIS("&a&lChuyển hoá Bảo vệ cấp", Material.RED_STAINED_GLASS_PANE, Collections.singletonList(KnightBlockAPI.toColor("&cKhông đủ EXP &e(thiếu " + KnightBlockAPI.decimalFormat((needExp - p.getExp()), "#.##") + " exp)"))));
        }
        gui.show(p);
        rateUpCul.put(p.getUniqueId(), successRate);
    }

    private static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    // Tính tỷ lệ thành công
    private static double calculateSuccessRate(int levelCul, int levelNgoc) {
        // Điều chỉnh levelCul để giảm ảnh hưởng của nó lên tỷ lệ thành công
        double adjustedLevelCul = levelCul * 0.7; // Có thể điều chỉnh hệ số 0.5 này theo nhu cầu

        // Tính tỷ lệ thành công dựa trên levelNgoc và adjustedLevelCul
        double rawSuccessRate = sigmoid(levelNgoc - adjustedLevelCul);

        // Đảm bảo tỷ lệ thành công nằm trong phạm vi từ 0 đến 1
        return Math.max(0, Math.min(1, rawSuccessRate));
    }

    private static List<String> writeLoreStatsCulture(int dmg, int tank, int hp, int speed) {
        return Arrays.asList(
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, dmg, 100, 25) + KnightBlock.toColor(" &4Sát thương"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, hp, 100, 25) + KnightBlock.toColor(" &cMáu"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, tank, 100, 25) + KnightBlock.toColor(" &7Chống chịu"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, speed, 100, 25) + KnightBlock.toColor(" &9Tốc độ"));
    }

    private static List<String> writeLoreStatsClass(int dmg, int tank, int hp, int speed, int magic, int crit) {
        return Arrays.asList(
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, dmg, 13, 25) + KnightBlock.toColor(" &4Sát thương"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, magic, 13, 25) + KnightBlock.toColor(" &dSát thương phép"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, crit, 13, 25) + KnightBlock.toColor(" &6Tỷ lệ siêu chí mạng"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, hp, 13, 25) + KnightBlock.toColor(" &cMáu"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, tank, 13, 25) + KnightBlock.toColor(" &7Chống chịu"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, speed, 13, 25) + KnightBlock.toColor(" &9Tốc độ"));
    }

    private static List<String> writeLoreStatsPlayer(PlayerStats stats, int level, double dmg, double tank, double hp, double speed, double magic, double crit) {

        return Arrays.asList(
                KnightBlock.toColor("&6Nền Văn Minh: " + stats.getCulture().getDisplayName()),
                KnightBlock.toColor("&6Cấp độ: " + stats.getCulture().getLevelDisplay(stats.getCulture().getLevel()) + " " + stats.getCulture().getRankName()),
                KnightBlock.toColor("&6Class: " + stats.getClassType().getDisplayName()),
                KnightBlock.toColor("&6Level: &a" + level), "",
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, dmg, 220, 25) + KnightBlock.toColor(" &4Sát thương"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, magic, 220, 25) + KnightBlock.toColor(" &dSát thương phép"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, crit, 60, 25) + KnightBlock.toColor(" &6Tỷ lệ siêu chí mạng"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, hp + 20, 220, 25) + KnightBlock.toColor(" &cMáu"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, tank, 220, 25) + KnightBlock.toColor(" &7Chống chịu"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, speed, 60, 25) + KnightBlock.toColor(" &9Tốc độ"));
    }

    private static List<String> writeLoreStatsCultureNextLevel(Player p) {
        PlayerStats stats = KnightBlockAPI.getPlayerStats(p);
        Culture culture = stats.getCulture();
        double scale = 0;
        for (int i = 1; i < culture.getLevel(); i++) {
            scale += 0.3;
        }
        double newScale = scale + 0.3;
        int dmg = culture.getType().getDamage();
        int tank = culture.getType().getTank();
        int hp = culture.getType().getHP();
        int speed = culture.getType().getSpeed();

        int oldDmg = (int) (dmg + (dmg * scale));
        int oldTank = (int) (tank + (tank * scale));
        int oldHp = (int) (hp + (hp * scale));
        int oldSpeed = (int) (speed + (speed * scale));

        int newDmg = (int) (dmg + (dmg * newScale));
        int newTank = (int) (tank + (tank * newScale));
        int newHp = (int) (hp + (hp * newScale));
        int newSpeed = (int) (speed + (speed * newScale));

        return Arrays.asList(
                "&eCấp hiện tại: " + culture.getRankName(), "",
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, oldDmg, 160, 25) + KnightBlock.toColor(" &4Sát thương"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, oldHp, 160, 25) + KnightBlock.toColor(" &cMáu"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, oldTank, 160, 25) + KnightBlock.toColor(" &7Chống chịu"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.GREEN, ChatColor.DARK_GRAY, oldSpeed, 160, 25) + KnightBlock.toColor(" &9Tốc độ"), "",
                "&eCấp tiếp theo: " + culture.getType().getLevelName(culture.getLevel() + 1) + "&a +(" + KnightBlockAPI.decimalFormat(newScale * 100, "#.##") + "% thuộc tính ban đầu)", "",
                KnightBlockAPI.createScaleBar('▇', ChatColor.YELLOW, ChatColor.DARK_GRAY, newDmg, 160, 25) + KnightBlock.toColor("&e +(" + (newDmg - oldDmg) + ")") + KnightBlock.toColor(" &4Sát thương"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.YELLOW, ChatColor.DARK_GRAY, newHp, 160, 25) + KnightBlock.toColor("&e +(" + (newHp - oldHp) + ")") + KnightBlock.toColor(" &cMáu"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.YELLOW, ChatColor.DARK_GRAY, newTank, 160, 25) + KnightBlock.toColor("&e +(" + (newTank - oldTank) + ")") + KnightBlock.toColor(" &7Chống chịu"),
                KnightBlockAPI.createScaleBar('▇', ChatColor.YELLOW, ChatColor.DARK_GRAY, newSpeed, 160, 25) + KnightBlock.toColor("&e +(" + (newSpeed - oldSpeed) + ")") + KnightBlock.toColor(" &9Tốc độ"));
    }
}
