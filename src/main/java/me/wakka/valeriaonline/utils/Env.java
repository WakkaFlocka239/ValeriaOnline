package me.wakka.valeriaonline.utils;

import me.wakka.valeriaonline.ValeriaOnline;

import java.util.Arrays;

public enum Env {
	DEV,
	PROD;

	public static boolean applies(Env... envs) {
		return Arrays.asList(envs).contains(ValeriaOnline.getEnv());
	}
}
