package toni.embeddiumextras.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import toni.embeddiumextras.EmbyConfig;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class OthersPage extends OptionPage {
    private static final SodiumOptionsStorage mixinsOptionsStorage = new SodiumOptionsStorage();

    public OthersPage() {
        super(Component.translatable("embeddium.extras.options.others.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(EmbyConfig.AttachMode.class, mixinsOptionsStorage)
                        .setName(Component.translatable("embeddium.extras.options.others.borderless.attachmode.title"))
                        .setTooltip(Component.translatable("embeddium.extras.options.others.borderless.attachmode.desc"))
                        .setControl(option -> new CyclingControl<>(option, EmbyConfig.AttachMode.class, new Component[] {
                                Component.translatable("embeddium.extras.options.common.attach"),
                                Component.translatable("embeddium.extras.options.common.replace"),
                                Component.translatable("embeddium.extras.options.common.off")
                        }))
                        .setBinding((options, value) -> EmbyConfig.borderlessAttachModeF11.set(value),
                                (options) -> EmbyConfig.borderlessAttachModeF11.get())
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, mixinsOptionsStorage)
                        .setName(Component.translatable("embeddium.extras.options.others.languagescreen.fastreload.title"))
                        .setTooltip(Component.translatable("embeddium.extras.options.others.languagescreen.fastreload.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> {
                                    EmbyConfig.fastLanguageReload.set(value);
                                    EmbyConfig.fastLanguageReloadCache = value;
                                },
                                (options) -> EmbyConfig.fastLanguageReloadCache)
                        .build()
                )
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
