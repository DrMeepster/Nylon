package drmeepster.nylon.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

public interface BasicInventoryExtension{

	public DefaultedList<ItemStack> getStacks();
}
