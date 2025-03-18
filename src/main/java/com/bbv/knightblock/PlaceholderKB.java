package com.bbv.knightblock;

import com.bbv.listener.PlayerListener;
import com.bbv.object.ClassType;
import com.bbv.object.LoaiTinhLinh;
import com.bbv.object.PlayerStats;
import com.bbv.object.TinhLinh;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.entity.Player;

public class PlaceholderKB extends PlaceholderExpansion {
    @Override
    public String onPlaceholderRequest(Player p, String params) {
        PlayerStats playerStats = KnightBlock.getPlayerStats(p);
        if (p == null) return "";
        if (playerStats == null) return "";
        switch (params) {
            case "culture":
                if (playerStats.hasCulture()) return playerStats.getCulture().getDisplayName();
                return "";
            case "culture_level":
                if (playerStats.hasCulture()) return playerStats.getCulture().getLevel() + "";
                return "0";
            case "culture_level_display":
                if (playerStats.hasCulture())
                    return playerStats.getCulture().getLevelDisplay(playerStats.getCulture().getLevel());
                return "0";
            case "culture_rank":
                if (playerStats.hasCulture()) return playerStats.getCulture().getRankName();
                return "";
            case "damage":
                return playerStats.getSTVatLy() + "";
            case "tank":
                return playerStats.getChongChiu() + "";
            case "hp":
                return playerStats.getHP() + "";
            case "speed":
                return playerStats.getNhanhNhen() + "";
            case "crit_rate":
                return playerStats.getCritRate() + "";
            case "exp":
                return EcoManager.formatNumber(p.getTotalExperience());
            case "total_exp":
                return p.getTotalExperience() + "";
            case "money":
                return EcoManager.formatNumber(EcoManager.getMoney(p));
            case "points":
                return PlayerPoints.getInstance().getAPI().look(p.getUniqueId()) + "";
            case "heal":
                return KnightBlockAPI.createScaleBar('♥', ChatColor.RED, ChatColor.DARK_GRAY, (int) p.getHealth(), (int) p.getMaxHealth(), 16);
            case "mana":
                AuraSkillsApi skillsApi = AuraSkillsApi.get();
                SkillsUser user = skillsApi.getUser(p.getUniqueId());
                return KnightBlockAPI.createScaleBar('⚡', ChatColor.AQUA, ChatColor.DARK_GRAY, (int) user.getMana(), (int) user.getMaxMana(), 16);
            case "tinhlinh":
                if (playerStats.getTinhLinh() != null)
                    return playerStats.getTinhLinh().getLoaiTinhLinh().getDisplayName();
                return "";
            case "tinhlinh_mana":
                if (playerStats.getTinhLinh() != null) {
                    TinhLinh tinhLinh = playerStats.getTinhLinh();
                    if (tinhLinh.isShowing()) {
                        return "&f &f &6NL: " + KnightBlockAPI.createScaleBar('❦', ChatColor.GREEN, ChatColor.DARK_GRAY, tinhLinh.getMana(), tinhLinh.getLoaiTinhLinh().getMaxMana(), 8);
                    } else if (tinhLinh.getMana() < tinhLinh.getMax()) {
                        return "&f &f &eĐang nghỉ... &7(&f" + tinhLinh.getMana() + "&7/&a" + tinhLinh.getMax() + "&7)";
                    } else if (tinhLinh.getMana() >= tinhLinh.getMax()) {
                        return "&f &f &6NL: " + KnightBlockAPI.createScaleBar('❦', ChatColor.GREEN, ChatColor.DARK_GRAY, tinhLinh.getMana(), tinhLinh.getLoaiTinhLinh().getMaxMana(), 8);
                    }
                }
                return "";
            case "tinhlinh_level":
                if (playerStats.getTinhLinh() != null) {
                    if (playerStats.getTinhLinh().getLoaiTinhLinh().equals(LoaiTinhLinh.NONE)) {
                        return "&flevel";
                    }
                    return playerStats.getTinhLinh().getLevel() + "";
                }
                return "";
            case "class":
                return playerStats.getClassType().getDisplayName();
            case "class_level":
                return p.getLevel() + "";
            case "class_dnt":
                String result = "";
                if (playerStats.getClassType().equals(ClassType.DAU_SI)) {
                    if (PlayerListener.diemNoiTai.containsKey(p.getUniqueId())) {
                        String[] strNoiTais = PlayerListener.diemNoiTai.get(p.getUniqueId()).split("%");
                        result = KnightBlock.toColor(ClassType.DAU_SI.getDisplayName() + ": &fĐiểm nội tại: &c" + strNoiTais[0] + "&f, Điểm hạ gục: &a" + strNoiTais[1]);
                    }
                }
                if (playerStats.getClassType().equals(ClassType.DO_DON)) {
                    if (PlayerListener.damageHapThu.containsKey(p.getUniqueId())) {
                        double damageHT = PlayerListener.damageHapThu.get(p.getUniqueId());
                        result = KnightBlock.toColor(ClassType.DO_DON.getDisplayName() + ": &fSát thương hấp thụ: &c" + KnightBlockAPI.decimalFormat(damageHT, "#.##") + " &fcộng dồn cho lần đánh tới");
                    }
                }
                if (playerStats.getClassType().equals(ClassType.XA_THU)) {
                    if (PlayerListener.dauAn_XaThu.containsKey(p.getUniqueId())) {
                        String[] strDauAn = PlayerListener.dauAn_XaThu.get(p.getUniqueId()).split("%");
                        int dauAn = Integer.parseInt(strDauAn[2]);
                        String targetName = strDauAn[0];
                        result = KnightBlock.toColor(ClassType.XA_THU.getDisplayName() + ": &fDấu ấn đã khắc lên &c" + targetName + "&f: &a" + dauAn);
                    }
                }
                return result;
            case "booster_amount":
                if (KnightBlock.BOOSTER.containsKey(p.getUniqueId())) {
                    return KnightBlock.BOOSTER.get(p.getUniqueId()).Left() + "";
                }
                return "0";
            case "booster_time":
                if (KnightBlock.BOOSTER.containsKey(p.getUniqueId())) {
                    return KnightBlockAPI.TimeFormat(p);
                }
                return "0";
            default:
                break;
        }
        return super.onPlaceholderRequest(p, params);
    }

    @Override
    public String getAuthor() {
        // TODO Auto-generated method stub
        return "_BBV_";
    }

    @Override
    public String getIdentifier() {
        // TODO Auto-generated method stub
        return "knightblock";
    }

    @Override
    public String getVersion() {
        // TODO Auto-generated method stub
        return null;

    }
}
