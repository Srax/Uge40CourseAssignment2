/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import entity.Address;
import entity.CityInfo;
import entity.Person;
import entity.PersonDTO;
import errorhandling.*;
import facade.FacadePerson;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jdk.nashorn.internal.parser.JSONParser;
import facade.*;
import java.util.List;

/**
 * REST Web Service
 *
 * @author thoma
 */
@Path("data")
public class RestPerson {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonParser jsonParser = new JsonParser();

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
    FacadePerson fp = new FacadePerson(emf);
    FacadeCityInfo fc = new FacadeCityInfo(emf);

    @Context
    private UriInfo context;

    public RestPerson() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        return "This is a restful API.";
    }

    //tester
    @Path("test/{hej}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String testPathParam(@PathParam("hej") String hej) {
        return gson.toJson(hej);
    }

    @Path("/person/hobby/{hobbyName}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getPersonWithHobby(@PathParam("hobbyName") String hobbyName)throws PersonNotFoundException{
        return fp.getPersonWithHobby(hobbyName);
    }
    
    @Path("/count/{hobbyName}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int getNumberOfPersonWithHobby(@PathParam("hobbyName") String hobbyName) throws ParamaterNoMatchException{
        return fp.getNumberOfPersonWithHobby(hobbyName);
    }
    
    @Path("/person/phone/{phoneNum}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PersonDTO getPersonByPhone(@PathParam("phoneNum") String phoneNum)throws ParamaterNoMatchException{
        return fp.getPersonByPhone(phoneNum);
    }

    @Path("/{zipCode}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getAllPersonsByZip(@PathParam("zipCode") String zipCode)throws InternalException{
        return fp.getAllPersonsByZip(zipCode);
    }
    
    @Path("/person/id/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PersonDTO getPersonById(@PathParam("id") Integer id)throws PersonNotFoundException{
        return fp.getPersonById(id);
    }

    @Path("createperson")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerson(String json) throws ParamaterNoMatchException {
        Person p = gson.fromJson(json, Person.class);
        if (p.getFirstName() == null || p.getLastName() == null || p.getEmail() == null) {
            throw new ParamaterNoMatchException("Please enter a valid firstname, lastname or email.s");
        } else if ((p.getFirstName().length() <= 1) || (p.getLastName().length() <= 1)) {
            throw new ParamaterNoMatchException("Your firstname and lastname must be at least 2 characters long.");
        }
        fp.addPerson(p);
        return Response.ok(json).build();
    }


    
    
    @Path("zipcodes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CityInfo> getAllCitys() {
        return fc.getAllZipCodes();
    }

    @Path("cityinfo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CityInfo> getAllCityInfo()throws InternalException{
        
        return fc.getAllCityInfo();
    }
    
    
}
