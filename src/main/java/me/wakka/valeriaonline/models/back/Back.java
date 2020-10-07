package me.wakka.valeriaonline.models.back;

import com.dieselpoint.norm.serialize.DbSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.wakka.valeriaonline.framework.persistence.serializer.LocationSerializer;
import me.wakka.valeriaonline.models.PlayerOwnedObject;
import org.bukkit.Location;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Back extends PlayerOwnedObject {
	@NonNull
	private UUID uuid;
	@DbSerializer(LocationSerializer.class)
	private Location location;

}
