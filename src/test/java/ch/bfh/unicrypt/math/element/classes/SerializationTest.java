/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.algebra.product.classes.Tuple;
import ch.bfh.unicrypt.math.group.classes.GStarSave;
import ch.bfh.unicrypt.math.algebra.product.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phil
 */
public class SerializationTest {
    
    public SerializationTest() {
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
     * Test for serialization of atomic elements
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    @Test
    public void testAtomicElementSerialize() throws IOException, ClassNotFoundException {
        
        final GStarSave g_q = new GStarSave(BigInteger.valueOf(23));
        
        Element atomic1 = g_q.getRandomElement();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(atomic1);
        oos.close();
        
        InputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        ois.close();
        
        Element atomic2 = (AtomicElement)o;
        
        assertTrue(g_q.areEqual(atomic1, atomic2));
    }
    
    /**
     * Test for serialization of tuple elements
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    @Test
    public void testTupleElementSerialize() throws IOException, ClassNotFoundException {
        ZPlusMod z_q = new ZPlusModClass(BigInteger.valueOf(19));
        final GStarSave g_q = new GStarSave(BigInteger.valueOf(23));
        
        final ProductGroup prodGroup = new ProductGroup(g_q,g_q,z_q);

        Tuple tuple1 = prodGroup.getRandomElement();
        //Works also by creating a Tuple with: ProductGroupClass.createTupleElement(atomic1, atomic2);
        
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ObjectOutputStream oos2 = new ObjectOutputStream(out2);
        oos2.writeObject(tuple1);
        oos2.close();
        
        InputStream in2 = new ByteArrayInputStream(out2.toByteArray());
        ObjectInputStream ois2 = new ObjectInputStream(in2);
        Object o2 = ois2.readObject();
        ois2.close();
        
        Element tuple2 = (Tuple)o2;
        
        assertTrue(prodGroup.areEqual(tuple1, tuple2));
    }
}
