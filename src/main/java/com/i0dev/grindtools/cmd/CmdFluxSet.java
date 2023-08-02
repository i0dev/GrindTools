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

public class CmdFluxSet extends GrindToolsCommand {

    public CmdFluxSet() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeInteger.get(), "amount");
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArg();
        int amount = this.readArg();

        if (amount <= 0) {
            Utils.msg(me, MLang.get().amountMustBePositive);
            return;
        }

        MPlayer mPlayer = MPlayer.get(player);
        mPlayer.setCurrency(amount);

        Utils.msg(sender, MLang.get().setFlux,
                new Pair<>("%amount%", String.valueOf(amount)),
                new Pair<>("%player%", player.getName()));
    }
}
