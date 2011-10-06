package models;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import play.db.jpa.Model;

public class Calendar implements Iterable<Event> {
	
	public User owner;
	public String name;
	public ArrayList<Event> events;
	public ArrayList<Event> publicEvents;
	
	public Calendar(User owner, String name) {
		this.owner = owner;
		this.name = name;
		this.events = new ArrayList<Event>();
		this.publicEvents = new ArrayList<Event>();
	}
	
	public void addEvent(Event event) {
		if (event.isPublic()) {
			this.publicEvents.add(event);
		}
		this.events.add(event);
		Collections.sort(this.events);
		Collections.sort(this.publicEvents);
	}

	public User getOwner() {
		return this.owner;
	}
	
	public String getName() {
		return this.name;
	}

	public ArrayList<Event> getEvents() {
		Collections.sort(this.events);
		return this.events;
	}
	
	public ArrayList<Event> getPublicEvents() {
		Collections.sort(this.publicEvents);
		return this.publicEvents;
	}

	public boolean isEmpty() {
		return this.events.isEmpty();
	}
	
	public boolean isOwner(User user) {
		return owner.equals(user);
	}

	public Iterator<Event> iterator() {
		Collections.sort(this.events);
		return this.events.iterator();
	}

	public Iterator<Event> getEventsAfter(User receivingUser, Date startingDate)  {
		ArrayList<Event> iterableEvents = new ArrayList<Event>();
		if (receivingUser.equals(this.owner)) {
			for (Event event : this.events) {
				if (!event.getStartDate().before(startingDate)) {
					iterableEvents.add(event);
				}
			}
		} else {
			for (Event event : this.publicEvents) {
				if (!event.getStartDate().before(startingDate)) {
					iterableEvents.add(event);
				}
			}
		}
		Collections.sort(iterableEvents);
		return iterableEvents.iterator();
	}

	public void createEvent(String eventName, String startDate, String endDate, boolean isPublic) {
		Date sDate = Parser.parseStringToDate(startDate);
    	Date eDate = Parser.parseStringToDate(endDate);
    	if (sDate != null && eDate != null) {
    		if (eventName.trim().length() > 0){
        		if (sDate.before(eDate)) {
        			Event newEvent = new Event(eventName, sDate, eDate, isPublic);
                	this.addEvent(newEvent);
            	}
        	}
    	}
	}
	
}
