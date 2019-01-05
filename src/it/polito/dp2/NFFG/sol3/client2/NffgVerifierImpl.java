package it.polito.dp2.NFFG.sol3.client2;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.TraversalPolicyReader;
import it.polito.dp2.NFFG.sol3.client2.VerificationResultImpl;
import it.polito.dp2.NFFG.sol3.client2.LinkImpl;
import it.polito.dp2.NFFG.sol3.client2.NodeImpl;
import it.polito.dp2.NFFG.sol3.client2.NffgImpl;
import it.polito.dp2.NFFG.sol3.client2.PolicyImpl;
import it.polito.dp2.NFFG.sol3.service.Link;
import it.polito.dp2.NFFG.sol3.service.Mynode;
import it.polito.dp2.NFFG.sol3.service.Nffg;
import it.polito.dp2.NFFG.sol3.service.Policy;
import it.polito.dp2.NFFG.sol3.service.VerificationResult;


public class NffgVerifierImpl implements NffgVerifier {

	static WebTarget target;
	static Client client;
	static String baseURI= System.getProperty("it.polito.dp2.NFFG.lab3.URL");
	
	 public static Set<NffgImpl> nffglist= new HashSet<NffgImpl>();
	 public static Set<PolicyImpl> policylist = new HashSet<PolicyImpl>();
	
	@Override
	public NffgReader getNffg(String arg0) {
		NffgReader nr = null;
		for(NffgImpl nf:nffglist)
		{
			if(nf.getName().equals(arg0))
			{
				nr = (NffgReader)nf;
			}
		}
		return nr;
	}

	@Override
	public Set<NffgReader> getNffgs() {
		Set<NffgReader> nr = new HashSet<NffgReader>();
	      for(NffgImpl nf:nffglist)
			{
				nr.add(nf);
			}       
			return nr;
	}

