package Errors;

@SuppressWarnings("serial")
public class InvalidItemIdException extends RuntimeException{
	public InvalidItemIdException() {
		super("Error al Cargar el Stage.");
	}
}
