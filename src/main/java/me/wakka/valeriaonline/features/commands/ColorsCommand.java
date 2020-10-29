package me.wakka.valeriaonline.features.commands;

import me.wakka.valeriaonline.framework.commands.models.CustomCommand;
import me.wakka.valeriaonline.framework.commands.models.annotations.Aliases;
import me.wakka.valeriaonline.framework.commands.models.annotations.Path;
import me.wakka.valeriaonline.framework.commands.models.events.CommandEvent;

@Aliases({"colours", "color"})
public class ColorsCommand extends CustomCommand {

	public ColorsCommand(CommandEvent event) {
		super(event);
	}

	@Path
	void colors() {
		line();
		send("&dMinecraft colors:");
		line();
		send(" &0 &&00  &1 &&11  &2 &&22  &3 &&33  &4 &&44  &5 &&55  &6 &&66  &7 &&77  ");
		send(" &8 &&88  &9 &&99  &a &&aa  &b &&bb  &c &&cc  &d &&dd  &e &&ee  &f &&ff  ");
		line();
		send(" &#&f123456 &fHex");
		line();
		send("&dMinecraft formats:");
		line();
		send("&f &&fk &kMagic&f  &&fl &lBold&f  &&fm &mStrike&f  &&fn &nUline&f  &&fo &oItalic&f  &&fr &fReset");
		line();
	}
}
