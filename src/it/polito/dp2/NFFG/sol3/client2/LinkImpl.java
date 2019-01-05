package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.NodeReader;


public class LinkImpl implements it.polito.dp2.NFFG.LinkReader {

	String link_name;
	NodeReader src,dst;
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.link_name;
	}

	@Override
	public NodeReader getDestinationNode() {
		// TODO Auto-generated method stub
		return this.dst;
	}

	@Override
	public NodeReader getSourceNode() {
		// TODO Auto-generated method stub
		return this.src;
	}
	
	public LinkImpl(String link_name,NodeReader src,NodeReader dst){
		this.link_name=link_name;
		this.src=src;
		this.dst=dst;
	}

}
