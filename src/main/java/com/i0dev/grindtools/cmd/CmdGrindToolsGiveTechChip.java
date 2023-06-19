package com.i0dev.grindtools.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;

public class CmdGrindToolsGiveUpgrade extends GrindToolsCommand {

    public CmdGrindToolsGiveUpgrade() {
        this.setVisibility(Visibility.SECRET);

    }


    @Override
    public void perform() throws MassiveException {
        super.perform();
    }
}
