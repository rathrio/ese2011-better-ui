package models;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

public class Event implements Comparable<Event> {
	
	public String name;
	public Date startDate;
	public Date endDate;
	public boolean isPublic;
	
	
	public Event(String name, Date startDate, Date endDate, boolean isPublic) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isPublic = isPublic;
	}
	
	public boolean happensOn(Date date) {
		Date day = Parser.parseDateToTimeLessDate(date);
		Date startDay = Parser.parseDateToTimeLessDate(startDate);
		Date endDay = Parser.parseDateToTimeLessDate(endDate);
		return startDay.equals(day) 
				|| endDay.equals(day) 
				|| (startDay.before(day) && endDay.after(day));
	}
	
	@Override
	public int compareTo(Event otherEvent) {
		Date otherStartDate = otherEvent.getStartDate();
		return startDate.compareTo(otherStartDate);
	}

	public void setPublic() {
		this.isPublic = true;
	}
	
	public void setPrivate() {
		this.isPublic = false;
	}
	
	public boolean isPublic() {
		return this.isPublic;
	}
	
	public Date getStartDate() {
		return this.startDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return this.name;
	}

	public void set(String newEventname, String startDate, String endDate,
			boolean isPublic) {
		this.name = newEventname;
		this.startDate = Parser.parseStringToDate(startDate);
		this.endDate = Parser.parseStringToDate(endDate);
		this.isPublic = isPublic;
	}
}
