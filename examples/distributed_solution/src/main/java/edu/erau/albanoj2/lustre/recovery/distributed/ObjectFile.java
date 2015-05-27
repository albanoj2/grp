package edu.erau.albanoj2.lustre.recovery.distributed;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

public class ObjectFile {
	
	private byte[] data;
	
	public ObjectFile (String path) throws IOException {
		this.data = FileUtils.readFileToByteArray(new File(System.getProperty("user.dir") + "/" + path));
	}

	public byte[] read (int lowerBound, int upperBound) {
		return Arrays.copyOfRange(this.data, lowerBound, upperBound);
	}
}
