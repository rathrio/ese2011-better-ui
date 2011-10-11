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

	public void createEvent(String eventname, String startDate, String endDate, boolean isPublic) {
		Date sDate = Parser.parseStringToDate(startDate);
    	Date eDate = Parser.parseStringToDate(endDate);
    	if (sDate != null && eDate != null) {
    		if (eventname.trim().length() > 0){
        		if (sDate.before(eDate)) {
        			Event newEvent = new Event(eventname, sDate, eDate, isPublic);
                	this.addEvent(newEvent);
            	}
        	}
    	}
	}

	public void deleteEventNamed(String eventname) {
		for (Event event : new ArrayList<Event>(this.events)) {
			if (eventname.equals(event.getName())) {
				this.events.remove(event);
				this.publicEvents.remove(event);
			}
		}
	}
	
	public Event getEventNamed(String eventname) {
		for (Event event : this.events) {
			if (eventname.equals(event.getName())) {
				return event;
			}
		}
		throw new NoSuchEventException(eventname);
	}
	
	/**
	 * @return the current month as a two-digit integer.
	 */
	public int getCurrentMonth() {
		Date date = new Date();
		return this.getMonthOf(date);
	}
	
	/**
	 * @param date
	 * @return the month of a given date as a two-digit integer.
	 */
	public int getMonthOf(Date date) {
		String month = Parser.parseDateToTwoDigitMonth(date);
		return Integer.parseInt(month);
	}
	
	/**
	 * @return the current year as a four-digit integer.
	 */
	public int getCurrentYear() {
		Date date = new Date();
		return this.getYearOf(date);
	}
	
	/**
	 * @param date
	 * @return the year of a given date as a four-digit integer.
	 */
	public int getYearOf(Date date) {
		String year = Parser.parseDateToYear(date);
		return Integer.parseInt(year);
	}
	
	/**
	 * @param month as a two-digit integer
	 * @return the whole English name of a month from a two-digit month as a String.
	 */
	public String getMonthAsString(int month) {
		String twoDigitMonth = month+"";
		return Parser.parseTwoDigitMonthToMonthAsString(twoDigitMonth);
	}
	
	/**
	 * @param month as String, for example "February"
	 * @return the month as a two-digit Integer.
	 */
	public int getMonthAsInt(String month) {
		String twoDigitMonth = Parser.parseMonthAsStringToTwoDigitMonth(month);
		return Integer.parseInt(twoDigitMonth);
	}
	
	/**
	 * @param month as a two-digit integer
	 * @return all the days of a given month as an ArrayList of Integers.
	 */
	public ArrayList<Date> getDaysOfMonth(int month) {
		ArrayList<Date> days = new ArrayList<Date>();
		//Screw the leap year...
		int i = 01;
		int lastDay = 31;
		if (month == 2) {
			lastDay = 28;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			lastDay = 30;
		}
		while (i <= lastDay) {
			String strI = i+"";
			String strMonth = month+"";
			Date day = Parser.parseStringToDate(strI + "." + strMonth + ".11 12:00");
			days.add(day);
			i++;
		}
		return days;
	}
	
	public boolean isToday(Date day) {
		Date date = new Date();
		date = Parser.parseDateToTimeLessDate(date);
		day = Parser.parseDateToTimeLessDate(day);
		String strDate = Parser.parseDateToString(date);
		String strDay = Parser.parseDateToString(day);
		return strDate.equals(strDay);
	}
	
	public boolean dayHasEvent(Date day) {
		for (Event e : this.events) {
			if (e.happensOn(day)) {
				return true;
			}
		}
		return false;
	}
	
}
