package it.polito.dp2.NFFG.sol3.client2;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;

public class NodeImpl implements NodeReader {

	String node_name;
	FunctionalType funcType;
	Set<LinkReader> link_list = new HashSet<LinkReader>();
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.node_name;
	}

	@Override
	public FunctionalType getFuncType() {
		// TODO Auto-generated method stub
		return this.funcType;
	}

	@Override
	public Set<LinkReader> getLinks() {
		// TODO Auto-generated method stub
		return this.link_list;
	}
	
	public NodeImpl (String node_name){
		this.node_name=node_name;
			
		//	node_name=nt.getNodeName();
			//funcType= FunctionalType.valueOf(nt.getFuncType());
		
				
	}
	public NodeImpl(String node_name,FunctionalType funcType,Set<LinkReader> node_links){
		this.node_name=node_name;
		this.funcType=funcType;
		this.link_list= node_links;
	}

}
