package me.wakka.valeriaonline.framework.persistence;

public enum MySQLDatabase {
	VALERIAONLINE;

	public String getDatabase() {
		String name = name().toLowerCase();
		String database = MySQLPersistence.config.getUsername();

		if (name.equals("valeriaonline"))
			return database;

		return database + "_" + name;
	}
}
