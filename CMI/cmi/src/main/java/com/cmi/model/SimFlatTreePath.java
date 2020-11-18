package com.cmi.model;

/**
 * @author Sergei Gribanov
 *
 */

public class SimFlatTreePath {
    private String pointTag;
    private String simTag;
    private String path;
    public SimFlatTreePath() {
    }
    public String getPointTag() {
	return pointTag;
    }
    public void setPointTag(String pointTag) {
	this.pointTag = pointTag;
    }
    public String getSimTag() {
	return simTag;
    }
    public void setSimTag(String simTag) {
	this.simTag = simTag;
    }
    public String getPath() {
	return path;
    }
    public void setPath(String path) {
	this.path = path;
    }
};