	@Override
	public Set<PolicyReader> getPolicies() {
		Set<PolicyReader> pr = new HashSet<PolicyReader>();
	      for(PolicyImpl po:policylist)
			{
				pr.add(po);	
			}  
			return pr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<PolicyReader> getPolicies(String arg0) {
		Set<PolicyReader> pr = new HashSet<PolicyReader>();
		for(PolicyImpl po:policylist)
		{
			if(po.getNffg().getName().equals(arg0))
			{
				pr.add(po);
			}
		}
		return pr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<PolicyReader> getPolicies(Calendar arg0) {
		Set<PolicyReader> pr = null;
		for(PolicyImpl po:policylist)
		{
			if(po.getResult().getVerificationTime().equals(arg0))
			{
				pr = (Set<PolicyReader>) po;
			}
		}
		return pr;
	}

	public NffgVerifierImpl(){
		policylist.clear();
		client = ClientBuilder.newClient();	    
		target = client.target(getBaseURI());
		System.out.println("NffgVerifierImpl------GETTING ALL NFFGS AND POLICIES FROM THE SERVICE----");
		List<Nffg> nffgL= getAllNffgs();
		List<Policy> policyL= getAllPolicies();
//************************************NFFG*********************************************		
		CopyOnWriteArrayList<Nffg> nffgs= new CopyOnWriteArrayList<Nffg>();
		nffgs.addAll(nffgL);
		for(Nffg nffg:nffgs){
			//System.out.println("INSIDE NFFG LOOP");
			NffgImpl nfi;
			//List<Mynode> nodes= new CopyOnWriteArrayList<Mynode>();
			//nodes.addAll(nffg.getNode());
			//System.out.print("Nodes added to CopyOnWriteArrayList: "+nodes.size()+"\n");
			Set<NodeReader> nr= new HashSet<NodeReader>();				
			for(Mynode node:nffg.getNode()){
				String node_name=node.getNodeId();
				FunctionalType ft= FunctionalType.valueOf(node.getFuncType());					
							
				Set<LinkReader> lr= new HashSet<LinkReader>();				
				for(Link link:node.getLink()){
					String link_name=link.getLinkId();					
					NodeImpl src= new NodeImpl(link.getSrc());
					NodeImpl dst= new NodeImpl(link.getDst());					
					LinkImpl li= new LinkImpl(link_name, src, dst);										
					lr.add(li);											
				}
				NodeImpl ni= new NodeImpl(node_name,ft,lr);
				nr.add(ni);										
			}
			nfi= new NffgImpl(nffg.getNffgId(),nffg.getLastUpadateTime().toGregorianCalendar(),nr);											
			nffglist.add(nfi);
		}
		System.out.println("NffgVerifierImpl - doNE LoADinG NffG-----");
//************************************POLICY*********************************************		
		//CopyOnWriteArrayList<Policy> policies= new CopyOnWriteArrayList<Policy>();
		//policies.addAll(policyL);
		for(Policy policy:policyL){
			//System.out.println("INSIDE POLICY LOOP");
			//System.out.println("POLICY ID: "+policy.getPolicyName());
			String policy_name=policy.getPolicyName();
			NffgImpl nffg_name= new NffgImpl(policy.getNffgId());
			Boolean isPos=policy.isIsPositive();								
			NodeImpl src= new NodeImpl(policy.getSrcNode());
			NodeImpl dst= new NodeImpl(policy.getDstNode());
			VerificationResult verR= new VerificationResult();
			CopyOnWriteArrayList<PolicyImpl> policyImpl= new CopyOnWriteArrayList<PolicyImpl>();
			policyImpl.addAll(policylist);
			verR= policy.getVerificationResult();
			if(verR!=null){
				//VERIFICATION RESULT
				Boolean res=policy.getVerificationResult().isResult();
				String message= policy.getVerificationResult().getMessage();
				XMLGregorianCalendar verTime=policy.getVerificationResult().getLastVerifiedTime();				
				VerificationResultImpl vi;
				//WHEN VERFICATION RESULT IS NOT EMPTY
				if(res!=null && message!=null && verTime!=null){
					
					//REACHABILITY POLICY
						PolicyImpl pi = null;
						PolicyImpl get_policy=new PolicyImpl(policy_name);
						vi=new VerificationResultImpl(res,message,verTime.toGregorianCalendar(),get_policy);
						pi= new PolicyImpl(policy_name,nffg_name,src,dst,vi,isPos);					
						policylist.add(pi);//}			
					
				}
				else{
					PolicyImpl pi_null= new PolicyImpl(policy_name,nffg_name,src,dst,isPos);
					policylist.add(pi_null);
				}
			}
			else{
			
					PolicyImpl pi_null= new PolicyImpl(policy_name,nffg_name,src,dst,isPos);
					policylist.add(pi_null);				
				
			}
			
		}
		
		System.out.println("NffgVerifierImpl - policylist size - "+policylist.size());
		System.out.println("NffgVerifierImpl - getpolicies size - "+getPolicies().size());
		//for(PolicyReader pr:getPolicies()){
		//	System.out.println("REaderpolicyID: "+pr.getName());
		//}
		System.out.println("-----done LoADinG pOlIcY-----");
		System.out.println("NffgVerifierImpl - policy size on hashmap - "+getAllPolicies().size());
		//for(Policy p:getAllPolicies()){
		//	System.out.println("HashMappolicyID: "+p.getPolicyName());
		//}
	}
	
	public List<Nffg> getAllNffgs(){
		 System.out.println("NffgVerifierImpl---Performing GetAllNFFGs-----");
		 client = ClientBuilder.newClient();	    
			target = client.target(getBaseURI());
		 List<Nffg> response= target.path("nffgs")
								 .request("application/json")
								 .accept("application/json")
								 .get(new GenericType<List<Nffg>>() {});
		 System.out.println("RESPONSE OF GET AL NFFGS RECEIVED");
		//for (Nffg n:response){
			//System.out.println("NffgVerifierImpl - NffgID:"+n.getNffgId()+"node size: "+n.getNode().size());
		//}
		return response;		
		
	}
	public List<Policy> getAllPolicies(){
		 System.out.println("NffgVerifierImpl---Performing GetAllPolicies-----");
		 client = ClientBuilder.newClient();	    
			target = client.target(getBaseURI());
			 List<Policy> response= target.path("policies")
								 .request("application/json")
								 .accept("application/json")
								 .get(new GenericType<List<Policy>>() {});
			 System.out.println("RESPONSE OF GET AL POLICIES RECEIVED");
			 //for (Policy n:response){
				//	System.out.println("NffgVerifierImpl - POLICYID:"+n.getPolicyName());
				//}
		return response;
	}
	private static URI getBaseURI() {
		try {
			if(System.getProperty("it.polito.dp2.NFFG.lab3.URL")==null){
				System.setProperty("it.polito.dp2.NFFG.lab3.URL", "http://localhost:8080/NffgService/rest");
				}
		} catch(Exception e){
			e.printStackTrace();
		}
	    return UriBuilder.fromUri(baseURI).build();
		
	}
}
