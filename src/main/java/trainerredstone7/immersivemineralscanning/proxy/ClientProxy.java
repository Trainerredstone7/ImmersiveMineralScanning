package trainerredstone7.immersivemineralscanning.proxy;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.RangedSampleDrillTile;
import trainerredstone7.immersivemineralscanning.gui.RangedSampleDrillGui;
import trainerredstone7.immersivemineralscanning.render.RangedSampleDrillRender;

/**
 * @author trainerredstone7
 * All clientside init.
 *
 */

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	
	
    @Override
	public void postInit(FMLPostInitializationEvent e) {
        ManualPages.Crafting manualPage0 = new ManualPages.Crafting(ManualHelper.getManual(), "rangedsampledrill0", new ItemStack(Item.getItemFromBlock(ImmersiveMineralScanning.wideRangeSampleDrillBlock)));
        ManualPages.Text manualPage1 = new ManualPages.Text(ManualHelper.getManual(), "rangedsampledrill1");
        ManualHelper.addEntry("rangedsampledrill", ManualHelper.CAT_GENERAL, manualPage0, manualPage1);
	}

	@SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ImmersiveMineralScanning.wideRangeSampleDrillBlock), 0, new ModelResourceLocation(ImmersiveMineralScanning.wideRangeSampleDrillBlock.getRegistryName(), "inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(RangedSampleDrillTile.class, new RangedSampleDrillRender());
    }

    public void preInit(FMLPreInitializationEvent event){
		OBJLoader.INSTANCE.addDomain(ImmersiveMineralScanning.MODID);
		IEOBJLoader.instance.addDomain(ImmersiveMineralScanning.MODID);
    }
	@Override
	public void openRangedSampleDrillGui(RangedSampleDrillTile tile) {
		Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new RangedSampleDrillGui(tile)));
	}
    
    
}
