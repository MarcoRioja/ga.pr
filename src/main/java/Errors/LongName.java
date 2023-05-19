package Errors;

public class LongName extends RuntimeException{
	public LongName() {
		super("Too Long name, please write less than 10 characters.");
	}
}
