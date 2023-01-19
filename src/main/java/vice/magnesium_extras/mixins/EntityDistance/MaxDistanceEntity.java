package vice.magnesium_extras.mixins.EntityDistance;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;
import vice.magnesium_extras.util.DistanceUtility;

@Mixin(EntityRendererManager.class)
public class MaxDistanceEntity
{
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, ClippingHelper clippingHelper, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir)
    {
        if (!MagnesiumExtrasConfig.enableDistanceChecks.get())
            return;

        if (!DistanceUtility.isEntityWithinDistance(
                entity,
                cameraX,
                cameraY,
                cameraZ,
                MagnesiumExtrasConfig.maxEntityRenderDistanceY.get(),
                MagnesiumExtrasConfig.maxEntityRenderDistanceSquare.get()
        ) && !entityWhitelisted(entity.getType().getRegistryName()))
        {
            cir.cancel();
        }
    }

    private boolean entityWhitelisted(ResourceLocation s) {
        return s != null && MagnesiumExtrasConfig.entityWhitelist.get().stream().anyMatch(s.toString()::equals);
    }

}

