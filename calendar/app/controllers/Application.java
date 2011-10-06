package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

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
    
    public static void createCalendar(String calendarName) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	user.createCalendar(calendarName);
    	index();
    }
    
    public static void createEvent(String calendarName, String eventName, String startDate, String endDate, boolean isPublic) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	Calendar cal = user.getCalNamed(calendarName);
    	cal.createEvent(eventName, startDate, endDate, isPublic);
    	displayEvents(user.getName(), calendarName);
    }
    
    public static boolean isConnectedUser(String username) {
    	String connectedUsername = Security.connected();
    	return username.equals(connectedUsername);
    }

}