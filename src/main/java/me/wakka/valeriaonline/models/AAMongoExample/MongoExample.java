package me.wakka.valeriaonline.models.AAMongoExample;

import dev.morphia.annotations.Converters;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.LocationConverter;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.UUIDConverter;
import me.wakka.valeriaonline.models.PlayerOwnedObject;
import org.bukkit.Location;

import java.util.UUID;

@Data
@Builder
@Entity("mongo_example")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Converters({UUIDConverter.class, LocationConverter.class})
public class MongoExample extends PlayerOwnedObject {
	@Id
	@NonNull
	private UUID uuid;
	private Location location;

}
