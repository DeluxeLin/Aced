package top.lingcar4870.aced.common.container;

import com.google.common.collect.Lists;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.lingcar4870.aced.common.DeathInheritDataHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class DeathInheritContainerMenu extends AbstractContainerMenu {
    public static final int MAX_STACK_SIZE = 8;
    private static final int SIZE = 9;
    private final DeathInheritContainer container;
    private final int containerSize;

    public DeathInheritContainerMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new DeathInheritContainer(SIZE), SIZE);
    }

    public DeathInheritContainerMenu(int containerId, Inventory playerInventory, DeathInheritContainer pContainer, int containerSize) {
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

    public DeathInheritContainer getContainer() {
        return container;
    }

    public int getContainerSize() {
        return containerSize;
    }

    @MethodsReturnNonnullByDefault
    public static class DeathInheritContainer implements Container {

        private final int size;
        private final NonNullList<ItemStack> items;
        @Nullable
        private List<ContainerListener> listeners;

        public void addListener(ContainerListener pListener) {
            if (this.listeners == null) {
                this.listeners = Lists.newArrayList();
            }

            this.listeners.add(pListener);
        }

        public void removeListener(ContainerListener pListener) {
            if (this.listeners != null) {
                this.listeners.remove(pListener);
            }
        }

        public DeathInheritContainer(int pSize) {
            this.size = pSize;
            this.items = NonNullList.withSize(pSize, ItemStack.EMPTY);
        }

        @Override
        public int getContainerSize() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            for(ItemStack itemstack : this.items) {
                if (!itemstack.isEmpty()) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public ItemStack getItem(int pIndex) {
            return pIndex >= 0 && pIndex < this.items.size() ? this.items.get(pIndex) : ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int pSlot, int pAmount) {
            ItemStack itemstack = ContainerHelper.removeItem(this.items, pSlot, pAmount);
            if (!itemstack.isEmpty()) {
                this.setChanged();
            }

            return itemstack;
        }

        @Override
        public ItemStack removeItemNoUpdate(int pIndex) {
            ItemStack itemstack = this.items.get(pIndex);
            if (itemstack.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                this.items.set(pIndex, ItemStack.EMPTY);
                return itemstack;
            }
        }

        @Override
        public void setItem(int pSlot, ItemStack pStack) {
            this.items.set(pSlot, pStack);
            if (!pStack.isEmpty() && pStack.getCount() > this.getMaxStackSize()) {
                pStack.setCount(this.getMaxStackSize());
            }

            this.setChanged();
        }

        @Override
        public void setChanged() {
            if (this.listeners != null) {
                for(ContainerListener containerlistener : this.listeners) {
                    containerlistener.containerChanged(this);
                }
            }
        }

        @Override
        public boolean stillValid(Player pPlayer) {
            return pPlayer.level().dimension() == Level.END;
        }

        @Override
        public void clearContent() {
            this.items.clear();
            this.setChanged();
        }

        @Override
        public int getMaxStackSize() {
            return MAX_STACK_SIZE;
        }

        public void fromTag(ListTag pContainerNbt) {
            for(int i = 0; i < this.getContainerSize(); ++i) {
                this.setItem(i, ItemStack.EMPTY);
            }

            for(int k = 0; k < pContainerNbt.size(); ++k) {
                CompoundTag compoundtag = pContainerNbt.getCompound(k);
                int j = compoundtag.getByte("Slot") & 255;
                if (j < this.getContainerSize()) {
                    this.setItem(j, ItemStack.of(compoundtag));
                }
            }
        }

        public ListTag createTag() {
            ListTag listtag = new ListTag();

            for(int i = 0; i < this.getContainerSize(); ++i) {
                ItemStack itemstack = this.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundtag = new CompoundTag();
                    compoundtag.putByte("Slot", (byte)i);
                    itemstack.save(compoundtag);
                    listtag.add(compoundtag);
                }
            }

            return listtag;
        }
    }
}
