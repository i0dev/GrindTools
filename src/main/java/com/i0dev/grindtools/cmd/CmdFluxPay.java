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

public class CmdFluxPay extends GrindToolsCommand {

    public CmdFluxPay() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeInteger.get(), "amount");
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    protected <T extends Enum<T>> T calcPerm() {
        return (T) Perm.FLUX_PAY;
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArg();
        int amount = this.readArg();

        if (amount <= 0) {
            Utils.msg(player, MLang.get().amountMustBePositive);
            return;
        }

        MPlayer mSender = MPlayer.get(me);
        MPlayer mReceiver = MPlayer.get(player);

        if (mSender.getCurrency() < amount) {
            Utils.msg(mSender, MLang.get().dontHaveEnoughFlux);
            return;
        }

        mSender.setCurrency(mSender.getCurrency() - amount);
        mReceiver.setCurrency(mReceiver.getCurrency() + amount);

        Utils.msg(mSender, MLang.get().youPaidFlux,
                new Pair<>("%amount%", String.valueOf(amount)),
                new Pair<>("%player%", mReceiver.getName())
        );


        Utils.msg(mReceiver, MLang.get().youReceivedFlux,
                new Pair<>("%amount%", String.valueOf(amount)),
                new Pair<>("%player%", mSender.getName()));
    }
}
