package drmeepster.nylon;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

public interface PublicInventory extends Inventory{

	public DefaultedList<ItemStack> getStacks();
}
