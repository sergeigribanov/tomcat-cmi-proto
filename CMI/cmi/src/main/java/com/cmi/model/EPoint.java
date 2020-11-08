package com.cmi.model;

import java.io.Serializable;

/**
 * @author Sergei Gribanov
 *
 */

public class EPoint implements Serializable {
    private String pointTag;
    private String expTag;
    private double beamEnergy;
    private double beamEnergyError;
    private double magneticField;
    public String getPointTag() {
	return pointTag;
    }
    public void setPointTag(String pointTag) {
	this.pointTag = pointTag;
    }
    public String getExpTag() {
	return expTag;
    }
    public void setExpTag(String expTag) {
	this.expTag = expTag;
    }
    public double getBeamEnergy() {
	return beamEnergy;
    }
    public void setBeamEnergy(double beamEnergy) {
	this.beamEnergy = beamEnergy;
    }
    public double getBeamEnergyError() {
	return beamEnergyError;
    }
    public void setBeamEnergyError(double beamEnergyError) {
	this.beamEnergyError = beamEnergyError;
    }
    public double getMagneticField() {
	return magneticField;
    }
    public void setMagneticField(double magneticField) {
	this.magneticField = magneticField;
    }
    
}
