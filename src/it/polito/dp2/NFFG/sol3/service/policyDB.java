package it.polito.dp2.NFFG.sol3.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class policyDB {

	
	public static ConcurrentMap<String,Policy> map= new ConcurrentHashMap<String,Policy>();
	private static String fixed = "policy";
	private static long last=0;
	
	public static ConcurrentMap<String, Policy> getMap() {
		return map;
	}

	public static void setMap(ConcurrentMap<String, Policy> map) {
		policyDB.map = map;
	}
	
	public static String getNext(){
		last++;
		return fixed+last;
	}
	
}
