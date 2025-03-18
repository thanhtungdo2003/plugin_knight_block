package com.bbv.object;

import com.bbv.blib.SimpleDatabaseManager;
import com.bbv.gui.CreateGUI;
import com.bbv.knightblock.KnightBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Culture {
    public static final String CULTURE_TABLE = "culture";
    private int Level;
    private String DisplayName;
    private CultureType Type;
    public static HashMap<UUID, BukkitTask> taskChoose = new HashMap<>();

    public Culture(Player p) {
        PlayerStats stats = KnightBlock.getPlayerStats(p);
        UUID uuid = p.getUniqueId();
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        if (databaseManager.getDataByKey(CULTURE_TABLE, "level", "uuid", uuid.toString()) == null) {
            Map<String, Object> dataStats = new HashMap<>();
            dataStats.put("uuid", uuid.toString());
            dataStats.put("type", "NONE");
            dataStats.put("level", 0);
            databaseManager.save(CULTURE_TABLE, dataStats);
        }
        this.Level = (int) databaseManager.getDataByKey(CULTURE_TABLE, "level", "uuid", uuid.toString());
        String typeName = (String) databaseManager.getDataByKey(CULTURE_TABLE, "type", "uuid", uuid.toString());
        this.Type = CultureType.valueOf(typeName);
        this.DisplayName = getType().getDisplayName();
        stats.setCulture(this);
        double scale = 0;
        for (int i = 1; i < getLevel(); i++) {
            scale += 0.3;
        }
        stats.setSTVatLy(stats.getSTVatLy() + getType().getDamage() + (getType().getDamage() * scale));
        stats.setChongChiu(stats.getChongChiu() + getType().getTank() + (getType().getTank() * scale));
        stats.setHP(stats.getHP() + getType().getHP() + (getType().getHP() * scale));
        stats.setNhanhNhen(stats.getNhanhNhen() + (getType().getSpeed() + (getType().getSpeed() * scale))*0.5);
        if (!stats.hasCulture()) {
            taskChoose.put(uuid, (new BukkitRunnable() {
                @Override
                public void run() {
                    if (!p.isOnline()) {
                        this.cancel();
                    }
                    if (!CreateGUI.guiUsing.containsKey(uuid)) {
                        CreateGUI.openChooseCultureGUI(p);
                    }
                }
            }).runTaskTimer(KnightBlock.pl, 0, 10));
        }
    }

    public static void createData() {
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        databaseManager.createSimpleTable(CULTURE_TABLE, Arrays.asList("uuid TEXT PRIMARY KEY", "level INT", "type TEXT"));
    }

    public CultureType getType() {
        return Type;
    }

    public void setType(CultureType type) {
        Type = type;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public int getLevel() {
        return Level;
    }

    public String getLevelDisplay(int level) {
        String str = "";
        switch (level) {
            case 0:
                str = "&7[⓿]";
                break;
            case 1:
                str = "&7[&a❶&7]";
                break;
            case 2:
                str = "&7[&3❷&7]";
                break;
            case 3:
                str = "&7[&e❸&7]";
                break;
            case 4:
                str = "&7[&6❹&7]";
                break;
            case 5:
                str = "&e[&c❺&e]";
                break;
            case 6:
                str = "&e[&d❻&e]";
                break;
            case 7:
                str = "&6[&a❼&6]";
                break;
            case 8:
                str = "&b《&e❽&b》";
                break;
            case 9:
                str = "&d《&e❾&d》";
                break;
            case 10:
                str = "&c&l《&6❿&c&l》";
                break;
        }
        return KnightBlock.toColor(str);
    }

    public void setLevel(int ver) {
        Level = ver;
    }

    public String getRankName() {
        return getType().getLevelName(getLevel());
    }


    @Override
    public String toString() {
        return "[" + getDisplayName() + ", " + getLevel() + ": " + getRankName() + "]";
    }
}
