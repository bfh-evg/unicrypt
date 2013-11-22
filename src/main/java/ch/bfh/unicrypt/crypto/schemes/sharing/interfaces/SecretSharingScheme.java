package ch.bfh.unicrypt.crypto.schemes.sharing.interfaces;

import ch.bfh.unicrypt.crypto.schemes.scheme.interfaces.Scheme;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public interface SecretSharingScheme
			 extends Scheme {

	public Set getShareSpace();

	public int getSize();

	public Tuple share(Element message);

	public Element recover(Tuple shares);

}
