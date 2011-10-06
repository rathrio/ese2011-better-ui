package models;

import java.util.NoSuchElementException;

public class NoSuchCalendarException extends NoSuchElementException {

	public NoSuchCalendarException(String msg) {
		super(msg);
	}
}
