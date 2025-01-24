package top.lingcar4870.aced.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import top.lingcar4870.aced.client.ClientDIDataHelper;

import java.util.function.Supplier;

public class S2CStoreDIDataPacket {
    public S2CStoreDIDataPacket() {}

    public S2CStoreDIDataPacket(FriendlyByteBuf ignored) {}

    public void toBytes(FriendlyByteBuf ignored) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(ClientDIDataHelper::writeDeathInheritDataToFile);
        ctx.get().setPacketHandled(true);
    }
}
