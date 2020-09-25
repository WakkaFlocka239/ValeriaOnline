package me.wakka.valeriaonline.framework.persistence;

public enum MySQLDatabase {
	VALERIAONLINE;

	public String getDatabase() {
		String name = name().toLowerCase();

		if (name.equals("valeriaonline"))
			return name;

		return "valeriaonline_" + name;
	}
}
