package eu.gloria.gs.services.teleoperation.ccd.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.CCD;

public class SetAutoGainOperation extends CCDOperation {

	private boolean auto;

	public SetAutoGainOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.auto = (Boolean) args.getArguments().get(2);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws RTSException {
		ccd.setAutoGain(this.auto);

		returns.setMessage("Set auto gain operation executed: " + this.auto
				+ ", " + this.getServer() + "," + this.getCCDName());

	}
}
