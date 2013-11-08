/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author reto
 */
public class BooleanElementTest {

    public BooleanElementTest() {
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

    /**
     * Test of getBoolean method, of class BooleanElement.
     */
    @Test
    public void testGetBoolean() {
        System.out.println("getBoolean");
        BooleanElement instance = null;
        boolean expResult = false;
        boolean result = instance.getBoolean();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of standardToStringContent method, of class BooleanElement.
     */
    @Test
    public void testStandardToStringContent() {
        System.out.println("standardToStringContent");
        BooleanElement instance = null;
        String expResult = "";
        String result = instance.standardToStringContent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
