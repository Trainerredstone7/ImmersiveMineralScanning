package trainerredstone7.immersivemineralscanning;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeInt;

@Config(modid = "immersivemineralscanning", category = "general")
public class ConfigGeneral {	
	@RangeInt(min = 1, max = Integer.MAX_VALUE)
	@LangKey(value = "immersivemineralscanning.config.scanradius")
	public static int chunkRadius = 60;
	
	@RangeInt(min = 1, max = Integer.MAX_VALUE)
	@LangKey(value = "immersivemineralscanning.config.scanrate")
	public static int scanrate = 100;
}
