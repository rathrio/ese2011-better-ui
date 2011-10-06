package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	public String name;
	@Transient public ArrayList<Calendar> cals;
	public String password;
	
	public User(String name) {
		this.name = name;
		this.cals = new ArrayList<Calendar>();
	}

	public ArrayList<Event> getVisibleEventsOnSpecificDayFrom(User user, Date date) {
		ArrayList<Calendar> cals = user.getCalendars();
		ArrayList<Event> visibleEvents = new ArrayList<Event>();
		if (this.equals(user)) {
			for (Calendar cal : cals) {
				for (Event e : cal.getEvents()) {
					if (e.happensOn(date)) {
						visibleEvents.add(e);
					}
				}
			}
		} else {
			for (Calendar cal : cals) {
				for (Event e : cal.getPublicEvents()) {
					if (e.happensOn(date)) {
						visibleEvents.add(e);
					}
				}
			}
		}
		return visibleEvents;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<Calendar> getCalendars() {
		return this.cals;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void createCalendar(String name) {
		if (name.trim().length() > 0){
			Calendar cal = new Calendar(this, name);
			this.cals.add(cal);
		}
	}

	public Calendar getCalNamed(String calendarname) {
		for (Calendar c : this.cals) {
			if (c.getName().equals(calendarname)) {
				return c;
			}
		}
		throw new NoSuchCalendarException(calendarname);
	}

}
