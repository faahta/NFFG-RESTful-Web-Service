package it.polito.dp2.NFFG.sol3.client1;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFFG.FactoryConfigurationError;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.NffgVerifierFactory;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;
import it.polito.dp2.NFFG.lab3.AlreadyLoadedException;
import it.polito.dp2.NFFG.lab3.ServiceException;
import it.polito.dp2.NFFG.lab3.UnknownNameException;
import it.polito.dp2.NFFG.sol3.service.Link;
import it.polito.dp2.NFFG.sol3.service.Mynode;
import it.polito.dp2.NFFG.sol3.service.Nffg;
import it.polito.dp2.NFFG.sol3.service.NffgDB;
import it.polito.dp2.NFFG.sol3.service.Policy;
import it.polito.dp2.NFFG.sol3.service.VerificationResult;


public class NFFGClient implements it.polito.dp2.NFFG.lab3.NFFGClient {

	Set<NffgReader> nffgs=null;
	Set<PolicyReader> policies=null;
	NffgReader loaded_nffg= null;
	Set<NodeReader> nodes= new HashSet<NodeReader>();
	static WebTarget target;
	static Client client;

	
	private static NffgVerifier ver;
	static String baseURI= System.getProperty("it.polito.dp2.NFFG.lab3.URL");
	static ConcurrentMap<String,Nffg> nffgmap= NffgDB.getMap();
	
