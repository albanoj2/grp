package edu.erau.albanoj2.lustre.recovery.distributed;

import java.util.ArrayList;
import java.util.HashMap;
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
		
		for (int i = 0; i < objectFiles.size(); i++) {
			// Iterate through each of the object files that make up the file to
			// be recovered and print the stripe data, keyed by the stripe 
			// index, for each object file that make up the file to be recovered
			
			// Extract the stripes for the object of interest
			Map<Integer, StripeData> stripes = Simulator.getStripes(i, metadata.getStripeCount(), objectFiles.get(i), metadata.getStripeSize(), metadata.getFileSize());
			
			// Print the stripe data
			System.out.println("Object " + i + ": " + stripes);
		}
	}

	/**
	 * Extracts the stripes, keyed by the index of the stripe, from an object 
	 * file that composes a file to be recovered. The metadata for this function 
	 * is obtained from the AMRT and the object files (in particular, the object
	 * file supplied to this function) are obtained from the AOFRT.
	 * 
	 * @param objectIndex
	 * 		The index of the object in the objects over which the file to be 
	 * 		recovered has been striped.
	 * @param stripeCount
	 * 		The stripe count, or total number of objects over which the file to 
	 * 		be recovered has been striped.
	 * @param objectFile
	 * 		The object file from which its constituent stripes should be 
	 * 		extracted. This file is the object file recovered from an OST using
	 * 		the AOFRT.
	 * @param stripeSize
	 * 		The size (or width) of each stripe, in bytes.
	 * @param fileSize
	 * 		The size of the file, in bytes.
	 * 
	 * @return
	 * 		A map containing the stripe data for each stripe contained in the 
	 * 		supplied object file, keyed by the index of the stripe.
	 */
	public static Map<Integer, StripeData> getStripes (int objectIndex, int stripeCount, ObjectFile objectFile, int stripeSize, int fileSize) {
		
		// Flag to denote if all the stripes from an object have been extracted
		boolean isCompleted = false;
		
		// Index denoting the current row
		int rowIndex = 0;
		
		// Map of stripe indices to stripe data
		Map<Integer, StripeData> stripes = new HashMap<>();
		
		while (!isCompleted) {
			
			// Calculate the stripe index
			int stripeIndex = (stripeCount * rowIndex) + objectIndex;
			
			// Compute the lower bound for stripe data
			int fileLowerBound = stripeIndex * stripeSize;
			
			// Create a stub for the upper bound
			int objectUpperBound;
			
			if (fileLowerBound < fileSize) {
				
				// Set the local lower bound to the row index times the stripe size
				int objectLowerBound = rowIndex * stripeSize;
				
				if (fileLowerBound + stripeSize > fileSize) {
					// The upper bound is the remaining bytes in the file
					objectUpperBound = objectLowerBound + (fileSize - fileLowerBound);
				}
				else {
					// The upper bound is the lower bound plus the stripe size
					objectUpperBound = objectLowerBound + stripeSize;
				}
				
				System.out.println(objectUpperBound);
				
				// Extract stripe data from the object file
				StripeData data = new StripeData(objectFile.read(objectLowerBound, objectUpperBound));
				
				// Add entry to the stripe map
				stripes.put(stripeIndex, data);
				
				// Increment the row index
				rowIndex++;
			}
			else {
				// The extraction of stripes is complete
				isCompleted = true;
			}
		}
		
		return stripes;
	}
}
