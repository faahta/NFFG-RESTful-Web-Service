package it.polito.dp2.NFFG.sol3.client2;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;

public class NffgImpl implements NffgReader {

	String nffg_name;
	Calendar updateTime;
	Set<NodeReader> node_list = new HashSet<NodeReader>();
	Set<LinkReader> link_list = new HashSet<LinkReader>();
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.nffg_name;
	}

	@Override
	public NodeReader getNode(String arg0) {
		// TODO Auto-generated method stub
		NodeReader node= null;
		for(NodeReader nr:node_list){
			if(nr.getName().equals(arg0)){
				node=nr;
			}			
		}
		return node;
	}

	@Override
	public Set<NodeReader> getNodes() {
		// TODO Auto-generated method stub
		return this.node_list;
	}

	@Override
	public Calendar getUpdateTime() {
		// TODO Auto-generated method stub
		return this.updateTime;
	}
	
	public NffgImpl(String nffg_name, Calendar updTime, Set<NodeReader> nodes) {
		this.nffg_name=nffg_name;
		this.updateTime=updTime;
		this.node_list=nodes;
		//this.
	}
	public NffgImpl(String nffg_name){
		this.nffg_name=nffg_name;
	}

}