	public NFFGClient(){		
		client = ClientBuilder.newClient();	    
		target = client.target(getBaseURI());
		
		if(System.getProperty("it.polito.dp2.NFFG.lab3.URL")==null){
			System.setProperty("it.polito.dp2.NFFG.lab3.URL", "http://localhost:8080/NffgService/rest");
		}
		
		System.setProperty("it.polito.dp2.NFFG.NffgVerifierFactory", "it.polito.dp2.NFFG.Random.NffgVerifierFactoryImpl");
		try {
			ver= NffgVerifierFactory.newInstance().newNffgVerifier();
		} catch (NffgVerifierException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void loadNFFG(String name) throws UnknownNameException, AlreadyLoadedException, ServiceException {		
		loaded_nffg=ver.getNffg(name);	
		for(Nffg nffg:getAllNffgs()){
			if(nffg.getNffgId().equals(name)){
				throw new AlreadyLoadedException();
			}
		}
		if(loaded_nffg!=null){
			Nffg nffg= new Nffg();
			nffg.setNffgId(name);
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				String date = sdf.format(new Date());
				
					XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
					nffg.setLastUpadateTime(toXMLGregorianCalendar(loaded_nffg.getUpdateTime()));
				} catch (DatatypeConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			for(NodeReader nr:loaded_nffg.getNodes()){			
				Mynode mynode=new Mynode();
				mynode.setNodeId(nr.getName());
				mynode.setFuncType(nr.getFuncType().name());
								
				for(LinkReader lr:nr.getLinks()){					
					Link link= new Link();
					link.setLinkId(lr.getName());
					link.setSrc(lr.getSourceNode().getName());
					link.setDst(lr.getDestinationNode().getName());
					mynode.getLink().add(link);
				}
				nffg.getNode().add(mynode);
				performNffgPost(nffg);				
			}
		}else{
			throw new UnknownNameException();
		}
		
	}

	@Override
	public void loadAll() throws AlreadyLoadedException, ServiceException {
		nffgs=ver.getNffgs();
		policies=ver.getPolicies();
			for(NffgReader nfr:nffgs){
				//check if there are existing NFFGs in the service
			
					for(Nffg n:getAllNffgs()){
						if(n.getNffgId().equals(nfr.getName())){
							throw new AlreadyLoadedException();
						}
					}
								
				Nffg nffg= new Nffg();
				if(nfr!=null){
					nffg.setNffgId(nfr.getName());
					try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
					sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
					String date = sdf.format(new Date());
					
						XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
						nffg.setLastUpadateTime(toXMLGregorianCalendar(nfr.getUpdateTime()));
					} catch (DatatypeConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					for(NodeReader nr:nfr.getNodes()){
						Mynode mynode= new Mynode();
						mynode.setNodeId(nr.getName());
						mynode.setFuncType(nr.getFuncType().name());
						for(LinkReader lr:nr.getLinks()){
							Link link= new Link();
							link.setLinkId(lr.getName());
							link.setSrc(lr.getSourceNode().getName());
							link.setDst(lr.getDestinationNode().getName());
							mynode.getLink().add(link);
						}
						nffg.getNode().add(mynode);						
					}
					//System.out.println("POSTED NFFG :" +nffg.getNffgId()+" NODE SIZE"+nffg.getNode().size());
			
					//POST TO NFFGSERVICE
					performNffgPost(nffg);				
				}
				
		}	
			for(PolicyReader pr:policies){			
				Policy policy= new Policy();
				policy.setPolicyName(pr.getName());
				policy.setNffgId(pr.getNffg().getName());
		        policy.setSrcNode(((ReachabilityPolicyReader)pr).getSourceNode().getName());
		        policy.setDstNode(((ReachabilityPolicyReader)pr).getDestinationNode().getName());
		        policy.setIsPositive(pr.isPositive());
		        VerificationResult vr= new VerificationResult();
		        VerificationResultReader vrr= pr.getResult();
		        if(vrr!=null){
		        	vr.setResult(vrr.getVerificationResult());
		        	vr.setMessage(vrr.getVerificationResultMsg());
		        	try {
						vr.setLastVerifiedTime(toXMLGregorianCalendar(vrr.getVerificationTime()) );
					} catch (DatatypeConfigurationException e) {
						e.printStackTrace();
					}
		        	
		        } else{
		        	vr.setResult(null);
		        	vr.setMessage(null);
		        	vr.setLastVerifiedTime(null);
		        }
		        policy.setVerificationResult(vr);
		        performPolicyPost(policy);
			}
		
				
	}

	@Override
	public void loadReachabilityPolicy(String name, String nffgName, boolean isPositive, String srcNodeName,
			String dstNodeName) throws UnknownNameException, ServiceException {
		System.out.println("NFFGClient - LOADING REACHABILITY POLICY TO SERVICE-----\n");
		CopyOnWriteArrayList<Policy> policies= new CopyOnWriteArrayList<Policy>();
		policies.addAll(getAllPolicies());
		for(Policy p:policies){
			//System.out.println("NFFGClient - CheCkInG if :"+p.getPolicyName()+" exists\n");
			if(p.getPolicyName().equals(name)){
				performPolicyPut(p); 				
				//System.out.println("pOlIcY :"+p.getPolicyName()+"EXISTS");
			}
		}		
		System.out.println("--doneChEcKinG IF poLiCy existS--");
		Policy policy= new Policy();
		System.out.println("--sEttInG poLiCy nfFGNamE--\n");
		policy.setPolicyName(name);
		System.out.println("--sEttInG poLiCy isPositive--\n");
		policy.setIsPositive(isPositive);
		
		
		CopyOnWriteArrayList<Nffg> myNffg= new CopyOnWriteArrayList<Nffg>();
		myNffg.addAll(getAllNffgs());
		//Nffg policynffg=null;
		for(Nffg nffg:myNffg){
			//System.out.println("--cHeKiNg IF poLiCy nfFG eXisTs--");
			if(nffg.getNffgId().equals(nffgName)){
				policy.setNffgId(nffgName);	//set nffg name only if it exists				
				CopyOnWriteArrayList<Mynode> myN= new CopyOnWriteArrayList<Mynode>();
				myN.addAll(nffg.getNode());
				for(Mynode mynode:myN){
					if(mynode.getNodeId().equals(srcNodeName)){
						System.out.println("--sEttInG poLiCy souRcENodE--\n");
						policy.setSrcNode(srcNodeName);
					}
					if(mynode.getNodeId().equals(dstNodeName)){
						System.out.println("--sEttInG poLiCy dEstInaTioNNodE--\n");
						policy.setDstNode(dstNodeName);
					}
				}				
				
			}
			System.out.println("NFFGClient - policy size BeforE Post - "+getAllPolicies().size());
			System.out.println("NFFGClient - policy size After Post - "+getAllPolicies().size());

			
		
		}
		
		performPolicyPost(policy);
		//performPolicyPost(policy);
		
	}

	@Override
	public void unloadReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
		//UnknownNameException un= new UnknownNameException();
		//boolean isDeleted=false;
		for(Policy p:getAllPolicies()){
			if(p.getPolicyName().equals(name)){
				deletePolicy(p.getPolicyName());//Delete the policy,If it exists in the service
				//isDeleted=true;
			}
		}
	

	}

	@Override
	public boolean testReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
		System.out.println("NFFGClient  -  TESTING REACHABILITY *********");
		boolean policyExists=false;
		Policy policy= new Policy();
		for(Policy p:getAllPolicies()){
			if(p.getPolicyName().equals(name)){
				policy=p;						
				policyExists=true;				
			}		
		}
		if(policyExists==false){			
			throw new UnknownNameException();
		} else
			return verifyPolicy(policy).isResult();
	}

//------------------------------OTHER METHODS-------------------------
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
	
