package edu.franklin.eac.model.pmp;

public class Room {
	private Integer roomId;
	private String name;
	
	/**
	 * Default constructor.
	 */
	public Room() {
	}
	
	/**
	 * Constructs a new Room with the assigned name.
	 * 
	 * @param name the name/number of the room
	 */
	public Room(String name){
		this.name = name;
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
	/**
	 * @return the roomId
	 */
	public Integer getRoomId() {
		return roomId;
	}
	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	
	/**
	 * TODO
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj
	 * @return
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Room) {
			Room r = (Room)obj;
			if(r.getRoomId() != null && getRoomId() != null)
				return getRoomId().equals(r.getRoomId());
			return getName().equalsIgnoreCase(r.getName());
		}
		return super.equals(obj);
	}
}
