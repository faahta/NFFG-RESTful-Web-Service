package it.polito.dp2.NFFG.sol3.client2;

import java.util.Calendar;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

public class VerificationResultImpl implements VerificationResultReader {

	Boolean result;
	String message;
	Calendar lastVerifiedTime;
	PolicyReader pr;
	@Override
	public PolicyReader getPolicy() {
		// TODO Auto-generated method stub
		return this.pr;
	}

	@Override
	public Boolean getVerificationResult() {
		// TODO Auto-generated method stub
		return this.result;
	}

	@Override
	public String getVerificationResultMsg() {
		// TODO Auto-generated method stub
		return this.message;
	}

	@Override
	public Calendar getVerificationTime() {
		// TODO Auto-generated method stub
		return this.lastVerifiedTime;
	}
	public VerificationResultImpl(Boolean res,String message,Calendar verTime,PolicyReader policy) {
			this.result=res;
			this.message=message;
			this.lastVerifiedTime=verTime;
			this.pr=policy;}
}