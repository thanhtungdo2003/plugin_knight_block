package com.bbv.skills;

import com.bbv.knightblock.KnightBlock;
public enum LoaiKyNang {
    AM_SAT("&cÁm Sát", 20, 15),
    CAN_QUET("&eCàn Quét", 20, 5),
    THIET_THE("&fThiết Thể", 20, 15),
    BOC_PHA("&cBộc Phá", 25, 25),
    LINH_TRAN("&dLinh Trận", 30, 20),
    BAO_KIEM("&fBão Kiếm", 40, 25),
    THIEN_KICH("&fThiên Kích", 20, 20),
    NHAT_KICH("&6Nhất Kích", 40, 20),
    PHAN_THAN("&bPhân Thân", 30, 35),
    CHIEU_HON("&8Chiều Hồn", 20, 8);
    private final String DisplayName;
    private final double ManaConsume;
    private final long Cooldown;

    private LoaiKyNang(String display, double manaConsume, int cooldown) {
        this.Cooldown = cooldown;
        this.DisplayName = display;
        this.ManaConsume = manaConsume;

    }

    public String getDisplayName() {
        return KnightBlock.toColor(DisplayName);
    }

    public double getManaConsume() {
        return ManaConsume;
    }

    public long getCooldown() {
        return Cooldown;
    }
}
