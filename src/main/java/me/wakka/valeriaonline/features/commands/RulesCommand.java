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
				.next("&7[&d+&7] &bCommunity Rules").command("/rules community")
				.newline()
				.next("&7[&d+&7] &bServer Rules").command("/rules server")
				.newline()
				.next("&7[&d+&7] &bEnd Rules").command("/rules end")
				.newline()
				.next("&7[&d+&7] &bStreaming Rules").command("/rules streaming")
		);
		line();
	}

	private void community(int page) {
		line();
		send("&f[&dCommunity Rules&f] ");
		send("&b1.&7 Staff ruling is final");
		send("&b2.&7 Be respectful to all players, and do not cause problems");
		send("&b3.&7 Use common sense");
		send("&b4.&7 No advertising, if you're unsure, ask a staff member");
		send("&b5.&7 No excessive profanity, spamming, or caps");
		send("&b6.&7 No bullying, NSFW content, politics, religion or repetitive use of derogatory terms");
		send("&b7.&7 English only in public chats. Use local/dms for other languages");
		send("&b8.&7 Do not impersonate other players, roles, or staff");
		send("&b9.&7 Report all bugs and exploitable features, and do not abuse them");

		line();
		send(json().next("&d « Main page  ").command("/rules").group());
		line();
	}

	private void server() {
		line();

		send("&f[&dServer Rules&f] ");
		send(json("&b1.&7 No mods, hacks, or cheats ").group()
				.next("&d[Exceptions]")
				.hover("&dMods that are okay to use:\n" +
						"  &d+&7 Mini-Map mods\n" +
						"  &d+&7 Optifine\n" +
						"  &d+&7 Shaders\n" +
						"  &d+&7 Sorting Mods (ex: Sorting Chests)\n" +
						"\n" +
						"&bUsing a mod not on this list? \n" +
						"&bAsk an admin about it"));
		send(json("&b2.&7 No griefing, raiding & stealing. ").group()
				.next("&d[More info]")
				.hover("&dIf you don't have permission from \n" +
						"the owner, don't touch it \n" +
						"\n" +
						"&d-&7 Includes replanting farms \n" +
						"&d-&7 Even if you're trusted \n" +
						"&7on the land, or it's unclaimed \n" +
						"\n" +
						"&bDoesn't apply to randomly \n" +
						"generated structures, \n" +
						"check discord for more info"));
		send("&b3.&7 PVP is prohibited outside of The End, including using death traps");
		send(json("&b4.&7 Do not create lag using minecraft mechanics or anti-afk machines ").group()
				.next("&d[More info]")
				.hover("&dIncludes:\n" +
						"  &d-&7 Excessive redstone machinery\n" +
						"  &d-&7 Massive amount of entities\n" +
						"  &d-&7 Permanently loading chunks\n" +
						"  &d-&7 etc."));
		send("&b5.&7 Give players a reasonable amount of space when building. If in doubt, ask the build/land owner");
		send("&b6.&7 No random 1x1 towers/holes, block spam or obscene structures/skins");
		send("&b7.&7 Do not share links on the server");
		send("&b8.&7 Do not sell in-game items for real money, or currency outside of the server");
		send("&b9.&7 Valeria Online has the right to use & modify all builds produced on the server");

		line();
		send(json().next("&d « Main page  ").command("/rules").group());
		line();
	}

	private void end() {
		line();

		send("&f[&dEnd Rules&f] ");
		send("&b1.&7 PVP is enabled, except at the Ender Dragon fight area");
		send("&b2.&7 Griefing, raiding, stealing, & death traps is allowed, except for teleport traps.");
		send("&b3.&7 The End will reset every couple of months");

		line();
		send(json().next("&d « Main page  ").command("/rules").group());
		line();
	}

	private void streaming() {
		line();

		send("&f[&dStreaming Rules&f] ");
		send("&b1.&7 You can ask the Admins for advertising your stream on our discord");
		send("&b2.&7 You and all your followers must follow the rules. If a large percentage do not, we may ban you and any people associated with your stream");

		line();
		send(json().next("&d « Main page  ").command("/rules").group());
		line();
	}


}
