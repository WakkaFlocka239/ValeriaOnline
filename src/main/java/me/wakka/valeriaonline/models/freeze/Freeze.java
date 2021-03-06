package me.wakka.valeriaonline.models.freeze;

import dev.morphia.annotations.Converters;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.UUIDConverter;
import me.wakka.valeriaonline.models.PlayerOwnedObject;

import java.util.UUID;

@Data
@Builder
@Entity("freeze")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Converters(UUIDConverter.class)
public class Freeze extends PlayerOwnedObject {
	@Id
	@NonNull
	private UUID uuid;
	private boolean frozen;

}
