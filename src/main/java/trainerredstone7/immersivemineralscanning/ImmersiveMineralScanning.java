package trainerredstone7.immersivemineralscanning;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
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
import net.minecraftforge.fml.common.Optional;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.ReservoirType;

@Mod(modid = ImmersiveMineralScanning.MODID, name = ImmersiveMineralScanning.NAME, version = ImmersiveMineralScanning.VERSION, dependencies = "required-after:forge@[14.23.1.2602,); required-after:immersiveengineering; after:immersivepetroleum")
@Mod.EventBusSubscriber
public class ImmersiveMineralScanning
{
    public static final String MODID = "immersivemineralscanning";
    public static final String NAME = "Immersive Mineral Scanning";
    public static final String VERSION = "0.1";

    public static Logger logger;
    @SidedProxy(clientSide = "trainerredstone7.immersivemineralscanning.proxy.ClientProxy", serverSide = "trainerredstone7.immersivemineralscanning.proxy.ServerProxy")
    public static CommonProxy proxy;
    @Mod.Instance
    public static ImmersiveMineralScanning instance;
    @GameRegistry.ObjectHolder(ImmersiveMineralScanning.MODID+":rangedsampledrill")
    public static RangedSampleDrillBlock wideRangeSampleDrillBlock;
    public static final CreativeTabs CREATIVE_TAB = new ImmersiveMineralScanningTab();
    public static final SimpleNetworkWrapper PACKET_HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    public static boolean immersivePetroleumPresent = false;
    //true if it's a reservoir, false if it's a mineral
    public Map<String, Boolean> resourceTypeMap;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);
//        File directory = event.getModConfigurationDirectory();
//        config = new Configuration(new File(directory.getPath(), "immersivemineralscanning.cfg"));
//        ConfigHelper.readConfig(config);
        logger.info("preinit");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        PACKET_HANDLER.registerMessage(SearchTargetUpdatePacket.ServerHandler.class, SearchTargetUpdatePacket.class, 0, Side.SERVER);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        resourceTypeMap = ExcavatorHandler.mineralList.keySet().stream().collect(Collectors.toMap(m -> m.name, m -> false));
        if (Loader.isModLoaded("immersivepetroleum")) {
			immersivePetroleumPresent = true;
			//can't use lambda or Java freaks out about ReservoirType; need to stuff it in an Optional method instead
			PumpjackHandler.reservoirList.keySet().stream().forEach(this::addToResourceTypeMap);
        }
        
        //        if (config.hasChanged()) {
//            config.save();
//        }
    }
    
    @Optional.Method(modid = "immersivepetroleum")
    private void addToResourceTypeMap(ReservoirType r) {
    	resourceTypeMap.putIfAbsent(r.name, true);
    }
    
    @SuppressWarnings("deprecation")
	@SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
//    	logger.info("about to register block");
    	event.getRegistry().register(new RangedSampleDrillBlock());
    	GameRegistry.registerTileEntity(RangedSampleDrillTile.class, MODID + "_rangedsampledrill");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	event.getRegistry().register(new ItemBlock(wideRangeSampleDrillBlock).setRegistryName(wideRangeSampleDrillBlock.getRegistryName()));
//    	logger.info("registered item");
    }
    
    @SubscribeEvent
    public static void syncConfig(ConfigChangedEvent.OnConfigChangedEvent event) {
    	if (event.getModID().equals(MODID)) {
    		ConfigManager.sync(MODID, Config.Type.INSTANCE);
    	}
    }
    
//	/**
//	 * Needs optional annotation so generic reference to ReservoirType doesn't blow up startup
//	 */
//  @Optional.Method(modid = "immersivepetroleum")
//	private void putReservoirTypesInResourceTypeMap() {
//		PumpjackHandler.reservoirList.keySet().stream().forEach(this::addToResourceTypeMap);
//	}


//    @Optional.Method(modid = "immersivepetroleum")
//    public void putReservoirTypesInResourceTypeMap() {
//    	
//    }
}
