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
    	Date activeDay = new Date();
    	render(user, cals, users, monthname, year, month, days, activeDay);
    }
    
    public static void displayCalendars(String username, String calendarname, String monthname, int year, int month,
    		ArrayList<Date> days, Date activeDay) {
    	User user = UserDatabase.getUserNamed(username);
    	ArrayList<Calendar> cals = user.getCalendars();
    	render(user, cals, monthname, year, month, days, activeDay);
    }
    
    public static void displayCalendar(String username, String calendarname, String monthname, int year, int month,
    		ArrayList<Date> days, Date activeDay) {
    	boolean isConnected = isConnectedUser(username);
    	User user = UserDatabase.getUserNamed(username);
    	User connectedUser = UserDatabase.getUserNamed(Security.connected());
    	Calendar calendar = user.getCalNamed(calendarname);
    	monthname = calendar.getMonthAsString(month);
    	days = calendar.getDaysOfMonth(month);
    	ArrayList<Event> events = connectedUser.getVisibleEventsOnSpecificDayFrom(user, activeDay);
    	if (month < 1) {
    		month = 12;
    		year -= 1;
    	}
    	if (month > 12) {
    		month = 1;
    		year += 1;
    	}
    	render(user, calendar, monthname, year, month, days, activeDay, events, isConnected);
    }
    
    public static void createCalendar(String calendarname) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	user.createCalendar(calendarname);
    	index();
    }
    
    public static void createEvent(String calendarname, String eventname, String startDate, 
    		String endDate, boolean isPublic, String monthname, int year, int month,
    		ArrayList<Date> days, Date activeDay) {
    	System.out.println(days);
    	String message = null;
    	User user = UserDatabase.getUserNamed(Security.connected());
    	Calendar cal = user.getCalNamed(calendarname);
    	if (isInvalidInput(startDate, endDate)) {
    		message = "Starting Date must not be later than ending Date";
    	}
    	cal.createEvent(eventname, startDate, endDate, isPublic);
    	displayCalendar(Security.connected(), calendarname, monthname, year, month, days, activeDay);
//    	displayEvents(user.getName(), calendarname, message);
    }
    
    public static void displayEditPage(String eventname, String calendarname, String message, String monthname,
    		int year, int month, ArrayList<Date> days, Date activeDay) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	Calendar cal = user.getCalNamed(calendarname);
    	Event event = cal.getEventNamed(eventname);
    	message = "";
    	render(event, user, cal, message, monthname, year, month, days, activeDay);
    }
    
    public static void editEvent(String calendarname, String oldEventname, 
    		String newEventname, String startDate, String endDate, boolean isPublic, String monthname,
    		int year, int month, ArrayList<Date> days, Date activeDay) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	String message = null;
    	Calendar cal = user.getCalNamed(calendarname);
    	Event event = cal.getEventNamed(oldEventname);
    	event.set(newEventname, startDate, endDate, isPublic);
    	if (isInvalidInput(startDate, endDate)) {
    		message = "please, be realistic. You are no neutrino!";
    		displayEditPage(oldEventname, calendarname, message, monthname, year, month, days, activeDay);
    	}
    	displayCalendar(user.getName(), calendarname, monthname, year, month, days, activeDay);
    }
    
    public static void deleteEvent(String eventname, String calendarname, String monthname, int year, int month,
    		ArrayList<Date> days, Date activeDay) {
    	User user = UserDatabase.getUserNamed(Security.connected());
    	String message = null;
    	Calendar cal = user.getCalNamed(calendarname);
    	cal.deleteEventNamed(eventname);
    	displayCalendar(user.getName(), calendarname, monthname, year, month, days, activeDay);
    }
    
    public static boolean isConnectedUser(String username) {
    	String connectedUsername = Security.connected();
    	return username.equals(connectedUsername);
    }
    
    public static boolean isInvalidInput(String startDate, String endDate) {
    	return Parser.parseStringToDate(startDate).after(Parser.parseStringToDate(endDate));
    }
    
}