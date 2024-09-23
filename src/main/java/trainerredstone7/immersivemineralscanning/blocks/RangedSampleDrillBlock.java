package trainerredstone7.immersivemineralscanning.blocks;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHasDummyBlocks;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHasObjProperty;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.RangedSampleDrillTile;

/**
 * 
 * @author trainerredstone7
 *
 */

@SuppressWarnings("deprecation")
public class RangedSampleDrillBlock extends Block {
	
	public RangedSampleDrillBlock() {
		super(Material.IRON);
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
		IProperty[] propertyArray = {IEProperties.FACING_ALL, IEProperties.MULTIBLOCKSLAVE, IEProperties.BOOLEANS[0]};
		IUnlistedProperty[] unlistedPropertyArray = {Properties.AnimationProperty, IOBJModelCallback.PROPERTY, IEProperties.OBJ_TEXTURE_REMAP, IEProperties.CONNECTIONS};
		return new ExtendedBlockState(this, propertyArray, unlistedPropertyArray);
	}
	
	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata,
	 * such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
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
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
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
				if(te instanceof IHasObjProperty)
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
		return BlockFaceShape.SOLID;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return 0;
	}
	
	/**
	 * Called When an Entity Collided with the Block
	 */
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityIEBase)
			((TileEntityIEBase)te).onEntityCollision(world, entity);
	}
	
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
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state) {
		if(type.equals(Lib.TOOL_HAMMER)) {
			return true;
		}
		return super.isToolEffective(type, state);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT;
	}
}
