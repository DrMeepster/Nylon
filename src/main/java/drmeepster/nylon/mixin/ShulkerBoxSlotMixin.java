package drmeepster.nylon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import drmeepster.nylon.AntiRecursiveSlot;
import net.minecraft.container.ShulkerBoxSlot;
import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

@Mixin(ShulkerBoxSlot.class)
public abstract class ShulkerBoxSlotMixin extends Slot{

	public ShulkerBoxSlotMixin(Inventory inventory, int num, int x, int y){
		super(inventory, num, x, y);
	}

	//Shulker box on anti recursive tag
	@Overwrite
	public boolean canInsert(ItemStack stack){
		return AntiRecursiveSlot.isAllowed(stack);
	}
}
