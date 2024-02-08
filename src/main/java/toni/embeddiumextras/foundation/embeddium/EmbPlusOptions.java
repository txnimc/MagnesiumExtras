package toni.embeddiumextras.foundation.embeddium;

import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import toni.embeddiumextras.EmbyConfig;
import toni.embeddiumextras.EmbyConfig.FPSDisplayGravity;
import toni.embeddiumextras.EmbyConfig.FPSDisplayMode;
import toni.embeddiumextras.EmbyConfig.FPSDisplaySystemMode;
import toni.embeddiumextras.EmbyConfig.FullScreenMode;
import toni.embeddiumextras.EmbyTools;
import net.minecraft.network.chat.Component;

import java.util.List;

public class EmbPlusOptions {
    public static Option<FullScreenMode> getFullscreenOption(MinecraftOptionsStorage options) {
        return OptionImpl.createBuilder(FullScreenMode.class, options)
                .setName(Component.translatable("embeddium.extras.options.screen.title"))
                .setTooltip(Component.translatable("embeddium.extras.options.screen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, FullScreenMode.class, new Component[] {
                        Component.translatable("embeddium.extras.options.screen.windowed"),
                        Component.translatable("embeddium.extras.options.screen.borderless"),
                        Component.translatable("options.fullscreen")
                }))
                .setBinding(EmbyConfig::setFullScreenMode, (opts) -> EmbyConfig.fullScreen.get()).build();
    }


    public static void setFPSOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts) {
        var builder = OptionGroup.createBuilder();

        builder.add(OptionImpl.createBuilder(FPSDisplayMode.class, sodiumOpts)
                .setName(Component.translatable("embeddium.extras.options.displayfps.title"))
                .setTooltip(Component.translatable("embeddium.extras.options.displayfps.desc"))
                .setControl((option) -> new CyclingControl<>(option, FPSDisplayMode.class, new Component[]{
                        Component.translatable("embeddium.extras.options.common.off"),
                        Component.translatable("embeddium.extras.options.common.simple"),
                        Component.translatable("embeddium.extras.options.common.advanced")
                }))
                .setBinding(
                        (opts, value) -> EmbyConfig.fpsDisplayMode.set(value),
                        (opts) -> EmbyConfig.fpsDisplayMode.get())
                .setImpact(OptionImpact.LOW)
                .build()
        );

        builder.add(OptionImpl.createBuilder(FPSDisplaySystemMode.class, sodiumOpts)
                .setName(Component.translatable("embeddium.extras.options.displayfps.system.title"))
                .setTooltip(Component.translatable("embeddium.extras.options.displayfps.system.desc"))
                .setControl((option) -> new CyclingControl<>(option, FPSDisplaySystemMode.class, new Component[]{
                        Component.translatable("embeddium.extras.options.common.off"),
                        Component.translatable("embeddium.extras.options.common.on"),
                        Component.translatable("embeddium.extras.options.displayfps.system.gpu"),
                        Component.translatable("embeddium.extras.options.displayfps.system.ram")
                }))
                .setBinding((options, value) -> EmbyConfig.fpsDisplaySystemMode.set(value),
                        (options) -> EmbyConfig.fpsDisplaySystemMode.get())
                .build()
        );

        var components = new Component[FPSDisplayGravity.values().length];
        for (int i = 0; i < components.length; i++) {
            components[i] = Component.translatable("embeddium.extras.options.displayfps.gravity." + FPSDisplayGravity.values()[i].name().toLowerCase());
        }

        builder.add(OptionImpl.createBuilder(FPSDisplayGravity.class, sodiumOpts)
                .setName(Component.translatable("embeddium.extras.options.displayfps.gravity.title"))
                .setTooltip(Component.translatable("embeddium.extras.options.displayfps.gravity.desc"))
                .setControl((option) -> new CyclingControl<>(option, FPSDisplayGravity.class, components))
                .setBinding(
                        (opts, value) -> EmbyConfig.fpsDisplayGravity.set(value),
                        (opts) -> EmbyConfig.fpsDisplayGravity.get())
                .build()
        );


        builder.add(OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.extras.options.displayfps.margin.title"))
                .setTooltip(Component.translatable("embeddium.extras.options.displayfps.margin.desc"))
                .setControl((option) -> new SliderControl(option, 4, 64, 1, (v) -> Component.literal(v + "px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> {
                            EmbyConfig.fpsDisplayMargin.set(value);
                            EmbyConfig.fpsDisplayMarginCache = value;
                        },
                        (opts) -> EmbyConfig.fpsDisplayMarginCache)
                .build()
        );

        builder.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.extras.options.displayfps.shadow.title"))
                .setTooltip(Component.translatable("embeddium.extras.options.displayfps.shadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.fpsDisplayShadow.set(value);
                            EmbyConfig.fpsDisplayShadowCache = value;
                        },
                        (options) -> EmbyConfig.fpsDisplayShadowCache)
                .build()
        );

        groups.add(builder.build());
    }

    public static void setPerformanceOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts) {
        var builder = OptionGroup.createBuilder();
        var fontShadow = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.extras.options.fontshadow.title"))
                .setTooltip(Component.translatable("embeddium.extras.options.fontshadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.fontShadows.set(value);
                            EmbyConfig.fontShadowsCache = value;
                        },
                        (options) -> EmbyConfig.fontShadowsCache)
                .setImpact(OptionImpact.VARIES)
                .build();


        var hideJEI = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.extras.options.jei.title"))
                .setTooltip(Component.translatable("embeddium.extras.options.jei.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.hideJREI.set(value);
                            EmbyConfig.hideJREICache = value;
                        },
                        (options) -> EmbyConfig.hideJREICache)
                .setImpact(OptionImpact.LOW)
                .setEnabled(EmbyTools.isModInstalled("jei"))
                .build();

        builder.add(fontShadow);
        builder.add(hideJEI);

        groups.add(builder.build());
    }


}
