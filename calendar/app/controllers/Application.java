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
        render(user, cals, users);
    }
    
    public static void displayCalendars(String username) {
    	User user = UserDatabase.getUserNamed(username);
    	ArrayList<Calendar> cals = user.getCalendars();
    	render(user, cals);
    }
    
    public static void displayEvents(String username, String calendarname) {
    	User user = UserDatabase.getUserNamed(username);
    	Calendar cal = user.getCalNamed(calendarname);
    	User connectedUser = UserDatabase.getUserNamed(Security.connected());
    	Iterator<Event> eventsIterator = cal.getEventsAfter(connectedUser, new Date());
    	ArrayList<Event> events = new ArrayList<Event>();
    	while (eventsIterator.hasNext()) {
    		events.add(eventsIterator.next());
    	}
    	Boolean isConnected = isConnectedUser(username);
    	render(user, cal, events, isConnected);
    }
    
    public static void createCalendar(String calendarname) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	user.createCalendar(calendarname);
    	index();
    }
    
    public static void createEvent(String calendarname, String eventname, String startDate, 
    		String endDate, boolean isPublic) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	Calendar cal = user.getCalNamed(calendarname);
    	cal.createEvent(eventname, startDate, endDate, isPublic);
    	displayEvents(user.getName(), calendarname);
    }
    
    public static void displayEditPage(String eventname, String calendarname) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	Calendar cal = user.getCalNamed(calendarname);
    	Event event = cal.getEventNamed(eventname);
    	render(event, user, cal);
    }
    
    public static void editEvent(String calendarname, String oldEventname, 
    		String newEventname, String startDate, String endDate, boolean isPublic) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	Calendar cal = user.getCalNamed(calendarname);
    	Event event = cal.getEventNamed(oldEventname);
    	event.set(newEventname, startDate, endDate, isPublic);
    	displayEvents(Security.connected(), calendarname);
    }
    
    public static void deleteEvent(String eventname, String calendarname) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	Calendar cal = user.getCalNamed(calendarname);
    	cal.deleteEventNamed(eventname);
    	displayEvents(Security.connected(), calendarname);
    }
    
    public static boolean isConnectedUser(String username) {
    	String connectedUsername = Security.connected();
    	return username.equals(connectedUsername);
    }
    
}