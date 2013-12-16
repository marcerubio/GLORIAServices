package eu.gloria.gs.services.teleoperation.ccd;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class ImageURLNotAvailableException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public ImageURLNotAvailableException(LogAction action)
	{
		super(action);
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
