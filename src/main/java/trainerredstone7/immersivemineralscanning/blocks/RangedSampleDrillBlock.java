package trainerredstone7.immersivemineralscanning.blocks;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.shader.CapabilityShader;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.CommonProxy;
import blusunrize.immersiveengineering.common.blocks.BlockIETileProvider;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IActiveState;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedHasObjProperty;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAttachedIntegerProperies;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IConfigurableSides;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDualState;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDynamicTexture;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IFaceShape;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHasDummyBlocks;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHasObjProperty;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ILightValue;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IMirrorAble;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.INeighbourChangeTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlacementInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPropertyPassthrough;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
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
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.RangedSampleDrillTile;

/**
 * 
 * @author trainerredstone7
 *
 */

/*
 * 
 * ClientUtils probably useful, particularly getSprite(ResourceLocation)
 * 
 */

public class RangedSampleDrillBlock extends Block {
	
	public RangedSampleDrillBlock() {
		super(Material.IRON);
//		super("metal_device1", Material.IRON, PropertyEnum.create("type", BlockTypes_WideRangeSampleDrill.class), ItemBlockIEBase.class, IEProperties.FACING_ALL, IEProperties.MULTIBLOCKSLAVE, IEProperties.BOOLEANS[0], Properties.AnimationProperty, IOBJModelCallback.PROPERTY, IEProperties.OBJ_TEXTURE_REMAP);
		this.setHardness(3.0F);
		this.setResistance(15.0F);
		lightOpacity = 0;
		setRegistryName("rangedsampledrill");
		setUnlocalizedName(ImmersiveMineralScanning.MODID+".rangedsampledrill");
		setCreativeTab(ImmersiveMineralScanning.CREATIVE_TAB);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
//		BlockStateContainer base = super.createBlockState();
		IProperty[] propertyArray = {IEProperties.FACING_ALL, IEProperties.MULTIBLOCKSLAVE, IEProperties.BOOLEANS[0]};
		IUnlistedProperty[] unlistedPropertyArray = {Properties.AnimationProperty, IOBJModelCallback.PROPERTY, IEProperties.OBJ_TEXTURE_REMAP, IEProperties.CONNECTIONS};
		return new ExtendedBlockState(this, propertyArray, unlistedPropertyArray);
//		IProperty[] array = new IProperty[1+this.additionalProperties.length];
//		array[0] = this.property;
//		for(int i = 0; i < this.additionalProperties.length; i++)
//			array[1+i] = this.additionalProperties[i];
//		if(this.additionalUnlistedProperties.length > 0)
//			return new ExtendedBlockState(this, array, additionalUnlistedProperties);
//		return new BlockStateContainer(this, array);
//		IUnlistedProperty[] unlisted = (base instanceof ExtendedBlockState)?((ExtendedBlockState)base).getUnlistedProperties().toArray(new IUnlistedProperty[0]): new IUnlistedProperty[0];
//		unlisted = Arrays.copyOf(unlisted, unlisted.length+1);
//		unlisted[unlisted.length-1] = IEProperties.CONNECTIONS;
//		return new ExtendedBlockState(this, base.getProperties().toArray(new IProperty[0]), unlisted);
	}
	
	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata,
	 * such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getActualState(state, world, pos);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IHasDummyBlocks) {
			state = state.withProperty(IEProperties.MULTIBLOCKSLAVE, ((IHasDummyBlocks)tile).isDummy());
		}
		return state;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new RangedSampleDrillTile();
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		//Depending on how I implement this, may need to add a core sample as a drop if it has one
		super.getDrops(drops, world, pos, state, fortune);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof RangedSampleDrillTile) {
			((RangedSampleDrillTile)tile).placeDummies(pos, state);
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof RangedSampleDrillTile)
			((RangedSampleDrillTile)tile).breakDummies(pos, state);
		super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		Item item = Item.getItemFromBlock(this);
		return item==Items.AIR?ItemStack.EMPTY: new ItemStack(item, 1, this.damageDropped(world.getBlockState(pos)));
	}
	
	/**
	 * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On the
	 * Server, this may perform additional changes to the world, like pistons replacing the block with an extended base. On
	 * the client, the update may involve replacing tile entities or effects such as sounds or particles
	 */
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam)
	{
		if(worldIn.isRemote&&eventID==255)
		{
			worldIn.notifyBlockUpdate(pos, state, state, 3);
			return true;
		}
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity!=null&&tileentity.receiveClientEvent(eventID, eventParam);
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getExtendedState(state, world, pos);
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState extended = (IExtendedBlockState)state;
			TileEntity te = world.getTileEntity(pos);
			if(te!=null) {
				if(te instanceof IHasObjProperty) //this is probably how the animation gets done
					extended = extended.withProperty(Properties.AnimationProperty, new OBJState(((IHasObjProperty)te).compileDisplayList(), true));
			}
			state = extended;
		}
		return state;
	}
	
	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack heldItem = player.getHeldItem(hand);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof RangedSampleDrillTile)
		{
			if (player.isSneaking()) {
				ImmersiveMineralScanning.proxy.openRangedSampleDrillGui((RangedSampleDrillTile) ((RangedSampleDrillTile) tile).getGuiMaster());
				return true;
			}
			else {
				boolean b = ((RangedSampleDrillTile)tile).interact(side, player, hand, heldItem, hitX, hitY, hitZ);
				return b;
			}
		}
		else return false;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return 0;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side)
	{
		//since I'm not doing all the meta block stuff maybe there is a way to shorten this down
		if(side!=null)
		{
			AxisAlignedBB bb = getBoundingBox(state, world, pos);
			double wMin = side.getAxis()==Axis.X?bb.minZ: bb.minX;
			double wMax = side.getAxis()==Axis.X?bb.maxZ: bb.maxX;
			double hMin = side.getAxis()==Axis.Y?bb.minZ: bb.minY;
			double hMax = side.getAxis()==Axis.Y?bb.maxZ: bb.maxY;
			if(wMin==0&&hMin==0&&wMax==1&&hMax==1)
				return BlockFaceShape.SOLID;
			else if(hMin==0&&hMax==1&&wMin==(1-wMax))
			{
				if(wMin > .375)
					return BlockFaceShape.MIDDLE_POLE_THIN;
				else if(wMin > .3125)
					return BlockFaceShape.MIDDLE_POLE;
				else
					return BlockFaceShape.MIDDLE_POLE_THICK;
			}
			else if(hMin==wMin&&hMax==wMax)
			{
				if(wMin > .375)
					return BlockFaceShape.CENTER_SMALL;
				else if(wMin > .3125)
					return BlockFaceShape.CENTER;
				else
					return BlockFaceShape.CENTER_BIG;
			}
				return BlockFaceShape.UNDEFINED;
		}
		return super.getBlockFaceShape(world, state, pos, side);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if(world.getBlockState(pos).getBlock()!=this) //do not know when this would happen
			return FULL_BLOCK_AABB;
		return super.getBoundingBox(state, world, pos);
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}

	//might need to override this depending on if I implement an inventory
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
	{
		return 0;
	}
	
	/**
	 * Called When an Entity Collided with the Block
	 */
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityIEBase)
			((TileEntityIEBase)te).onEntityCollision(world, entity);
	}
	
