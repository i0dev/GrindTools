package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.MPlayer;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;

public class CmdFluxRemove extends GrindToolsCommand {

    public CmdFluxRemove() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeInteger.get(), "amount");
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArg();
        int amount = this.readArg();

        if (amount <= 0) {
            Utils.msg(sender, MLang.get().amountMustBePositive);
            return;
        }
        MPlayer mPlayer = MPlayer.get(player);
        long newAmount = mPlayer.getCurrency() - amount;

        if (newAmount < 0) {
            Utils.msg(sender, MLang.get().amountMustBe,
                    new Pair<>("%amount%", String.valueOf(MPlayer.get(player).getCurrency()))
            );
            return;
        }

        mPlayer.setCurrency(mPlayer.getCurrency() - amount);

        Utils.msg(sender, MLang.get().removedFlux,
                new Pair<>("%amount%", String.valueOf(amount)),
                new Pair<>("%player%", player.getName()));
    }
}
