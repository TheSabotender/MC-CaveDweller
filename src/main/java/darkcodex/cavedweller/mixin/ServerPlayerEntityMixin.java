package darkcodex.cavedweller.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import darkcodex.cavedweller.CaveDweller;
import darkcodex.cavedweller.SurfaceDamage;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {    
    int logFrames;

    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if(!CaveDweller.sunlightBurns)
            return;

        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        World world = player.getWorld();
        CheckValidPlayer(player, world);
    }

    private void CheckValidPlayer(ServerPlayerEntity player, World world) {
        if(player.isCreative() || player.isSpectator())
            return;

        DimensionType dimType = world.getDimension();

        boolean isDweller = false;
        for(int i = 0; i < CaveDweller.dwellers.length; i++) {
            if(player.getName().getString().toLowerCase().equals(CaveDweller.dwellers[i].toLowerCase())) {
                isDweller = true;
                break;
            }
        }

        if(isDweller && dimType.natural() && !dimType.hasCeiling())
            CheckSurface(player, world);
    }

    private void CheckSurface(ServerPlayerEntity player, World world) {
        boolean visible = world.isSkyVisible(player.getBlockPos());
        boolean time = world.isDay();
        boolean isWet = player.isWet();

        if(player.isOnFire())
            return;

        if(visible && time && !isWet) {
            logFrames++;
            if(logFrames > CaveDweller.logFrameCount)
            {
                player.sendMessageToClient(Text.of("You are exposed to the sky, find shelter!"), true);
                logFrames = 0;
            }

            //player.damage(SurfaceDamage.source(player.getWorld()), CaveDweller.sunlightBurn);
            //player.setOnFireFor(CaveDweller.sunlightBurnDuration);
        }
    }
}