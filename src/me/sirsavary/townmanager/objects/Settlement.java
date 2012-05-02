package me.sirsavary.townmanager.objects;


public abstract class Settlement {
	
	String ID;
	
	public Settlement(String ID) {
		this.ID = ID;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setID(String newID) {
		ID = newID;
	}

}
