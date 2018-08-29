package com.craftingdead.mod.common.core;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

/**
 * Used at runtime to inject our custom access rules
 * 
 * @author Sm0keySa1m0n
 *
 */
public class CDAccessTransformer extends AccessTransformer {

	public CDAccessTransformer() throws IOException {
		super("craftingdead_at.cfg");
	}

}
