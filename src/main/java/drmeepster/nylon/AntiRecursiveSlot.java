package drmeepster.nylon;

import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AntiRecursiveSlot extends Slot{
	
	public static final Tag<Item> ANTIRECURSIVE = new ItemTags.CachingTag(new Identifier("nylon:antirecursive"));

	public AntiRecursiveSlot(Inventory inventory, int num, int x, int y){
		super(inventory, num, x, y);
	}
	
	@Override
	public boolean canInsert(ItemStack stack){
		return isAllowed(stack);
	}
	
	public static boolean isAllowed(ItemStack stack){
		return !ANTIRECURSIVE.contains(stack.getItem()); 
	}
}
