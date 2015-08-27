package edu.franklin.eac.dao;

public class Person {

	private String fullName;
	private String title;
	private String department;
	private String uid;
	private String extension;
	private String building;
	private String room;
	private String supervisor;
		
	public Person(String fullName, String title, String department, String uid, String extension, String building, String room, String supervisor) {
		super();
		this.fullName = fullName;
		this.title = title;
		this.department = department;
		this.uid = uid;
		this.extension = extension;
		this.building = building;
		this.room = room;
		this.supervisor = supervisor;
	}
	
	public Person(){
		super();
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getBuilding() {
		return building;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getRoom() {
		return room;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getSupervisor() {
		return supervisor;
	}
}
