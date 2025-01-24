package top.lingcar4870.aced.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.lingcar4870.aced.Aced;
import top.lingcar4870.aced.EventHandler;
import top.lingcar4870.aced.client.ClientDIDataHelper;
import top.lingcar4870.aced.packet.C2SAttachDIDataPacket;

@OnlyIn(Dist.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(at = @At("RETURN"), method = "createTitle", cancellable = true)
    private void createTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(cir.getReturnValue() + " 灵车特供疯魔版");
    }

    @Inject(at = @At("RETURN"), method = "doWorldLoad")
    private void doWorldLoad(String pLevelId, LevelStorageSource.LevelStorageAccess pLevel, PackRepository pPackRepository, WorldStem pWorldStem, boolean pNewWorld, CallbackInfo ci) {
        if (((PrimaryLevelData) pWorldStem.worldData()).loadedPlayerTag != null) {
            Aced.LOGGER.debug("Loaded Existing World");
        } else {
            Aced.LOGGER.debug("Loaded New World");
            if (ClientDIDataHelper.hasActiveDIData()) {
                EventHandler.DIDataAttachClientHandler.create(ClientDIDataHelper.getActiveDIData());

                //笑点解析: 此时世界还未加载完成, 发包就炸
                // Aced.MAIN_CHANNEL.sendToServer(new C2SAttachDIDataPacket(ClientDIDataHelper.getActiveDIData()));

                ClientDIDataHelper.deleteDIDataFile();
            }
        }
    }
}
