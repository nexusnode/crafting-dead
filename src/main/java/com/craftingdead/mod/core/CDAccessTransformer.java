package com.craftingdead.mod.core;

import java.io.IOException;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

public class CDAccessTransformer extends AccessTransformer {

	public CDAccessTransformer() throws IOException {
		super("opposingforces_at.cfg");
	}

}
