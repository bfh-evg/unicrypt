package ch.bfh.unicrypt.math.algebra.additive;

import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialField;
import ch.bfh.unicrypt.math.algebra.additive.parameters.ECPolynomialFieldParameters;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ECPolynomialFieldTest {

	@Test
	public void ContainsXTest() throws Exception {
		ECPolynomialField ec = ECPolynomialField.getInstance(ECPolynomialFieldParameters.SECT113r1);
		PolynomialField field = ec.getFiniteField();
		BitArray b1 = BitArray.getInstance("11000101110100100111011000101001101111101000111111100000101000001101010011110101100000001010110011001111000101");
		BitArray b2 = BitArray.getInstance("0011111110011101111100101000100000001000011111010001100011110110001101101100010110110010000010010011001100010011");
		PolynomialElement x1 = field.getElement(b1);
		PolynomialElement x2 = field.getElement(b2);
		assertTrue(ec.contains(x1));
		assertFalse(ec.contains(x2));

	}

	@Test
	public void ContainsXYTest() throws Exception {
		ECPolynomialField ec = ECPolynomialField.getInstance(ECPolynomialFieldParameters.SECT113r1);
		PolynomialField field = ec.getFiniteField();
		BitArray b1 = BitArray.getInstance("11000101110100100111011000101001101111101000111111100000101000001101010011110101100000001010110011001111000101");
		BitArray b2 = BitArray.getInstance("0101111110011101111100101000100000001000011111010001100011110110001101101100010110110010000010010011001100010011");
		BitArray b3 = BitArray.getInstance("0101111110011101111100101000100000001000011111010001100011110110001101101100010110110010000010010011001100010010");
		PolynomialElement x1 = field.getElement(b1);
		PolynomialElement y1 = field.getElement(b2);
		PolynomialElement y2 = field.getElement(b3);
		assertTrue(ec.contains(x1, y1));
		assertFalse(ec.contains(x1, y2));
	}

	@Test
	public void addTest() throws Exception {
		ECPolynomialField ec = ECPolynomialField.getInstance(ECPolynomialFieldParameters.SECT113r1);
		PolynomialField field = ec.getFiniteField();
		BitArray b1 = BitArray.getInstance("10001111101000111011110111101100110110111110001111011011000110011111100011111100000010111011001010011000001101101");
		BitArray b2 = BitArray.getInstance("00001011001111001111100101000101010000111011001011000100100000011111110001110110000101111001011101010111101010111");
		BitArray b3 = BitArray.getInstance("10000000101110000110001001110010110100101101100111110100100101010001101011111111111001101111100011011111010011001");
		BitArray b4 = BitArray.getInstance("01100100101111110101101100000100111111100011001011011111011110100111011011110011100011110010011010100000001101");
		BitArray b5 = BitArray.getInstance("1010000011101110101000001110000011100010010000110011010001100010011101001111111011100010111100001010101000111101");
		BitArray b6 = BitArray.getInstance("00101111101010110001101111101011111001100111111100011110010111100101101011000011001000111101011101001111001001011");
		PolynomialElement x1 = field.getElement(b1);
		PolynomialElement y1 = field.getElement(b2);
		PolynomialElement x2 = field.getElement(b3);
		PolynomialElement y2 = field.getElement(b4);
		PolynomialElement rx = field.getElement(b5);
		PolynomialElement ry = field.getElement(b6);
		ECPolynomialElement e1 = ec.getElement(x1, y1);
		ECPolynomialElement e2 = ec.getElement(x2, y2);
		ECPolynomialElement result = ec.getElement(rx, ry);
		assertEquals(result, e1.add(e2));
		assertEquals(e2, e2.add(ec.getIdentityElement()));
		assertEquals(e2, ec.getIdentityElement().add(e2));

	}

	@Test
	public void timesTest() throws Exception {
		ECPolynomialField ec = ECPolynomialField.getInstance(ECPolynomialFieldParameters.SECT113r1);
		PolynomialField field = ec.getFiniteField();
		BitArray b1 = BitArray.getInstance("10001111101000111011110111101100110110111110001111011011000110011111100011111100000010111011001010011000001101101");
		BitArray b2 = BitArray.getInstance("00001011001111001111100101000101010000111011001011000100100000011111110001110110000101111001011101010111101010111");
		BitArray b5 = BitArray.getInstance("11100110010100001001101011110011110000111100111000011100010101101100001000111000110101011100010010100100011010101");
		BitArray b6 = BitArray.getInstance("10110100010100110111000001001011011000001101101011101001100100111011000011000101111101101000101010111110100100011");
		PolynomialElement x1 = field.getElement(b1);
		PolynomialElement y1 = field.getElement(b2);
		PolynomialElement rx = field.getElement(b5);
		PolynomialElement ry = field.getElement(b6);
		ECPolynomialElement e1 = ec.getElement(x1, y1);
		ECPolynomialElement result = ec.getElement(rx, ry);
		assertEquals(result, e1.add(e1));

	}

	@Test
	public void invertTest() throws Exception {
		ECPolynomialField ec = ECPolynomialField.getInstance(ECPolynomialFieldParameters.SECT113r1);
		PolynomialField field = ec.getFiniteField();
		BitArray b1 = BitArray.getInstance("10001111101000111011110111101100110110111110001111011011000110011111100011111100000010111011001010011000001101101");
		BitArray b2 = BitArray.getInstance("00001011001111001111100101000101010000111011001011000100100000011111110001110110000101111001011101010111101010111");
		BitArray b5 = BitArray.getInstance("10001111101000111011110111101100110110111110001111011011000110011111100011111100000010111011001010011000001101101");
		BitArray b6 = BitArray.getInstance("1000010010011111010001001010100110011000010100010001111110011000000001001000101000011100001001011100111110011101");
		PolynomialElement x1 = field.getElement(b1);
		PolynomialElement y1 = field.getElement(b2);
		PolynomialElement rx = field.getElement(b5);
		PolynomialElement ry = field.getElement(b6);
		ECPolynomialElement e1 = ec.getElement(x1, y1);
		ECPolynomialElement result = ec.getElement(rx, ry);
		assertEquals(result, e1.invert());
		assertEquals(ec.getIdentityElement(), ec.getIdentityElement().invert());
	}

}
