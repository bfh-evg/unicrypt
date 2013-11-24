package ch.bfh.unicrypt.crypto.schemes.sharing.interfaces;

import ch.bfh.unicrypt.crypto.schemes.scheme.interfaces.Scheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.Random;

public interface SecretSharingScheme
			 extends Scheme {

	public Set getShareSpace();

	public int getSize();

	public Element[] share(Element message);

	public Element[] share(Element message, Random random);

	public Element recover(Element... shares);

}
