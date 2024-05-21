package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.Perm;
import com.i0dev.grindtools.cmd.type.TypeOfflinePlayer;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.MPlayer;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdFluxBalance extends GrindToolsCommand {

    public CmdFluxBalance() {
        this.addParameter(TypeOfflinePlayer.get(), "player", "you");
        this.setAliases("bal", "balance");
    }

    @Override
    protected <T extends Enum<T>> T calcPerm() {
        return (T) Perm.FLUX_BALANCE;
    }

    @Override
    public void perform() throws MassiveException {
        OfflinePlayer player = this.readArg(me);
        MPlayer mPlayer = MPlayer.get(player);

        if (me != null && player.getName().equals(me.getName())) {
            Utils.msg(me, MLang.get().yourFluxBalance,
                    new Pair<>("%amount%", String.valueOf(mPlayer.getCurrency()))
            );
        } else {
            if (!Perm.FLUX_BALANCE_OTHERS.has(sender, true)) return;
            Utils.msg(me, MLang.get().playersFluxBalance,
                    new Pair<>("%player%", player.getName()),
                    new Pair<>("%amount%", String.valueOf(mPlayer.getCurrency()))
            );
        }
    }
}
