package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;

public class SafeECGroupFp extends ECGroupFp {

	private SafeECGroupFp(ZMod field, DualisticElement a,
			DualisticElement b, DualisticElement gx, DualisticElement gy,
			BigInteger order, BigInteger h) {
		super(field, a, b, gx, gy, order, h);
		// TODO Auto-generated constructor stub
	}

	
	public static SafeECGroupFp getInstance(final String SecCurve) {
	    ZMod field;
	    DualisticElement a,b,gx,gy;
	    BigInteger order,h;
		
		if (SecCurve == "test") {
			field=ZModPrime.getInstance(13);
			a=field.getElement(4);
			b=field.getElement(12);
			gx=field.getElement(9);
			gy=field.getElement(6);
			order=new BigInteger("19");
			h=new BigInteger("2");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);

	    }
		else if (SecCurve == "secp112r1") {
			field=ZModPrime.getInstance(new BigInteger("DB7C2ABF62E35E668076BEAD208B",16));
			a=field.getElement(new BigInteger("DB7C2ABF62E35E668076BEAD2088",16));
			b=field.getElement(new BigInteger("659EF8BA043916EEDE8911702B22",16));
			gx=field.getElement(new BigInteger("09487239995A5EE76B55F9C2F098",16));
			gy=field.getElement(new BigInteger("A89CE5AF8724C0A23E0E0FF77500",16));
			order=new BigInteger("DB7C2ABF62E35E7628DFAC6561C5",16);
			h=new BigInteger("1");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);
	    }
		else if (SecCurve == "secp160r1") {
			field=ZModPrime.getInstance(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF7FFFFFFF",16));
			a=field.getElement(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF7FFFFFFC",16));
			b=field.getElement(new BigInteger("1C97BEFC54BD7A8B65ACF89F81D4D4ADC565FA45",16));
			gx=field.getElement(new BigInteger("4A96B5688EF573284664698968C38BB913CBFC82",16));
			gy=field.getElement(new BigInteger("23A628553168947D59DCC912042351377AC5FB32",16));
			order=new BigInteger("0100000000000000000001F4C8F927AED3CA752257",16);
			h=new BigInteger("1");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);
	    }
		else if (SecCurve == "secp192k1") {
			field=ZModPrime.getInstance(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFEE37",16));
			a=field.getElement(new BigInteger("0",16));
			b=field.getElement(new BigInteger("3",16));
			gx=field.getElement(new BigInteger("DB4FF10EC057E9AE26B07D0280B7F4341DA5D1B1EAE06C7D",16));
			gy=field.getElement(new BigInteger("9B2F2F6D9C5628A7844163D015BE86344082AA88D95E2F9D",16));
			order=new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFE26F2FC170F69466A74DEFD8D",16);
			h=new BigInteger("1");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);
	    }
		else if (SecCurve == "secp192r1") {
			field=ZModPrime.getInstance(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFF",16));
			a=field.getElement(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFC",16));
			b=field.getElement(new BigInteger("64210519E59C80E70FA7E9AB72243049FEB8DEECC146B9B1",16));
			gx=field.getElement(new BigInteger("188DA80EB03090F67CBF20EB43A18800F4FF0AFD82FF1012",16));
			gy=field.getElement(new BigInteger("07192B95FFC8DA78631011ED6B24CDD573F977A11E794811",16));
			order=new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFF99DEF836146BC9B1B4D22831",16);
			h=new BigInteger("1");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);
	    }
		else if (SecCurve == "secp224k1") {
			field=ZModPrime.getInstance(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFE56D",16));
			a=field.getElement(new BigInteger("0",16));
			b=field.getElement(new BigInteger("5",16));
			gx=field.getElement(new BigInteger("A1455B334DF099DF30FC28A169A467E9E47075A90F7E650EB6B7A45C",16));
			gy=field.getElement(new BigInteger("7E089FED7FBA344282CAFBD6F7E319F7C0B0BD59E2CA4BDB556D61A5",16));
			order=new BigInteger("010000000000000000000000000001DCE8D2EC6184CAF0A971769FB1F7",16);
			h=new BigInteger("1");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);
	    }
		else if (SecCurve == "secp224r1") {
			field=ZModPrime.getInstance(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF000000000000000000000001",16));
			a=field.getElement(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFE",16));
			b=field.getElement(new BigInteger("B4050A850C04B3ABF54132565044B0B7D7BFD8BA270B39432355FFB4",16));
			gx=field.getElement(new BigInteger("B70E0CBD6BB4BF7F321390B94A03C1D356C21122343280D6115C1D21",16));
			gy=field.getElement(new BigInteger("BD376388B5F723FB4C22DFE6CD4375A05A07476444D5819985007E34",16));
			order=new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFF16A2E0B8F03E13DD29455C5C2A3D",16);
			h=new BigInteger("1");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);
	    }
		else if (SecCurve == "secp256k1") {
			field=ZModPrime.getInstance(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F",16));
			a=field.getElement(new BigInteger("0",16));
			b=field.getElement(new BigInteger("7",16));
			gx=field.getElement(new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798",16));
			gy=field.getElement(new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8",16));
			order=new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141",16);
			h=new BigInteger("1");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);
	    }
		else if (SecCurve == "secp256r1") {
			field=ZModPrime.getInstance(new BigInteger("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF",16));
			a=field.getElement(new BigInteger("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC",16));
			b=field.getElement(new BigInteger("5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B",16));
			gx=field.getElement(new BigInteger("6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296",16));
			gy=field.getElement(new BigInteger("4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5",16));
			order=new BigInteger("FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551",16);
			h=new BigInteger("1");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);
	    }
		else if (SecCurve == "secp384r1") {
			field=ZModPrime.getInstance(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFF",16));
			a=field.getElement(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFC",16));
			b=field.getElement(new BigInteger("B3312FA7E23EE7E4988E056BE3F82D19181D9C6EFE8141120314088F5013875AC656398D8A2ED19D2A85C8EDD3EC2AEF",16));
			gx=field.getElement(new BigInteger("AA87CA22BE8B05378EB1C71EF320AD746E1D3B628BA79B9859F741E082542A385502F25DBF55296C3A545E3872760AB7",16));
			gy=field.getElement(new BigInteger("3617DE4A96262C6F5D9E98BF9292DC29F8F41DBD289A147CE9DA3113B5F0B8C00A60B1CE1D7E819D7A431D7C90EA0E5F",16));
			order=new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC7634D81F4372DDF581A0DB248B0A77AECEC196ACCC52973",16);
			h=new BigInteger("1");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);
	    }
		else if (SecCurve == "secp521r1") {
			field=ZModPrime.getInstance(new BigInteger("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF",16));
			a=field.getElement(new BigInteger("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC",16));
			b=field.getElement(new BigInteger("0051953EB9618E1C9A1F929A21A0B68540EEA2DA725B99B315F3B8B489918EF109E156193951EC7E937B1652C0BD3BB1BF073573DF883D2C34F1EF451FD46B503F00",16));
			gx=field.getElement(new BigInteger("00C6858E06B70404E9CD9E3ECB662395B4429C648139053FB521F828AF606B4D3DBAA14B5E77EFE75928FE1DC127A2FFA8DE3348B3C1856A429BF97E7E31C2E5BD66",16));
			gy=field.getElement(new BigInteger("011839296A789A3BC0045C8A5FB42C7D1BD998F54449579B446817AFBD17273E662C97EE72995EF42640C550B9013FAD0761353C7086A272C24088BE94769FD16650",16));
			order=new BigInteger("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFA51868783BF2F966B7FCC0148F709A5D03BB5C9B8899C47AEBB6FB71E91386409",16);
			h=new BigInteger("1");
	    	return new SafeECGroupFp(field, a, b, gx, gy, order, h);
	    }
	    else{
	    	throw new IllegalArgumentException("Wrong parameter: "+SecCurve);
	    }
	    
	  }
	
	

}
