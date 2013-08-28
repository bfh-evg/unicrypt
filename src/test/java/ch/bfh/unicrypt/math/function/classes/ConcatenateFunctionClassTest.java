/**
 * 
 */
package ch.bfh.unicrypt.math.function.classes;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction.ConcatParameter;
import ch.bfh.unicrypt.math.function.interfaces.ConcatenateFunction;
import ch.bfh.unicrypt.math.group.classes.PowerGroup;
import ch.bfh.unicrypt.math.additive.classes.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;
import ch.bfh.unicrypt.math.java2unicrypt.classes.ExternalDataMapperClass;

/**
 * @author reto
 *
 */
public class ConcatenateFunctionClassTest {

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
   * Test method for {@link ch.bfh.unicrypt.math.function.classes.ConcatenateFunction#ConcatenateFunctionClass(ch.bfh.unicrypt.math.group.interfaces.ProductGroup, ch.bfh.unicrypt.math.function.classes.ConcatenateFunction.ConcatParameter, ch.bfh.unicrypt.math.util.mapper.definition.Mapper)}.
   */
  @Test
  public void testConcatenateFunctionClass() {
    ProductGroup tupleGroup=new PowerGroup(ZPlus.getInstance(), 2);
    ConcatParameter concatParam=ConcatParameter.PipeBrackets;
    ExternalDataMapperClass mapper=new ExternalDataMapperClass(); 
    ConcatenateFunction concat=new ConcatenateFunction(tupleGroup,concatParam,mapper);    
    
    
    AtomicElement concatElement=concat.apply(mapper.getEncodedElement("Hello"),mapper.getEncodedElement("World"));
    AtomicElement concatElement2=concat.apply(concatElement,concatElement);
    Assert.assertEquals("Hello|World|Hello|World",new String(concatElement2.getValue().toByteArray()));
    
    
    
  }

  
}
