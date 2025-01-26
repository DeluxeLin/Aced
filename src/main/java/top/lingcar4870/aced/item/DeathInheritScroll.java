package top.lingcar4870.aced.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import top.lingcar4870.aced.common.container.DeathInheritContainerMenu;

import javax.annotation.ParametersAreNonnullByDefault;

public class DeathInheritScroll extends Item {
    public DeathInheritScroll() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @NotNull @ParametersAreNonnullByDefault
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.dimension() != Level.END) {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
        }

        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                    (id, inv, player) -> new DeathInheritContainerMenu(id, inv),
                    Component.empty()
            ));
        }

        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide);
    }
}
