/**
 * 
 */
package ch.bfh.unicrypt.concat.classes;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.unicrypt.crypto.concat.classes.ConcatSchemeClass;
import ch.bfh.unicrypt.crypto.concat.interfaces.ConcatScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction.ConcatParameter;
import ch.bfh.unicrypt.math.java2unicrypt.classes.ExternalDataMapperClass;

/**
 * @author reto
 *
 */
public class ConcatSchemeClassTest {

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link ch.bfh.unicrypt.crypto.concat.classes.ConcatSchemeClass#ConcatSchemeClass(ch.bfh.unicrypt.math.function.classes.ConcatenateFunction.ConcatParameter, ch.bfh.unicrypt.math.util.mapper.definition.Mapper)}.
   */
  @Test
  public void testConcatSchemeClass() {    
    ConcatParameter concatParam=ConcatParameter.PipeBrackets;
    ExternalDataMapperClass mapper=new ExternalDataMapperClass(); 
    ConcatScheme concat=new ConcatSchemeClass(concatParam,mapper);    
    
    
    AtomicElement concatElement=concat.concat(mapper.getEncodedElement("Hello"),mapper.getEncodedElement("World"));
    AtomicElement concatElement2=concat.concat(concatElement);
    
    Assert.assertEquals("Hello|World",new String(concatElement2.getValue().toByteArray()));
    
    
    
  }  
  /**
   * Test method for {@link ch.bfh.unicrypt.concat.classes.ConcatSchemeClass#ConcatSchemeClass(ConcatParameter, ch.bfh.unicrypt.math.util.mapper.interfaces.Mapper))}.
   */
  @Test
  public void testConcatList() {    
    ConcatParameter concatParam=ConcatParameter.PipeBrackets;
    ExternalDataMapperClass mapper=new ExternalDataMapperClass(); 
    ConcatScheme concat=new ConcatSchemeClass(concatParam,mapper);    
    
    
    AtomicElement concatElement1=concat.concat(mapper.getEncodedElement("Hello"));
    AtomicElement concatElement2=concat.concat(mapper.getEncodedElement("World"));
    List<Element>concatList=new ArrayList<Element>();
    concatList.add(concatElement1);
    concatList.add(concatElement2);
    AtomicElement concatElement=concat.concat(concatList);
    Assert.assertEquals("Hello|World",new String(concatElement.getValue().toByteArray()));
        
  }  

}
