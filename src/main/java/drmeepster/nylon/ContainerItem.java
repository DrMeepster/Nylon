package drmeepster.nylon;

import java.util.NoSuchElementException;

import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;

public abstract class ContainerItem<C extends Container, S extends AbstractContainerScreen<C>, I extends Inventory> extends Item{

	public final Identifier CONTAINER_ID;

	public ContainerItem(Settings settings, Identifier containerId, int size){
		super(settings);

		this.CONTAINER_ID = containerId;
	}

	public ItemStack getContainerStack(PlayerEntity player, PacketByteBuf buf){
		return player.inventory.getInvStack(buf.readInt());
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

	public abstract class ContainerProvider implements ContainerFactory<C>{
		
		public ContainerProvider(InventoryProvider inventory){
			
		}

		@Override
		public C create(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf){
			return this.create(syncId, identifier, player, ContainerItem.this.getContainerStack(player, buf), buf);
		}

		public abstract C create(int syncId, Identifier identifier, PlayerEntity player, ItemStack stack,
			PacketByteBuf buf);
	}

	public abstract class ScreenProvider implements ContainerFactory<S>{

		@Override
		public S create(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf){
			Container container = ContainerProviderImpl.INSTANCE.createContainer(syncId, identifier, player, buf);

			ItemStack stack = ContainerItem.this.getContainerStack(player, buf);

			return this.create(syncId, identifier, player, stack, container, buf);
		}

		public abstract S create(int syncId, Identifier identifier, PlayerEntity player, ItemStack stack,
			Container container, PacketByteBuf buf);
	}

	/**
	 * Should be used for creation of Container
	 */
	public abstract class InventoryProvider implements ItemInventoryProvider<I>{

		public int size;

		public InventoryProvider(int estimatedSize){
			this.size = estimatedSize;
		}

		@Override
		public I create(int syncId, Identifier identifier, PlayerEntity player, ItemStack stack,
			Container container, PacketByteBuf buf){

			DefaultedList<ItemStack> list = DefaultedList.ofSize(this.size, ItemStack.EMPTY);

			Inventories.fromTag(stack.getTag(), list);

			return this.fromList(list);
		}

		public abstract I fromList(DefaultedList<ItemStack> list);

	}
}
