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
	group.qm			= quest master
	group.dm 			- dungeon master
	group.builder 		- builder

	group.creator		- admin & builder
	group.staff			- trial & builder

              group.owner
            /             \
       group.dev        group.qm
       group.hadmin     group.dm
       group.admin      group.builder
       group.mod           |
       group.trial         |
            \             /
                exstaff
 */
