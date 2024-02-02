package vice.rubidium_extras.mixins.EntityDistance;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import vice.rubidium_extras.config.MagnesiumExtrasConfig;
import vice.rubidium_extras.features.EntityDistance.IWhitelistCheck;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements IWhitelistCheck {
    @Unique private boolean extras$checked = false;
    @Unique private boolean extras$whitelisted = false;

    @Shadow public abstract ResourceLocation getDefaultLootTable();

    @Override
    @Unique
    public boolean extras$isAllowed() {
        if (extras$checked) return extras$whitelisted;

        if (!MagnesiumExtrasConfig.ConfigSpec.isLoaded()) MagnesiumExtrasConfig.loadConfig();
        ResourceLocation currentLocation = getDefaultLootTable();

        for (String item : MagnesiumExtrasConfig.entityDistanceWhitelist.get()) {
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
}
