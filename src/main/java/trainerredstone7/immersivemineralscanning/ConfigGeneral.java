package trainerredstone7.immersivemineralscanning;

import org.apache.logging.log4j.Level;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Configuration;

@Config(modid = "immersivemineralscanning", category = "general")
public class ConfigGeneral {
//	@Ignore
//	private static final int MIN_CHUNK_RADIUS = 1;
//	@Ignore
//	private static final int MAX_CHUNK_RADIUS = Integer.MAX_VALUE;
//	@Ignore
//	private static final int DEFAULT_CHUNK_RADIUS = 60;
	
	@RangeInt(min = 1, max = Integer.MAX_VALUE)
	@LangKey(value = "immersivemineralscanning.config.scanradius")
	public static int chunkRadius = 60;
	
	@RangeInt(min = 1, max = Integer.MAX_VALUE)
	@LangKey(value = "immersivemineralscanning.config.scanrate")
	public static int scanrate = 10;

//	public static void readConfig(Configuration config) {
//		ImmersiveMineralScanning.logger.info("reading config");
//        try {
//            config.load();
//            config.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
//            chunkRadius = config.getInt("chunkRadius", CATEGORY_GENERAL, DEFAULT_CHUNK_RADIUS, MIN_CHUNK_RADIUS, MAX_CHUNK_RADIUS, new TextComponentTranslation("config.immersivemineralscanning.chunkradius").getFormattedText());
//        } catch (Exception e) {
//            ImmersiveMineralScanning.logger.log(Level.ERROR, "Problem loading config file!", e);
//        } finally {
//            if (config.hasChanged()) {
//                config.save();
//            }
//        }
//		
//	}

}
