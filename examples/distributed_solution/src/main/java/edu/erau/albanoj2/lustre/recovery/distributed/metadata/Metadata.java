package edu.erau.albanoj2.lustre.recovery.distributed.metadata;

import java.util.ArrayList;
import java.util.List;

public class Metadata {
	
	private List<ObjectOstMapping> objectOstMappings;
	private int stripeSize;
	private int fileSize;
	
	public Metadata () {
		this.objectOstMappings = new ArrayList<>();
	}
	
	public Metadata addObjectMapping (ObjectOstMapping mapping) {
		this.objectOstMappings.add(mapping);
		return this;
	}
	
	public int getStripeCount() {
		return this.objectOstMappings.size();
	}
	
	public int getStripeSize() {
		return stripeSize;
	}
	
	public Metadata setStripeSize(int stripeSize) {
		this.stripeSize = stripeSize;
		return this;
	}
	
	public int getFileSize() {
		return fileSize;
	}
	
	public Metadata setFileSize (int fileSize) {
		this.fileSize = fileSize;
		return this;
	}
}
