package top.lingcar4870.aced.client;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.lingcar4870.aced.Aced;
import top.lingcar4870.aced.common.DeathInheritDataHelper;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@OnlyIn(Dist.CLIENT)
public class ClientDIDataHelper {
    @Nullable
    private static CompoundTag activeDIData = null;

    public static void init() {
        Aced.LOGGER.debug("Initializing ClientDIHelper...");

        if (hasDIDataFile()) {
            readDIDataFromFile();
        }
    }

    public static boolean hasDIDataFile() {
        File file = new File(Minecraft.getInstance().gameDirectory, "death_inherit_data.dat");
        return file.exists();
    }

    public static CompoundTag getActiveDIData() {
        if (activeDIData == null && hasDIDataFile()) {
            readDIDataFromFile();
        }
        return activeDIData;
    }

    public static boolean hasActiveDIData() {
        return activeDIData != null;
    }

    public static void readDIDataFromFile() {
        Aced.LOGGER.debug("Reading DeathInheritData from death_inherit_data.dat");
        File saveFile = new File(Minecraft.getInstance().gameDirectory, "death_inherit_data.dat");
        if (!saveFile.exists()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(saveFile)) {
            activeDIData = TagParser.parseTag(new String(fis.readAllBytes()));
        } catch (IOException | CommandSyntaxException e) {
            Aced.LOGGER.error("Failed to read DeathInheritData!", e);
        }
    }

    public static void deleteDIDataFile() {
        try {
            Files.deleteIfExists(new File(Minecraft.getInstance().gameDirectory, "death_inherit_data.dat").toPath());
        } catch (IOException e) {
            Aced.LOGGER.error("Failed to delete death_inherit_data.dat!", e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void writeDeathInheritDataToFile() {
        Player player = Minecraft.getInstance().player;
        assert player != null;
        CompoundTag data = DeathInheritDataHelper.popDeathInheritData(player);
        Aced.LOGGER.debug("Current Game Dir: {}", Minecraft.getInstance().gameDirectory);
        Aced.LOGGER.debug("Saving DeathInheritData: {}", data);
        activeDIData = data;

        File saveFile = new File(Minecraft.getInstance().gameDirectory, "death_inherit_data.dat");
        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(saveFile);
            fos.write(data.toString().getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (IOException e) {
            Aced.LOGGER.error("Failed to storage DeathInheritData!", e);
        }
    }
}
