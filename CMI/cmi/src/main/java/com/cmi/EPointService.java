package com.cmi;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.SQLException;
import java.io.InputStream;
import java.io.IOException;
import javax.ws.rs.core.StreamingOutput;

import com.cmi.model.EPoint;
import com.cmi.database.EPointJDBC;
import com.cmi.filetransfering.IOFStreamer;

// import com.cmi.security.AuthentificationFilter;

import javax.annotation.security.RolesAllowed;

/**
 * @author Sergei Gribanov
 *
 */

@Path("/points")
public class EPointService {
    private String url;
    private String user;
    private String password;
    private EPointJDBC pjdbc;
    public EPointService() {
	url = "jdbc:postgresql://172.16.238.11/cmi";
	user = "postgres";
	password = "1234";
	try {
	    pjdbc = new EPointJDBC(url, user, password);
	} catch (ClassNotFoundException e) {
	    // TO DO
	} catch (SQLException e) {
	    // TO DO
	}
    }
    @RolesAllowed("ADMIN")
    @GET
    @Path("/download")
    public Response downloadFile() {
        StreamingOutput fileStream = IOFStreamer.outputStream("/data/tr_ph_run455g57.root");
	// check null or throw exception
        return Response
	    .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
	    .header("content-disposition","attachment; filename = tr_ph_run45557.root")
	    .build();
    }
    @RolesAllowed("ADMIN")
    @POST
    @Path("/upload")
    @Consumes({MediaType.APPLICATION_OCTET_STREAM})
    public Response uploadFile(InputStream fileInputStream) {
	IOFStreamer.uploadFile("/data/test.root", fileInputStream);
	return Response.ok("Data uploaded successfully !!").build();
    }
    @RolesAllowed("ADMIN")
    @GET
    @Path("/{pointTag}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEPointJSONHandler(@PathParam("pointTag") String pointTag) {
	try {
	    EPoint pt = pjdbc.getEPoint(pointTag);
	    return Response.status(200).entity(pt).build();
	} catch (SQLException e) {
	    // TO DO
	}
	return Response.status(200).entity(new EPoint()).build(); // TO DO : remove !!!
    }
    @RolesAllowed("ADMIN")
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEPointList() {
	try {
	    ArrayList<EPoint> array = pjdbc.getListOfEPoints();
	    return Response.status(200).entity(array).build();
	} catch (SQLException e) {
	    // TO DO
	}
	return Response.status(200).entity(new ArrayList<EPoint>()).build();
    }
    @RolesAllowed("ADMIN")
    @GET
    @Path("/list/{expTag}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEPointList(@PathParam("expTag") String expTag) {
	try {
	    ArrayList<EPoint> array = pjdbc.getListOfEPoints(expTag);
	    return Response.status(200).entity(array).build();
	} catch (SQLException e) {
	    // TO DO
	}
	return Response.status(200).entity(new ArrayList<EPoint>()).build();
    }
    @RolesAllowed("ADMIN")
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEPointHandler(EPoint point) {
	String result = "energy point added successfully : " + point.getPointTag();
	// write code to add energy point into db or in-memory.
	try {
	    pjdbc.addEPoint(point);
	} catch (SQLException e) {
	    // TO DO
	}
	return Response.status(201).entity(result).build();
    }
}
