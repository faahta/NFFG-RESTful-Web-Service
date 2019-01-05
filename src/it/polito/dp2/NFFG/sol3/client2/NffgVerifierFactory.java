package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.sol3.client2.NffgVerifierImpl;

public class NffgVerifierFactory extends it.polito.dp2.NFFG.NffgVerifierFactory{

	private NffgVerifierImpl monitor;
	@Override
	public NffgVerifier newNffgVerifier() throws NffgVerifierException {
		monitor=new NffgVerifierImpl();			
	    return monitor;
	}

}
