package eu.gloria.gs.services.teleoperation.ccd.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.CCD;

public class SetGammaOperation extends CCDOperation {

	private double gain;

	public SetGammaOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.gain = (Long) args.getArguments().get(2);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws TeleoperationException {
		ccd.setGain((long) this.gain);

		returns.setMessage("Set gain operation executed: " + this.gain + ", "
				+ this.getServer() + "," + this.getCCDName());

	}
}
