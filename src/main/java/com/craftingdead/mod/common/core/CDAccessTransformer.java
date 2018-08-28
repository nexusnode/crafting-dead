package com.craftingdead.mod.common.core;

import java.io.IOException;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

public class CDAccessTransformer extends AccessTransformer {

	public CDAccessTransformer() throws IOException {
		super("craftingdead_at.cfg");
	}

}
