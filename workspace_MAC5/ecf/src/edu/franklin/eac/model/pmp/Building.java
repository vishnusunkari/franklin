package edu.franklin.eac.model.pmp;

public class Building {
	private Integer buildingId;
	private String name;
	
	/**
	 * Default constructor
	 */
	public Building() {
	}
	
	/**
	 * Constructs a new Building with the assigned name.
	 * 
	 * @param name the name
	 */
	public Building(String name){
		this.name = name;
	}
	
	/**
	 * @return the buildingId
	 */
	public Integer getBuildingId() {
		return buildingId;
	}
	/**
	 * @param buildingId the buildingId to set
	 */
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Building){
			Building b = (Building)obj;
			if(b.getBuildingId() != null && getBuildingId() != null)
				return getBuildingId().equals(b.getBuildingId());
			return getName().equalsIgnoreCase(b.getName());
		}
		return super.equals(obj);
	}
}
