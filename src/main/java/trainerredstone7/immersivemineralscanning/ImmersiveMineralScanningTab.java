package trainerredstone7.immersivemineralscanning;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ImmersiveMineralScanningTab extends CreativeTabs {

	public ImmersiveMineralScanningTab() {
		super(ImmersiveMineralScanning.MODID);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(Item.getItemFromBlock(ImmersiveMineralScanning.wideRangeSampleDrillBlock));
	}

}
