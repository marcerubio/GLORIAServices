package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class MaxReservationTimeException extends ActionException {

	/**
	 * 
	 */
	public MaxReservationTimeException(Action action) {
		super(action);
	}
	
	public MaxReservationTimeException() {
		super();
	}

	public MaxReservationTimeException(long current, long max, String user) {
		super(new Action());

		this.getAction().put("current", current);
		this.getAction().put("max", max);
		this.getAction().put("user", user);

	}

	private static final long serialVersionUID = 5848333110207976076L;

}