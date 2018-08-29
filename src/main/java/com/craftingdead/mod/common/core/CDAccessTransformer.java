package com.craftingdead.mod.common.core;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class CDAccessTransformer extends AccessTransformer {

    public CDAccessTransformer() throws IOException {
        super("craftingdead_at.cfg");
    }

}
