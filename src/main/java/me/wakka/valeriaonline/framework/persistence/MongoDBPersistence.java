package me.wakka.valeriaonline.framework.persistence;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.annotations.Entity;
import dev.morphia.mapping.MapperOptions;
import lombok.SneakyThrows;
import me.wakka.valeriaonline.ValeriaOnline;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.ChatColorConverter;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.ColorConverter;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.ItemMetaConverter;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.ItemStackConverter;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.LocalDateConverter;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.LocalDateTimeConverter;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.LocationConverter;
import me.wakka.valeriaonline.framework.persistence.serializer.mongodb.UUIDConverter;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class MongoDBPersistence {
	protected static final Morphia morphia = new Morphia();
	private static Map<MongoDBDatabase, Datastore> databases = new HashMap<>();

//	static {
//		ConfigUtils.getSettings().("databases.mongodb.host", "localhost");
//		ValeriaOnline.getInstance().addConfigDefault("databases.mongodb.port", 27017);
//		ValeriaOnline.getInstance().addConfigDefault("databases.mongodb.username", "root");
//		ValeriaOnline.getInstance().addConfigDefault("databases.mongodb.password", "password");
//		ValeriaOnline.getInstance().addConfigDefault("databases.mongodb.prefix", "");
//	}

	@SneakyThrows
	private static void openConnection(MongoDBDatabase dbType) {
		DatabaseConfig config = new DatabaseConfig("mongodb");

		// Paper compat
		morphia.getMapper().setOptions(MapperOptions.builder().classLoader(ValeriaOnline.getInstance().getClass().getClassLoader()).build());
		new Reflections("me.wakka.valeriaonline.models").getTypesAnnotatedWith(Entity.class);

		MongoCredential root = MongoCredential.createScramSha1Credential(config.getUsername(), "admin", config.getPassword().toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress(), root, MongoClientOptions.builder().build());
		Datastore datastore = morphia.createDatastore(mongoClient, config.getPrefix() + dbType.getDatabase());
		morphia.getMapper().getConverters().addConverter(new ChatColorConverter(morphia.getMapper()));
		morphia.getMapper().getConverters().addConverter(new ColorConverter(morphia.getMapper()));
		morphia.getMapper().getConverters().addConverter(new ItemMetaConverter(morphia.getMapper()));
		morphia.getMapper().getConverters().addConverter(new ItemStackConverter(morphia.getMapper()));
		morphia.getMapper().getConverters().addConverter(new LocalDateConverter(morphia.getMapper()));
		morphia.getMapper().getConverters().addConverter(new LocalDateTimeConverter(morphia.getMapper()));
		morphia.getMapper().getConverters().addConverter(new LocationConverter(morphia.getMapper()));
		morphia.getMapper().getConverters().addConverter(new UUIDConverter(morphia.getMapper()));
		databases.put(dbType, datastore);
	}

	public static Datastore getConnection(MongoDBDatabase vodb) {
		try {
			if (databases.get(vodb) == null)
				openConnection(vodb);
			return databases.get(vodb);
		} catch (Exception ex) {
			ValeriaOnline.severe("Could not establish connection to the MongoDB \"" + vodb.getDatabase() + "\" database: " + ex.getMessage());
			return null;
		}
	}

	public static void shutdown() {
		databases.values().forEach(datastore -> {
			try {
				datastore.getMongo().close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}


}
