package edu.erau.albanoj2.lustre.recovery.distributed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.erau.albanoj2.lustre.recovery.distributed.metadata.Metadata;
import edu.erau.albanoj2.lustre.recovery.distributed.metadata.ObjectOstMapping;

/**
 * Simulator class that drives the example and displays the mapping of stripe index to stripe data
 * 
 * @author Justin Albano
 */
public class Simulator {

	/**
	 * The main function that simulates the execution of the get_stripes(..) 
	 * function. Although the metadata and object files in the actual execution 
	 * of the get_stripes(..) function would be obtained from the AMRT and 
	 * AOFRT, respectively, this data is manually configured and simulated.
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) throws Exception {
		
		// Create the simulated metadata; this metadata would normally come from
		// the AMRT, but in this case, is hard-coded to simulate the AMRT
		Metadata metadata = new Metadata()
			.setStripeSize(5)
			.setFileSize(39)
			.addObjectMapping(new ObjectOstMapping().fromObject(0).toOst(0))
			.addObjectMapping(new ObjectOstMapping().fromObject(1).toOst(1))
			.addObjectMapping(new ObjectOstMapping().fromObject(2).toOst(2));
		
		// Create a list of all object files; these object files would normally 
		// be obtained from the AOFRT, but in this case, these objects are 
		// manually created to simulate the AOFRT
		List<ObjectFile> objectFiles = new ArrayList<ObjectFile>() {{
			add(new ObjectFile("object_files/object_0.txt"));
			add(new ObjectFile("object_files/object_1.txt"));
			add(new ObjectFile("object_files/object_2.txt"));
		}};
		
		// Create the Partial Striping Component (PSC) to test
		Psc psc = new Psc();
		
		for (int i = 0; i < objectFiles.size(); i++) {
			// Iterate through each of the object files that make up the file to
			// be recovered and print the stripe data, keyed by the stripe 
			// index, for each object file that make up the file to be recovered
			
			// Extract the stripes for the object of interest
			Map<Integer, StripeData> stripes = psc.getStripes(i, metadata.getStripeCount(), objectFiles.get(i), metadata.getStripeSize(), metadata.getFileSize());
			
			// Print the stripe data
			System.out.println("Object " + i + ": " + stripes);
		}
	}
}
