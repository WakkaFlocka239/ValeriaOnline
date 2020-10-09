package me.wakka.valeriaonline.features.commands;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Arg;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

public class RulesCommand extends CustomCommand {

	public RulesCommand(CommandEvent event) {
		super(event);
	}

	@Path("[string] [integer]")
	void rules(@Arg("menu") String category, @Arg("1") int page) {
		line(5);
		switch (category) {
			case "global":
			case "main":
			case "community":
				community(page);
				break;
			case "streaming":
			case "youtube":
			case "twitch":
				streaming();
				break;
			case "ingame":
			case "in-game":
			case "minecraft":
			case "survival":
			case "server":
				server();
				break;
			case "end":
			case "theend":
				end();
				break;
			default:
				menu();
				break;
		}
	}

	private void menu() {
		send();
		send("&d&lClick on the lines below&7 to read the rules for each category.");
		line();
		send(json()
				.next("&7[+] &dCommunity Rules").command("/rules community")
				.newline()
				.next("&7[+] &dServer Rules").command("/rules server")
				.newline()
				.next("&7[+] &dEnd Rules").command("/rules end")
				.newline()
				.next("&7[+] &dStreaming Rules").command("/rules streaming")
		);
		line();
	}

	private void community(int page) {
		line();
		send("&dThese rules apply to all connected programs");
		line();

		send("1. Staff ruling is final");
		send("2. Be respectful to all players, and do not cause problems");
		send("3. Use common sense");
		send("4. No advertising, if you're unsure, ask a staff member");
		send("5. No excessive profanity, spamming, or caps");
		send("6. No bullying, NSFW content, politics, religion or repetitive use of derogatory terms");
		send("7. English only in public chats. Use local/dms for other languages");
		send("8. Do not impersonate other players, roles, or staff");
		send("9. Report all bugs and exploitable features, and do not abuse them");

		line();
		send(json().next("&d « Main page  ").command("/rules").group());
		line();
	}

	private void server() {
		line();

		send(json("1. No mods, hacks, or cheats ").group()
				.next("&d[Exceptions]")
				.hover("Mods that are okay to use:\n" +
						"  + Mini-Map mods\n" +
						"  + Optifine\n" +
						"  + Shaders\n" +
						"  + Sorting Mods (ex: Sorting Chests)\n" +
						"Using a mod not on this list? \n" +
						"Contact an Admin and ask if it's okay"));
		send(json("2. No griefing, raiding & stealing. If you don't have permission from the owner, don't touch it ").group()
				.next("&d[More info]")
				.hover("Includes replanting farms\n" +
						"\n" +
						"It doesn't matter if you're trusted \n" +
						"on the land, or it's unclaimed\n" +
						"\n" +
						"Doesn't apply to randomly generated structures\n" +
						"- Check discord for more info"));
		send("3. PVP is prohibited outside of The End, including using death traps");
		send(json("4. Do not create lag using minecraft mechanics or anti-afk machines ").group()
				.next("&d[More info]")
				.hover("Includes:\n" +
						"  - Excessive redstone machinery\n" +
						"  - Massive amount of entities\n" +
						"  - Permanently loading chunks\n" +
						"  - etc."));
		send("5. Give players a reasonable amount of space when building. If in doubt, ask the build/land owner");
		send("6. No random 1x1 towers/holes, block spam or obscene structures/skins");
		send("7. Do not share links on the server");
		send("8. Do not sell in-game items for real money, or currency outside of the server");
		send("9. Valeria Online has the right to use & modify all builds produced on the server");

		line();
		send(json().next("&d « Main page  ").command("/rules").group());
		line();
	}

	private void end() {
		line();

		send("1. PVP is enabled, except at the Ender Dragon fight area");
		send("2. Griefing, raiding, stealing, & death traps is allowed, except for teleport traps.");
		send("3. The End will reset every couple of months");

		line();
		send(json().next("&d « Main page  ").command("/rules").group());
		line();
	}

	private void streaming() {
		line();

		send("1. You can ask the Admins for advertising your stream on our discord");
		send("2. You and all your followers must follow the rules. If a large percentage do not, we may ban you and any people associated with your stream");

		line();
		send(json().next("&d « Main page  ").command("/rules").group());
		line();
	}


}
