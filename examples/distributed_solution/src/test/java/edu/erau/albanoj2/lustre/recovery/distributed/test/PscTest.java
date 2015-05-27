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

public class PscTest {

	private Psc psc;
	private Metadata happyPathMetadata;
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
		Assert.assertEquals("AAAAA", this.stripeDataExtractionHelper(0, 0));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectZeroStripeThree () {
		Assert.assertEquals("DDDDD", this.stripeDataExtractionHelper(0, 3));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectZeroStripeSix () {
		Assert.assertEquals("GGGGG", this.stripeDataExtractionHelper(0, 6));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectOneStripeOne () {
		Assert.assertEquals("BBBBB", this.stripeDataExtractionHelper(1, 1));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectOneStripeFour () {
		Assert.assertEquals("EEEEE", this.stripeDataExtractionHelper(1, 4));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectOneStripeSeven () {
		Assert.assertEquals("HHHHH", this.stripeDataExtractionHelper(1, 7));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectTwoStripeTwo () {
		Assert.assertEquals("CCCCC", this.stripeDataExtractionHelper(2, 2));
	}
	
	@Test
	public void testGetStripesCorrectDataForObjectTwoStripeFive () {
		Assert.assertEquals("FFFFF", this.stripeDataExtractionHelper(2, 5));
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
	public void testGetStripesCorrectNumberOfStripesForObjectZero () {
		Assert.assertEquals(3, this.getStripeCountForObjectHelper(0), 3);
	}
	
	@Test
	public void testGetStripesCorrectNumberOfStripesForObjectOne () {
		Assert.assertEquals(3, this.getStripeCountForObjectHelper(1), 3);
	}
	
	@Test
	public void testGetStripesCorrectNumberOfStripesForObjectTwo () {
		Assert.assertEquals(3, this.getStripeCountForObjectHelper(0), 2);
	}
	
	/***************************************************************************
	 * Edge-case Tests
	 **************************************************************************/

	@Test
	public void testGetStripesWithStripeSizeOfZeroReturnsEmptyStripeMap () {
		Assert.assertTrue(psc.getStripes(0, 0, null, 0, 10).isEmpty());
	}
	
	@Test
	public void testGetStripesWithFileSizeOfZeroReturnsEmptyStripeMap () {
		Assert.assertTrue(psc.getStripes(0, 0, null, 10, 0).isEmpty());
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
	
	public String stripeDataExtractionHelper (int objectId, int stripeId) {
		
		// Obtain the stripes from the PSC
		Map<Integer, StripeData> stripes = this.psc.getStripes(
				objectId, 
				this.happyPathMetadata.getStripeCount(), 
				this.happyPathObjectFiles.get(objectId), 
				this.happyPathMetadata.getStripeSize(), 
				this.happyPathMetadata.getFileSize());
		
		return stripes.get(stripeId).toString();		
	}
}
