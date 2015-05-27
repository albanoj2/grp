package edu.erau.albanoj2.lustre.recovery.distributed.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.erau.albanoj2.lustre.recovery.distributed.ObjectFile;
import edu.erau.albanoj2.lustre.recovery.distributed.Psc;
import edu.erau.albanoj2.lustre.recovery.distributed.StripeData;
import edu.erau.albanoj2.lustre.recovery.distributed.metadata.Metadata;
import edu.erau.albanoj2.lustre.recovery.distributed.test.helper.HappyPathMetadata;
import edu.erau.albanoj2.lustre.recovery.distributed.test.helper.HappyPathObjectFileList;

/**
 * Test cases for the Partial Striping Component (PSC). The test cases 
 * containing in this test fixture are based on the PSC algorithm described in 
 * Listing 1 and use the example data described in section 2. The "happy path" 
 * is extracted from the distributed solution described in section 4 (a use case
 * is not provided in the text of the paper, but the description of the 
 * requirements that the algorithm in Listing 1 must fulfill can be used to 
 * create such a use case).
 * 
 * @author Justin Albano
 */
public class PscTest {
	
	/***************************************************************************
	 * Attributes
	 **************************************************************************/

	/**
	 * The Partial Striping Component under test.
	 */
	private Psc psc;
	
	/**
	 * The metadata used in the happy path test cases.
	 */
	private Metadata happyPathMetadata;
	
	/**
	 * The list of object files used in the happy path test cases.
	 */
	private List<ObjectFile> happyPathObjectFiles;

	@Before
	public void setUp () throws IOException {
		
		// Create the Partial Striping Component (PSC) under test
		this.psc = new Psc();
		
		// Create the happy path data to exercise the happy path
		this.happyPathMetadata = new HappyPathMetadata();
		this.happyPathObjectFiles = new HappyPathObjectFileList();
	}
	
	/***************************************************************************
	 * Happy Path Tests
	 **************************************************************************/
	
	/**
	 * -------------------------------------------------------------------------
	 * Correct Stripe Data
	 * ===================
	 * The following tests ensure that the get_stripes algorithm illustrated in 
	 * Listing 1 correctly obtains the stripe data for the example object files 
	 * described in section 2 of the paper.
	 * -------------------------------------------------------------------------
	 */
	
