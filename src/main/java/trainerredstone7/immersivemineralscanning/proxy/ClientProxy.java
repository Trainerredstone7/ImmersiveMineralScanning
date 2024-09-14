package trainerredstone7.immersivemineralscanning.proxy;

import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;

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
