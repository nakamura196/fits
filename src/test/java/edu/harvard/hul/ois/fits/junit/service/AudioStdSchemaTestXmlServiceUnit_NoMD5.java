/* 
 * Copyright 2015 Harvard University Library
 * 
 * This file is part of FITS (File Information Tool Set).
 * 
 * FITS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * FITS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FITS.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.harvard.hul.ois.fits.junit.service;

import java.io.File;
import java.util.Scanner;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.harvard.hul.ois.fits.Fits;
import edu.harvard.hul.ois.fits.FitsOutput;
import edu.harvard.hul.ois.fits.tests.AbstractXmlUnitTest;

/**
 * These tests compare actual FITS output with expected output on audio files.
 * NOTE: This is an integration test that requires a running web application with the
 * FITS Service running.
 * 
 * @author dan179
 */
@Ignore
public class AudioStdSchemaTestXmlServiceUnit_NoMD5 extends AbstractXmlUnitTest {

	@BeforeClass
	public static void initializeHttpClient() throws Exception {
		AbstractXmlUnitTest.beforeServiceTest();
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		AbstractXmlUnitTest.afterServiceTest();
	}
    
    @Test  
	public void testAudioChunk() throws Exception {   

		String inputFilename = "testchunk.wav";
    	File input = new File("testfiles/" + inputFilename);
    	FitsOutput fitsOut = super.examine(input);
    	fitsOut.saveToDisk("test-generated-output/" + inputFilename + ACTUAL_OUTPUT_FILE_SUFFIX);
    	
		XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
		String actualXmlStr = serializer.outputString(fitsOut.getFitsXml());

		// Read in the expected XML file
		Scanner scan = new Scanner(new File(
				"testfiles/output/" + inputFilename + EXPECTED_OUTPUT_FILE_SUFFIX));
		String expectedXmlStr = scan.
				useDelimiter("\\Z").next();
		scan.close();

		testActualAgainstExpected(actualXmlStr, expectedXmlStr, inputFilename);
	}

	
	@Test  
	public void testAudioMD_noMD5() throws Exception {
		
		// use an alternate fits.xml file where a MD5 checksum is not generated
		File fitsConfigFile = new File("testfiles/properties/fits_no_md5_audio.xml");
    	Fits fits = new Fits(null, fitsConfigFile);
		
		// First generate the FITS output
		String inputFilename = "test.wav";
    	File input = new File("testfiles/" + inputFilename);
		FitsOutput fitsOut = fits.examine(input);
    	fitsOut.saveToDisk("test-generated-output/" + inputFilename + ACTUAL_OUTPUT_FILE_SUFFIX);

		XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
		String actualXmlStr = serializer.outputString(fitsOut.getFitsXml());

		// Read in the expected XML file
		Scanner scan = new Scanner(new File(
				"testfiles/output/" + inputFilename + EXPECTED_OUTPUT_FILE_SUFFIX));
		String expectedXmlStr = scan.
				useDelimiter("\\Z").next();
		scan.close();

		testActualAgainstExpected(actualXmlStr, expectedXmlStr, inputFilename);
	}
}