	public String performNffgPost(Nffg nffg) throws AlreadyLoadedException{
		System.out.println("--------------- Starting NFFG Post...\n");
		if(getAllNffgs()!=null){
			for(Nffg n:getAllNffgs()){
				if(n.getNffgId().equals(nffg.getNffgId())){
					System.out.println("EXISTING NFFG ID: "+n.getNffgId());
					return n.getNffgId();
				}
			}
		}	
		System.out.println("--------------- Performing NFFG Post --------------- \n");

		Nffg response= target.path("nffgs")
						 	 .request("application/json")
						     .accept("application/json")
						     .post(Entity.entity(nffg,"application/json"),Nffg.class);
		
		System.out.println();
		System.out.println("RESPONSE OF NEW NFFG POST RECEIVED");
		System.out.println("NffgClient- CREATED NFFG ID: "+response.getNffgId()+" NODE SIZE: "+response.getNode().size());	
		
		return response.getNffgId();
	}
	
	public String performPolicyPost(Policy policy){
		//if(getAllPolicies()!=null){
			for(Policy p:getAllPolicies()){
				if(p.getNffgId().equals(policy.getPolicyName())){
					return p.getPolicyName();
				}
			}
		
		System.out.println("--------------- Performing POLICY Post --------------- \n");
		client = ClientBuilder.newClient();	    
		target = client.target(getBaseURI());
		Policy response= target.path("policies")
				 				.request("application/json")
				 				.accept("application/json")
				 				.post(Entity.entity(policy,"application/json"),Policy.class);
		System.out.println();
		System.out.println("RESPONSE OF NEW POLICY POST RECEIVED");
		System.out.println("NffgClient - CREATED POLICY ID: "+response.getPolicyName());
		return response.getPolicyName();	
				
		
			
	} 
	public Policy performPolicyPut(Policy policy){
		System.out.println("--------------- Performing POLICY Update --------------- \n");
		client = ClientBuilder.newClient();	    
		target = client.target(getBaseURI());
		System.out.println("--------------- Performing POLICY Post --------------- \n");
		Policy response= target.path("policies")
				 				.request("application/json")
				 				.accept("application/json")
				 				.put(Entity.entity(policy,"application/json"),Policy.class);
		return response;
	}
	
	public List<Nffg> getAllNffgs(){
			client = ClientBuilder.newClient();
			 target = client.target(getBaseURI());
			 List<Nffg> response= target.path("nffgs")
								 .request()
								 .accept("application/json")
								 .get(new GenericType<List<Nffg>>() {});
			
		return response;
		
	}
	
	public List<Policy> getAllPolicies(){
		 client = ClientBuilder.newClient();
		 target = client.target(getBaseURI());
		 List<Policy> response= target.path("policies")
							 		  .request()
							          .accept(MediaType.APPLICATION_JSON)
							          .get(new GenericType<List<Policy>>() {});
			
		 return response;
	}
	
	public void deletePolicy(String policyName){;
		client = ClientBuilder.newClient();	    
		target = client.target(getBaseURI());
		String response= target.path("policies")
						 		 .path(policyName)
								 .request()
								 .accept("application/json")
								 .delete(String.class);
		System.out.println("RESPONSE OF UNLOAD POLICY RECEIVED");
		System.out.println("deleted POLICy: "+response);	
	}
	
	public VerificationResult verifyPolicy(Policy policy){
		
			client = ClientBuilder.newClient();	    
			target = client.target(getBaseURI());
			Policy response= target.path("policies")
									.path(policy.getPolicyName())
									.path("verification")
									.request("application/json")
									.accept("application/json")
									.put(Entity.entity(policy,"application/json"),Policy.class);
			System.out.println();
			System.out.println("-----------RESPONSE OF VERIFY POLICY RECEIVED-----------");
			//System.out.println("Verification Result: "+response.getVerificationResult().isResult());
			//System.out.println("Message: "+response.getVerificationResult().getMessage());
			//System.out.println("Last Verified Time: "+response.getVerificationResult().getLastVerifiedTime());
			
			return 	response.getVerificationResult();	
	}
	
	public static XMLGregorianCalendar toXMLGregorianCalendar(Calendar c)
			  throws DatatypeConfigurationException {
			 GregorianCalendar gc = new GregorianCalendar();
			 gc.setTimeInMillis(c.getTimeInMillis());
			 XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
			 return xc;
			}
	//GET CURRENT DATETIME AS XMLGREGORIAN
		 public XMLGregorianCalendar getXMLGregorianCalendarNow() 
		            throws DatatypeConfigurationException
		    {
		        GregorianCalendar gregorianCalendar = new GregorianCalendar();
		        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
		        XMLGregorianCalendar now = 
		            datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		        return now;
		    }
	

}
