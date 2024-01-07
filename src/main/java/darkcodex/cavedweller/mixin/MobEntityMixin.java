package darkcodex.cavedweller.mixin;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.GoalSelector;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import darkcodex.cavedweller.CaveDweller;
import darkcodex.cavedweller.Config;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    
    @Inject(method = "baseTick()V", at = @At("HEAD"))
    public void baseTick(CallbackInfo info) {
        if(!Config.getVillagersFearPlayers())
            return;

        MobEntity entity = (MobEntity)(Object)this;
        World world = entity.getEntityWorld();
        PlayerEntity player = world.getClosestPlayer(entity, 16);

        if(player != null && entity.canSee(player))
            CheckAfraid(entity, player, world);        
    }

    @Inject(method = "initGoals()V", at = @At("HEAD"))
    protected void initGoals(CallbackInfo info) {
        MobEntity entity = (MobEntity)(Object)this;
        if(entity instanceof PathAwareEntity)
        {
            PathAwareEntity pathEntity = (PathAwareEntity)entity;
            GoalSelector goalSelector = ((MobEntityAccessor)entity).getGoalSelector();
            goalSelector.add(1, new EscapeDangerGoal(pathEntity, 1.0));
        }        
    }

    private void CheckAfraid(MobEntity entity, PlayerEntity player, World world) {
        if(player.isCreative() || player.isSpectator())
        return;
    
        DimensionType dimType = world.getDimension();
        if(!dimType.natural() || dimType.hasCeiling())
            return;

        boolean isSurface = entity.getBlockY() >= world.getSeaLevel() + Config.getVillagerFearLevel();
        boolean isDweller = false;
        for(int i = 0; i < Config.getDwellers().length; i++) {
            if(player.getName().getString().toLowerCase().equals(Config.getDwellers()[i].toLowerCase())) {
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
                //entity.emitGameEvent(GameEvent.ENTITY_DAMAGE);
            }               
        }
    }
}
