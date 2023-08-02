package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.Perm;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.MPlayer;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;

public class CmdFluxBalance extends GrindToolsCommand {

    public CmdFluxBalance() {
        this.addParameter(TypePlayer.get(), "player", "you");
        this.setAliases("bal", "balance");
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArg(me);
        MPlayer mPlayer = MPlayer.get(player);

        if (player == me) {
            Utils.msg(player, MLang.get().yourFluxBalance,
                    new Pair<>("%amount%", String.valueOf(mPlayer.getCurrency()))
            );
        } else {
            if (!Perm.BALANCE_OTHERS.has(sender, true)) return;
            Utils.msg(player, MLang.get().playersFluxBalance,
                    new Pair<>("%player%", player.getName()),
                    new Pair<>("%amount%", String.valueOf(mPlayer.getCurrency()))
            );
        }
    }
}
