package it.polito.dp2.NFFG.sol3.service;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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

import it.polito.dp2.NFFG.lab3.AlreadyLoadedException;

public class NffgService {
	static WebTarget target;
	static Client client;
	//static String baseURI= System.getProperty("it.polito.dp2.NFFG.lab2.URL");
	public static ConcurrentMap<String,Policy> policymap= policyDB.getMap();
	public static ConcurrentMap<String,Nffg> nffgmap= NffgDB.getMap();

	
	public NffgService(){
		client = ClientBuilder.newClient();	    
		target = client.target(getBaseURI());	
	} 
	
//********************************NFFGS************************************
	public CopyOnWriteArrayList<Nffg> getNffgs(){
		return new CopyOnWriteArrayList<Nffg>(nffgmap.values());
		
	}
	
	public Nffg getNffg(String id){
		if(id==null) return null;
		return nffgmap.get(id);
	}
	
	public Nffg createNffg(Nffg nffg) throws DatatypeConfigurationException, AlreadyLoadedException {
		
		System.out.println("Inside create nffg Method");

		System.out.println("------------CHECKING IF NFFG TO EXISTS IN NffgService--------------");
		for(Nffg n:getNffgs()){			
			if(n.getNffgId().equals(nffg)){
				throw new AlreadyLoadedException();
			}
		}
		System.out.println("------------SAVING NFFG TO NEO4JXML--------------");
		performNeo4JNffgPost(nffg);
		System.out.println("------------SAVING NFFG TO NffgService--------------");
	
		nffgmap.put(nffg.getNffgId(), nffg);					
		System.out.println("------------DONE! NFFG CREATED--------------");
		
		return nffg;
				
	}
	
	

	
//*********METHODS TO READ AND WRITE TO NEO4JXML**************
	
	public Nffg performNeo4JNffgPost(Nffg nffg){
		if(nffg==null) return null;
		
		Node nffgnode= new Node();
		Labels label= new Labels();		
		label.getValue().add("NFFG");
		nffgnode.setLabels(label);
		
		Property nffgprop= new Property();
		nffgprop.setName("name");
		nffgprop.setValue(nffg.getNffgId());
		nffgnode.getProperty().add(nffgprop);

		performNodePost(nffgnode);
		
		System.out.println("NEO4J - nFfG node created");
		CopyOnWriteArrayList<Mynode> nodes=new CopyOnWriteArrayList<Mynode>();
		nodes.addAll(nffg.getNode());
		for(Mynode mynode: nodes){
			System.out.println("NEO4J - INSIDE node loop");
			Node node= new Node();						
			Property prop= new Property();
			prop.setName("name");
			prop.setValue(mynode.getNodeId());
			node.getProperty().add(prop);	//set name property	
			System.out.println("NEO4J - perFoRmIng node post...");
			performNodePost(node);
			
			//make 'belongs' relation with nffgnode
			Relationship belongs= new Relationship();
			belongs.setSrcNode(Integer.toString(nodeExists(nffgnode)) );
			belongs.setDstNode(Integer.toString(nodeExists(node)));
			belongs.setType("belongs");
			System.out.println("NEO4J - perFoRmIng belongs relationship post...");
			performRelationPost(belongs);
			
			CopyOnWriteArrayList<Link> links= new CopyOnWriteArrayList<Link>();
			links.addAll(mynode.getLink());
			for(Link link:links){
				System.out.println("NEO4J - INSIDE relationship loop");
				Relationship rship= new Relationship();
				//SOURCE NODE	
				Node srcnode= new Node();
				Property sprop= new Property();
				sprop.setName("name");
				sprop.setValue(link.getSrc());				
											
				srcnode.getProperty().add(sprop);	//set property name	

				rship.setSrcNode(Integer.toString(nodeExists(srcnode)));
				
				//DESTINATION NODE
				Node dstnode=new Node();		
				Property dprop= new Property();
				dprop.setName("name");
				dprop.setValue(link.getDst());
				
				dstnode.getProperty().add(dprop);	//set property name	
					
				rship.setDstNode(Integer.toString(nodeExists(dstnode)));
				
				rship.setType("Link");					
				performRelationPost(rship);			
			}
		}
		System.out.println("DONE! NFFG CREATED ON NEO4JXML");
		return nffg;		
	}
	
