package trainerredstone7.immersivemineralscanning.blocks;

import java.util.Arrays;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.BlockIETileProvider;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDevice1;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice1;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityBelljar;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityBlastFurnacePreheater;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityChargingStation;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityDynamo;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityElectricLantern;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFloodlight;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFluidPipe;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFurnaceHeater;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntitySampleDrill;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityTeslaCoil;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityThermoelectricGen;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityTurret;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityTurretChem;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityTurretGun;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.WideRangeSampleDrillTile;

/**
 * 
 * @author trainerredstone7
 *
 */

/*
 * I've added everything from BlockMetalDevice1 that could possibly be necessary.
 * TODO: Figure out whether to extend BlockIETileProvider or go further up the hierarchy
 * BlockIETileProvider is probably correct class to extend to get proper linkage between block and tile
 * Still need to figure out for sure how block, tile, and model get linked though
 * 
 * BlockIETileProvider itself doesn't connect to any rendering - must be on either the tile or render side
 * The tile doesn't connect to rendering either
 * 
 * ClientUtils probably useful, particularly getSprite(resourceLocation_
 * The renderer might be looking at getCustomStateMapping
 */

public class WideRangeSampleDrillBlock extends BlockIETileProvider<BlockTypes_WideRangeSampleDrill> {
	
	public WideRangeSampleDrillBlock() {
		super("metal_device1", Material.IRON, PropertyEnum.create("type", BlockTypes_WideRangeSampleDrill.class), ItemBlockIEBase.class, IEProperties.FACING_ALL, IEProperties.MULTIBLOCKSLAVE, IEProperties.BOOLEANS[0], Properties.AnimationProperty, IOBJModelCallback.PROPERTY, IEProperties.OBJ_TEXTURE_REMAP);
		this.setHardness(3.0F);
		this.setResistance(15.0F);
		lightOpacity = 0;
		this.setMetaBlockLayer(BlockTypes_WideRangeSampleDrill.WIDE_RANGE_SAMPLE_DRILL.getMeta(), BlockRenderLayer.CUTOUT);
		this.setNotNormalBlock(BlockTypes_WideRangeSampleDrill.WIDE_RANGE_SAMPLE_DRILL.getMeta());
		this.setMetaMobilityFlag(BlockTypes_WideRangeSampleDrill.WIDE_RANGE_SAMPLE_DRILL.getMeta(), EnumPushReaction.BLOCK);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		BlockStateContainer base = super.createBlockState();
		IUnlistedProperty[] unlisted = (base instanceof ExtendedBlockState)?((ExtendedBlockState)base).getUnlistedProperties().toArray(new IUnlistedProperty[0]): new IUnlistedProperty[0];
		unlisted = Arrays.copyOf(unlisted, unlisted.length+1);
		unlisted[unlisted.length-1] = IEProperties.CONNECTIONS;
		return new ExtendedBlockState(this, base.getProperties().toArray(new IProperty[0]), unlisted);
	}
	
	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}
	
	//Called by client proxy, presumably to get the model
	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock) {
		return "core_drill";
	}
	
	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack) {

		for(int hh = 1; hh <= 2; hh++) {
				BlockPos pos2 = pos.add(0, hh, 0);
				if(world.isOutsideBuildHeight(pos2)||!world.getBlockState(pos2).getBlock().isReplaceable(world, pos2))
					return false;
		}
		return true;

	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
	public TileEntity createBasicTE(World world, BlockTypes_WideRangeSampleDrill type)
	{
		return new WideRangeSampleDrillTile();
	}
	
	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}
	
	//Override since this is not a block from Immersive Engineering
	@Override
	public String createRegistryName()
	{
		return ImmersiveMineralScanning.MODID+":"+name;
	}
}
