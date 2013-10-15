/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.repository.image.ImageRepositoryException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class LoadObjectImageFromRepository extends ServiceOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		try {
			String dateParameter = (String) this.getArguments()[0];
			String idParameter = (String) this.getArguments()[1];
			String objectParameter = (String) this.getArguments()[2];

			Date day = null;

			try {
				day = (Date) this.getContext().getExperimentContext()
						.getParameterValue(dateParameter);
			} catch (NoSuchExperimentException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			String object = null;

			try {
				object = (String) this.getContext().getExperimentContext()
						.getParameterValue(objectParameter);
			} catch (NoSuchExperimentException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(day);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			Date from = calendar.getTime();

			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			Date to = calendar.getTime();

			List<Integer> imageIds;
			int imageId = -1;
			try {
				imageIds = this.getImageRepository().getAllObjectImagesByDate(
						object, from, to);

				if (imageIds != null) {
					imageId = imageIds.get((int) (Math.random() * imageIds
							.size()));
				}
			} catch (ImageRepositoryException e1) {

			}

			try {
				this.getContext().getExperimentContext()
						.setParameterValue(idParameter, imageId);
			} catch (NoSuchExperimentException
					| UndefinedExperimentParameterException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

		} catch (ExperimentParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

}
