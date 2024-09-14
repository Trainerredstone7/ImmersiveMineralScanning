package trainerredstone7.immersivemineralscanning;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import trainerredstone7.immersivemineralscanning.blocks.WideRangeSampleDrillBlock;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.WideRangeSampleDrillTile;
import trainerredstone7.immersivemineralscanning.proxy.CommonProxy;

import org.apache.logging.log4j.Logger;

@Mod(modid = ImmersiveMineralScanning.MODID, name = ImmersiveMineralScanning.NAME, version = ImmersiveMineralScanning.VERSION, dependencies = "required-after:immersiveengineering; after:immersivepetroleum")
@Mod.EventBusSubscriber
public class ImmersiveMineralScanning
{
    public static final String MODID = "immersivemineralscanning";
    public static final String NAME = "Immersive Mineral Scanning";
    public static final String VERSION = "0.1";

    private static Logger logger;
    @SidedProxy(clientSide = "trainerredstone7.immersivemineralscanning.proxy.ClientProxy", serverSide = "trainerredstone7.immersivemineralscanning.proxy.ServerProxy")
    public static CommonProxy proxy;
    @Mod.Instance
    public static ImmersiveMineralScanning instance;
    @GameRegistry.ObjectHolder(ImmersiveMineralScanning.MODID+":rangedsampledrill")
    public static WideRangeSampleDrillBlock wideRangeSampleDrillBlock;
    public static final CreativeTabs creativeTab = new ImmersiveMineralScanningTab();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);
//        logger.info("preinit");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
//    	logger.info("about to register block");
    	event.getRegistry().register(new WideRangeSampleDrillBlock());
    	GameRegistry.registerTileEntity(WideRangeSampleDrillTile.class, MODID + "_rangedsampledrill");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	event.getRegistry().register(new ItemBlock(wideRangeSampleDrillBlock).setRegistryName(wideRangeSampleDrillBlock.getRegistryName()).setCreativeTab(creativeTab));
//    	logger.info("registered item");
    }
}
