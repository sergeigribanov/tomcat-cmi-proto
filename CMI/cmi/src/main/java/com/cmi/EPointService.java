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
import java.io.File;
import java.io.IOException;
import javax.ws.rs.core.StreamingOutput;

import com.cmi.model.EPoint;
import com.cmi.database.EPointJDBC;
import com.cmi.model.SimFlatTreePath;
import com.cmi.database.SimFlatTreePathJDBC;

import com.cmi.database.DataBaseException;
import com.cmi.filetransfering.IOFStreamer;

import javax.annotation.security.RolesAllowed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;


/**
 * @author Sergei Gribanov
 *
 */

@Path("/points")
public class EPointService {
    private EPointJDBC epointJDBC;
    private SimFlatTreePathJDBC pathJDBC;
    public EPointService() {
	try {
	    epointJDBC = new EPointJDBC("db_epoints", "/etc/conf.d/dbconfig_CMI.json");
	    pathJDBC = new SimFlatTreePathJDBC("db_epoints", "/etc/conf.d/dbconfig_CMI.json");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    @RolesAllowed({"ADMIN", "READONLY"})
    @GET
    @Path("/{pointTag}/sim/{simTag}/tr_ph")
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
    public Response downloadSimTrPhFile(@PathParam("pointTag") String pointTag,
					@PathParam("simTag") String simTag) {
	try {
	    final String path = pathJDBC.getSimFlatTreePath(pointTag, simTag)
		.getPath();
	    StreamingOutput fileStream = IOFStreamer.download(path);
        return Response
	    .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
	    .header("content-disposition","attachment; filename = test.root")
	    .build();
	} catch (Exception e) {
	    // !!! TO DO : catch exception carefully, set right response statuses
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode node = mapper.createObjectNode();
	    node.put("error", e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
		.entity(node).build();
	}
    }
    @RolesAllowed("ADMIN")
    @POST
    @Path("/{pointTag}/sim/{simTag}/tr_ph")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response uploadSimTrPhFile(@PathParam("pointTag") String pointTag,
				      @PathParam("simTag") String simTag,
				      InputStream fileInputStream) {
	try {
	    final String path = pathJDBC.getSimFlatTreePath(pointTag, simTag)
		.getPath();
	    IOFStreamer.uploadFile(path, fileInputStream);
	} catch (Exception e) {
	    // !!! TO DO
	}
	ObjectMapper mapper = new ObjectMapper();
	ObjectNode node = mapper.createObjectNode();
	node.put("status", "Data uploaded successfully!");
	return Response.ok(node).build();
    }
    @RolesAllowed({"ADMIN", "READONLY"})
    @GET
    @Path("/{pointTag}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEPointJSONHandler(@PathParam("pointTag") String pointTag) {
	try {
	    EPoint pt = epointJDBC.getEPoint(pointTag);
	    return Response.status(200).entity(pt).build();
	} catch (SQLException e) {
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode node = mapper.createObjectNode();
	    node.put("error", e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
		.entity(node).build();
	} catch (DataBaseException e) {
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode node = mapper.createObjectNode();
	    node.put("error", e.toString());
	    return Response.status(Response.Status.NOT_FOUND).entity(node).build();
	}
    }
    @RolesAllowed({"ADMIN", "READONLY"})
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEPointList() {
	try {
	    ArrayList<EPoint> array = epointJDBC.getListOfEPoints();
	    return Response.status(200).entity(array).build();
	} catch (SQLException e) {
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode node = mapper.createObjectNode();
	    node.put("error", e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
		.entity(node).build();
	}
    }
    @RolesAllowed({"ADMIN", "READONLY"})
    @GET
    @Path("/list/{expTag}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEPointList(@PathParam("expTag") String expTag) {
	try {
	    ArrayList<EPoint> array = epointJDBC.getListOfEPoints(expTag);
	    return Response.status(200).entity(array).build();
	} catch (DataBaseException e) {
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode node = mapper.createObjectNode();
	    node.put("error", e.toString());
	    return Response.status(Response.Status.NOT_FOUND).entity(node).build();
	} catch (SQLException e) {
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode node = mapper.createObjectNode();
	    node.put("error", e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
		.entity(node).build();
	}
    }
    @RolesAllowed("ADMIN")
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEPointHandler(EPoint point) {
	// write code to add energy point into db or in-memory.
	try {
	    epointJDBC.addEPoint(point);
	} catch (SQLException e) {
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode node = mapper.createObjectNode();
	    node.put("error", e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
		.entity(node).build();
	}
	ObjectMapper mapper = new ObjectMapper();
	ObjectNode node = mapper.createObjectNode();
	node.put("status", String.format("Energy point with tag (%s) was added successfully!",
					 point.getPointTag()));
	return Response.status(Response.Status.CREATED).entity(node).build();
    }
}