	public List<Node> getAllNodes(){
		List<Node> response = null;
		
			client = ClientBuilder.newClient();	    
			target = client.target(getBaseURI());
			 response= target.path("resource")
								 .path("nodes")
								 .request()
								 .accept(MediaType.APPLICATION_XML)
								 .get(new GenericType<List<Node>>() {});
			
		return response;
	}
	private static URI getBaseURI() {
		
	    return UriBuilder.fromUri("http://localhost:8080/Neo4JXML/rest").build();
		
	}
	public int performNodePost(Node node){

		for(Node n:getAllNodes()){
			if(n.getProperty().get(0).getValue().equals(node.getProperty().get(0).getValue())){
				return Integer.parseInt(n.getId());
			}
		}
			client = ClientBuilder.newClient();	    
			target = client.target(getBaseURI());
			Node response= target.path("resource")
								 .path("node")
								 .request("application/xml")
								 .post(Entity.entity(node,"application/xml"),Node.class);
			System.out.println();
			System.out.println("RESPONSE OF neo4J NODE POST RECEIVED");
			System.out.println("Node ID: "+Integer.parseInt(response.getId()));	
			return Integer.parseInt(response.getId());
}
	public void performRelationPost(Relationship rship){
		client = ClientBuilder.newClient();	    
		target = client.target(getBaseURI());
			Relationship response= target.path("resource")
										 .path("node/"+rship.getSrcNode()+"/relationship")
										 .request(MediaType.APPLICATION_XML)									 
										 .post(Entity.entity(rship,MediaType.APPLICATION_XML), Relationship.class);
	
			System.out.println("RESPONSE OF RELATIONSHIP POST RECEIVED");
			System.out.println("RELATION ID: "+response.getId());
			System.out.println("SOURCE: "+response.getSrcNode());
			System.out.println("DESTINATION: "+response.getDstNode());
			System.out.println("TYPE: "+response.getType());	
	
	}
	public int  nodeExists(Node node){
		
		Node nresponse = null;
		
			for(Node n:getAllNodes()){
			if(node.getProperty().get(0).getValue().equals(n.getProperty().get(0).getValue())){
				return Integer.parseInt(n.getId());
				}
			}
			//if node doesn't exist create the node and return it's id
			System.out.println("NEW NODE IS BEING CREATED...");
			client = ClientBuilder.newClient();	    
			target = client.target(getBaseURI());
			nresponse= target.path("resource")
							 .path("node")
							 .request("application/xml")
							 .post(Entity.entity(node,"application/xml"),Node.class);
			
		
		return Integer.parseInt(nresponse.getId());	
						
	}
		
//***************************POLICIES***************************************	
	public List<Policy> getPolicies(){
		return new ArrayList<Policy>(policymap.values());
	}
	public Policy getPolicy(String id){
		if(id==null)	return null;
		return policymap.get(id);
	}
	
	public Policy createPolicy(Policy policy) throws  AlreadyLoadedException{
		if(policy==null) return null;		
		System.out.println("Inside create policy Method");

		System.out.println("------------CHECKING IF policy TO EXISTS IN NffgService--------------");
		for(Policy p:getPolicies()){			
			if(p.getPolicyName().equals(policy.getPolicyName())){
				deletePolicy(policy.getPolicyName()); 
			}
		}
		policymap.put(policy.getPolicyName(), policy);	//create the policy
		return policy;
	
	}
	
