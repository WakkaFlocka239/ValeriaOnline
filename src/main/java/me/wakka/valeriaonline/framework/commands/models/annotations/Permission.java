package me.wakka.valeriaonline.framework.commands.models.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
	String value();

	boolean absolute() default false;
}

/*
	group.owner 		- owner
	group.dev			- developer
	group.hadmin 		- head admin
	group.admin 		- admin
	group.mod 			- mod
	group.trial 		- trial mod

	group.qm			- quest master
	group.dm 			- dungeon master
	group.builder 		- builder

	group.creator		- qm
	group.staff			- mod & qm

              group.owner
              group.dev
              group.hadmin
              group.admin
            /             \
       group.mod        group.dm
       group.trial      group.builder
           |            group.qm
            \             /
                veteran
 */
