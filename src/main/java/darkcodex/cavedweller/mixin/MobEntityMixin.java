package darkcodex.cavedweller.mixin;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.mob.MobEntity;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.event.GameEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import darkcodex.cavedweller.CaveDweller;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Inject(method = "baseTick()V", at = @At("HEAD"))
    public void baseTick(CallbackInfo info) {
        if(!CaveDweller.villagersFearPlayers)
            return;

        MobEntity entity = (MobEntity)(Object)this;
        World world = entity.getEntityWorld();
        PlayerEntity player = world.getClosestPlayer(entity, 16);

        if(player != null && entity.canSee(player))
            CheckAfraid(entity, player, world);        
    }

    private void CheckAfraid(MobEntity entity, PlayerEntity player, World world) {
        if(player.isCreative() || player.isSpectator())
        return;
    
        DimensionType dimType = world.getDimension();
        if(!dimType.natural() || dimType.hasCeiling())
            return;

        boolean isSurface = entity.getBlockY() >= world.getSeaLevel() + CaveDweller.villagerFearLevel;
        boolean isDweller = false;
        for(int i = 0; i < CaveDweller.dwellers.length; i++) {
            if(player.getName().getString().toLowerCase().equals(CaveDweller.dwellers[i].toLowerCase())) {
                isDweller = true;
                break;
            }
        }

        if(isSurface && isDweller) {
            if(entity instanceof IronGolemEntity)
            {
                ((IronGolemEntity)entity).chooseRandomAngerTime();
            }
            else if(entity instanceof VillagerEntity)
            {
                entity.setAttacker(player);   
                entity.setAttacking(player);
                entity.emitGameEvent(GameEvent.ENTITY_DAMAGE);
            }               
        }
    }
}
