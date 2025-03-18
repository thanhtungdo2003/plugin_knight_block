package com.bbv.skills;

import com.bbv.object.DamageType;
import org.bukkit.entity.LivingEntity;

public interface KyNangDacBiet{
    LoaiKyNang getType();
    LivingEntity getCaster();
    boolean cast();
    void disable();
    boolean isActive();
    void setDamage(double damage);
    void setDamageType(DamageType type);
}
