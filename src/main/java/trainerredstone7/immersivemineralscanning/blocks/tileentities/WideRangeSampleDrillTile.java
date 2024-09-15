package trainerredstone7.immersivemineralscanning.blocks.tileentities;

import java.util.Map;
import java.util.Random;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.DimensionChunkCoords;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralSelection;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralWorldInfo;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntitySampleDrill;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.OilWorldInfo;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.ReservoirType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Optional;
import trainerredstone7.immersivemineralscanning.ConfigGeneral;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;

/**
 * Tile for the sample drill.
 * @author TrainerRedstone7
 * 
 */

public class WideRangeSampleDrillTile extends TileEntitySampleDrill {
	
	//Whether the drill is looking for a reservoir vs. a mineral
	public boolean searchingForReservoir = false;
	public MineralMix mineralSearchTarget = ExcavatorHandler.mineralList.keySet().parallelStream().findAny().get();
	//This should always be a ReservoirType, but cannot define it as such since we're not sure Immersive Petroleum is loaded
	public Object reservoirSearchTarget;
	public boolean foundTarget = false;
	public DimensionChunkCoords targetLocation;
	
	@Override
	public void update()
	{
		ApiUtils.checkForNeedlessTicking(this);
		if(dummy!=0||world.isAirBlock(getPos().add(0, -1, 0))||!sample.isEmpty())
			return;
		if(world.isRemote&&active)
		{
			process += ConfigGeneral.scanrate;
			return;
		}

		boolean powered = world.isBlockIndirectlyGettingPowered(getPos()) > 0;
		final boolean prevActive = active;
		if(!active&&powered)
			active = true;
		else if(active&&!powered&&foundTarget)
			active = false;
		if (!world.isRemote) {
			ImmersiveMineralScanning.logger.info("active = " + active);
			ImmersiveMineralScanning.logger.info("sample empty? " + sample.isEmpty());
			ImmersiveMineralScanning.logger.info("energy: " + energyStorage.getEnergyStored());
		}

		if(active)
			if(energyStorage.extractEnergy(IEConfig.Machines.coredrill_consumption, false)==IEConfig.Machines.coredrill_consumption) {	
				ImmersiveMineralScanning.logger.info("has energy");
				if (!world.isRemote) {
					DimensionChunkCoords drillChunk = new DimensionChunkCoords(world.provider.getDimension(), getPos().getX() >> 4, getPos().getZ() >> 4);
					int maxChunks = (2 * ConfigGeneral.chunkRadius - 1) * (2 * ConfigGeneral.chunkRadius - 1);
//					ImmersiveMineralScanning.logger.info("scanrate:" + ConfigGeneral.scanrate);
					ImmersiveMineralScanning.logger.info("progress: " + process + "/" + maxChunks);
					for (int i = 0; i < ConfigGeneral.scanrate && process < maxChunks && !foundTarget; i++) {
						int[] offset = getOffsetFromIndex(process);
						DimensionChunkCoords chunkToSearch = drillChunk.withOffset(offset[0], offset[1]);
						if (!searchingForReservoir) {
							MineralMix mix = fastGetMineralWorldInfo(world, chunkToSearch, false).mineral;
							if (mix == mineralSearchTarget) {
								foundTarget = true;
								targetLocation = chunkToSearch;
							}
						} else if (ImmersiveMineralScanning.immersivePetroleumPresent) {
							ReservoirType type = fastGetOilWorldInfo(world, chunkToSearch).type;
							if (reservoirSearchTarget instanceof ReservoirType && (ReservoirType) reservoirSearchTarget == type) {
								foundTarget = true;
								targetLocation = chunkToSearch;
							}
						}
						ImmersiveMineralScanning.logger.info(chunkToSearch.toString() + ": " + foundTarget);
						process++;
					}
					if (foundTarget) {
						MineralWorldInfo info = ExcavatorHandler.getMineralWorldInfo(world, targetLocation.x, targetLocation.z);
						this.sample = createCoreSample(world, targetLocation.x, targetLocation.z, info);
						//might not need these here
						active = false;
						process = 0;
					}
					if (process >= maxChunks) {
						active = false;
						process = 0;
					}
				}
//				if(process >= IEConfig.Machines.coredrill_time)
//				{
//					int cx = getPos().getX() >> 4;
//					int cz = getPos().getZ() >> 4;
//					MineralWorldInfo info = ExcavatorHandler.getMineralWorldInfo(world, cx, cz);
//					this.sample = createCoreSample(world, (getPos().getX() >> 4), (getPos().getZ() >> 4), info);
//				}
				this.markDirty();
				this.markContainingBlockForUpdate(null);
			}
		if(prevActive!=active)
		{
			this.markDirty();
			this.markContainingBlockForUpdate(null);
		}
	}
	
	public void placeDummies(BlockPos pos, IBlockState state)
	{
		ImmersiveMineralScanning.logger.info(mineralSearchTarget.name);
		for(int i = 1; i <= 2; i++)
		{
			world.setBlockState(pos.add(0, i, 0), state);
			((TileEntitySampleDrill)world.getTileEntity(pos.add(0, i, 0))).dummy = i;
		}
	}
	
