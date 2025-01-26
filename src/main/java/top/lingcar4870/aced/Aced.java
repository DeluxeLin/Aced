package top.lingcar4870.aced;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import top.lingcar4870.aced.client.ClientDIDataHelper;
import top.lingcar4870.aced.client.gui.DeathInheritGUI;
import top.lingcar4870.aced.common.container.AcedContainers;
import top.lingcar4870.aced.item.AcedItems;
import top.lingcar4870.aced.packet.C2SAttachDIDataPacket;
import top.lingcar4870.aced.packet.S2CStoreDIDataPacket;

@Mod(Aced.MODID)
public class Aced {
    public static final String MODID = "aced";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel MAIN_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Aced.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static IEventBus modBus;

    public Aced(FMLJavaModLoadingContext context) {
        modBus = context.getModEventBus();
        AcedItems.ITEMS.register(modBus);
        AcedContainers.MENUS.register(modBus);
        modBus.register(this);

        context.registerConfig(ModConfig.Type.COMMON, AcedConfig.COMMON);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Registering Packets...");
        MAIN_CHANNEL.registerMessage(0, S2CStoreDIDataPacket.class, S2CStoreDIDataPacket::toBytes, S2CStoreDIDataPacket::new, S2CStoreDIDataPacket::handle);
        MAIN_CHANNEL.registerMessage(1, C2SAttachDIDataPacket.class, C2SAttachDIDataPacket::toBytes, C2SAttachDIDataPacket::new, C2SAttachDIDataPacket::handle);
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(AcedContainers.DEATH_INHERIT_MENU.get(), DeathInheritGUI::new);
            ClientDIDataHelper.init();
        });
    }
}