	//TEST REACHABILITY
	public boolean isReachable(String srcName,String destName){
		System.out.println("......TESTING REACHABILITY........");
		System.out.println("FROM: "+srcName);
		System.out.println("TO: "+destName);
	
		String src = null;
		String dst = null;
	
		Node s= new Node();
		Property sprop= new Property();
		sprop.setName("name");
		sprop.setValue(srcName);
		s.getProperty().add(sprop);
		System.out.println("SOURCE NODE...CHECKING IF IT EXISTS");
		
		for(Node n:getAllNodes()){
			if(s.getProperty().get(0).getValue().equals(n.getProperty().get(0).getValue())){					
				src=n.getId();
				System.out.println("SOURCE NODE EXISTS...WITH ID: "+n.getId());
			}
		}
		//System.out.println("out from source node for loop");
		if(src==null){
			return false;
		}
		Node d= new Node();
		Property dprop= new Property();
		dprop.setName("name");
		dprop.setValue(destName);
		d.getProperty().add(dprop);
		//System.out.println("DESTINATION NODE...CHECKING IF IT EXISTS");
		
		for(Node n:getAllNodes()){
				if(d.getProperty().get(0).getValue().equals(n.getProperty().get(0).getValue())){
					dst=n.getId();
					//System.out.println("DESTINATION NODE EXISTS...WITH ID: "+n.getId());
				}
		}
		//System.out.println("out from destination node for loop");
		if(dst==null){
			return false;
		}			
		//System.out.println("SOURCE NODE ID: "+src);
		//System.out.println("DESTINATION NODE ID: "+dst);
		client = ClientBuilder.newClient();	    
		target = client.target(getBaseURI());
		List<Path> response= target.path("resource")
									.path("node")
									.path(src)
									.path("paths").queryParam("dst", dst)
									.request()
									.accept(MediaType.APPLICATION_XML)
									.get(new GenericType<List<Path>>() {});
		System.out.println("...RECEIVED PATH GET RESPONSE");
		for(Path p:response){
			if(p.getRelationship().isEmpty()||p.getNode().isEmpty()){
				System.out.println("NOT REACHABLE...about to throw exception..");
				return false;
			}
			else{return true;}
		}
		return false;
	}
	
	public Policy deletePolicy(String id){
		if(id==null)	return null;
		return policymap.remove(id);
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
	 public static XMLGregorianCalendar toXMLGregorianCalendar(Calendar c)
			  throws DatatypeConfigurationException {
			 GregorianCalendar gc = new GregorianCalendar();
			 gc.setTimeInMillis(c.getTimeInMillis());
			 XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
			 return xc;
			}
	 public Nffg deleteNffg(String Id){
		 Nffg deleted=nffgmap.remove(Id);
		 if(deleted==null)	System.out.println("nothing to delete");
		 System.out.println("Done Deleting!");
		 return deleted;
	 }
	 
	 public static void main(String[] args){
		 NffgService service= new NffgService();
		
			//System.out.println("nffg ID: "+nf.getNffgId());
			 service.deleteAllNodes();
			
		 
	 }

	
	 public Policy updatePolicy(Policy toBeVerified) {

				 VerificationResult vr= new VerificationResult();
				 vr.setResult(toBeVerified.getVerificationResult().isResult());
				 vr.setLastVerifiedTime(toBeVerified.getVerificationResult().getLastVerifiedTime());
				 vr.setMessage(toBeVerified.getVerificationResult().getMessage());				 
				 toBeVerified.setVerificationResult(vr);

				 return  policymap.put(toBeVerified.getPolicyName(), toBeVerified);

	}
		public void deleteAllNodes(){    	
			try{
				target = client.target(getBaseURI());		
				System.out.println("-----DELETING ALL EXISTING NODES IN THE NEO4J SERVICE----------");
				String response= target.path("resource")
									   .path("nodes")
									   .request(MediaType.APPLICATION_XML)
									   .accept(MediaType.TEXT_PLAIN)
									   .delete(String.class);			
				System.out.println("after delete");
				System.out.println("RESPONSE OF DELETE ALL NODES RECEIVED");
				System.out.println("LIST OF NODES: "+response);
				System.out.println("..........................DONE DELETING..........................");
			} catch(Exception e){
				e.printStackTrace();
			}
		}
}