//	@Override
//	public boolean useCustomStateMapper()
//	{
//		return true;
//	}
//	
	
//	/*Called by client proxy. This string is appended to the meta block name 
//	*("metal_device1" for the block used for the sample drill) and used as the resource location string for the registry
//	*/
//	
//	@Override
//	public String getCustomStateMapping(int meta, boolean itemBlock) {
//		return "core_drill";
//	}
	
//	@Override
//	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack) {
//
//		for(int hh = 1; hh <= 2; hh++) {
//				BlockPos pos2 = pos.add(0, hh, 0);
//				if(world.isOutsideBuildHeight(pos2)||!world.getBlockState(pos2).getBlock().isReplaceable(world, pos2))
//					return false;
//		}
//		return true;
//
//	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}
	
	/**
	 * @return true if the state occupies all of its 1x1x1 cube
	 */
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		if(type.equals(Lib.TOOL_HAMMER)) {
			return true;
		}
		return super.isToolEffective(type, state);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return 0;
//		if(state==null||!this.equals(state.getBlock()))
//			return 0;
//		return state.getValue(this.property).getMeta();
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.CUTOUT;
	}
	
//	@Override
//	public IBlockState getStateFromMeta(int meta)
//	{
//		return this.getDefaultState().withProperty(this.property, fromMeta(meta));
//	}
	
	
//	@Override
//	public boolean allowHammerHarvest(IBlockState state)
//	{
//		return true;
//	}
	
	//Override since this is not a block from Immersive Engineering
//	@Override
//	public String createRegistryName()
//	{
//		return ImmersiveMineralScanning.MODID+":"+name;
//	}
}
