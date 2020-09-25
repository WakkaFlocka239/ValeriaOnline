package me.wakka.valeriaonline.framework.persistence;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.sqlmakers.MySqlMaker;
import lombok.SneakyThrows;
import me.wakka.valeriaonline.ValeriaOnline;

import java.util.HashMap;
import java.util.Map;

public class MySQLPersistence {
	private static final Map<MySQLDatabase, Database> databases = new HashMap<>();

	@SneakyThrows
	private static void openConnection(MySQLDatabase dbType) {
		Class.forName("com.mysql.jdbc.Driver");

		DatabaseConfig config = new DatabaseConfig("mysql");
		Database database = new Database();
		database.setJdbcUrl("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/"
				+ config.getPrefix() + dbType.getDatabase() + "?useSSL=false&relaxAutoCommit=true&characterEncoding=UTF-8");
		database.setUser(config.getUsername());
		database.setPassword(config.getPassword());
		database.setSqlMaker(new MySqlMaker());
		database.setMaxPoolSize(3);
		databases.put(dbType, database);
	}

	public static Database getConnection(MySQLDatabase dbType) {
		try {
			if (databases.get(dbType) == null)
				openConnection(dbType);

			ValeriaOnline.log("Established connection to the MySQL \"" + dbType.getDatabase() + "\" database");
			return databases.get(dbType);
		} catch (Exception ex) {
			ValeriaOnline.severe("Could not establish connection to the MySQL \"" + dbType.getDatabase() + "\" database: " + ex.getMessage());
			return null;
		}
	}

	public static void shutdown() {
		databases.values().forEach(database -> {
			try {
				database.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

}
