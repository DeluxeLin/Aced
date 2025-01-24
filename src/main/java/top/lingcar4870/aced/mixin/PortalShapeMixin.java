package top.lingcar4870.aced.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalShape.class)
public abstract class PortalShapeMixin {
    @Inject(method = "lambda$static$0(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z", at = @At("RETURN"), cancellable = true)
    private static void modifyFrameBlockPredicate(BlockState p_77720_, BlockGetter p_77721_, BlockPos p_77722_, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(p_77720_.isPortalFrame(p_77721_, p_77722_) || p_77720_.is(Blocks.CRYING_OBSIDIAN));
    }
}
