package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import com.sun.tools.internal.xjc.reader.gbind.ConnectedComponent;

import net.spy.memcached.protocol.GetCallbackWrapper;

import models.*;
import models.Calendar;

@With(Secure.class)
public class Application extends Controller {

    public static void index() {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	ArrayList<Calendar> cals = user.getCalendars();
    	ArrayList<User> users = UserDatabase.getUsersExcept(user);
    	Calendar cal = cals.get(0);
    	int month = cal.getCurrentMonth();
    	int year = cal.getCurrentYear();
    	String monthname = cal.getMonthAsString(month);
    	ArrayList<Date> days = cal.getDaysOfMonth(month);
    	render(user, cals, users, monthname, year, month, days);
    }
    
    public static void displayCalendars(String username) {
    	User user = UserDatabase.getUserNamed(username);
    	ArrayList<Calendar> cals = user.getCalendars();
    	render(user, cals);
    }
    
    public static void displayCalendar(String username, String calendarname, String monthname, int year, int month, ArrayList<Date> days) {
    	User user = UserDatabase.getUserNamed(username);
    	Calendar calendar = user.getCalNamed(calendarname);
    	monthname = calendar.getMonthAsString(month);
    	days = calendar.getDaysOfMonth(month);
    	if (month < 1) {
    		month = 12;
    		year -= 1;
    	}
    	if (month > 12) {
    		month = 1;
    		year += 1;
    	}
    	render(user, calendar, monthname, year, month, days);
    }
    
    public static void displayEvents(String username, String calendarname, String message) {
    	User user = UserDatabase.getUserNamed(username);
    	Calendar cal = user.getCalNamed(calendarname);
    	User connectedUser = UserDatabase.getUserNamed(Security.connected());
    	Iterator<Event> eventsIterator = cal.getEventsAfter(connectedUser, new Date());
    	ArrayList<Event> events = new ArrayList<Event>();
    	while (eventsIterator.hasNext()) {
    		events.add(eventsIterator.next());
    	}
    	Boolean isConnected = isConnectedUser(username);
    	render(user, cal, events, isConnected, message);
    }
    
    public static void createCalendar(String calendarname) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	user.createCalendar(calendarname);
    	index();
    }
    
    public static void createEvent(String calendarname, String eventname, String startDate, 
    		String endDate, boolean isPublic) {
    	String message = null;
    	User user = UserDatabase.getUserNamed(Security.connected());
    	Calendar cal = user.getCalNamed(calendarname);
    	if (isInvalidInput(startDate, endDate)) {
    		message = "Starting Date must not be later than ending Date";
    	}
    	cal.createEvent(eventname, startDate, endDate, isPublic);
    	displayEvents(user.getName(), calendarname, message);
    }
    
    public static void displayEditPage(String eventname, String calendarname, String message) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	Calendar cal = user.getCalNamed(calendarname);
    	Event event = cal.getEventNamed(eventname);
    	render(event, user, cal, message);
    }
    
    public static void editEvent(String calendarname, String oldEventname, 
    		String newEventname, String startDate, String endDate, boolean isPublic) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	String message = null;
    	Calendar cal = user.getCalNamed(calendarname);
    	Event event = cal.getEventNamed(oldEventname);
    	event.set(newEventname, startDate, endDate, isPublic);
    	if (isInvalidInput(startDate, endDate)) {
    		message = "please, be realistic. You are no neutrino!";
    		displayEditPage(oldEventname, calendarname, message);
    	}
    	displayEvents(Security.connected(), calendarname, message);
    }
    
    public static void deleteEvent(String eventname, String calendarname) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	String message = null;
    	Calendar cal = user.getCalNamed(calendarname);
    	cal.deleteEventNamed(eventname);
    	displayEvents(Security.connected(), calendarname, message);
    }
    
    public static boolean isConnectedUser(String username) {
    	String connectedUsername = Security.connected();
    	return username.equals(connectedUsername);
    }
    
    public static boolean isInvalidInput(String startDate, String endDate) {
    	return Parser.parseStringToDate(startDate).after(Parser.parseStringToDate(endDate));
    }
    
}