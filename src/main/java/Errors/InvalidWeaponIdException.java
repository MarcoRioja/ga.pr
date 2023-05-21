package Errors;

@SuppressWarnings("serial")
public class InvalidWeaponIdException extends RuntimeException{
	public InvalidWeaponIdException() {
		super("Error al Cargar el Stage.");
	}
}
