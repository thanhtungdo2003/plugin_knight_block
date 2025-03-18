package com.bbv.object;

import com.bbv.blib.SimpleDatabaseManager;
import com.bbv.knightblock.KnightBlockAPI;
import com.bbv.knightblock.KnightBlock;
import dev.aurelium.auraskills.api.ability.Ability;
import dev.aurelium.auraskills.api.mana.ManaAbility;
import dev.aurelium.auraskills.api.skill.Skill;
import dev.aurelium.auraskills.api.stat.Stat;
import dev.aurelium.auraskills.api.stat.StatModifier;
import dev.aurelium.auraskills.api.trait.Trait;
import dev.aurelium.auraskills.api.trait.TraitModifier;
import dev.aurelium.auraskills.api.user.SkillsUser;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlayerStats implements SkillsUser {
    private final UUID uuidPlayer;
    public static final String STATS_TABLE = "Stats";
    private double STVatLy;
    private double STPhep;
    private double ChongChiu;
    private double HP;
    private double NhanhNhen;
    private double CritRate;
    private int level_class;
    private Culture culture;
    private TinhLinh tinhLinh;
    private ClassType classType;
    private final Set<Type> weaponTypes;

    public PlayerStats(Player p) {
        this.uuidPlayer = p.getUniqueId();
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        if (databaseManager.getDataByKey(STATS_TABLE, "dmg_vl", "uuid", getUuidPlayer().toString()) == null) {
            Map<String, Object> dataStats = new HashMap<>();
            dataStats.put("uuid", getUuidPlayer().toString());
            dataStats.put("dmg_vl", 0D);
            dataStats.put("dmg_magic", 0D);
            dataStats.put("tank", 0D);
            dataStats.put("hp", 0D);
            dataStats.put("nhanh_nhen", 0D);
            dataStats.put("crit_rate", 0D);
            dataStats.put("level_class", 0);
            dataStats.put("class_type", "NONE");
            databaseManager.save(STATS_TABLE, dataStats);
        }
        this.STVatLy = (double) databaseManager.getDataByKey(STATS_TABLE, "dmg_vl", "uuid", p.getUniqueId().toString());
        this.STPhep = (double) databaseManager.getDataByKey(STATS_TABLE, "dmg_magic", "uuid", p.getUniqueId().toString());
        this.ChongChiu = (double) databaseManager.getDataByKey(STATS_TABLE, "tank", "uuid", p.getUniqueId().toString());
        this.HP = (double) databaseManager.getDataByKey(STATS_TABLE, "hp", "uuid", p.getUniqueId().toString());
        this.NhanhNhen = (double) databaseManager.getDataByKey(STATS_TABLE, "nhanh_nhen", "uuid", p.getUniqueId().toString());
        this.CritRate = (double) databaseManager.getDataByKey(STATS_TABLE, "crit_rate", "uuid", p.getUniqueId().toString());
        this.classType = ClassType.valueOf((String) databaseManager.getDataByKey(STATS_TABLE, "class_type", "uuid", p.getUniqueId().toString()));
        this.level_class = (int) databaseManager.getDataByKey(STATS_TABLE, "level_class", "uuid", p.getUniqueId().toString());
        Set<Type> weaponTypesSet = new HashSet<>();
        switch (getClassType()) {
            case NONE:
                weaponTypesSet = null;
                break;
            case SAT_THU:
                weaponTypesSet.add(Type.SWORD);
                weaponTypesSet.add(Type.MUSKET);
                weaponTypesSet.add(Type.DAGGER);
                weaponTypesSet.add(Type.get("KATANA"));
                weaponTypesSet.add(Type.get("STAVE"));
                break;
            case DAU_SI:
                weaponTypesSet.add(Type.get("AXE"));
                weaponTypesSet.add(Type.SPEAR);
                weaponTypesSet.add(Type.WHIP);
                weaponTypesSet.add(Type.get("LANCE"));
                weaponTypesSet.add(Type.get("HALBERD"));
                weaponTypesSet.add(Type.get("LONG_SWORD"));
                break;
            case PHAP_SU:
                weaponTypesSet.add(Type.STAFF);
                weaponTypesSet.add(Type.GAUNTLET);
                weaponTypesSet.add(Type.LUTE);
                weaponTypesSet.add(Type.get("WAND"));
                weaponTypesSet.add(Type.get("GREATSTAFF"));
                weaponTypesSet.add(Type.get("GREATAXE"));
                weaponTypesSet.add(Type.get("GREATSWORD"));
                weaponTypesSet.add(Type.get("GREATBOW"));
                weaponTypesSet.add(Type.get("GREATHAMER"));
                break;
            case XA_THU:
                weaponTypesSet.add(Type.BOW);
                weaponTypesSet.add(Type.CROSSBOW);
                weaponTypesSet.add(Type.MUSKET);
                break;
            case DO_DON:
                weaponTypesSet.add(Type.HAMMER);
                weaponTypesSet.add(Type.get("AXE"));
                weaponTypesSet.add(Type.SPEAR);
                weaponTypesSet.add(Type.get("SHIELD"));
                weaponTypesSet.add(Type.get("LONG_SWORD"));
                break;
        }
        double valueAdd = p.getLevel() * 0.025;
        this.weaponTypes = weaponTypesSet;
        setSTVatLy(getSTVatLy() + getClassType().getDamage() * valueAdd);
        setChongChiu(getChongChiu() + getClassType().getTank() * valueAdd);
        setHP(getHP() + getClassType().getHp() * valueAdd);
        setNhanhNhen(getNhanhNhen() + (getClassType().getSpeed() * valueAdd));
        setCritRate(getCritRate() + (getClassType().getCritRate() * valueAdd));
        setSTPhep(getSTPhep() + (getClassType().getMagicDamage() * valueAdd));
        this.tinhLinh = null;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public Set<Type> getWeaponTypes() {
        return weaponTypes;
    }

    public void setTinhLinh(TinhLinh tinhLinh) {
        this.tinhLinh = tinhLinh;
    }

    public boolean isActivedTinhLinh() {
        return tinhLinh.isShowing();
    }

    public TinhLinh getTinhLinh() {
        if (tinhLinh == null) return new TinhLinh(getPlayer());
        return tinhLinh;
    }

    public double getCritRate() {
        return CritRate;
    }

    public double getSTPhep() {
        return STPhep;
    }

    public void setSTPhep(double STPhep) {
        this.STPhep = STPhep;
    }

    public int getLevel_class() {
        return level_class;
    }

    public void setLevel_class(int level_class) {
        this.level_class = level_class;
    }

    public void setCritRate(double critRate) {
        if (critRate >= 50) {
            critRate = 50;
        }
        CritRate = critRate;
    }

    public void setCulture(Culture culture) {
        this.culture = culture;
    }

    public double getChongChiu() {
        return ChongChiu;
    }

    public double getHP() {
        return HP;
    }

    public double getNhanhNhen() {
        return NhanhNhen;
    }

    public double getSTVatLy() {
        return STVatLy;
    }

    public void setChongChiu(double chongChiu) {
        ChongChiu = chongChiu;
    }

    public void setHP(double HP) {
        this.HP = HP;
    }

    public void setNhanhNhen(double nhanhNhen) {
        NhanhNhen = Math.min(nhanhNhen, 50D);
    }

    public void setSTVatLy(double STVatLy) {
        this.STVatLy = STVatLy;
    }

    public static void createData() {
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        databaseManager.createSimpleTable(STATS_TABLE, Arrays.asList("uuid TEXT PRIMARY KEY", "dmg_vl DOUBLE", "dmg_magic DOUBLE", "tank DOUBLE"
                , "hp DOUBLE", "the_luc INT", "the_luc_now INT", "nhanh_nhen DOUBLE", "crit_rate DOUBLE", "class_type TEXT", "level_class INT"));
    }

    public double getDamageData() {
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        return (double) databaseManager.getDataByKey(STATS_TABLE, "dmg_vl", "uuid", uuidPlayer.toString());
    }

    public double getDamageMagicData() {
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        return (double) databaseManager.getDataByKey(STATS_TABLE, "dmg_magic", "uuid", uuidPlayer.toString());
    }

    public double getTankData() {
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        return (double) databaseManager.getDataByKey(STATS_TABLE, "tank", "uuid", uuidPlayer.toString());
    }

    public double getHpData() {
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        return (double) databaseManager.getDataByKey(STATS_TABLE, "hp", "uuid", uuidPlayer.toString());
    }

    public double getSpeedData() {
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        return (double) databaseManager.getDataByKey(STATS_TABLE, "nhanh_nhen", "uuid", uuidPlayer.toString());
    }

    public double getCritData() {
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        return (double) databaseManager.getDataByKey(STATS_TABLE, "crit_rate", "uuid", uuidPlayer.toString());
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuidPlayer);
    }

    public Culture getCulture() {
        return culture;
    }

    public UUID getUuidPlayer() {
        return uuidPlayer;
    }

    private void sendNotEnoughExp(Player p, double exp) {
        KnightBlock.messager.msg(p, KnightBlock.toColor("&cCòn thiếu &e" + KnightBlockAPI.decimalFormat(exp, "#.##") + " &cExp để nâng cấp"));
    }

    private boolean takeExp(@NotNull Player p, double exp) {
        if (p.getTotalExperience() >= exp) {
            p.setTotalExperience((int) (p.getTotalExperience() - exp));
            return true;
        } else {
            sendNotEnoughExp(p, exp - p.getTotalExperience());
            return false;
        }
    }

    private void sendUpdateSuccessfully(Player p, String display, double value, double valueAdd) {
        p.sendMessage("");
        KnightBlock.messager.msg(p, KnightBlock.toColor(display + " &f" + KnightBlockAPI.decimalFormat(value, "#.##") + " &a(+" + KnightBlockAPI.decimalFormat(valueAdd - value, "#.##") + ") &9--> &f" + KnightBlockAPI.decimalFormat(valueAdd, "#.##")));
        p.sendMessage("");
    }

    private void sendSetSuccessfully(Player p, String display, double value) {
        p.sendMessage("");
        KnightBlock.messager.msg(p, KnightBlock.toColor(display + " &e--> &a" + KnightBlockAPI.decimalFormat(value, "#.##")));
        p.sendMessage("");
    }

    public void update(StatsType type) {
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        float needExp;
        Player p = Bukkit.getPlayer(this.uuidPlayer);
        if (p == null) return;
        double value;
        switch (type) {
            case DAMAGE_BASE:
                needExp = (float) Math.pow((getDamageData() * 0.5) + 50, 1.4);
                if (!takeExp(p, needExp)) {
                    return;
                }
                value = getDamageData() + 0.01;
                databaseManager.updateDataByColum(STATS_TABLE, "dmg_vl", value, "uuid", p.getUniqueId().toString());
                sendUpdateSuccessfully(p, "&c⚔ Sát thương", getDamageData(), value);
                break;
            case DAMAGE_MAGIC:
                needExp = (float) Math.pow((getDamageMagicData() * 0.5) + 50, 1.4);
                if (!takeExp(p, needExp)) {
                    return;
                }
                value = getDamageMagicData() + 0.01;
                databaseManager.updateDataByColum(STATS_TABLE, "dmg_magic", value, "uuid", p.getUniqueId().toString());
                sendUpdateSuccessfully(p, "&d\uD83D\uDD25 Sát thương Phép", getDamageMagicData(), value);
                break;
            case TANK:
                needExp = (float) Math.pow((getTankData() * 0.5) + 50, 1.4);
                if (!takeExp(p, needExp)) {
                    return;
                }
                value = getTankData() + 0.01;
                databaseManager.updateDataByColum(STATS_TABLE, "tank", value, "uuid", p.getUniqueId().toString());
                sendUpdateSuccessfully(p, "&7✠ Chịu đòn", getTankData(), value);
                break;
            case HP:
                if (getHpData() > 99D) {
                    KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐã đạt giới hạn nâng cấp"));
                    return;
                }
                needExp = (float) Math.pow((getHpData() * 0.5) + 50, 1.4);
                if (!takeExp(p, needExp)) {
                    return;
                }
                value = getHpData() + 0.01;
                if (value > 99) {
                    value = 100;
                }
                databaseManager.updateDataByColum(STATS_TABLE, "hp", value, "uuid", p.getUniqueId().toString());
                sendUpdateSuccessfully(p, "&c❤ Máu", getHpData(), value);
                break;
            case NHANH_NHEN:
                if (getSpeedData() > 89.99) {
                    KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐã đạt giới hạn nâng cấp"));
                    return;
                }
                needExp = (float) Math.pow((getSpeedData() * 0.5) + 50, 1.6);
                if (!takeExp(p, needExp)) {
                    return;
                }
                value = getSpeedData() + 0.01;
                if (value > 89.99) {
                    value = 90;
                }
                databaseManager.updateDataByColum(STATS_TABLE, "nhanh_nhen", value, "uuid", p.getUniqueId().toString());
                sendUpdateSuccessfully(p, "&e⚡ Nhanh nhẹn", getSpeedData(), value);
                break;
            case CRIT_RATE:
                if (getCritData() > 39.99) {
                    KnightBlock.messager.msg(p, KnightBlock.toColor("&cĐã đạt giới hạn nâng cấp"));
                    return;
                }
                needExp = (float) Math.pow((getCritData() * 0.5) + 50, 1.85);
                if (!takeExp(p, needExp)) {
                    return;
                }
                value = getCritData() + 0.01;
                if (value > 39.99) {
                    value = 40;
                }
                databaseManager.updateDataByColum(STATS_TABLE, "crit_rate", value, "uuid", p.getUniqueId().toString());
                sendUpdateSuccessfully(p, "&6✷ Tỷ lệ Siêu chí mạng", getCritData(), value);
                break;
            default:
                break;
        }
        KnightBlock.reloadPlayer(p);
    }

    public void set(StatsType type, double value) {
        SimpleDatabaseManager databaseManager = new SimpleDatabaseManager(KnightBlock.DATABASE_NAME);
        Player p = Bukkit.getPlayer(this.uuidPlayer);
        if (p == null) return;
        switch (type) {
            case DAMAGE_BASE:
                databaseManager.updateDataByColum(STATS_TABLE, "dmg_vl", value, "uuid", p.getUniqueId().toString());
                sendSetSuccessfully(p, "&c⚔ Sát thương", value);
                setSTVatLy(value);
                break;
            case DAMAGE_MAGIC:
                databaseManager.updateDataByColum(STATS_TABLE, "dmg_magic", value, "uuid", p.getUniqueId().toString());
                sendSetSuccessfully(p, "&c\uD83D\uDD25 Sát thương Phép", value);
                setSTPhep(value);
                break;
            case TANK:
                databaseManager.updateDataByColum(STATS_TABLE, "tank", value, "uuid", p.getUniqueId().toString());
                sendSetSuccessfully(p, "&7✠ Chịu đòn", value);
                setChongChiu(value);
                break;
            case HP:
                if (value > 99) {
                    value = 100;
                }
                databaseManager.updateDataByColum(STATS_TABLE, "hp", value, "uuid", p.getUniqueId().toString());
                sendSetSuccessfully(p, "&c❤ Máu", value);
                setHP(value);
                break;
            case NHANH_NHEN:
                if (value > 59.99) {
                    value = 60;
                }
                databaseManager.updateDataByColum(STATS_TABLE, "nhanh_nhen", value, "uuid", p.getUniqueId().toString());
                sendSetSuccessfully(p, "&e⚡ Nhanh nhẹn", value);
                setNhanhNhen(value);
                KnightBlock.reloadPlayer(p);
                break;
            case CRIT_RATE:
                if (value > 39.99) {
                    value = 40;
                }
                databaseManager.updateDataByColum(STATS_TABLE, "crit_rate", value, "uuid", p.getUniqueId().toString());
                sendSetSuccessfully(p, "&6✷ Tỷ lệ Siêu chí mạng", value);
                setCritRate(value);
                break;
            default:
                break;
        }
        KnightBlock.reloadPlayer(p);
    }


    public boolean hasCulture() {
        if (culture == null) return false;
        return culture.getType() != CultureType.NONE;
    }


    @Override
    public UUID getUuid() {
        return uuidPlayer;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public double getSkillXp(Skill skill) {
        return 0;
    }

    @Override
    public void addSkillXp(Skill skill, double amountToAdd) {

    }

    @Override
    public void addSkillXpRaw(Skill skill, double amountToAdd) {

    }

    @Override
    public void setSkillXp(Skill skill, double amount) {

    }

    @Override
    public int getSkillLevel(Skill skill) {
        return 0;
    }

    @Override
    public void setSkillLevel(Skill skill, int level) {

    }

    @Override
    public double getSkillAverage() {
        return 0;
    }

    @Override
    public double getStatLevel(Stat stat) {
        return 0;
    }

    @Override
    public double getBaseStatLevel(Stat stat) {
        return 0;
    }

    @Override
    public double getMana() {
        return 0;
    }

    @Override
    public double getMaxMana() {
        return 0;
    }

    @Override
    public void setMana(double mana) {

    }

    @Override
    public boolean consumeMana(double amount) {
        SkillsUser user = KnightBlock.users.get(uuidPlayer);
        double mana = user.getMana();
        if (mana >= amount) {
            user.setMana(mana - amount);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getPowerLevel() {
        return 0;
    }

    @Override
    public void addStatModifier(StatModifier statModifier) {

    }

    @Override
    public void removeStatModifier(String name) {

    }

    @Override
    public @Nullable StatModifier getStatModifier(String name) {
        return null;
    }

    @Override
    public Map<String, StatModifier> getStatModifiers() {
        return null;
    }

    @Override
    public void addTraitModifier(TraitModifier traitModifier) {

    }

    @Override
    public void removeTraitModifier(String name) {

    }

    @Override
    public @Nullable TraitModifier getTraitModifier(String name) {
        return null;
    }

    @Override
    public Map<String, TraitModifier> getTraitModifiers() {
        return null;
    }

    @Override
    public double getEffectiveTraitLevel(Trait trait) {
        return 0;
    }

    @Override
    public double getBonusTraitLevel(Trait trait) {
        return 0;
    }

    @Override
    public int getAbilityLevel(Ability ability) {
        return 0;
    }

    @Override
    public int getManaAbilityLevel(ManaAbility manaAbility) {
        return 0;
    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
