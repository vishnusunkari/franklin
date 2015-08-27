package edu.franklin.eac.model.pmp;

public class Location {
	private Integer locationId;
	private Building building;
	private Room room;
	
	/**
	 * Default constructor
	 */
	public Location() {
	}
	
	/**
	 * @return the locationId
	 */
	public Integer getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the building
	 */
	public Building getBuilding() {
		return building;
	}
	/**
	 * @param building the building to set
	 */
	public void setBuilding(Building building) {
		this.building = building;
	}
	/**
	 * @return the room
	 */
	public Room getRoom() {
		return room;
	}
	/**
	 * @param room the room to set
	 */
	public void setRoom(Room room) {
		this.room = room;
	}
	
	/**
	 * TODO
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj
	 * @return <code>true</code> if the objects are equal; false otherwise
	 */
	public boolean equals(Object obj) {
		if(obj instanceof Location){
			Location l = (Location)obj;
			if(l.getLocationId() != null && getLocationId() != null)
				return getLocationId().equals(l.getLocationId());
			return getBuilding().equals(l.getBuilding()) && getRoom().equals(l.getRoom());
		}
		return super.equals(obj);
	}
}
