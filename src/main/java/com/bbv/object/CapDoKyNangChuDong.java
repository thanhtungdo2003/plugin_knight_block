package com.bbv.object;

public enum CapDoKyNangChuDong implements KeySkills {

    CAP_1(RIGHT + RIGHT, 1),
    CAP_2(RIGHT + LEFT, 3),
    CAP_3(LEFT + RIGHT, 5),
    CAP_4(RIGHT + LEFT + LEFT, 7),
    CAP_5(LEFT + LEFT + RIGHT, 9);
    private final String key;
    private final int levelCul;

    private CapDoKyNangChuDong(String key, int levelCul) {
        this.key = key;
        this.levelCul = levelCul;
    }

    public int getLevelCul() {
        return levelCul;
    }

    public String getKey() {
        return key;
    }

}
