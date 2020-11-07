package com.cmi;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.SQLException;

import com.cmi.model.EPoint;
import com.cmi.database.EPointJDBC;

/**
 * @author Sergei Gribanov
 *
 */

@Path("/epoint")
public class EPointService {
    private String url = "jdbc:postgresql://172.17.0.3/cmi";
    private String user = "postgres";
    private String password = "1234";
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEPointJSONHandler() {
	EPoint pt = new EPoint();
	pt.setExpTag("HIGH2017");
	pt.setPointTag("800_40142");
	pt.setBeamEnergy(800.3);
	pt.setBeamEnergyError(0.5);
	pt.setMagneticField(1.3);
	return Response.status(200).entity(pt).build();
    }
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEPointList() {
	try {
	    EPointJDBC pjdbc = new EPointJDBC(url, user, password);
	    ArrayList<EPoint> array = pjdbc.getAllEPoints();
	    return Response.ok(array).build();
	} catch (ClassNotFoundException e) {
	    // TO DO
	} catch (SQLException e) {
	    // TO DO
	}
	return Response.ok(new ArrayList<EPoint>()).build();
	
    }
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEPointHandler(EPoint point) {
	String result = "energy point added successfully : " + point.getPointTag();
	// write code to add energy point into db or in-memory.
	return Response.status(201).entity(result).build();
    }
}
