package com.bbv.object;

import com.bbv.blib.ConfigFile;
import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.MobExecutor;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoulSpawner implements Listener {
    private Location spawnPoint;
    private String id;
    private String title;
    private String mobName;
    private Tuple<String, Integer> childAmount;
    private int tempChildAmount;
    private BukkitTask task;
    private String displayChildName;
    private LivingEntity mythicMob;

    private ArmorStand amTitle;
    private ArmorStand amStatus;
    private ArmorStand amStatusMob;
    private MythicMob mythicChild;
    private List<Entity> listHologram = new ArrayList<>();
    private boolean isError = false;


    public SoulSpawner(String id) {
        try {
            MobExecutor mobExecutor = MythicBukkit.inst().getMobManager();
            this.id = id;
            ConfigFile configFile = new ConfigFile(KnightBlock.pl, "SoulSpawner.yml");
            FileConfiguration f = configFile.getConfigFile();
            this.title = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(f.getString(id + ".Title")));
            this.spawnPoint = f.getLocation(id + ".SpawnPoint").getBlock().getLocation().add(0.5, 0, 0.5);
            this.mobName = f.getString(id + ".MobName");
            String childName = f.getString(id + ".Bandit.Name");
            int childAmount = f.getInt(id + ".Bandit.Amount");
            this.childAmount = new Tuple<>(childName, childAmount);
            tempChildAmount = 0;
            if (mobExecutor.getMythicMob(childName).orElse(null).getDisplayName() != null) {
                displayChildName = mobExecutor.getMythicMob(childName).orElse(null).getDisplayName().toString();
            } else {
                displayChildName = mobExecutor.getMythicMob(childName).get().getInternalName();
            }
            mythicMob = null;
            mythicChild = mobExecutor.getMythicMob(childName).orElse(null);
        } catch (Exception ex) {
            System.out.println("Đã xảy ra lỗi khi tạo SoulSpawner từ file: " + ex.getMessage());
            isError = true;
        }
    }

    public MythicMob getMythicChild() {
        if (isError) return null;
        return mythicChild;
    }

    public ArmorStand getAmStatus() {
        return amStatus;
    }

    public BukkitTask getTask() {
        return task;
    }

    public ArmorStand getAmStatusMob() {
        return amStatusMob;
    }

    public ArmorStand getAmTitle() {
        return amTitle;
    }

    private ArmorStand createHologram(Location location) {
        if (isError) return null;
        ArmorStand am = (ArmorStand) spawnPoint.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        am.setVisible(false);
        am.setCustomName(title);
        am.setGravity(false);
        am.setSmall(true);
        am.setBasePlate(false);
        am.setCustomNameVisible(true);
        listHologram.add(am);
        return am;
    }

    public void active() {
        if (isError)return;
        World world = spawnPoint.getWorld();
        task = (new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity eInRadius : world.getNearbyEntities(spawnPoint, 20, 20, 20)) {
                    if (eInRadius instanceof Player && ((Player) eInRadius).isOnline()) {
                        if (amTitle != null) amTitle.remove();
                        if (amStatus != null) amStatus.remove();
                        if (amStatusMob != null) amStatusMob.remove();
                        listHologram.remove(amTitle);
                        listHologram.remove(amStatus);
                        listHologram.remove(amStatusMob);
                        amTitle = createHologram(spawnPoint.clone().add(0, 1, 0));
                        amStatus = createHologram(spawnPoint.clone().add(0, 0.5, 0));
                        amStatusMob = createHologram(spawnPoint.clone().add(0, 0, 0));
                        amStatus.setCustomName(KnightBlock.toColor("&eLinh hồn &a" + childAmount.Left().replace("_","") + " &eĐã hiến tế &a" + tempChildAmount + "&7/&9" + childAmount.Right()));
                        if (mythicMob != null && !mythicMob.isDead()) {
                            amStatusMob.setCustomName(KnightBlock.toColor("&aĐã xuất hiện, &eMáu: &c" + KnightBlockAPI.decimalFormat(mythicMob.getHealth(), "#.##") + "&7/&c" + mythicMob.getMaxHealth()));
                            if (mythicMob.getLocation().distance(spawnPoint) > 40) {
                                mythicMob.teleport(spawnPoint);
                            }
                        } else {
                            amStatusMob.setCustomName(KnightBlock.toColor("&c&oSắp xuất hiện..."));
                        }
                        for (Entity eRemove : world.getNearbyEntities(spawnPoint, 3, 3, 3)) {
                            if ((eRemove instanceof ArmorStand) && !listHologram.contains(eRemove)) {
                                eRemove.remove();
                            }
                        }
                        break;
                    }
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 50);
    }

    public LivingEntity getMob() {
        if (isError)return null;
        return mythicMob;
    }

    public void spawnMob() {
        if (isError)return;
        if (tempChildAmount >= childAmount.Right()) {
            tempChildAmount = 0;
            mythicMob = KnightBlockAPI.spawnMythicMob(mobName, spawnPoint);
            spawnPoint.getWorld().strikeLightningEffect(spawnPoint);
        }
    }

    public int getTempChildAmount() {
        return tempChildAmount;
    }

    public void setTempChildAmount(int tempChildAmount) {
        this.tempChildAmount = tempChildAmount;
    }

    public void addTempChildKilled(int valueAdd) {
        if (isError)return;
        this.tempChildAmount += valueAdd;
        if (amStatus != null && !amStatus.isDead()) {
            amStatus.setCustomName(KnightBlock.toColor("&eLinh hồn &a" + childAmount.Left().replace("_","") + " &eĐã hiến tế &a" + tempChildAmount + "&7/&9" + childAmount.Right()));
        }
    }

    public String getTitle() {
        return title;
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public String getId() {
        return id;
    }

    public String getMobName() {
        return mobName;
    }

    public Tuple<String, Integer> getChildAmount() {
        return childAmount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChildAmount(Tuple<String, Integer> childAmount) {
        this.childAmount = childAmount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMobName(String mobName) {
        this.mobName = mobName;
    }

    public void setSpawnPoint(Location spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    @EventHandler
    private void onAddSoul(MythicMobDeathEvent event) {
        if (isError) return;
        World world = spawnPoint.getWorld();
        if (event.getEntity().getWorld().equals(world)) {
            if (mythicMob == null || mythicMob.isDead()) {
                if (event.getMobType().equals(mythicChild)) {
                    List<Location> locationsEffect = new ArrayList<>();
                    Location eLoc = event.getEntity().getLocation();
                    Vector direction = spawnPoint.clone().add(0, 3, 0).toVector().subtract(eLoc.toVector()).normalize().multiply(0.5);
                    for (int i = 0; i < 20; i += 1) {
                        locationsEffect.add(eLoc.add(direction).clone());
                    }
                    (new BukkitRunnable() {
                        int count = 0;

                        @Override
                        public void run() {
                            count++;

                            if (count > 100) {
                                addTempChildKilled(1);
                                spawnMob();
                                this.cancel();
                            } else {
                                if (locationsEffect.size() > count) {
                                    world.spawnParticle(Particle.SOUL, locationsEffect.get(count), 0);
                                    world.spawnParticle(Particle.SOUL_FIRE_FLAME, locationsEffect.get(count), 1, 0.2, 0.2, -0.2, 0);
                                } else {
                                    addTempChildKilled(1);
                                    spawnMob();
                                    this.cancel();
                                }
                            }
                        }
                    }).runTaskTimer(KnightBlock.pl, 0, 0);
                }
            }
        }
    }

    @EventHandler
    private void onMobUnActive(MythicMobDeathEvent event) {
        if (isError)return;
        if (event.getMobType().equals(mythicMob)) {
            mythicMob = null;
        }
    }
}
