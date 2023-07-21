package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

@EditorName("config")
public class MConf extends Entity<MConf> {

    protected static transient MConf i;

    public static MConf get() {
        return i;
    }

    public List<String> aliasesGrindTools = MUtil.list("grindtools");

    @Override
    public MConf load(MConf that) {
        super.load(that);
        return this;
    }

}
