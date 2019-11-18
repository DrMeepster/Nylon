package drmeepster.nylon;

import java.util.NoSuchElementException;

import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;

public abstract class ContainerItem<C extends Container, S extends AbstractContainerScreen<C>, I extends Inventory>
	extends Item{

	public final Identifier CONTAINER_ID;

	public int size;

	public ContainerItem(Settings settings, Identifier containerId, int size){
		super(settings);

		this.CONTAINER_ID = containerId;
		this.size = size;
	}

	public ItemStack getContainerStack(PlayerEntity player, PacketByteBuf buf){
		return player.inventory.getInvStack(buf.duplicate().readInt());
	}

	public void open(World world, PlayerEntity player, ItemStack stack){
		if(world.isClient){
			return;
		}

		int index = player.inventory.getSlotWithStack(stack);

		if(index == -1){
			throw new NoSuchElementException("stack not in player's inventory");
		}

		ContainerProviderRegistry.INSTANCE.openContainer(this.CONTAINER_ID, player, buf -> {
			buf.writeInt(index);
		});
	}

	public DefaultedList<ItemStack> getStacks(ItemStack stack){
		DefaultedList<ItemStack> list = DefaultedList.ofSize(this.size, ItemStack.EMPTY);

		Inventories.fromTag(stack.getTag(), list);

		return list;
	}

	public void setStacks(ItemStack stack, DefaultedList<ItemStack> stacks){
		Inventories.toTag(stack.getTag(), stacks);
	}

	public abstract class ContainerProvider implements ContainerFactory<Container>{

		@Override
		public C create(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf){
			return this.create(syncId, identifier, player, ContainerItem.this.getContainerStack(player, buf), buf);
		}

		public abstract C create(int syncId, Identifier identifier, PlayerEntity player, ItemStack stack,
			PacketByteBuf buf);
	}

	@SuppressWarnings("rawtypes")
	public abstract class ScreenProvider implements ContainerFactory<AbstractContainerScreen>{

		@Override
		public S create(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf){
			C container = ContainerProviderImpl.INSTANCE.createContainer(syncId, identifier, player, buf);

			ItemStack stack = ContainerItem.this.getContainerStack(player, buf);

			return this.create(syncId, identifier, player, stack, container, buf);
		}

		public abstract S create(int syncId, Identifier identifier, PlayerEntity player, ItemStack stack, C container,
			PacketByteBuf buf);
	}

	/**
	 * Should be used for creation of Container
	 */
	public abstract class InventoryProvider implements ItemInventoryProvider<I>{

		@Override
		public I create(int syncId, Identifier identifier, PlayerEntity player, ItemStack stack, PacketByteBuf buf){
			return this.create(syncId, identifier, player, stack, ContainerItem.this.getStacks(stack), buf);
		}

		public abstract I create(int syncId, Identifier identifier, PlayerEntity player, ItemStack stack,
			DefaultedList<ItemStack> list, PacketByteBuf buf);
	}
	
	public abstract class ContainerInventory extends BasicInventory{
		
		public ItemStack containerStack;
		
		public ContainerInventory(ItemStack containerStack, int size){
			super(size);
			
			this.containerStack = containerStack;
		}

		public ContainerInventory(ItemStack containerStack, ItemStack... stacks){
			super(stacks);
			
			this.containerStack = containerStack;
		}
		
		{
			this.addListener(new ContainerInventoryListener());
		}
		
		public ItemStack getContainerStack(){
			return containerStack;
		}
	}
	
	public class ContainerInventoryListener implements InventoryListener{

		@Override
		public void onInvChange(Inventory inventory){
			@SuppressWarnings("unchecked")
			ContainerInventory inv = (ContainerInventory) inventory;
			
			ContainerItem.this.setStacks(inv.getContainerStack(), ((PublicInventory)inv).getStacks());
		}
	}
}
