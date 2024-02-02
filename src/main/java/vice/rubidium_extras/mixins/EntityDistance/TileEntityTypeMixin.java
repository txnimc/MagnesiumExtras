package vice.rubidium_extras.mixins.EntityDistance;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import vice.rubidium_extras.config.MagnesiumExtrasConfig;
import vice.rubidium_extras.features.EntityDistance.IWhitelistCheck;

import javax.annotation.Nullable;

@Mixin(BlockEntityType.class)
public abstract class TileEntityTypeMixin implements IWhitelistCheck {
    @Unique private boolean extras$checked = false;
    @Unique private boolean extras$whitelisted = false;

    public boolean extras$isAllowed() {
        if (extras$checked) return extras$whitelisted;
        ResourceLocation currentLocation = getKey((BlockEntityType<?>) (Object) this); // CAN'T BE NULL, BECAUSE IT EXISTS

        if (!MagnesiumExtrasConfig.ConfigSpec.isLoaded()) MagnesiumExtrasConfig.loadConfig();
        for (String item : MagnesiumExtrasConfig.tileDistanceWhitelist.get()) {
            String[] result = item.split(":");

            if (result[1].equals("*") && currentLocation.getNamespace().equals(result[0])) {
                extras$whitelisted = true;
                break;
            } else if (currentLocation.toString().equals(result[0] + ":" + result[1])) {
                extras$whitelisted = true;
                break;
            }
        }

        extras$checked = true;
        return extras$whitelisted;
    }

    @Shadow
    @Nullable
    public static ResourceLocation getKey(BlockEntityType<?> pBlockEntityType) {
        throw new UnsupportedOperationException("stub!");
    }
}
