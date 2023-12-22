package darkcodex.cavedweller.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import darkcodex.cavedweller.CaveDweller;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    DamageSource damageSource = new DamageSource(null);
    int logFrames;

    @Inject(method = "tick", at = @At("HEAD"))
    protected void tick() {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        World world = player.getWorld();
        CheckValidPlayer(player, world);
    }

    private void CheckValidPlayer(ServerPlayerEntity player, World world) {
        if(player.isCreative() || player.isSpectator())
            return;

        DimensionType dimType = world.getDimension();
        if(dimType.natural() && !dimType.hasCeiling())
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
                player.sendMessageToClient(Text.of("You are exposed to the sky, find shelter!"), false);

            player.damage(damageSource, CaveDweller.sunlightBurn);
            player.setOnFireFor(CaveDweller.sunlightBurnDuration);
        }
    }
}