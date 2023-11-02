package me.srrapero720.embeddiumplus.mixins.BorderlessFullscreen;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Window.class)
public interface MainWindowAccessor {
    @Accessor()
    void setDirty(boolean value);
}