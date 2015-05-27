package edu.erau.albanoj2.lustre.recovery.distributed.test.helper;

import edu.erau.albanoj2.lustre.recovery.distributed.metadata.Metadata;
import edu.erau.albanoj2.lustre.recovery.distributed.metadata.ObjectOstMapping;

public class HappyPathMetadata extends Metadata {

	public HappyPathMetadata () {
		this.setStripeSize(5)
			.setFileSize(40)
			.addObjectMapping(new ObjectOstMapping().fromObject(0).toOst(0))
			.addObjectMapping(new ObjectOstMapping().fromObject(1).toOst(1))
			.addObjectMapping(new ObjectOstMapping().fromObject(2).toOst(2));
	}
}