	/**
	 * Get the mineral info for a chunk without having to load or generate the chunk.
	 * @param world
	 * @param chunkCoords
	 * @param guaranteed
	 * @return
	 */
	public static MineralWorldInfo fastGetMineralWorldInfo(World world, DimensionChunkCoords chunkCoords, boolean guaranteed)
	{
		if(world.isRemote)
			return null;
		MineralWorldInfo worldInfo = ExcavatorHandler.mineralCache.get(chunkCoords);
		if(worldInfo==null)
		{
			MineralMix mix = null;
			/*
			 * Replacement for world.getChunkFromChunkCoords(chunkCoords.x, chunkCoords.z).getRandomWithSeed(940610)
			 * Same behavior, but does not load or generate the chunk
			 */
			Random r = new Random(world.getSeed() + (long)(chunkCoords.x * chunkCoords.x * 4987142) + (long)(chunkCoords.x * 5947611) + (long)(chunkCoords.z * chunkCoords.z) * 4392871L + (long)(chunkCoords.z * 389711) ^ 940610);
			double dd = r.nextDouble();

			boolean empty = !guaranteed&&dd > ExcavatorHandler.mineralChance;
			if(!empty)
			{
				MineralSelection selection = new MineralSelection(world, chunkCoords, 2);
				if(selection.getTotalWeight() > 0)
				{
					int weight = selection.getRandomWeight(r);
					for(Map.Entry<MineralMix, Integer> e : selection.getMinerals())
					{
						weight -= e.getValue();
						if(weight < 0)
						{
							mix = e.getKey();
							break;
						}
					}
				}
			}
			worldInfo = new MineralWorldInfo();
			worldInfo.mineral = mix;
			ExcavatorHandler.mineralCache.put(chunkCoords, worldInfo);
		}
		return worldInfo;
	}
	
	/**
	 * Get the reservoir info for a chunk without having to load or generate the chunk.
	 * @param world
	 * @param chunkCoords
	 * @return
	 */
	public static OilWorldInfo fastGetOilWorldInfo(World world, DimensionChunkCoords chunkCoords)
	{
		if (world.isRemote)
			return null;
		int dim = world.provider.getDimension();
		//chunkX and chunkZ should be divided by depositSize, but it's always 1 (and it's private)
		OilWorldInfo worldInfo = PumpjackHandler.oilCache.get(chunkCoords);
		if (worldInfo == null)
		{
			ReservoirType res = null;
			/*
			 * Replacement for world.getChunkFromChunkCoords(chunkCoords.x, chunkCoords.z).getRandomWithSeed(90210)
			 * Same behavior, but does not load or generate the chunk
			 */
			Random r = new Random(world.getSeed() + (long)(chunkCoords.x * chunkCoords.x * 4987142) + (long)(chunkCoords.x * 5947611) + (long)(chunkCoords.z * chunkCoords.z) * 4392871L + (long)(chunkCoords.z * 389711) ^ 90210);
			double dd = r.nextDouble();
			boolean empty = dd > PumpjackHandler.oilChance;
			double size = r.nextDouble();
			int query = r.nextInt();

			if (!empty)
			{
				Biome biome = world.getBiomeForCoordsBody(new BlockPos(chunkCoords.x << 4, 64, chunkCoords.z << 4));
				int totalWeight = PumpjackHandler.getTotalWeight(dim, biome);
				if (totalWeight > 0)
				{
					int weight = Math.abs(query % totalWeight);
					for (Map.Entry<ReservoirType, Integer> e : PumpjackHandler.reservoirList.entrySet())
					{
						if (e.getKey().validDimension(dim) && e.getKey().validBiome(biome))
						{
							weight -= e.getValue();
							if (weight < 0)
							{
								res = e.getKey();
								break;
							}
						}
					}
				}
			}

			int capacity = 0;

			if (res != null)
			{
				capacity = (int) (size * (res.maxSize - res.minSize)) + res.minSize;
			}

			worldInfo = new OilWorldInfo();
			worldInfo.capacity = capacity;
			worldInfo.current = capacity;
			worldInfo.type = res;
			PumpjackHandler.oilCache.put(chunkCoords, worldInfo);
		}
		return worldInfo;
	}
	
	/**
	 * Used to check chunks in a spiral outwards from the drill.
	 * Credit to Robert Prehn https://medium.com/acrossthegalaxy/grid-spiral-search-formula-3b476bfdd2df
	 * @param n The index of the location in the spiral.
	 * @return an (x,y) pair indicating offset
	 */
	private int[] getOffsetFromIndex(int n) {
		n += 1; //this formula starts on an index of 1, but want to call it using an index that starts on 0
		int k = (int) Math.ceil((Math.sqrt(n)-1)/2);
		int t = 2*k;
		int m = (t+1)*(t+1);
		if (n >= m-t) {
			return new int[] {k-(m-n), -k};
		}
		else {
			m = m-t;
		}
		if (n >= m-t) {
			return new int[] {-k, -k+(m-n)};
		}
		else {
			m = m-t;
		}
		if (n >= m-t) {
			return new int[] {-k+(m-n), k};
		}
		else {
			return new int[] {k, k-(m-n-t)};
		}
//		//alternative method; probably slower
//		int m = (int) Math.floor(Math.sqrt(n));
//		int x = (int) Math.pow(-1, m+1)*((n-m*(m+1))*(((int) Math.floor(2*Math.sqrt(n))+1)%2)+(int) Math.ceil(m/2.0));
//		int y = (int) Math.pow(-1, m)*((n-m*(m+1))*(((int) Math.floor(2*Math.sqrt(n)))%2)-(int) Math.ceil(m/2.0));
//		return new int[] {x,y};
		
	}
}
