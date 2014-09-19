package com.brandon3055.draconicevolution.common.items;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.common.lib.Strings;

public class DraconiumDust extends ItemDE {
	public DraconiumDust() {
		this.setUnlocalizedName(Strings.draconiumDustName);
		this.setCreativeTab(DraconicEvolution.tolkienTabBlocksItems);
		ModItems.register(this);
	}

}