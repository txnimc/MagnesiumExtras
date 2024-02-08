package toni.embeddiumextras.mixins.impl.entitydistance;

import toni.embeddiumextras.EmbyConfig;
import toni.embeddiumextras.EmbyTools;
import toni.embeddiumextras.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityDispatcherMixin {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void inject$shouldRender(E entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir) {
        if (!EmbyConfig.entityDistanceCullingCache) return;

        if (!((IWhitelistCheck) entity.getType()).embPlus$isAllowed() && !EmbyTools.isEntityWithinDistance(
                entity,
                cameraX,
                cameraY,
                cameraZ,
                EmbyConfig.entityCullingDistanceYCache,
                EmbyConfig.entityCullingDistanceXCache
        )) {
            cir.setReturnValue(false);
        }
    }
}