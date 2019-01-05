package it.polito.dp2.NFFG.sol3.service;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import it.polito.dp2.NFFG.lab3.AlreadyLoadedException;

//baseURI : http://localhost:8080/NffgService/rest
@Path("/policies")
@Api(value = "/policies", description = "a collection of policy objects")
public class policiesResource {

	NffgService service= new NffgService();
	
	@GET
	@ApiOperation(	value = "get the policy object ", notes = "xml and json formats")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public List<Policy> getPoliciesXML(){		
		List<Policy> policy= service.getPolicies();
		return policy;		
	}
	@GET
	@Path("{id}")
	@ApiOperation(	value = "get a single policy object", notes = "XML format")
	@ApiResponses(value = {
	    		@ApiResponse(code = 200, message = "OK"),
	    		@ApiResponse(code = 404, message = "Not Found"),
	    		@ApiResponse(code = 500, message = "Internal Server Error")})
	 @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Policy getPolicyXML(@PathParam("id") String id){
		Policy policy= service.getPolicy(id);
		return policy;
	}
	
	
	
    @POST
    @ApiOperation(	value = "create a new policy object", notes = "xml and json formats"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 201, message = "Created"),
    		@ApiResponse(code = 403, message = "Forbidden because policy post failed"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    
	public Response postPolicyXML(Policy policy,@Context UriInfo uriInfo) throws DatatypeConfigurationException, AlreadyLoadedException{
    	Policy created= service.createPolicy(policy);
    	if (created != null) { // success
        	UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        	URI u = builder.path(created.getPolicyName()).build();
        	return Response.created(u).entity(created).build();
    	} else
    		throw new ForbiddenException("Forbidden because Policy post failed");
    }   
    @PUT
    @Path("{id}/verification")
    @ApiOperation(	value = "update policy verifcation", notes = "XML and JSON formats"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 403, message = "Forbidden because policy verification failed"),
			@ApiResponse(code = 404, message = "Not found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response putVerfiyPolicyXML(@PathParam("id") String id,Policy policy,@Context UriInfo uriInfo) throws Exception{
    	Policy toBeVerified= service.getPolicy(id);
    		
			//if(toBeVerified==null)	throw new NotFoundException();
			VerificationResult vr= new VerificationResult();
			
			if(service.isReachable(toBeVerified.getSrcNode(), toBeVerified.getDstNode())&&toBeVerified.isIsPositive()){				
				vr.setResult(true);
				vr.setMessage("Policy result is true");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				String date = sdf.format(new Date());
				XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
				vr.setLastVerifiedTime(xmlCal);
				toBeVerified.setVerificationResult(vr);
				
			}
			if(!service.isReachable(toBeVerified.getSrcNode(), toBeVerified.getDstNode())&&toBeVerified.isIsPositive()){
				vr.setResult(false);
				vr.setMessage("Policy result is false");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				String date = sdf.format(new Date());
				XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
				vr.setLastVerifiedTime(xmlCal);
				toBeVerified.setVerificationResult(vr);
			}
			if(service.isReachable(toBeVerified.getSrcNode(), toBeVerified.getDstNode())&&!toBeVerified.isIsPositive()){
				vr.setResult(false);
				vr.setMessage("Policy result is false");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				String date = sdf.format(new Date());
				XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
				vr.setLastVerifiedTime(xmlCal);
				toBeVerified.setVerificationResult(vr);
			}
			if(!service.isReachable(toBeVerified.getSrcNode(), toBeVerified.getDstNode())&&!toBeVerified.isIsPositive()){
				vr.setResult(true);
				vr.setMessage("Policy result is true");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				String date = sdf.format(new Date());
				XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
				vr.setLastVerifiedTime(xmlCal);
				toBeVerified.setVerificationResult(vr);
			}
			Policy newpolicy= service.updatePolicy(toBeVerified);
			if (newpolicy == null)
	    		throw new ForbiddenException("Forbidden");
			else
	    		return Response.ok(newpolicy).build();
			
		
    	//return Response.ok(toBeVerified).build();
  
    }
    
    @PUT
    @ApiOperation(	value = "update policy verifcation", notes = "XML and JSON formats"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 403, message = "Forbidden because policy verification failed"),
			@ApiResponse(code = 404, message = "Not found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response putPolicyXML(Policy policy,@Context UriInfo uriInfo){
		//policy.setVerificationResult(null);
		Policy updated=service.updatePolicy(policy);
    	if(updated==null) return Response.notModified().build();
    	return Response.ok(updated).build();
    	
    	
    }
    
    
		 	 
	 @DELETE
	 @Path("{id}")
	 @ApiOperation(	value = "remove a Policy object", notes = "XML and Plain text formats"
	 )
	    @ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "Not Found"),
		@ApiResponse(code = 500, message = "Internal Server Error")})
	 
	 @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	    public Policy deletePolicyJson(@PathParam("id") String id){	    	
	    	Policy deleted = service.deletePolicy(id);
	    	if (deleted != null) { // success
	        	return deleted;
	    	} else
	    		throw new NotFoundException();	
	    }
	 
}
