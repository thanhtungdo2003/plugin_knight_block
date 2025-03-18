package com.bbv.object;

import com.bbv.knightblock.KnightBlock;


public enum CultureType {
    NONE(0, KnightBlock.toColor("&cX"), "", "", "", "", "", "", "", "", "", "", "", 0, 0, 0, 0),
    AI_CAP(1, KnightBlock.toColor("&bAi Cập"), KnightBlock.toColor(""),
            "&7Nô lệ", "&fKnight", "&2Sage", "&bSovereign", "&eEmperor"
            , "&6Pharaoh", "&cThần Horus", "&6&lThần Chiến Tranh", "&d&lThần Osiris", "&c&lThần &e&lMẶT TRỜI",
            12, 18, 20, 10),
    BAC_AU(2, KnightBlock.toColor("&fBắc Âu"), KnightBlock.toColor(""),
            "&7Gatherer", "&fViking", "&2Warrior", "&bCông Tước", "&eKing", "&6HighKing",
            "&cThần Heimdall", "&b&lThần Chiến Tranh", "&d&lThần &b&lThor", "&c&lThần &6&lOdin",
            15, 30, 10, 5),
    HY_LAP(3, KnightBlock.toColor("&2Hy Lạp"), KnightBlock.toColor(""), "&7Laborer", "&fAdventurer", "&3Chiến Binh",
            "&bStrategist", "&eHero", "&cThần Hephaestus", "&b&lThần Poseidon", "&e&lThần Apollo", "&c&lThần Athena", "&e&lThần &6&lZeus",
            20, 15, 10, 15),
    TRUNG_HOA(4, KnightBlock.toColor("&cTrung Hoa"), KnightBlock.toColor(""), "&7Vô Danh", "&2Thiếu Hiệp", "&3Đại Hiệp", "&aCao Thủ",
            "&cChiến Thần", "&6&lThần Long", "&b&lChân Tiên", "&e&lKim Tiên", "&6&lĐại La", "&d&lĐạo Tổ",
            30, 5, 10, 15),
    VIET_NAM(5, KnightBlock.toColor("&6Nam Quốc"), KnightBlock.toColor(""), "&7Nông Dân", "&fLính Quèn", "&bChiến Binh",
            "&eThủ Lĩnh", "&dAnh Hùng", "&a&lThánh Gióng", "&b&lThuỷ Tinh", "&2&lSơn Tinh", "&6&lHùng Vương", "&c&lLạc &6&lLong",
            25, 5, 5, 25);
    private final String DisplayName;
    private final String Description;
    private final int ID;
    private String LevelName1;
    private String LevelName2;
    private String LevelName3;
    private String LevelName4;
    private String LevelName5;
    private String LevelName6;
    private String LevelName7;
    private String LevelName8;
    private String LevelName9;
    private String LevelName10;
    private int Damage;
    private int Tank;
    private int HP;
    private int Speed;

    CultureType(int id, String displayName, String description, String levelName1, String levelName2, String levelName3, String levelName4, String levelName5, String levelName6, String levelName7, String levelName8, String levelName9, String levelName10,
                int dmg, int hp, int tank, int speed) {
        this.DisplayName = displayName;
        this.Description = description;
        this.ID = id;
        this.LevelName1 = levelName1;
        this.LevelName2 = levelName2;
        this.LevelName3 = levelName3;
        this.LevelName4 = levelName4;
        this.LevelName5 = levelName5;
        this.LevelName6 = levelName6;
        this.LevelName7 = levelName7;
        this.LevelName8 = levelName8;
        this.LevelName9 = levelName9;
        this.LevelName10 = levelName10;
        this.Damage = dmg;
        this.Tank = tank;
        this.HP = hp;
        this.Speed = speed;
    }

    public String getLevelName(int level) {
        switch (level) {
            case 1:
                return KnightBlock.toColor(LevelName1);
            case 2:
                return KnightBlock.toColor(LevelName2);
            case 3:
                return KnightBlock.toColor(LevelName3);
            case 4:
                return KnightBlock.toColor(LevelName4);
            case 5:
                return KnightBlock.toColor(LevelName5);
            case 6:
                return KnightBlock.toColor(LevelName6);
            case 7:
                return KnightBlock.toColor(LevelName7);
            case 8:
                return KnightBlock.toColor(LevelName8);
            case 9:
                return KnightBlock.toColor(LevelName9);
            case 10:
                return KnightBlock.toColor(LevelName10);
        }
        return KnightBlock.toColor("&7Chưa có");
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public String getDescription() {
        return Description;
    }

    public int getID() {
        return ID;
    }

    public int getDamage() {
        return Damage;
    }

    public int getTank() {
        return Tank;
    }


    public int getHP() {
        return HP;
    }

    public int getSpeed() {
        return Speed;
    }
}
