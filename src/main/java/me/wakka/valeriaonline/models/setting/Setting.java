package me.wakka.valeriaonline.models.setting;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.wakka.valeriaonline.framework.persistence.serializer.mysql.LocationSerializer;
import me.wakka.valeriaonline.utils.Utils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Setting {
	@NonNull
	private String id;
	@NonNull
	private String type;
	private String value;

	public Setting(Player player, String type, String value) {
		this(player.getUniqueId().toString(), type, value);
	}

	public OfflinePlayer getOfflinePlayer() {
		return Utils.getPlayer(id);
	}

	public boolean getBoolean() {
		if ("1".equalsIgnoreCase(value)) return true;
		return Boolean.parseBoolean(value);
	}

	public void setBoolean(boolean value) {
		this.value = String.valueOf(value);
	}

	public Location getLocation() {
		if (value == null)
			return null;
		return new LocationSerializer().deserialize(value);
	}

	public void setLocation(Location location) {
		this.value = new LocationSerializer().serialize(location);
	}

	public List<Location> getLocationList() {
		if (value == null)
			return null;

		List<Location> result = new ArrayList<>();
		for (String locationStr : value.split(";")) {
			result.add(new LocationSerializer().deserialize(locationStr));
		}

		return result;
	}

	public void setLocationList(List<Location> locList) {
		if (Utils.isNullOrEmpty(locList)) {
			this.value = null;
			return;
		}

		List<String> locStrList = new ArrayList<>();
		for (Location loc : locList) {
			locStrList.add(new LocationSerializer().serialize(loc));
		}

		this.value = String.join(";", locStrList);
	}

	public void addToLocationList(Location location) {
		String locStr = new LocationSerializer().serialize(location);
		if (Strings.isNullOrEmpty(this.value))
			this.value = locStr;
		else
			this.value = this.value + ";" + locStr;
	}

	public void removeFromLocationList(Location location) {
		List<Location> locations = getLocationList();
		locations.remove(location);
		setLocationList(locations);
	}

	public int getInt() {
		if (Utils.isInt(value))
			return Integer.parseInt(value);
		return 0;
	}

	public void setInt(int value) {
		this.value = String.valueOf(value);
	}

	public double getDouble() {
		if (Utils.isDouble(value))
			return Double.parseDouble(value);
		return 0;
	}

	public void setDouble(double value) {
		this.value = String.valueOf(value);
	}

	public Map<String, Object> getJson() {
		Map<String, Object> map = new Gson().fromJson(value, Map.class);
		if (map == null)
			return new HashMap<>();
		return map;
	}

	public void setJson(Map<String, Object> map) {
		this.value = new Gson().toJson(new HashMap<>(map));
	}

}
