package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.ParticleManager;
import com.bbv.object.DamageType;
import com.bbv.object.PlayerStats;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.mcmonkey.sentinel.SentinelTrait;
import org.mcmonkey.sentinel.targeting.SentinelTarget;
import org.mcmonkey.sentinel.targeting.SentinelTargetList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SPhanThan extends KyNangHoatDong implements KyNangDacBiet {
    private boolean Active;
    private final LoaiKyNang loaiKyNang;
    private final UUID caster;
    private BukkitTask task;
    public static List<NPC> npcs = new ArrayList<>();

    public SPhanThan(UUID uuid) {
        this.loaiKyNang = LoaiKyNang.PHAN_THAN;
        this.caster = uuid;
        this.Active = false;
        this.task = null;
    }

    @Override
    public LoaiKyNang getType() {
        return loaiKyNang;
    }

    @Override
    public boolean cast() {
        Player p = getCaster();
        if (isActive()) {
            return false;
        }
        if (!KnightBlockAPI.getTimeLeftMess(p, loaiKyNang.name())) {
            return false;
        }
        KnightBlockAPI.setTimeLeft(p, loaiKyNang.name(), loaiKyNang.getCooldown());
        if (!KnightBlock.users.get(caster).consumeMana(loaiKyNang.getManaConsume())) {
            return false;
        }
        Active = true;
        addSkillActive(this);
        PlayerStats stats = KnightBlockAPI.getPlayerStats(p);
        for (int i = 0; i < 3; i++) {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(org.bukkit.entity.EntityType.PLAYER, KnightBlock.toColor("&ePhân Thân &7[" + p.getName() + "]"));
            npcs.add(npc);
            SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
            skinTrait.setSkinName(p.getName());
            npc.spawn(p.getLocation());
            npc.getEntity().setVelocity(p.getEyeLocation().getDirection().setY(0.2));
            ItemStack sword = new ItemStack(Material.IRON_SWORD); // Kiếm kim cương
            npc.getTrait(Equipment.class).set(0, sword);
            // Thêm Sentinel trait vào NPC để nó có thể tấn công
            SentinelTrait sentinel = npc.getOrAddTrait(SentinelTrait.class);
            sentinel.damage = stats.getSTVatLy(); // Tốc độ tấn công (ticks)
            sentinel.setHealth(p.getHealth() * 2); // Máu của NPC
            sentinel.deathXP = 0;
            sentinel.speed = 2;
            sentinel.addTarget("monsters");
            sentinel.addTarget("players");
            sentinel.addTarget("mobs");
        }
        (new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                count++;
                if (count > 20) {
                    for (NPC npc : npcs) {
                        npc.destroy();
                        this.cancel();
                        disable();
                    }
                } else {
                    for (NPC npc : npcs) {
                        if (npc != null && npc.getEntity() != null && !npc.getEntity().isDead()) {
                            for (Entity e : npc.getEntity().getNearbyEntities(2, 2, 2)) {
                                if (e instanceof LivingEntity && !(e.equals(p)) && !e.equals(npc.getEntity())) {
                                    ((LivingEntity) e).damage(stats.getSTVatLy(), p);
                                }
                            }
                        }
                    }
                }
            }
        }).runTaskTimer(KnightBlock.pl, 0, 20);
        return true;
    }


    @Override
    public Player getCaster() {
        return Bukkit.getPlayer(caster);
    }

    @Override
    public void disable() {
        Active = false;
        removeSkillActive(this);
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public boolean isActive() {
        return Active;
    }

    @Override
    public void setDamage(double damage) {

    }

    @Override
    public void setDamageType(DamageType type) {

    }
}
