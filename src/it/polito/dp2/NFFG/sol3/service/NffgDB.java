package it.polito.dp2.NFFG.sol3.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class NffgDB {

	public static ConcurrentMap<String,Nffg> map= new ConcurrentHashMap<String,Nffg>();
	
	
	private static String fixed = "nffg";
	private static long last=0;
	
	public static ConcurrentMap<String, Nffg> getMap() {
		return map;
	}

	public static void setMap(ConcurrentMap<String, Nffg> map) {
		NffgDB.map = map;
	}
	
	
	
	public static String getNext(){
		last++;
		return fixed+last;
	}
	
	
}
