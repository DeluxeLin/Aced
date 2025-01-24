package top.lingcar4870.aced.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import top.lingcar4870.aced.Aced;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SAttachDIDataPacket {
    private final CompoundTag data;

    public C2SAttachDIDataPacket(CompoundTag data) {
        this.data = data;
    }

    public C2SAttachDIDataPacket(FriendlyByteBuf buf) {
        this.data = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    @SuppressWarnings("DataFlowIssue")
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> this.attachDIData2NewSave(ctx.get().getSender()));
        ctx.get().setPacketHandled(true);
    }

    protected void attachDIData2NewSave(ServerPlayer target) {
        Aced.LOGGER.debug("Attaching DeathInheritData to new save...");

        var enderchest = target.getEnderChestInventory();

        enderchest.fromTag(this.data.getList("Items", Tag.TAG_COMPOUND));
    }
}
