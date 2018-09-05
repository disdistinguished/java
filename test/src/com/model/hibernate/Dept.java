package com.model.hibernate;

import java.io.Serializable;

public class Dept implements Serializable{
	 private int DepNo;
     private String DepName;
     private String Deplocation;
	public int getDepNo() {
		return DepNo;
	}
	public void setDepNo(int depNo) {
		DepNo = depNo;
	}
	public String getDepName() {
		return DepName;
	}
	public void setDepName(String depName) {
		DepName = depName;
	}
	public String getDeplocation() {
		return Deplocation;
	}
	public void setDeplocation(String deplocation) {
		Deplocation = deplocation;
	}
     
}