	@Test
	public void testGetStripesCorrectDataForObjectZeroStripeZero () {
		Assert.assertEquals("AAAAA", this.happyPathStripeDataExtractionHelper(0, 0));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectZeroStripeThree () {
		Assert.assertEquals("DDDDD", this.happyPathStripeDataExtractionHelper(0, 3));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectZeroStripeSix () {
		Assert.assertEquals("GGGGG", this.happyPathStripeDataExtractionHelper(0, 6));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectOneStripeOne () {
		Assert.assertEquals("BBBBB", this.happyPathStripeDataExtractionHelper(1, 1));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectOneStripeFour () {
		Assert.assertEquals("EEEEE", this.happyPathStripeDataExtractionHelper(1, 4));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectOneStripeSeven () {
		Assert.assertEquals("HHHHH", this.happyPathStripeDataExtractionHelper(1, 7));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectTwoStripeTwo () {
		Assert.assertEquals("CCCCC", this.happyPathStripeDataExtractionHelper(2, 2));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectTwoStripeFive () {
		Assert.assertEquals("FFFFF", this.happyPathStripeDataExtractionHelper(2, 5));
	}
	
	/**
	 * -------------------------------------------------------------------------
	 * Correct Stripe Count
	 * ====================
	 * The following tests ensure that the get_stripes algorithm illustrated in 
	 * Listing 1 obtains the correct number of stripes for the example object 
	 * files described in section 2 of the paper. These test supplement the 
	 * "Correct Stripe Data" tests above, ensuring that the stripes in the 
	 * previous tests are the only stripes obtained (there are not more, 
	 * incorrect stripes obtained, which would not be discovered using the 
	 * "Correct Stripe Data" tests).
	 * -------------------------------------------------------------------------
	 */
	
	@Test
	public void testGetStripesEnsureCorrectNumberOfStripesForObjectZero () {
		Assert.assertEquals(3, this.getStripeCountForObjectHelper(0), 3);
	}
	
	@Test
	public void testGetStripesEnsureCorrectNumberOfStripesForObjectOne () {
		Assert.assertEquals(3, this.getStripeCountForObjectHelper(1), 3);
	}
	
	@Test
	public void testGetStripesEnsureCorrectNumberOfStripesForObjectTwo () {
		Assert.assertEquals(3, this.getStripeCountForObjectHelper(0), 2);
	}
	
	/***************************************************************************
	 * Edge-case Tests
	 **************************************************************************/

	@Test
	public void testGetStripesWithStripeSizeOfZeroEnsureEmptyStripeMap () {
		Assert.assertTrue(psc.getStripes(0, 0, null, 0, 10).isEmpty());
	}
	
	@Test
	public void testGetStripesWithFileSizeOfZeroEnsureEmptyStripeMap () {
		Assert.assertTrue(psc.getStripes(0, 0, null, 10, 0).isEmpty());
	}
	
	/**
	 * Tests the functionality of the get_stripes algorithm to cope with file 
	 * sizes that are not stripe-size aligned (file sizes that are not an 
	 * integer multiple of the stripe size). In this test, a stripe size of 5 
	 * bytes is used with a file size of 39 bytes (not stripe-size aligned), and
	 * the functionality of the algorithm is tested to ensure that only 4 bytes
	 * are read from the last stripe.
	 */
	@Test
	public void testGetStripesLastStripeHasLessDataThanStripeSizeEnsureCorrectStripeData () throws IOException {
	
		Assert.assertEquals("HHHH", this.stripeDataExtractionHelper(new Metadata(
				this.happyPathMetadata).setFileSize(39),
				this.happyPathObjectFiles,
				1, 7
			)
		);
	}
	
	/***************************************************************************
	 * Helper Functions
	 **************************************************************************/
	
	/**
	 * Obtains the number of stripes extracted from the happy path objects at 
	 * the supplied object index.
	 * 
	 * @param objectId
	 * 		The object ID of the object from which the number of extracted 
	 * 		stripes should be counted.
	 * 
	 * @return
	 * 		The number of stripe extracted from the happy path object with the 
	 * 		supplied object index.
	 */
	public int getStripeCountForObjectHelper (int objectId) {
		
		// Obtain the stripes from the PSC
		Map<Integer, StripeData> stripes = this.psc.getStripes(
				objectId, 
				this.happyPathMetadata.getStripeCount(), 
				this.happyPathObjectFiles.get(objectId), 
				this.happyPathMetadata.getStripeSize(), 
				this.happyPathMetadata.getFileSize());
		
		return stripes.size();
	}
	
	/**
	 * Extracts the stripe data, in string form, for the object and stripe at 
	 * the supplied object index and stripe index, respectively.
	 * 
	 * @param metadata
	 * 		The metadata for the file.
	 * @param objectFileList
	 * 		The list of object files for the file.
	 * @param objectId
	 * 		The index of the object to extract the stripe data from.
	 * @param stripeId
	 * 		The index of the stripe from the object at the supplied object index
	 * 		to extract the stripe data from.
	 * 
	 * @return
	 * 		The stripe data, in string form, for the object and stripe at the 
	 * 		supplied object and stripe indices, respectively.
	 */
	public String stripeDataExtractionHelper (Metadata metadata, List<ObjectFile> objectFileList, int objectId, int stripeId) {
		
		// Obtain the stripes from the PSC
		Map<Integer, StripeData> stripes = this.psc.getStripes(
				objectId, 
				metadata.getStripeCount(), 
				objectFileList.get(objectId), 
				metadata.getStripeSize(), 
				metadata.getFileSize());
		
		return stripes.get(stripeId).toString();		
	}
	
	/**
	 * @return
	 * 		Obtains the stripe data for the happy path objects at the supplied 
	 * 		object and stripe indices.
	 */
	public String happyPathStripeDataExtractionHelper (int objectId, int stripeId) {
		return this.stripeDataExtractionHelper(this.happyPathMetadata, this.happyPathObjectFiles, objectId, stripeId);
	}
}
