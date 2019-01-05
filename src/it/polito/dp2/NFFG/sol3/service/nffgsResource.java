package it.polito.dp2.NFFG.sol3.service;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.datatype.DatatypeConfigurationException;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import it.polito.dp2.NFFG.lab3.AlreadyLoadedException;


//baseURI : http://localhost:8080/NffgService/rest
@Path("/nffgs")
@Api(value = "/nffgs", description = "a collection of NFFG objects")
public class nffgsResource {

	NffgService service = new NffgService();
	
	@GET
	@ApiOperation(	value = "get the nffg objects ", notes = "xml and json formats")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public List<Nffg> getNffgsXML(){
		System.out.println("Inside get Method");
		List<Nffg> nffgs= service.getNffgs();
		return nffgs;
	}
	
	@GET
	@Path("{nffgId}")
	@ApiOperation(	value = "get a single NFFG object", notes = "XML format")
	@ApiResponses(value = {
	    		@ApiResponse(code = 200, message = "OK"),
	    		@ApiResponse(code = 404, message = "Not Found"),
	    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})	 
	public Nffg getNffgXML(@PathParam("nffgId") String nffgId){
		System.out.println("Inside get with id Method");
		Nffg nffg= service.getNffg(nffgId);
		if(nffg==null) throw new NotFoundException();
		return nffg;		
	}
	
    @POST
    @ApiOperation(	value = "create a new NFFG object", notes = "xml and json format"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 201, message = "Created"),
    		@ApiResponse(code = 403, message = "Forbidden because Nffg post failed"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response postNffgXML(Nffg nffg,@Context UriInfo uriInfo) throws DatatypeConfigurationException, AlreadyLoadedException {
	
    	System.out.println("Inside Post Method");
    		Nffg created=service.createNffg(nffg);
    		//Nffg created=service.performNeo4JNffgPost(nffg);
   
    	
        	UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        	URI u = builder.path(created.getNffgId()).build();
        	return Response.created(u).entity(created).build();
    	
    	
    }    
    @DELETE
    @Path("{id}")
    @ApiOperation(	value = "remove a nffg object", notes = "json and xml formats"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Nffg deleteNegotiateJson(@PathParam("id") String id){
    	
    	Nffg deleted = service.deleteNffg(id);
    	if (deleted != null) { // success
    		
        	return deleted;
    	} else
    		throw new NotFoundException();	

    }
    
  /*  
    @POST
    @ApiOperation(	value = "create one or more NFFGs", notes = "XML format"
    		)
    @ApiResponses(value = {
    		@ApiResponse(code = 201, message = "Created"),
    		@ApiResponse(code = 403, message = "Forbidden because negotiation failed"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public Response postNffgsXML(List<Nffg> nffgs,@Context UriInfo uriInfo) throws Exception{
		for(Nffg nffg:nffgs){
			Nffg created= service.createNffg(nffg);
			if(created!=null){
				UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	        	URI u = builder.path(created.getNffgId()).build();
	        	return Response.created(u).entity(created).build();
			} else{
	    		throw new ForbiddenException("Forbidden because Nffg post failed");
			}
		}
    	return Response.notModified().build();
    	
    	
    }*/
 //*******************************************NODES*********************************************
  /*
    @GET
	@ApiOperation(	value = "get the node objects of and NFFG ", notes = "XML format")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces(MediaType.APPLICATION_XML)
	public List<Mynode> getNodesXML(@PathParam("nffgId") String nffgId){
		List<Mynode> nodes= service.getMynodes(nffgId);
		if(nodes==null)	throw new NotFoundException();
		return nodes;
		
	}
	*/
/*	
	@GET
	@Path("{nodeId}")
	@ApiOperation(	value = "get a single node object of an NFFG", notes = "XML format")
	@ApiResponses(value = {
	    		@ApiResponse(code = 200, message = "OK"),
	    		@ApiResponse(code = 404, message = "Not Found"),
	    		@ApiResponse(code = 500, message = "Internal Server Error")})
	 @Produces({MediaType.APPLICATION_XML})
	 
	public Mynode getNodeXML(@PathParam("nffgId") String nffgId,@PathParam("nodeId") String nodeId){
		Mynode mynode= service.getMynode(nffgId,nodeId);
		if(mynode==null) throw new NotFoundException();
		return mynode;		
	}
	
    
    @POST
    @Path("{nffgId}/nodes")
    @ApiOperation(	value = "create a new node object", notes = "XML format"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 201, message = "Created"),
    		@ApiResponse(code = 403, message = "Forbidden"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public Response postNodeXML( @PathParam("nffgId") String nffgId, Mynode mynode,@Context UriInfo uriInfo) throws Exception{
		
    	Mynode created=service.createNode(nffgId,mynode);

    	if (created != null) { // success
        	UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        	URI u = builder.path(created.getNodeId()).build();
        	return Response.created(u).entity(created).build();
    	} else
    		throw new ForbiddenException("Forbidden, Nffg post failed");
    	
    }
	*/
}
