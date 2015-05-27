package edu.erau.albanoj2.lustre.recovery.distributed.metadata;

public class ObjectOstMapping {

	private int objectId;
	private int ostId;
	
	public ObjectOstMapping fromObject (int objectId) {
		this.objectId = objectId;
		return this;
	}
	
	public ObjectOstMapping toOst (int ostId) {
		this.ostId = ostId;
		return this;
	}
	
	public int getObjectId () {
		return this.objectId;
	}
	
	public int getOstId () {
		return this.ostId;
	}
}
