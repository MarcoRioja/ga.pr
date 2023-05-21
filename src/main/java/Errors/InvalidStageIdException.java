package Errors;

@SuppressWarnings("serial")
public class InvalidStageIdException extends RuntimeException{
	public InvalidStageIdException() {
		super("Error al Cargar el Stage.");
	}

}
