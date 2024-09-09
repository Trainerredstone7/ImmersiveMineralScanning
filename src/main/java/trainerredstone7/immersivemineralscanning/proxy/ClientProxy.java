package trainerredstone7.immersivemineralscanning.proxy;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author trainerredstone7
 * All clientside init.
 *
 */

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    }
}
