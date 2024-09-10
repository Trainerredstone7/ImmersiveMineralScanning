package trainerredstone7.immersivemineralscanning.blocks;

import java.util.Locale;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase.IBlockEnum;
import net.minecraft.util.IStringSerializable;

public enum BlockTypes_WideRangeSampleDrill implements IStringSerializable, IBlockEnum {
	WIDE_RANGE_SAMPLE_DRILL;

	@Override
	public int getMeta() {
		return 0;
	}

	@Override
	public boolean listForCreative() {
		return true;
	}

	@Override
	public String getName() {
		return this.toString().toLowerCase(Locale.ENGLISH);
	}

}
