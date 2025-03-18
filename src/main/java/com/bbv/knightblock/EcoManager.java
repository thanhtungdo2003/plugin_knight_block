package com.bbv.knightblock;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EcoManager {
    static Economy econ;

    static {
        econ = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
    }

    @SuppressWarnings("deprecation")
    public static double getMoney(Player player) {
        return econ.getBalance(player.getName());
    }

    public static boolean addMoney(Player player, double amount) {
        if (econ.hasAccount(player)) {
            return econ.depositPlayer(player, amount).transactionSuccess();
        }
        return false;
    }

    public static void onTakeMoney(Player player, double amount) {
        if (econ.hasAccount(player)) {
            econ.withdrawPlayer(player, amount).transactionSuccess();
        }
    }

    public static boolean takeMoney(Player p, double amount) {
        if ((getMoney(p) - amount) >= 0) {
            onTakeMoney(p, amount);
            return true;
        } else {
            KnightBlock.messager.msg(p, KnightBlock.toColor("&cBạn không có đủ &e" + amount + " &c$ để làm việc này"));
            return false;
        }
    }

    public static String formatNumber(double number) {
        if (number >= 1_000_000) {
            return KnightBlockAPI.decimalFormat(number / 1_000_000, "#.##") + "m";
        } else if (number >= 1_000) {
            return KnightBlockAPI.decimalFormat(number / 1_000, "#.##") + "k";
        } else {
            return String.valueOf(number);
        }
    }
}
