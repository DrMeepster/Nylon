package drmeepster.nylon;

import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

@FunctionalInterface
public interface ItemInventoryProvider<T extends Inventory> {

	public T create(int syncId, Identifier identifier, PlayerEntity player, ItemStack stack, Container container,
		PacketByteBuf buf);
}