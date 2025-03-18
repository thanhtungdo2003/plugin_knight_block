package com.bbv.object;

import com.bbv.blib.SimpleDatabaseManager;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.KnightBlock;
import com.bbv.skills.KyNangTinhLinh;
import com.bbv.stotage.TinhLinhData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TinhLinh implements Mana {

    public static String tinhLinhCase;
    private TinhLinhData tinhLinhData;
    private LoaiTinhLinh loaiTinhLinh;
    private int Level;
    private LivingEntity entity;
    private boolean onShow;
    private BukkitTask task;
    private BukkitTask taskMana = null;
    private LivingEntity target;
    private int Mana;
    private final UUID uuid;
    private Collection<Material> foods = new ArrayList<>();

    public TinhLinh(Player p) {
        KnightBlock.getPlayerStats(p).setTinhLinh(this);
        this.uuid = p.getUniqueId();
        this.tinhLinhData = new com.bbv.stotage.TinhLinh(p);
        this.loaiTinhLinh = tinhLinhData.getTypeActive();
        onShow = false;
        target = null;
        ChatColor color = ChatColor.of(new java.awt.Color(37, 225, 158, 218));
        tinhLinhCase = color + "&l\uD83D\uDC7B Tinh Linh";
        if (loaiTinhLinh != null) {
            this.Level = tinhLinhData.getLevel(loaiTinhLinh);
            this.Mana = tinhLinhData.getMana(loaiTinhLinh);
            Collection<Material> foods_ = new ArrayList<>();
            switch (loaiTinhLinh) {
                case HACAM:
                    foods_.add(Material.SOUL_SAND);
                    foods_.add(Material.SOUL_SOIL);
                    foods_.add(Material.MAGMA_CREAM);
                    break;
                case LUA:
                    foods_.add(Material.FIRE_CHARGE);
                    foods_.add(Material.LAVA_BUCKET);
                    foods_.add(Material.TORCH);
                    break;
                case NUOC:
                    foods_.add(Material.WATER_BUCKET);
                    foods_.add(Material.SWEET_BERRIES);
                    break;
                case DAT:
                    foods_.add(Material.DIRT);
                    foods_.add(Material.STONE);
                    foods_.add(Material.COBBLESTONE);
                    break;
                case SET:
                    foods_.add(Material.REDSTONE);
                    foods_.add(Material.REDSTONE_TORCH);
                    foods_.add(Material.GOLD_NUGGET);
                    break;
            }
            foods = foods_;
            if (this.loaiTinhLinh != LoaiTinhLinh.NONE && tinhLinhData.isActive(loaiTinhLinh)) {
                show();
            }
        } else {
            loaiTinhLinh = LoaiTinhLinh.NONE;
        }
    }

    public void show() {
        Player owner = Bukkit.getPlayer(uuid);
        if (KnightBlockAPI.getPlayerStats(owner) == null) return;
        TinhLinh activedTinhLinh = KnightBlockAPI.getPlayerStats(owner).getTinhLinh();
        if (activedTinhLinh == null)return;
        if (owner.isOnline()) {
            if (getLoaiTinhLinh().equals(LoaiTinhLinh.NONE)) {
                KnightBlock.messager.msg(owner, KnightBlock.toColor("&cBạn chưa có Tinh Linh nào"));
                return;
            }
            if (activedTinhLinh.task != null) {
                activedTinhLinh.task.cancel();
            }
            if (activedTinhLinh.entity != null) {
                activedTinhLinh.entity.remove();
            }
            activedTinhLinh.entity = null;
            activedTinhLinh.task = null;
            activedTinhLinh.onShow = true;
            if (activedTinhLinh.taskMana != null) {
                activedTinhLinh.taskMana.cancel();
                activedTinhLinh.taskMana = null;
            }
            setUpEntity();
            activedTinhLinh.task = (new BukkitRunnable() {
                @Override
                public void run() {
                    if (activedTinhLinh.entity == null) this.cancel();
                    if (!owner.isOnline()) {
                        this.cancel();
                        activedTinhLinh.hide(false, false);
                    } else {
                        if (!entity.isDead()) {
                            if (activedTinhLinh.getMana() <= 0) {
                                this.cancel();
                                activedTinhLinh.hide(false, true);
                                KnightBlock.messager.msg(owner, KnightBlock.toColor("&7Tinh linh đã hết năng lượng, cần nghỉ ngơi hoặc ăn :D"));
                                return;
                            }
                            if (activedTinhLinh.entity != null) {
                                if (!(activedTinhLinh.entity.getWorld().getName().equals(owner.getWorld().getName()))) {
                                    activedTinhLinh.entity.teleport(owner);
                                } else {
                                    if ((activedTinhLinh.entity.getLocation().distance(owner.getLocation()) > 6)) {
                                        Location l = owner.getEyeLocation();
                                        activedTinhLinh.entity.teleport(KnightBlockAPI.createRandomLocation(l, 2));
                                    }
                                }
                            }
                            activedTinhLinh.entity.getWorld().spawnParticle(Particle.SONIC_BOOM, activedTinhLinh.entity.getLocation(), 1);
                            if (activedTinhLinh.getLoaiTinhLinh().equals(LoaiTinhLinh.NUOC)) {
                                ((Allay) activedTinhLinh.entity).startDancing();
                            } else if (activedTinhLinh.getLoaiTinhLinh().equals(LoaiTinhLinh.GIO)) {
                                ((Allay) activedTinhLinh.entity).startDancing();
                            } else if (activedTinhLinh.getLoaiTinhLinh().equals(LoaiTinhLinh.SET)) {
                                ((Allay) activedTinhLinh.entity).startDancing();
                            } else if (activedTinhLinh.getLoaiTinhLinh().equals(LoaiTinhLinh.DAT)) {
                                ((Zoglin) activedTinhLinh.entity).setBaby(true);
                            }
                            if (activedTinhLinh.target != null && !activedTinhLinh.target.isDead()) {
                                if (!(activedTinhLinh.entity.getWorld().getName().equals(target.getWorld().getName()))) {
                                    activedTinhLinh.target = null;
                                } else {
                                    if ((activedTinhLinh.entity.getLocation().distance(owner.getLocation()) > 18)) {
                                        activedTinhLinh.target = null;
                                    }
                                }
                                KyNangTinhLinh.onSkill(owner, activedTinhLinh.target);
                            } else {
                                activedTinhLinh.target = null;
                                for (Entity nearbyEntity : owner.getNearbyEntities(20, 20, 20)) {
                                    if (nearbyEntity instanceof Mob) {
                                        Mob livingEntity = (Mob) nearbyEntity;
                                        if (livingEntity.getTarget() != null && livingEntity.getTarget().equals(owner)) {
                                            KyNangTinhLinh.onSkill(owner, livingEntity);
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            this.cancel();
                            activedTinhLinh.hide(false, false);
                        }
                    }
                }
            }).runTaskTimer(KnightBlock.pl, 0, 60);
        }
    }

    public Collection<Material> getFoods() {
        return foods;
    }

    private void setUpEntity() {
        Player owner = Bukkit.getPlayer(uuid);
        if (owner != null && owner.isOnline()) {
            entity = (LivingEntity) owner.getWorld().spawnEntity(KnightBlockAPI.createRandomLocation(owner.getEyeLocation(), 2), loaiTinhLinh.getEntityType());
            entity.setMetadata("NPC", new FixedMetadataValue(KnightBlock.pl, "tinhlinh"));
            entity.setCustomName(KnightBlock.toColor(tinhLinhCase + " " + getLoaiTinhLinh().getDisplayName() + " &7[" + owner.getName() + "]"));
            entity.setCustomNameVisible(true);
            entity.setCanPickupItems(false);
            entity.setInvulnerable(false);
            entity.setRemoveWhenFarAway(false);
            if (entity instanceof Allay) {
                ((Allay) entity).setCanDuplicate(false);
            } else if (entity instanceof MagmaCube) {
                ((MagmaCube) entity).setSize(1);
            } else if (entity instanceof Zoglin) {
                ((Zoglin) entity).setBaby();
            }
            if (getLoaiTinhLinh().equals(LoaiTinhLinh.SET)) {
                entity.getEquipment().setItemInMainHand(new ItemStack(Material.NETHER_STAR));
                entity.setGlowing(true);
            } else if (getLoaiTinhLinh().equals(LoaiTinhLinh.HACAM)) {
                entity.getEquipment().setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));
            } else if (getLoaiTinhLinh().equals(LoaiTinhLinh.NUOC)) {
                entity.getEquipment().setItemInMainHand(new ItemStack(Material.HEART_OF_THE_SEA));
            } else if (getLoaiTinhLinh().equals(LoaiTinhLinh.GIO)) {
                entity.setFireTicks(0);
            }
        }
    }

    public void hide(boolean sendMess, boolean hoiMana) {
        Player owner = Bukkit.getPlayer(uuid);
        TinhLinh activedTinhLinh = KnightBlockAPI.getPlayerStats(owner).getTinhLinh();
        if (!KnightBlockAPI.getPlayerStats(owner).isActivedTinhLinh()) {
            if (sendMess && owner != null && owner.isOnline()) {
                KnightBlock.messager.msg(owner, KnightBlock.toColor("&cKhông có Tinh Linh nào đang hoạt động"));
            }
        }
        if (activedTinhLinh.task != null) {
            activedTinhLinh.task.cancel();
        }
        if (activedTinhLinh.entity != null) {
            activedTinhLinh.entity.remove();
        }
        activedTinhLinh.entity = null;
        activedTinhLinh.task = null;
        activedTinhLinh.onShow = false;
        activedTinhLinh.tinhLinhData.setMana(activedTinhLinh.loaiTinhLinh, getMana());
        if (sendMess && owner != null && owner.isOnline()) {
            KnightBlock.messager.msg(owner, KnightBlock.toColor("&eTinh Linh của bạn đã ngừng hoạt động"));
        }
        if (hoiMana) {
            if (KnightBlock.pl.isEnabled()) {
                activedTinhLinh.taskMana = (new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (owner == null || !owner.isOnline()) {
                            this.cancel();
                        } else {
                            if (!activedTinhLinh.tinhLinhData.isActive(activedTinhLinh.loaiTinhLinh)) {
                                this.cancel();
                            } else {
                                if (activedTinhLinh.isShowing()) {
                                    this.cancel();
                                } else {
                                    if (activedTinhLinh.getMana() >= activedTinhLinh.getMax()) {
                                        this.cancel();
                                        if (activedTinhLinh.tinhLinhData.isActive(activedTinhLinh.loaiTinhLinh)) {
                                            activedTinhLinh.show();
                                            KnightBlock.messager.msg(owner, KnightBlock.toColor("&7Tinh Linh của bạn đã đủ năng lượng và hoạt động"));
                                        }
                                    } else {
                                        activedTinhLinh.ManaAdd(1);
                                    }
                                }
                            }
                        }
                    }
                }).runTaskTimer(KnightBlock.pl, 0, 20 * 5);
            }
        }
    }

    public boolean isShowing() {
        if (entity != null) {
            return onShow;
        }
        return false;
    }

    public Location getLocation() {
        if (onShow) {
            return entity.getLocation();
        }
        return null;
    }

    public int getLevel() {
        return Level;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LoaiTinhLinh getLoaiTinhLinh() {
        return loaiTinhLinh;
    }

    public Entity getEntity() {
        return entity;
    }

    private void sendUpdateSuccessfully(Player p, String display, double value, double valueAdd) {
        p.sendMessage("");
        KnightBlock.messager.msg(p, KnightBlock.toColor(display + " &f" + value + " &6--> &e" + valueAdd));
        p.sendMessage("");
    }

    private void sendSetSuccessfully(Player p, String display, double value) {
        p.sendMessage("");
        KnightBlock.messager.msg(p, KnightBlock.toColor(display + " &e--> &a" + KnightBlockAPI.decimalFormat(value, "#.##")));
        p.sendMessage("");

    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getMana() {
        return Mana;
    }

    public void setMana(int mana) {
        Mana = mana;
    }

    public void consumeMana(int value) {
        if ((getMana() - value) >= 0) {
            setMana(this.Mana - value);
        } else {
            setMana(0);
        }
    }

    public void ManaAdd(int value) {
        setMana(Math.min(this.Mana + value, this.loaiTinhLinh.getMaxMana()));
    }


    public void setLoaiTinhLinh(LoaiTinhLinh loaiTinhLinh) {
        this.loaiTinhLinh = loaiTinhLinh;
    }

    public void update() {
        Player owner = Bukkit.getPlayer(uuid);
        if (owner != null && owner.isOnline()) {
            if (getLevel() < 10) {
                PlayerStats stats = KnightBlock.getPlayerStats(owner);
                if (stats != null) {
                    if (stats.getCulture().getLevel() < getLevel() + 1) {
                        KnightBlock.messager.msg(owner, KnightBlock.toColor("&7Bạn cần nâng cấp độ của bản thân lên cấp " + stats.getCulture().getLevelDisplay(stats.getCulture().getLevel() + 1) + " &7để nâng cấp &bTinh Linh"));
                        return;
                    }
                }
                ItemStack linhThach = owner.getInventory().getItemInMainHand();
                if (linhThach.getItemMeta() != null && linhThach.getItemMeta().getLore() != null) {
                    ItemMeta im = linhThach.getItemMeta();
                    if (im.getDisplayName().contains(KnightBlock.toColor("&b&lLinh &f&lThạch &6("))) {
                        KnightBlockAPI.removeItemInHand(owner, 1);
                        int rate = im.getCustomModelData();
                        if (KnightBlockAPI.getRandom(rate, 100)) {
                            int value = getLevel() + 1;
                            tinhLinhData.setLevel(loaiTinhLinh, value);
                            sendUpdateSuccessfully(owner, "&dNâng cấp thành công", getLevel(), value);
                            setLevel(value);
                        } else {
                            if (getLevel() > 1) {
                                int value = getLevel() - 1;
                                tinhLinhData.setLevel(loaiTinhLinh, value);
                                setLevel(value);
                                KnightBlock.messager.msg(owner, KnightBlock.toColor("&cNâng cấp thất bại, Tinh Linh đã suy yếu (-1Lv) :("));
                            } else {
                                KnightBlock.messager.msg(owner, KnightBlock.toColor("&cNâng cấp thất bại!"));
                            }
                        }
                    }
                }
                owner.getWorld().spawnParticle(Particle.TOTEM, entity.getLocation(), 30);
                owner.playSound(entity.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.3F, 0F);
            } else {
                KnightBlock.messager.msg(owner, KnightBlock.toColor("&eĐã đạt giới hạn nâng cấp"));
            }
        }
    }

    public Player getOwner() {
        return Bukkit.getPlayer(uuid);
    }

    public void set(LoaiTinhLinh type) {
        tinhLinhData.give(type);
        KnightBlock.messager.msg(getOwner(), KnightBlock.toColor("&aBạn đã sở hữu &bTinh Linh " + type.getDisplayName()));
    }

    public TinhLinhData getTinhLinhData() {
        return tinhLinhData;
    }

    @Override
    public int getMax() {
        return getLoaiTinhLinh().getMaxMana();
    }

    @Override
    public int getNow() {
        return getMana();
    }

    @Override
    public void setMax(int value) {

    }

    @Override
    public void setNow(int value) {
        setMana(value);
    }

    @Override
    public void consume(int value) {
        consumeMana(value);
    }

    @Override
    public void add(int value) {
        ManaAdd(value);
    }
}
