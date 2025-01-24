package top.lingcar4870.aced;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import top.lingcar4870.aced.item.AcedItems;
import top.lingcar4870.aced.packet.C2SAttachDIDataPacket;
import top.lingcar4870.aced.packet.S2CStoreDIDataPacket;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath() && event.getEntity().getInventory().countItem(AcedItems.DEATH_INHERIT_SCROLL.get()) != 0) {
            event.getEntity().sendSystemMessage(Component.literal("Test DeathInherit"));
            Aced.MAIN_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                    new S2CStoreDIDataPacket());
        }
    }

    public static class DIDataAttachClientHandler {
        private final CompoundTag data;

        private DIDataAttachClientHandler(CompoundTag data) {
            this.data = data;
        }

        public static void create(CompoundTag data) {
            MinecraftForge.EVENT_BUS.register(new DIDataAttachClientHandler(data));
        }

        @SubscribeEvent
        protected void onClientLogIn(ClientPlayerNetworkEvent.LoggingIn event) {
            Aced.MAIN_CHANNEL.sendToServer(new C2SAttachDIDataPacket(data));
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
