package trainerredstone7.immersivemineralscanning.blocks.tileentities;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntitySampleDrill;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class WideRangeSampleDrillTile extends TileEntitySampleDrill {
	public void placeDummies(BlockPos pos, IBlockState state)
	{
		for(int i = 1; i <= 2; i++)
		{
			world.setBlockState(pos.add(0, i, 0), state);
			((TileEntitySampleDrill)world.getTileEntity(pos.add(0, i, 0))).dummy = i;
		}
	}
}
