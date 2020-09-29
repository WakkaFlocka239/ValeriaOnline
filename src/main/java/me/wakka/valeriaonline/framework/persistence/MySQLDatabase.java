package me.wakka.valeriaonline.framework.persistence;

public enum MySQLDatabase {
	VALERIAONLINE;

	public String getDatabase() {
		String name = name().toLowerCase();

		if (name.equals("valeriaonline"))
			return "customer_131501_valeriaonline";

		return "customer_131501_valeriaonline_" + name;
	}
}
