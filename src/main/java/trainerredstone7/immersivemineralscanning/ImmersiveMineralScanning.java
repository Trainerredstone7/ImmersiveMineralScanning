package trainerredstone7.immersivemineralscanning;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.lib.manual.ManualPages;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.ReservoirType;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import trainerredstone7.immersivemineralscanning.blocks.RangedSampleDrillBlock;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.RangedSampleDrillTile;
import trainerredstone7.immersivemineralscanning.network.SearchTargetUpdatePacket;
import trainerredstone7.immersivemineralscanning.proxy.CommonProxy;

/**
 * @author trainerredstone7
 *
 */
@Mod(modid = ImmersiveMineralScanning.MODID, name = ImmersiveMineralScanning.NAME, version = ImmersiveMineralScanning.VERSION, dependencies = "required-after:forge@[14.23.1.2602,); required-after:immersiveengineering; after:immersivepetroleum")
@Mod.EventBusSubscriber
public class ImmersiveMineralScanning
{
    public static final String MODID = "immersivemineralscanning";
    public static final String NAME = "Immersive Mineral Scanning";
    public static final String VERSION = "1.1";

    public static Logger logger;
    @SidedProxy(clientSide = "trainerredstone7.immersivemineralscanning.proxy.ClientProxy", serverSide = "trainerredstone7.immersivemineralscanning.proxy.ServerProxy")
    public static CommonProxy proxy;
    @Mod.Instance
    public static ImmersiveMineralScanning instance;
    @GameRegistry.ObjectHolder(ImmersiveMineralScanning.MODID+":rangedsampledrill")
    public static RangedSampleDrillBlock wideRangeSampleDrillBlock;
    public static final CreativeTabs CREATIVE_TAB = new ImmersiveMineralScanningTab();
    public static final SimpleNetworkWrapper PACKET_HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel("imscanning"); //mod id is too long
    public static boolean immersivePetroleumPresent = false;
    //true if it's a reservoir, false if it's a mineral
    public Map<String, Boolean> resourceTypeMap;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        PACKET_HANDLER.registerMessage(SearchTargetUpdatePacket.ServerHandler.class, SearchTargetUpdatePacket.class, 0, Side.SERVER);
        blusunrize.immersiveengineering.common.Config.manual_int.put("rangedsampledrill_chunkRadius", ConfigGeneral.chunkRadius);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        resourceTypeMap = ExcavatorHandler.mineralList.keySet().stream().collect(Collectors.toMap(m -> m.name, m -> false));
        if (Loader.isModLoaded("immersivepetroleum")) {
			immersivePetroleumPresent = true;
			logger.info("Immersive Petroleum present, enabling compatibility");
			//can't use lambda or Java freaks out about ReservoirType; need to stuff it in an Optional method instead
			PumpjackHandler.reservoirList.keySet().forEach(this::addToResourceTypeMap);
        }
        proxy.postInit(event);
    }
    
    @Optional.Method(modid = "immersivepetroleum")
    private void addToResourceTypeMap(ReservoirType r) {
    	resourceTypeMap.putIfAbsent(r.name, true);
    }
    
    @SuppressWarnings("deprecation")
	@SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
    	event.getRegistry().register(new RangedSampleDrillBlock());
    	GameRegistry.registerTileEntity(RangedSampleDrillTile.class, MODID + "_rangedsampledrill");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	event.getRegistry().register(new ItemBlock(wideRangeSampleDrillBlock).setRegistryName(wideRangeSampleDrillBlock.getRegistryName()));
    }
    
    @SubscribeEvent
    public static void syncConfig(ConfigChangedEvent.OnConfigChangedEvent event) {
    	if (event.getModID().equals(MODID)) {
    		ConfigManager.sync(MODID, Config.Type.INSTANCE);
    	}
    }
}
