package me.wakka.valeriaonline.framework.persistence;

public enum MongoDBDatabase {
	VALERIAONLINE;

	public String getDatabase() {
		String name = name().toLowerCase();

		if (name.equals("valeriaonline"))
			return name;

		return "valeriaonline_" + name;
	}
}
