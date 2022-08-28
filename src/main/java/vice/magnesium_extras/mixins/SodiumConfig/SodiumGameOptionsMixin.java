package vice.magnesium_extras.mixins.SodiumConfig;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.GameSettings;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;
import vice.magnesium_extras.mixins.BorderlessFullscreen.MainWindowAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(SodiumGameOptionPages.class)
public class SodiumGameOptionsMixin
{
    @Shadow @Final private static SodiumOptionsStorage sodiumOpts;

    //@Inject(at = @At("HEAD"), method = "experimental", remap = false, cancellable = true)

    @Shadow @Final private static MinecraftOptionsStorage vanillaOpts;

    //private static void experimental(CallbackInfoReturnable<OptionPage> cir)


    @Inject(
            method = "general",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            remap = false,
            cancellable = true
    )
    private static void InjectGeneral(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups)
    {
        OptionImpl<GameSettings, MagnesiumExtrasConfig.FullscreenMode> fullscreenMode = OptionImpl.createBuilder( MagnesiumExtrasConfig.FullscreenMode.class, vanillaOpts)
                .setName("Fullscreen Mode")
                .setTooltip("Windowed - the game will display in a small window.\nBorderless - the game will be fullscreened, and locked to your monitor's refresh rate, but allow you to tab out easily.\nFullscreen - the game will display in native fullscreen mode.")
                .setControl((opt) -> new CyclingControl<>(opt, MagnesiumExtrasConfig.FullscreenMode.class, new String[] { "Windowed", "Borderless", "Fullscreen"}))
                .setBinding(
                        (opts, value) -> {
                            MagnesiumExtrasConfig.fullScreenMode.set(value);
                            opts.fullscreen = value != MagnesiumExtrasConfig.FullscreenMode.WINDOWED;

                            Minecraft client = Minecraft.getInstance();
                            MainWindow window = client.getWindow();
                            if (window != null && window.isFullscreen() != opts.fullscreen)
                            {
                                window.toggleFullScreen();
                                opts.fullscreen = window.isFullscreen();
                            }

                            if (window != null && opts.fullscreen)
                            {
                                ((MainWindowAccessor) (Object) window).setDirty(true);
                                window.changeFullscreenVideoMode();
                            }
                        },
                        (opts) -> MagnesiumExtrasConfig.fullScreenMode.get())
                .build();

        ReplaceOption(groups, "Fullscreen", fullscreenMode);
    }


    private static void ReplaceOption(List<OptionGroup> groups, String name, Option<?> replaceOption)
    {
        List<OptionGroup> newList = new ArrayList<>();

        for (OptionGroup optionGroup : groups)
        {
            OptionGroup.Builder builder = OptionGroup.createBuilder();

            for (Option<?> option : optionGroup.getOptions())
            {
                builder.add(Objects.equals(option.getName(), name) ? replaceOption : option);
            }

            newList.add(builder.build());
        }

        groups.clear();
        groups.addAll(newList);
    }

    @ModifyConstant(method = "advanced", remap = false, constant = @Constant(stringValue = "Experimental"))
    private static String ChangeCategoryName(String old) {
        return "Extras";
    }
}