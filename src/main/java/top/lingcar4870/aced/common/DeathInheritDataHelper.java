package top.lingcar4870.aced.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import top.lingcar4870.aced.Aced;

public class DeathInheritDataHelper {
    public static CompoundTag getDeathInheritData(Player player) {
        assert player != null;
        CompoundTag playerData = player.getPersistentData();
        if (playerData.contains("DeathInheritData", Tag.TAG_COMPOUND))
            return playerData.getCompound("DeathInheritData");
        return new CompoundTag();
    }

    public static void writeDeathInheritData(Player player, CompoundTag data) {
        assert player != null && data != null;
        CompoundTag playerData = player.getPersistentData();
        playerData.put("DeathInheritData", data);
        Aced.LOGGER.debug("Saved DeathInheritData: {}", data);
    }

    public static CompoundTag popDeathInheritData(Player player) {
        assert player != null;
        CompoundTag playerData = player.getPersistentData();
        CompoundTag ret = playerData.getCompound("DeathInheritData").copy();
        playerData.remove("DeathInheritData");
        return ret;
    }
}
