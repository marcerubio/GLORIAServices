package eu.gloria.gs.services.experiment.online.models;

public class ContextNotReadyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3312920179746575637L;

	public ContextNotReadyException(String parameterName) {
		super(parameterName);
	}
	
}