package edu.erau.albanoj2.lustre.recovery.distributed;

import java.util.HashMap;
import java.util.Map;

public class Psc {

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
	public Map<Integer, StripeData> getStripes (int objectIndex, int stripeCount, ObjectFile objectFile, int stripeSize, int fileSize) {
		
		// Flag to denote if all the stripes from an object have been extracted
		boolean isCompleted = false;
		
		// Index denoting the current row
		int rowIndex = 0;
		
		// Map of stripe indices to stripe data
		Map<Integer, StripeData> stripes = new HashMap<>();
		
		if (stripeSize == 0 || fileSize == 0) {
			// The stripe size or file size is 0
			isCompleted = true;
		}
		
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
