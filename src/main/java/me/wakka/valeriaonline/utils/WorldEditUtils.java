package me.wakka.valeriaonline.utils;

import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.wakka.valeriaonline.framework.exceptions.postconfigured.InvalidInputException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldEditUtils {
	@NonNull
	private final org.bukkit.World world;
	private final BukkitWorld bukkitWorld;
	private final World worldEditWorld;
	@Getter
	private final WorldGuardUtils worldGuardUtils;
	@Getter
	private static final String schematicsDirectory = "plugins/FastAsyncWorldEdit/schematics/";
	@Getter
	private static final WorldEditPlugin plugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

	public WorldEditUtils(@NonNull org.bukkit.entity.Entity entity) {
		this(entity.getWorld());
	}

	public WorldEditUtils(@NonNull Location location) {
		this(location.getWorld());
	}

	public WorldEditUtils(@NonNull Block block) {
		this(block.getWorld());
	}

	public WorldEditUtils(@NonNull String world) {
		this(Bukkit.getWorld(world));
	}

	public WorldEditUtils(@NonNull org.bukkit.World world) {
		this.world = world;
		this.bukkitWorld = new BukkitWorld(world);
		this.worldEditWorld = bukkitWorld;
		this.worldGuardUtils = new WorldGuardUtils(world);
	}

	public EditSession getEditSession() {
		return new EditSessionBuilder(worldEditWorld).fastmode(true).build();
	}

	private File getSchematicFile(String fileName, boolean lookForExisting) {
		File file = new File(schematicsDirectory + fileName + "." + BuiltInClipboardFormat.SPONGE_SCHEMATIC.getPrimaryFileExtension());
		if (!file.exists() && lookForExisting)
			file = new File(schematicsDirectory + fileName + "." + BuiltInClipboardFormat.MCEDIT_SCHEMATIC.getPrimaryFileExtension());
		return file;
	}

	@SneakyThrows
	public Clipboard getSchematic(String fileName) {
		File file = getSchematicFile(fileName, true);
		if (!file.exists())
			throw new InvalidInputException("Schematic " + fileName + " does not exist");

		return ClipboardFormats.findByFile(file).load(file);
	}

	public Vector3 toVector3(Location location) {
		return Vector3.at(location.getX(), location.getY(), location.getZ());
	}

	public BlockVector3 toBlockVector3(Location location) {
		return BlockVector3.at(location.getX(), location.getY(), location.getZ());
	}

	public BlockVector3 toBlockVector3(Vector3 vector) {
		return BlockVector3.at(vector.getX(), vector.getY(), vector.getZ());
	}

	public Location toLocation(Vector3 vector) {
		return new Location(world, vector.getX(), vector.getY(), vector.getZ());
	}

	public Location toLocation(BlockVector3 vector) {
		return new Location(world, vector.getX(), vector.getY(), vector.getZ());
	}

	public BukkitPlayer getPlayer(Player player) {
		return BukkitAdapter.adapt(player);
	}

	public Region getPlayerSelection(Player player) {
		return getPlayer(player).getSelection();
	}

	public enum SelectionChangeDirectionType {
		HORIZONTAL {
			@Override
			BlockVector3[] getVectors() {
				return new BlockVector3[]{ BlockVector3.UNIT_X, BlockVector3.UNIT_MINUS_X, BlockVector3.UNIT_Z, BlockVector3.UNIT_MINUS_Z };
			}
		},
		VERTICAL {
			@Override
			BlockVector3[] getVectors() {
				return new BlockVector3[]{ BlockVector3.UNIT_Y, BlockVector3.UNIT_MINUS_Y };
			}
		},
		ALL {
			@Override
			BlockVector3[] getVectors() {
				List<BlockVector3> vectors = new ArrayList<>();
				Stream.of(HORIZONTAL, VERTICAL).map(value -> Arrays.asList(value.getVectors())).forEach(vectors::addAll);
				return vectors.toArray(new BlockVector3[0]);
			}
		};

		abstract BlockVector3[] getVectors();

		public BlockVector3[] applyChanges(int amount) {
			return Stream.of(getVectors())
					.map(vector -> vector.multiply(amount))
					.toArray(BlockVector3[]::new);
		}
	}

	public enum SelectionChangeType {
		EXPAND,
		CONTRACT
	}

	@SneakyThrows
	public void changeSelection(Player player, SelectionChangeType changeType, SelectionChangeDirectionType directionType, int amount) {
		if (amount <= 0) return;
		LocalSession session = plugin.getSession(player);
		Region region = session.getSelection(worldEditWorld);
		int oldSize = region.getArea();
		BlockVector3[] directions = directionType.applyChanges(amount);

		if (changeType == SelectionChangeType.EXPAND)
			region.expand(directions);
		else if (changeType == SelectionChangeType.CONTRACT)
			region.contract(directions);

		getPlayer(player).setSelection(region);
		session.getRegionSelector(worldEditWorld).learnChanges();
		int newSize = region.getArea();
		session.getRegionSelector(worldEditWorld).explainRegionAdjust(getPlayer(player), session);
//		1.12: BBC.SELECTION_EXPAND.send(worldEditPlayer, (newSize - oldSize));
//		1.15?: actor.printInfo(TranslatableComponent.of("worldedit.expand.expanded.vert", new Component[]{TextComponent.of(changeSize)}));
	}

	public void setSelection(Player player, Location location) {
		setSelection(player, location, location);
	}

	public void setSelection(Player player, BlockVector3 vector) {
		setSelection(player, vector);
	}

	public void setSelection(Player player, Location min, Location max) {
		setSelection(player, toBlockVector3(min), toBlockVector3(min));
	}

	public void setSelection(Player player, BlockVector3 min, BlockVector3 max) {
		setSelection(player, new CuboidRegion(min, max));
	}

	public void setSelection(Player player, Region region) {
		LocalSession session = plugin.getSession(player);
		getPlayer(player).setSelection(region);
		com.sk89q.worldedit.entity.Player worldEditPlayer = plugin.wrapPlayer(player);
		session.getRegionSelector(worldEditWorld).explainPrimarySelection(worldEditPlayer, session, region.getMinimumPoint());
		session.getRegionSelector(worldEditWorld).explainSecondarySelection(worldEditPlayer, session, region.getMaximumPoint());
	}

	public List<Block> getBlocks(ProtectedRegion region) {
		return getBlocks((CuboidRegion) worldGuardUtils.convert(region));
	}

	public List<Block> getBlocks(Region region) {
		return getBlocks((CuboidRegion) region);
	}

	public List<Block> getBlocks(CuboidRegion region) {
		List<Block> blockList = new ArrayList<>();
		for (int x = region.getMinimumPoint().getBlockX(); x <= region.getMaximumPoint().getBlockX(); x++)
			for (int y = region.getMinimumPoint().getBlockY(); y <= region.getMaximumPoint().getBlockY(); y++)
				for (int z = region.getMinimumPoint().getBlockZ(); z <= region.getMaximumPoint().getBlockZ(); z++)
					blockList.add(world.getBlockAt(x, y, z));
		return blockList;
	}

	public Set<BaseBlock> toBaseBlocks(Set<BlockType> blockTypes) {
		return blockTypes.stream().map(blockType -> blockType.getDefaultState().toBaseBlock()).collect(Collectors.toSet());
	}

	public RandomPattern toRandomPattern(Set<BlockType> baseBlocks) {
		RandomPattern pattern = new RandomPattern();
		baseBlocks.forEach(baseBlock -> pattern.add(baseBlock, (float) 100 / baseBlocks.size()));
		return pattern;
	}

	public RandomPattern toRandomPattern(Map<BlockType, Double> materials) {
		RandomPattern pattern = new RandomPattern();
		materials.forEach(pattern::add);
		return pattern;
	}

	public Clipboard copy(Location min, Location max) {
		return copy(worldGuardUtils.getRegion(min, max));
	}

	public Clipboard copy(Region region) {
		Clipboard clipboard = new BlockArrayClipboard(region);
		try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(worldEditWorld, -1)) {
			ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
			Operations.completeLegacy(copy);
		} catch (WorldEditException ex) {
			ex.printStackTrace();
		}
		return clipboard;
	}

	public Paste paster() {
		return new Paste();
	}

	@Data
	@NoArgsConstructor
	public class Paste {
		private Clipboard clipboard;
		private BlockVector3 vector;
		private boolean pasteAir = true;
		private Transform transform;

		public Paste file(String fileName) {
			return clipboard(getSchematic(fileName));
		}

		public Paste clipboard(Clipboard clipboard) {
			this.clipboard = clipboard;
			return this;
		}

		public Paste at(Location location) {
			return at(toBlockVector3(location));
		}

		public Paste at(BlockVector3 vector) {
			this.vector = vector;
			return this;
		}

		public Paste air(boolean pasteAir) {
			this.pasteAir = pasteAir;
			return this;
		}

		public Paste transform(Transform transform) {
			this.transform = transform;
			return this;
		}

		public void paste() {
			clipboard.paste(worldEditWorld, vector, pasteAir, transform);
		}
	}

	public void save(String fileName, Location min, Location max) {
		save(fileName, toBlockVector3(min), toBlockVector3(max));
	}

	public void save(String fileName, Region region) {
		save(fileName, region.getMinimumPoint(), region.getMaximumPoint());
	}

	@SneakyThrows
	public void save(String fileName, BlockVector3 min, BlockVector3 max) {
		CuboidRegion region = new CuboidRegion(worldEditWorld, min, max);
		new BlockArrayClipboard(region).save(getSchematicFile(fileName, false), BuiltInClipboardFormat.SPONGE_SCHEMATIC);
	}

	public void set(String region, BlockType blockType) {
		set(worldGuardUtils.convert(worldGuardUtils.getProtectedRegion(region)), blockType);
	}

	public void set(Region region, BlockType blockType) {
		// TODO move back to WE
		getBlocks(region).forEach(block -> block.setType(Material.valueOf(blockType.getId().replaceAll("minecraft:", "").toUpperCase())));
//		EditSession editSession = getEditSession();
//		editSession.setBlocks(region, blockType.getDefaultState().toBaseBlock());
//		editSession.flushQueue();
	}

	public void replace(Region region, BlockType from, BlockType to) {
		replace(region, Collections.singleton(from), Collections.singleton(to));
	}

	public void replace(Region region, Set<BlockType> from, Set<BlockType> to) {
		replace(region, from, toRandomPattern(to));
	}

	public void replace(Region region, Set<BlockType> from, Map<BlockType, Double> pattern) {
		replace(region, from, toRandomPattern(pattern));
	}

	public void replace(Region region, Set<BlockType> from, Pattern pattern) {
		EditSession editSession = getEditSession();
		editSession.replaceBlocks(region, toBaseBlocks(from), pattern);
		editSession.flushQueue();
	}

	public Region expandAll(Region region, int amount) {
		region.expand(SelectionChangeDirectionType.ALL.applyChanges(amount));
		return region;
	}

	public Region contractAll(Region region, int amount) {
		region.contract(SelectionChangeDirectionType.ALL.applyChanges(amount));
		return region;
	}

	@SneakyThrows
	public void fixFlat(LocalSession session, Region region) {
		region.expand(Direction.UP.toBlockVector().multiply(500));
		region.expand(Direction.DOWN.toBlockVector().multiply(500));
		set(region, BlockTypes.AIR);
		region.expand(Direction.DOWN.toBlockVector().multiply(500));
		region.contract(Direction.DOWN.toBlockVector().multiply(500));
		session.getRegionSelector(region.getWorld()).learnChanges();
		set(region, BlockTypes.BEDROCK);
		region.expand(Direction.UP.toBlockVector().multiply(3));
		region.contract(Direction.UP.toBlockVector().multiply(1));
		session.getRegionSelector(region.getWorld()).learnChanges();
		set(region, BlockTypes.GRASS_BLOCK);
	}

}
