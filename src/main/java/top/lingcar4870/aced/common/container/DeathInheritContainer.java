package top.lingcar4870.aced.common.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.lingcar4870.aced.Aced;
import top.lingcar4870.aced.common.DeathInheritDataHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DeathInheritContainer extends AbstractContainerMenu {
    private static final int MAX_SIZE = Aced.DEATH_INHERIT_SLOTS;
    private final SimpleContainer container;
    private final int containerSize;

    public DeathInheritContainer(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(MAX_SIZE), MAX_SIZE);
    }

    public DeathInheritContainer(int containerId, Inventory playerInventory, SimpleContainer pContainer, int containerSize) {
        super(AcedContainers.DEATH_INHERIT_MENU.get(), containerId);

        checkContainerSize(pContainer, containerSize);
        this.container = pContainer;
        this.containerSize = containerSize;
        pContainer.startOpen(playerInventory.player);
        int containerRows = (containerSize - 1) / 9 + 1;

        int i = (containerRows - 4) * 18;

        for(int j = 0; j < containerRows; ++j) {
            for(int k = 0; k < 9; ++k) {
                if (k + j * 9 >= this.containerSize) break;
                this.addSlot(new Slot(pContainer, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }

        if (playerInventory.player.getPersistentData().contains("DeathInheritData")) {
            container.fromTag(DeathInheritDataHelper.getDeathInheritData(playerInventory.player).getList("Items", Tag.TAG_COMPOUND));
        }
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.level().dimension() == Level.END;
    }

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        DeathInheritDataHelper.writeDeathInheritData(pPlayer, this.saveData());
        this.container.stopOpen(pPlayer);
    }

    private CompoundTag saveData() {
        CompoundTag ret = new CompoundTag();

        ret.put("Items", container.createTag());

        return ret;
    }

    public SimpleContainer getContainer() {
        return container;
    }

    public int getContainerSize() {
        return containerSize;
    }
}
