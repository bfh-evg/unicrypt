package ch.bfh.unicrypt.hash;

import java.math.BigInteger;

import ch.bfh.unicrypt.concat.classes.ConcatSchemeClass;
import ch.bfh.unicrypt.concat.interfaces.ConcatScheme;
import ch.bfh.unicrypt.hash.classes.HashSchemeClass;
import ch.bfh.unicrypt.hash.interfaces.HashScheme;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunctionClass.ConcatParameter;
import ch.bfh.unicrypt.math.function.classes.HashFunctionClass;
import ch.bfh.unicrypt.math.group.classes.BooleanGroupClass;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.interfaces.BooleanGroup;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.util.mapper.classes.CharsetXRadixYMapperClass;

public class HashExample {
  public static void main(final String[] args) {
    final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
    final HashScheme sha256 = new HashSchemeClass(HashFunctionClass.SHA256, concat, new ZPlusModClass(BigInteger.valueOf(10)));
    final BooleanGroup bg = BooleanGroupClass.getInstance();
    final ZPlus zp = ZPlusClass.getInstance();
    final ZPlusMod zpm = new ZPlusModClass(BigInteger.valueOf(200));
    final GStarSave zss = new GStarSaveClass(BigInteger.valueOf(23));

    final Element hashElement = sha256.hash(bg.createElement(false),
        zp.createElement(BigInteger.valueOf(13)),
        zpm.createElement(BigInteger.valueOf(10)),
        zss.createElement(BigInteger.valueOf(3)));
    System.out.println(hashElement);

  }
}
