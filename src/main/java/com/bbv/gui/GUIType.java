package com.bbv.gui;

import com.bbv.knightblock.KnightBlock;

public enum GUIType {
    CHON_VAN_MINH(KnightBlock.toColor("&7[&8&lCHỌN NỀN VĂN HOÁ&7]"), 27),
    DOI_VAN_MINH(KnightBlock.toColor("&7[&8&lĐỔI NỀN VĂN HOÁ&7]"), 27),
    CHON_CLASS(KnightBlock.toColor("&7[&8&lCHỌN CLASS&7]"), 27),
    STATS_VIEW(KnightBlock.toColor("&7[&8&lTHUỘC TÍNH CƠ BẢN&7]"), 27),
    NANG_LEVEL_CULTURE(KnightBlock.toColor("&7[&8&lCHUYỂN HOÁ &a&lEXP&7]"), 54),
    CHI_TIET_TINH_LINH(KnightBlock.toColor("&7[&8&lCHI TIẾT TINH LINH&7]"), 27),
    TUYET_KY(KnightBlock.toColor("&7[&8&lKỸ NĂNG ĐẶC BIỆT&7]"), 54),
    CAN_CAU(KnightBlock.toColor("&7[&8&lĐIỀU CHỈNH CẦN CÂU&7]"), 27),
    UP_WEAPON(KnightBlock.toColor("&7[&8&lCƯỜNG HÓA VŨ KHÍ&7]"), 54),
    KHO_KHOANG_SAN(KnightBlock.toColor("&7[&8&lKHO KHOÁNG SẢN&7]"), 54),
    KHO_KHOANG_SAN_OTHER(KnightBlock.toColor("&7[&8&lKHO KHOÁNG SẢN NGƯỜI KHÁC&7]"), 54),
    TINH_LINH(KnightBlock.toColor("&7[&8&lTINH LINH&7]"), 54);
    private final String  Title;
    private final int  Size;
    private GUIType(String title, int size){
        this.Title =  title;
        this.Size = size;
    }

    public int getSize() {
        return Size;
    }

    public String getTitle() {
        return Title;
    }
}
