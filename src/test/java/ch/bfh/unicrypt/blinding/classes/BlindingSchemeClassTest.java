package ch.bfh.unicrypt.blinding.classes;

import java.math.BigInteger;
import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.unicrypt.crypto.blinding.classes.StandardBlindingScheme;
import ch.bfh.unicrypt.crypto.blinding.interfaces.BlindingScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.GStarSave;
import ch.bfh.unicrypt.math.algebra.product.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.interfaces.AtomicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;
import ch.bfh.unicrypt.math.utility.RandomUtil;

public class BlindingSchemeClassTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlindingSchemeClassNull() {
    BlindingScheme bs = new StandardBlindingScheme(null);
  }

  @Test
  public void testBlindingSchemeClass() {
    Group g = new ZPlusModClass(BigInteger.ONE);
    BlindingScheme bs = new StandardBlindingScheme(g);
    Assert.assertSame(g, bs.getBlindingSpace());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBlindElementNonInvertableNull() {
    Random r = RandomUtil.createRandomGenerator(new byte[] { 1 });
    Group g = new GStarSave(BigInteger.valueOf(23));
    BlindingScheme bs = new StandardBlindingScheme(g);
    Element be = bs.blind(null);
    Assert.assertTrue(g.contains(be));
  }

  @Test
  public void testBlindElementNonInvertable() {
    Random r = RandomUtil.createRandomGenerator(new byte[] { 1 });
    Group g = new GStarSave(BigInteger.valueOf(23));
    Element e = g.getRandomElement(r);
    BlindingScheme bs = new StandardBlindingScheme(g);
    Element be = bs.blind(e);
    Assert.assertTrue(g.contains(be));
  }

  @Test
  public void testBlindElementAdditiveElement() {
    Random r = RandomUtil.createRandomGenerator(new byte[] { 1 });
    AtomicGroup g = new GStarSave(BigInteger.valueOf(23));
    AtomicElement e = g.getRandomElement(r);
    AdditiveElement blindingValue = g.getMinOrderGroup().getRandomElement(r);
    BlindingScheme bs = new StandardBlindingScheme(g);
    Element be = bs.blind(e, blindingValue);
    Assert.assertEquals(e.selfApply(blindingValue.getValue()), be);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBlindElementAdditiveElementNull() {
    Random r = RandomUtil.createRandomGenerator(new byte[] { 1 });
    AtomicGroup g = new GStarSave(BigInteger.valueOf(23));
    AtomicElement e = g.getRandomElement(r);
    AdditiveElement blindingValue = g.getMinOrderGroup().getRandomElement(r);
    BlindingScheme bs = new StandardBlindingScheme(g);
    Element be = bs.blind(e, (AdditiveElement)null);
  }

  @Test
  public void testBlindElementRandom() {
    Random r = RandomUtil.createRandomGenerator(new byte[] { 1 });
    Group g = new GStarSave(BigInteger.valueOf(23));
    Element e = g.getRandomElement(r);
    BlindingScheme bs = new StandardBlindingScheme(g);
    AtomicElement be = (AtomicElement) bs.blind(e, r);
    Assert.assertEquals(BigInteger.valueOf(4), be.getValue());
  }
  @Test
  public void testBlindElementRandomNull() {
    Random r = RandomUtil.createRandomGenerator(new byte[] { 1 });
    Group g = new GStarSave(BigInteger.valueOf(23));
    Element e = g.getRandomElement(r);
    BlindingScheme bs = new StandardBlindingScheme(g);
    AtomicElement be = (AtomicElement) bs.blind(e, (Random)null);
    Assert.assertTrue(g.contains(be));
  }
  

  @Test
  public void testGetBlindingSpace() {
    Group g = new GStarSave(BigInteger.valueOf(23));
    BlindingScheme bs = new StandardBlindingScheme(g);
    Assert.assertEquals(g, bs.getBlindingSpace());
  }

  @Test
  public void testGetBlindingValueSpace() {    
    Group g = new GStarSave(BigInteger.valueOf(23));
    BlindingScheme bs = new StandardBlindingScheme(g);
    Assert.assertEquals(g.getMinOrderGroup(), bs.getBlindingValueSpace());
  }

  @Test
  public void testGetBlindingFunction() {   
    Group g = new GStarSave(BigInteger.valueOf(23));
    BlindingScheme bs = new StandardBlindingScheme(g);
    ProductGroup domainProductGroup =new ProductGroup(g,g.getMinOrderGroup());
    Assert.assertEquals(domainProductGroup,bs.getBlindingFunction().getDomain());
    Assert.assertEquals(g,bs.getBlindingFunction().getCoDomain());    
  }

  @Test(expected= IllegalArgumentException.class)
  public void testGetBlindingFunctionElementNull() {
    Random r = RandomUtil.createRandomGenerator(new byte[] { 1 });
    Group g = new GStarSave(BigInteger.valueOf(23));
    Element e = g.getRandomElement(r);
    BlindingScheme bs = new StandardBlindingScheme(g);
    AdditiveElement blindingValue = bs.getBlindingValueSpace().getRandomElement(r);    
    Function function=bs.getBlindingFunction(null);
    function.apply(blindingValue);  
  }
  
  @Test(expected= IllegalArgumentException.class)
  public void testGetBlindingFunctionElementWrong() {
    Random r = RandomUtil.createRandomGenerator(new byte[] { 1 });
    Group g = new GStarSave(BigInteger.valueOf(23));
    Element e = g.getRandomElement(r);
    BlindingScheme bs = new StandardBlindingScheme(g);
    AdditiveElement blindingValue = bs.getBlindingValueSpace().getRandomElement(r);    
    Function function=bs.getBlindingFunction(blindingValue);
  }
  
  @Test
  public void testGetBlindingFunctionElement() {
    Random r = RandomUtil.createRandomGenerator(new byte[] { 1 });
    Group g = new GStarSave(BigInteger.valueOf(23));
    Element e = g.getRandomElement(r);
    BlindingScheme bs = new StandardBlindingScheme(g);
    AdditiveElement blindingValue = bs.getBlindingValueSpace().getRandomElement(r);    
    Function function=bs.getBlindingFunction(e);
    Element blindedElement=function.apply(blindingValue);    
  }

}
