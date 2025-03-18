package com.bbv.knightblock;

import net.md_5.bungee.api.ChatColor;

public enum MessagerType {
    TIME_FORMAT("&a{min} &fPhút &a{sec} &fGiây"),
    GIVE_BOOST("&eBạn đã nhận &dBooster {type} &ax{amount} ({rate}%) &etrong thời gian {time}"),
    HAS_LEVEL_BOOSTER("&eBạn đang sở hữu &dBooster cấp độ cao hơn"),
    GIVE_INVICINBLE("&eĐã kích hoạt bảo vệ PVP trong thời gian &a{time}s &c&oBị giảm 50% sát thương"),
    NO_ENOUGH_ITEMS("&eBạn không có đủ &c{type} &eđể kích hoạt chế độ bảo vệ"),
    ON_VIP_FLY("§x§0§8§9§A§F§BĐ§x§1§0§9§C§F§Bã §x§1§F§A§1§F§Bb§x§2§6§A§3§F§Bậ§x§2§E§A§6§F§Bt §x§3§D§A§A§F§Cc§x§4§4§A§D§F§Ch§x§4§C§A§F§F§Cế §x§5§B§B§4§F§Cđ§x§6§2§B§6§F§Cộ §x§7§1§B§A§F§Cb§x§7§9§B§D§F§Ca§x§8§0§B§F§F§Cy §x§8§F§C§4§F§Dt§x§9§7§C§6§F§Dr§x§9§E§C§8§F§Do§x§A§6§C§B§F§Dn§x§A§D§C§D§F§Dg &a{time}"),
    CANCEL_VIP_FLY("§x§F§B§0§8§0§8Đ§x§F§B§0§C§0§9ã §x§F§C§1§5§0§Ck§x§F§C§1§A§0§Eế§x§F§C§1§E§0§Ft §x§F§C§2§7§1§2t§x§F§C§2§B§1§3h§x§F§D§2§F§1§5ú§x§F§D§3§4§1§6c §x§F§D§3§D§1§9t§x§F§D§4§1§1§Ah§x§F§D§4§5§1§Cờ§x§F§E§4§A§1§Di §x§F§E§5§2§2§0g§x§F§E§5§7§2§2i§x§F§E§5§B§2§3a§x§F§E§6§0§2§4n §x§F§F§6§8§2§7b§x§F§F§6§D§2§9a§x§F§F§7§1§2§Ay"),
    CANCEL_BOOST("&dBooster {type} &eđã hết thời gian");

    private final String content;

    private MessagerType(String content) {
        this.content = content;
    }

    public String getContent() {
        return ChatColor.translateAlternateColorCodes('&', content);
    }
}
