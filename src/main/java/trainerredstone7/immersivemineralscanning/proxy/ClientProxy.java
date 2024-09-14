package trainerredstone7.immersivemineralscanning.proxy;

import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.immersiveengineering.client.render.TileRenderSampleDrill;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntitySampleDrill;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.WideRangeSampleDrillTile;
import trainerredstone7.immersivemineralscanning.render.WideRangeSampleDrillRender;

/**
 * @author trainerredstone7
 * All clientside init.
 *
 */

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ImmersiveMineralScanning.wideRangeSampleDrillBlock), 0, new ModelResourceLocation(ImmersiveMineralScanning.wideRangeSampleDrillBlock.getRegistryName(), "inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(WideRangeSampleDrillTile.class, new WideRangeSampleDrillRender());
    }
    /*
     * TODO figure out everything to get custom IE renderer working for my block
     * https://www.mcjty.eu/docs/1.12/rendering/block-obj and following tutorials will probably be
     * helpful in deciphering IE code
     */
    public void preInit(FMLPreInitializationEvent event){
		OBJLoader.INSTANCE.addDomain(ImmersiveMineralScanning.MODID);
		IEOBJLoader.instance.addDomain(ImmersiveMineralScanning.MODID);
    }
}
