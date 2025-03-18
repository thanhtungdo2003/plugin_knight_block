package com.bbv.object;

import com.bbv.knightblock.EcoManager;
import com.bbv.knightblock.KnightBlock;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class Wanteds {
    public static HashMap<UUID, Wanteds> playerWanteds = new HashMap<>();
    private UUID uuid;
    private int bounty;
    private UUID caster;
    private int timerOut; //tick: 20=1s
    private BukkitTask task;
    private String reson;

    public Wanteds(Player caster, Player target, int bounty, String reson) {
        this.uuid = target.getUniqueId();
        this.caster = caster.getUniqueId();
        this.bounty = bounty;
        this.reson = reson;
        this.timerOut = 6000;
    }

    public void start() {
        playerWanteds.put(uuid, this);
        Player p = Bukkit.getPlayer(uuid);
        Player caster = Bukkit.getPlayer(this.caster);
        if (!EcoManager.takeMoney(caster, bounty)){
            KnightBlock.messager.msg(caster,KnightBlock.toColor("Bạn nghèo!"));
            return;
        }
        assert p != null;
        String namePlayer = p.getName();
        if (reson != null) {
            Bukkit.broadcastMessage(KnightBlock.toColor("&b" + caster.getName() + " &x&F&F&1&4&1&4&lĐ&x&F&F&1&B&1&B&lã &x&F&F&2&8&2&8&lp&x&F&F&2&F&2&F&lh&x&F&F&3&6&3&6&lá&x&F&F&3&C&3&C&lt &x&F&F&4&A&4&A&ll&x&F&F&5&0&5&0&lệ&x&F&F&5&7&5&7&ln&x&F&F&5&7&5&7&lh &x&F&F&5&7&5&7&lt&x&F&F&5&7&5&7&lr&x&F&F&5&7&5&7&lu&x&F&F&5&7&5&7&ly &x&F&F&5&7&5&7&ln&x&F&F&5&7&5&7&lã &e" + p.getName() + " &6&l(" + bounty + "$) &eVới lý do: &f"+reson));
        }else {
            Bukkit.broadcastMessage(KnightBlock.toColor("&b" + caster.getName() + " &x&F&F&1&4&1&4&lĐ&x&F&F&1&B&1&B&lã &x&F&F&2&8&2&8&lp&x&F&F&2&F&2&F&lh&x&F&F&3&6&3&6&lá&x&F&F&3&C&3&C&lt &x&F&F&4&A&4&A&ll&x&F&F&5&0&5&0&lệ&x&F&F&5&7&5&7&ln&x&F&F&5&7&5&7&lh &x&F&F&5&7&5&7&lt&x&F&F&5&7&5&7&lr&x&F&F&5&7&5&7&lu&x&F&F&5&7&5&7&ly &x&F&F&5&7&5&7&ln&x&F&F&5&7&5&7&lã &e" + p.getName() + " &6&l(" + bounty + "$)"));

        }
        task = (new BukkitRunnable() {
            @Override
            public void run() {
                playerWanteds.remove(uuid);
                Bukkit.broadcastMessage(KnightBlock.toColor("&x&1&F&F&B&0&8&lĐ&x&2&8&F&B&1&2&lã &x&3&B&F&B&2&5&lh&x&4&4&F&B&2&F&lế&x&4&E&F&C&3&9&lt &x&6&0&F&C&4&C&lt&x&6&A&F&C&5&6&lh&x&7&3&F&C&5&F&lờ&x&7&D&F&C&6&9&li &x&8&F&F&C&7&C&lg&x&9&9&F&D&8&6&li&x&A&2&F&D&9&0&la&x&A&B&F&D&9&A&ln &x&B&E&F&D&A&D&lt&x&B&7&F&9&A&4&lr&x&B&0&F&5&9&C&lu&x&A&9&F&0&9&3&ly &x&9&C&E&8&8&2&ln&x&9&5&E&4&7&A&lã &x&8&7&D&B&6&9&ln&x&8&0&D&7&6&0&lg&x&7&9&D&3&5&8&lư&x&7&2&C&E&4&F&lờ&x&6&B&C&A&4&7&li &x&5&E&C&2&3&6&lc&x&5&7&B&D&2&D&lh&x&5&0&B&9&2&5&lơ&x&4&9&B&5&1&C&li &6" + namePlayer));
                EcoManager.addMoney(caster, bounty);
                bounty = 0;
                this.cancel();
            }
        }).runTaskLater(KnightBlock.pl, timerOut);
    }

    public BukkitTask getTask() {
        return task;
    }

    public UUID getUuid() {
        return uuid;
    }

    public static HashMap<UUID, Wanteds> getPlayerWanteds() {
        return playerWanteds;
    }

    public int getBounty() {
        return bounty;
    }

    public int getTimerOut() {
        return timerOut;
    }

    public UUID getCaster() {
        return caster;
    }
}
