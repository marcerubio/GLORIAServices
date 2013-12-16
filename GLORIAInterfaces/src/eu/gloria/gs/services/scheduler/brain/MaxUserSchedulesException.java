package eu.gloria.gs.services.scheduler.brain;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class MaxUserSchedulesException extends ActionException {

	/**
	 * 
	 */
	public MaxUserSchedulesException()
	{
		super(new LogAction());
	}
		
	private static final long serialVersionUID = 5848333110207976076L;

}
