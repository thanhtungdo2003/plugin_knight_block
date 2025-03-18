package com.bbv.object;


import com.bbv.blib.ConfigFile;
import com.bbv.knightblock.EcoManager;
import com.bbv.knightblock.KnightBlock;
import io.r2dbc.spi.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class KhoKhoangSan {
    private UUID uuid;
    private final String ownerName;
    private final HashMap<String, Tuple<Integer, Double>> items = new HashMap<>();
    private final List<String> trusters = new ArrayList<>();
    private final ConfigFile configFile;
    private int max;

    public KhoKhoangSan(Player p) {
        this.configFile = new ConfigFile(KnightBlock.pl, "KhoKhoangSan/" + p.getName() + ".yml");

        HashMap<String, Object> defaultData = new HashMap<>();
        defaultData.put("Owner", p.getName());
        defaultData.put("MaxAmount", 30000);
        defaultData.put("Items", Collections.singletonList(Material.COBBLESTONE.name() + " " + 0));
        defaultData.put("Truster", Collections.singletonList(""));
        configFile.setDefaultConfigurationFile(defaultData);

        String playerName = configFile.getConfigFile().getString("Owner");
        this.ownerName = playerName;
        this.max = configFile.getConfigFile().getInt("MaxAmount");
        if (playerName != null && Bukkit.getPlayer(playerName) != null) {
            this.uuid = Bukkit.getPlayer(playerName).getUniqueId();
        }
        for (String itemKey : configFile.getConfigFile().getStringList("Items")) {
            String[] itemKeySplit = itemKey.split(" ");
            Material type = Material.getMaterial(itemKeySplit[0]);
            double cost = 0D;
            switch (type) {
                case COBBLESTONE:
                    cost = 0.5;
                    break;
                case GOLD_INGOT:
                    cost = 9;
                    break;
                case IRON_INGOT:
                    cost = 3;
                    break;
                case COAL:
                case LAPIS_LAZULI:
                case REDSTONE:
                case QUARTZ:
                    cost = 1;
                    break;
                case DIAMOND:
                    cost = 17;
                    break;
                case EMERALD:
                    cost = 16;
                    break;
            }
            this.items.put(itemKeySplit[0], new Tuple<>(Integer.parseInt(itemKeySplit[1]), cost));
        }
        this.trusters.addAll(configFile.getConfigFile().getStringList("Truster"));
    }

    public UUID getUuid() {
        return uuid;
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public HashMap<String, Tuple<Integer, Double>> getItems() {
        return items;
    }

    public List<String> getTrusters() {
        return trusters;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Player getOwner() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean isTruster(Player p) {
        return (trusters.contains(p.getName()));
    }

    public int getAmountOfItem(Material type) {
        if (items.containsKey(type.name())) {
            return items.get(type.name()).Left();
        }
        return 0;
    }

    public boolean add(Material type, int amount) {
        Tuple<Integer, Double> tuple = items.get(type.name());
        if (tuple == null) {
            double cost = 0D;
            switch (type) {
                case COBBLESTONE:
                    cost = 0.5;
                    break;
                case GOLD_INGOT:
                    cost = 9;
                    break;
                case IRON_INGOT:
                    cost = 3;
                    break;
                case COAL:
                case LAPIS_LAZULI:
                case REDSTONE:
                case QUARTZ:
                    cost = 1;
                    break;
                case DIAMOND:
                    cost = 17;
                    break;
                case EMERALD:
                    cost = 16;
                    break;
            }
            items.put(type.name(), new Tuple<>(amount, cost));
        } else {
            int newAmount = tuple.Left() + amount;
            if (newAmount > max) return false;
            items.put(type.name(), new Tuple<>(newAmount, tuple.Right()));
        }
        return true;
    }

    public int getMax() {
        return max;
    }

    public boolean withdraw(Material type, int amount, Inventory inventory) {
        Tuple<Integer, Double> tuple = items.get(type.name());
        if (amount > tuple.Left()) return false;
        if (inventory.firstEmpty() == -1) return false;
        int newAmount = tuple.Left() - amount;
        items.put(type.name(), new Tuple<>(newAmount, tuple.Right()));
        ItemStack item = new ItemStack(type);
        item.setAmount(amount);
        inventory.addItem(item);
        return true;
    }

    public boolean withdrawAll(Material type, Inventory inventory) {
        if (inventory.firstEmpty() == -1) return false;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) {
                if (!withdraw(type, 64, inventory)) {
                    return false;
                }
            }
        }
        return true;
    }

    public double sell(Material type, int amount) {
        if (amount <= 0) return 0;
        Tuple<Integer, Double> tuple = items.get(type.name());
        if (amount > tuple.Left()) return 0;
        int newAmount = tuple.Left() - amount;
        items.put(type.name(), new Tuple<>(newAmount, tuple.Right()));
        EcoManager.addMoney(getOwner(), amount * tuple.Right());
        return amount * tuple.Right();
    }

    public double sellAll() {
        double total = 0;
        for (String type : items.keySet()) {
           total += sell(Material.getMaterial(type), items.get(type).Left());
        }
        return total;
    }

    public void save() {
        List<String> dataItems = new ArrayList<>();
        for (String key : items.keySet()) {
            dataItems.add(key + " " + items.get(key).Left());
        }
        configFile.setConfigurationFile("Items", dataItems);
        configFile.setConfigurationFile("MaxAmount", max);
        configFile.setConfigurationFile("Truster", trusters);
    }
}
