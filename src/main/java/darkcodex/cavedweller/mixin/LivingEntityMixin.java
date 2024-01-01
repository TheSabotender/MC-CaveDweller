package darkcodex.cavedweller.mixin;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import darkcodex.cavedweller.CaveDweller;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void mobTick() {
        LivingEntity entity = (LivingEntity)(Object)this;
        ServerWorld sWorld = (ServerWorld)entity.getWorld();
        PlayerLookup.world(sWorld).forEach(player -> {
            CheckEntityType(entity, player, sWorld);
        });
    }

    private void CheckEntityType(LivingEntity entity, ServerPlayerEntity player, World world) {
        if(!CaveDweller.villagersFearPlayers)
            return;

        if(!(entity instanceof VillagerEntity))
            return;

        CheckAfraid(entity, player, world);
    }

    private void CheckAfraid(LivingEntity entity, ServerPlayerEntity player, World world) {
        if(player.isCreative() || player.isSpectator())
            return;
        
        DimensionType dimType = world.getDimension();
        if(!dimType.natural() || dimType.hasCeiling())
            return;

        boolean isSurface = entity.getBlockY() >= world.getSeaLevel() + CaveDweller.villagerFearLevel;
        boolean isDweller = false;
        for(int i = 0; i < CaveDweller.dwellers.length; i++) {
            if(player.getName().getString().equals(CaveDweller.dwellers[i])) {
                isDweller = true;
                break;
            }
        }
        if(isSurface && isDweller) {
            entity.setAttacker(player);
        }
    }
}
