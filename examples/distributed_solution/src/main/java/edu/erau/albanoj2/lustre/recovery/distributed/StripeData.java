package edu.erau.albanoj2.lustre.recovery.distributed;

import java.io.UnsupportedEncodingException;

public class StripeData {

	private byte[] data;
	
	public StripeData (byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	@Override
	public String toString () {
		try {
			return new String(this.data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}
