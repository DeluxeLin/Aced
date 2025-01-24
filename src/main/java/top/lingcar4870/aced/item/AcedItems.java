package top.lingcar4870.aced.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.lingcar4870.aced.Aced;

public class AcedItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Aced.MODID);

    public static final RegistryObject<Item> DEATH_INHERIT_SCROLL = ITEMS.register("death_inherit_scroll", DeathInheritScroll::new);
}
