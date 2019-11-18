package drmeepster.nylon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import drmeepster.nylon.PublicInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.util.DefaultedList;

@Mixin(BasicInventory.class)
public abstract class BasicInventoryMixin implements Inventory, RecipeInputProvider, PublicInventory {
	
	@Shadow
	private DefaultedList<ItemStack> stackList;
	
	@Override
	public DefaultedList<ItemStack> getStacks(){
		return stackList;
	}
}
