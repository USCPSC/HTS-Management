package com.ttw.itds.ui.shared.codec;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rzauel
 */
public class CustomDateTimeDeserializerTest {
    
    public CustomDateTimeDeserializerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testPattern1String() throws Exception {
        System.out.println("Read Date Time from String 1");
        String inputString;
        DateTime expResult;
        DateTime result;
        
        inputString = "2016-12-31 12:25";
        expResult = new DateTime(2016,12,31,12,25);
        result = CustomDateTimeDeserializer.readDateTimeStringPattern1(inputString);
        assertEquals(expResult.toString(), result.toString());
    }
    
}
