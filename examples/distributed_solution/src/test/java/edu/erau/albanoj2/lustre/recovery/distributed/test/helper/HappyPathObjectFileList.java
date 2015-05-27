package edu.erau.albanoj2.lustre.recovery.distributed.test.helper;

import java.io.IOException;
import java.util.ArrayList;

import edu.erau.albanoj2.lustre.recovery.distributed.ObjectFile;

@SuppressWarnings("serial")
public class HappyPathObjectFileList extends ArrayList<ObjectFile> {

	public HappyPathObjectFileList () throws IOException {
		this.add(new ObjectFile("object_files/object_0.txt"));
		this.add(new ObjectFile("object_files/object_1.txt"));
		this.add(new ObjectFile("object_files/object_2.txt"));
	}
}
