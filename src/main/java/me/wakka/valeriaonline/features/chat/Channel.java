package me.wakka.valeriaonline.features.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
	String prefix;
	String name;
	String permission;
}
