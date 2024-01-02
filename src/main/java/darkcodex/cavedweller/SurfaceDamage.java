package darkcodex.cavedweller;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class SurfaceDamage {
    public static final RegistryKey<DamageType> SUN_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("cave-dweller", "sun_damage"));
    public static final RegistryKey<DamageType> FEAR_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("cave-dweller", "fear_damage"));
 
    public static DamageSource source(World world) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(SUN_DAMAGE));
    }

    public static DamageSource fear(World world, Entity entity) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(FEAR_DAMAGE), entity);
    }
}