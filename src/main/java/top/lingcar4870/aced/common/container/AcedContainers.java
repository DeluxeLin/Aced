package top.lingcar4870.aced.common.container;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.lingcar4870.aced.Aced;

public class AcedContainers {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Aced.MODID);

    public static final RegistryObject<MenuType<DeathInheritContainerMenu>> DEATH_INHERIT_MENU = MENUS.register("death_inherit", () -> IForgeMenuType.create((windowId, inv, data) -> new DeathInheritContainerMenu(windowId, inv)));
}
