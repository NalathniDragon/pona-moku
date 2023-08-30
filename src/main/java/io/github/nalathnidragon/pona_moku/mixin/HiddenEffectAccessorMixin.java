package io.github.nalathnidragon.pona_moku.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatusEffectInstance.class)
public interface HiddenEffectAccessorMixin {
	@Accessor StatusEffectInstance getHiddenEffect();
	@Accessor void setHiddenEffect(StatusEffectInstance hiddenEffect);
}
