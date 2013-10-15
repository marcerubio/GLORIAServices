/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class GetRADEC extends ServiceOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		try {

			String rtNameParameter = (String) this.getArguments()[0];
			String rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);

			String mountNameParameter = (String) this.getArguments()[1];
			String mountName = (String) this.getContext().getExperimentContext()
					.getParameterValue(mountNameParameter);

			String raParameter = (String) this.getArguments()[2];
			String decParameter = (String) this.getArguments()[3];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			double ra = this.getMountTeleoperation().getRA(rtName, mountName);
			double dec = this.getMountTeleoperation().getDEC(rtName, mountName);

			this.getContext().getExperimentContext().setParameterValue(
					raParameter, ra);
			this.getContext().getExperimentContext().setParameterValue(
					decParameter, dec);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException
				| MountTeleoperationException | DeviceOperationFailedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}
}