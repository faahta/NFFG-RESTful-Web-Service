package it.polito.dp2.NFFG.sol3.client2;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;
;

public class PolicyImpl implements PolicyReader{

	String policy_name;
	NffgReader nffg_name;
	VerificationResultReader result;
	Boolean isPos;
	Set<FunctionalType> traversedFunc= new HashSet<FunctionalType>();
	NodeReader srcNode,dstNode;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.policy_name;
	}

	@Override
	public NffgReader getNffg() {
		// TODO Auto-generated method stub
		return this.nffg_name;
	}

	@Override
	public VerificationResultReader getResult() {
		// TODO Auto-generated method stub
		return this.result;
	}

	@Override
	public Boolean isPositive() {
		// TODO Auto-generated method stub
		return this.isPos;
	}

	public PolicyImpl(String policy_name){
		this.policy_name=policy_name;
	}
	public PolicyImpl(String policy_name,NffgReader nffg_name,NodeReader srcNode,NodeReader dstNode,
				Boolean isPos){
		//if(v!=null){
			this.policy_name=policy_name;
			this.nffg_name=nffg_name;
			this.srcNode=srcNode;
			this.dstNode=dstNode;
			this.isPos=isPos;}
	public PolicyImpl(String policy_name,NffgReader nffg_name,NodeReader srcNode,NodeReader dstNode,
			VerificationResultReader v,Boolean isPos){
		//if(v!=null){
			this.policy_name=policy_name;
			this.nffg_name=nffg_name;
			this.srcNode=srcNode;
			this.dstNode=dstNode;
			this.isPos=isPos;
			this.result=v;
		}

}
