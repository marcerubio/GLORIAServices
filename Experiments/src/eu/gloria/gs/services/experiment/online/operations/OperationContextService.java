package eu.gloria.gs.services.experiment.online.operations;

import java.util.List;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.online.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.online.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.online.data.ReservationInformation;
import eu.gloria.gs.services.experiment.online.models.ExperimentContextService;
import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.online.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.online.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.online.reservation.NoSuchReservationException;
import eu.gloria.gs.services.repository.image.ImageRepositoryException;
import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.repository.rt.data.DeviceType;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.gs.services.teleoperation.dome.DomeOpeningState;
import eu.gloria.gs.services.teleoperation.dome.DomeTeleoperationException;
import eu.gloria.gs.services.teleoperation.focuser.FocuserTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;
import eu.gloria.gs.services.teleoperation.scam.SCamTeleoperationException;

public class OperationContextService extends ExperimentContextService {

	public void treatOperation(OperationContext operationContext,
			String operation, Object... operationArguments)
			throws ExperimentOperationException {

		if (operation.equals("pointToObject")) {
			this.pointToObject(operationContext, operationArguments);
		} else if (operation.equals("park")) {
			this.park(operationContext, operationArguments);
		} else if (operation.equals("getStream")) {
			this.getStream(operationContext, operationArguments);
		} else if (operation.equals("takeImage")) {
			this.takeImage(operationContext, operationArguments);
		} else if (operation.equals("constrainedMountMove")) {
			this.constrainedMountMove(operationContext, operationArguments);
		} else if (operation.equals("domeMove")) {
			this.domeMove(operationContext, operationArguments);
		} else if (operation.equals("getContinuousImage")) {
			this.getContinuousImage(operationContext, operationArguments);
		} else if (operation.equals("loadCCDAttributes")) {
			this.loadCCDAttributes(operationContext, operationArguments);
		} else if (operation.equals("saveCCDAttributes")) {
			this.saveCCDAttributes(operationContext, operationArguments);
		} else if (operation.equals("setParameter")) {
			this.setParameter(operationContext, operationArguments);
		} else if (operation.equals("executeSequence")) {
			this.executeSequence(operationContext, operationArguments);
		} else if (operation.equals("changeFocusRelative")) {
			this.changeFocusRelative(operationContext, operationArguments);
		}
	}

	private void executeSequence(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		for (Object argument : operationArguments) {
			String operationNameParameter = (String) argument;
			String operationName = null;

			try {
				operationName = (String) operationContext
						.getExperimentContext().getParameterValue(
								operationNameParameter);
			} catch (ExperimentParameterException | NoSuchExperimentException
					| ExperimentNotInstantiatedException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			OperationContext subContext = null;
			try {
				subContext = operationContext.getExperimentContext()
						.getOperation(operationName);
			} catch (NoSuchOperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			}
			subContext.execute();
		}
	}

	private void setParameter(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {
			String parameterName = (String) operationArguments[0];

			String valueParameter = (String) operationArguments[1];
			Object value = (Object) operationContext.getExperimentContext()
					.getParameterValue(valueParameter);

			try {
				operationContext.getExperimentContext().setParameterValue(
						parameterName, value);
			} catch (NoSuchExperimentException
					| UndefinedExperimentParameterException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void saveCCDAttributes(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {

			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			String brightnessParameter = (String) operationArguments[2];
			Integer brightness = (Integer) operationContext
					.getExperimentContext().getParameterValue(
							brightnessParameter);

			String contrastParameter = (String) operationArguments[3];
			Integer contrast = (Integer) operationContext
					.getExperimentContext()
					.getParameterValue(contrastParameter);

			String gainParameter = (String) operationArguments[4];
			Integer gain = (Integer) operationContext.getExperimentContext()
					.getParameterValue(gainParameter);

			String exposureParameter = (String) operationArguments[5];
			Double exposure = (Double) operationContext.getExperimentContext()
					.getParameterValue(exposureParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			try {
				this.getCCDTeleoperation().setExposureTime(rtName, camName,
						Math.min(2.0, exposure));
			} catch (CCDTeleoperationException e) {
				if (!e.getMessage().contains("be assigned"))
					throw new ExperimentOperationException(e.getMessage());
			}

			try {
				this.getCCDTeleoperation().setBrightness(rtName, camName,
						brightness);
			} catch (CCDTeleoperationException e) {
				if (!e.getMessage().contains("be assigned"))
					throw new ExperimentOperationException(e.getMessage());
			}
			try {
				this.getCCDTeleoperation().setContrast(rtName, camName,
						contrast);
			} catch (CCDTeleoperationException e) {
				if (!e.getMessage().contains("be assigned"))
					throw new ExperimentOperationException(e.getMessage());
			}
			try {
				this.getCCDTeleoperation().setGain(rtName, camName, gain);
			} catch (CCDTeleoperationException e) {
				if (!e.getMessage().contains("be assigned"))
					throw new ExperimentOperationException(e.getMessage());
			}
		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void loadCCDAttributes(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {

			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			String brightnessParameter = (String) operationArguments[2];
			String contrastParameter = (String) operationArguments[3];
			String gainParameter = (String) operationArguments[4];
			String exposureParameter = (String) operationArguments[5];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			double exposure = -1;
			int brightness = -1;
			int contrast = -1;
			int gain = -1;

			try {
				exposure = this.getCCDTeleoperation().getExposureTime(rtName,
						camName);
			} catch (CCDTeleoperationException e) {
				if (!e.getMessage().contains("be recovered"))
					throw new ExperimentOperationException(e.getMessage());
			}

			try {
				brightness = (int) this.getCCDTeleoperation().getBrightness(
						rtName, camName);
			} catch (CCDTeleoperationException e) {
				if (!e.getMessage().contains("be recovered"))
					throw new ExperimentOperationException(e.getMessage());
			}
			try {
				contrast = (int) this.getCCDTeleoperation().getContrast(rtName,
						camName);
			} catch (CCDTeleoperationException e) {
				if (!e.getMessage().contains("be recovered"))
					throw new ExperimentOperationException(e.getMessage());
			}
			try {
				gain = (int) this.getCCDTeleoperation()
						.getGain(rtName, camName);
			} catch (CCDTeleoperationException e) {
				if (!e.getMessage().contains("be recovered"))
					throw new ExperimentOperationException(e.getMessage());
			}

			operationContext.getExperimentContext().setParameterValue(
					brightnessParameter, brightness);
			operationContext.getExperimentContext().setParameterValue(
					contrastParameter, contrast);
			operationContext.getExperimentContext().setParameterValue(
					gainParameter, gain);
			operationContext.getExperimentContext().setParameterValue(
					exposureParameter, exposure);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void getContinuousImage(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {
			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			String urlParameter = (String) operationArguments[2];

			String formatParameter = (String) operationArguments[3];
			String format = (String) operationContext.getExperimentContext()
					.getParameterValue(formatParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String url = null;
			String imageId = null;
			double exposure = 0.0;

			try {
				this.getCCDTeleoperation().stopContinueMode(rtName, camName);
				exposure = this.getCCDTeleoperation().getExposureTime(rtName,
						camName);
			} catch (CCDTeleoperationException e) {
				if (!e.getMessage().contains("stop")) {
					throw new ExperimentOperationException(
							"Cannot recover the continuous image url from the camera");
				}
			}

			try {
				imageId = this.getCCDTeleoperation().startContinueMode(rtName,
						camName);

			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			int retries = 0;

			while (retries < 10 && url == null) {

				try {
					url = this.getCCDTeleoperation().getImageURL(rtName,
							camName, imageId,
							ImageExtensionFormat.valueOf(format));

				} catch (CCDTeleoperationException e) {

					if (e.getMessage().contains("yet")) {
						System.out
								.println("Wating for the image to be created...");
						try {
							Thread.sleep((int) (exposure * 1000 + 100));
						} catch (InterruptedException s) {
						}
					} else
						throw new ExperimentOperationException(e.getMessage());
				}

				retries++;
			}

			if (url == null) {
				throw new ExperimentOperationException(
						"Cannot recover the continuous image url from the camera");
			}

			operationContext.getExperimentContext().setParameterValue(
					urlParameter, url);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void domeMove(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		String rtName = null;
		String domeName = null;
		String movement = null;

		try {
			String rtNameParameter = (String) operationArguments[0];
			rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String domeNameParameter = (String) operationArguments[1];
			domeName = (String) operationContext.getExperimentContext()
					.getParameterValue(domeNameParameter);

			String movementParameter = (String) operationArguments[2];
			movement = (String) operationContext.getExperimentContext()
					.getParameterValue(movementParameter);
		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		try {

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			if (movement.toUpperCase().equals("OPEN")) {
				this.getDomeTeleoperation().open(rtName, domeName);
			} else if (movement.toUpperCase().equals("CLOSE")) {
				this.getDomeTeleoperation().close(rtName, domeName);
			} else {
				throw new ExperimentOperationException(
						"The movement direction is incorrect: '" + movement
								+ "'");
			}
		} catch (DomeTeleoperationException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void changeFocusRelative(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		String rtName = null;
		String focusName = null;
		Integer steps = null;

		try {
			String rtNameParameter = (String) operationArguments[0];
			rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String focusNameParameter = (String) operationArguments[1];
			focusName = (String) operationContext.getExperimentContext()
					.getParameterValue(focusNameParameter);

			String stepsParameter = (String) operationArguments[2];
			steps = (Integer) operationContext.getExperimentContext()
					.getParameterValue(stepsParameter);
		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		try {
			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			this.getFocuserTeleoperation().moveRelative(rtName, focusName,
					steps);

		} catch (FocuserTeleoperationException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void constrainedMountMove(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {

			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String mountNameParameter = (String) operationArguments[1];
			String mountName = (String) operationContext.getExperimentContext()
					.getParameterValue(mountNameParameter);

			String directionParameter = (String) operationArguments[2];
			String direction = (String) operationContext.getExperimentContext()
					.getParameterValue(directionParameter);

			String maxMovesParameter = (String) operationArguments[3];
			int maxMoves = (Integer) operationContext.getExperimentContext()
					.getParameterValue(maxMovesParameter);

			String currentMovesParameter = (String) operationArguments[4];
			int currentMoves = (Integer) operationContext
					.getExperimentContext().getParameterValue(
							currentMovesParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			if (direction.equals("LEFT")) {
				if (currentMoves > -maxMoves) {
					try {
						this.getMountTeleoperation()
								.moveWest(rtName, mountName);

						operationContext.getExperimentContext()
								.setParameterValue(currentMovesParameter,
										currentMoves - 1);
					} catch (MountTeleoperationException e) {
						throw new ExperimentOperationException(e.getMessage());
					}
				} else {
					throw new ExperimentOperationException(
							"Cannot move left because it is on the limit");
				}
			} else if (direction.equals("RIGHT")) {
				if (currentMoves < maxMoves) {
					try {
						this.getMountTeleoperation()
								.moveEast(rtName, mountName);

						operationContext.getExperimentContext()
								.setParameterValue(currentMovesParameter,
										currentMoves + 1);
					} catch (MountTeleoperationException e) {
						throw new ExperimentOperationException(e.getMessage());
					}
				} else {
					throw new ExperimentOperationException(
							"Cannot move right because it is on the limit");
				}
			} else if (direction.equals("UP")) {
				if (currentMoves < maxMoves) {
					try {
						this.getMountTeleoperation().moveNorth(rtName,
								mountName);

						operationContext.getExperimentContext()
								.setParameterValue(currentMovesParameter,
										currentMoves + 1);
					} catch (MountTeleoperationException e) {
						throw new ExperimentOperationException(e.getMessage());
					}
				} else {
					throw new ExperimentOperationException(
							"Cannot move up because it is on the limit");
				}
			} else if (direction.equals("DOWN")) {
				if (currentMoves > -maxMoves) {
					try {
						this.getMountTeleoperation().moveSouth(rtName,
								mountName);

						operationContext.getExperimentContext()
								.setParameterValue(currentMovesParameter,
										currentMoves - 1);
					} catch (MountTeleoperationException e) {
						throw new ExperimentOperationException(e.getMessage());
					}
				} else {
					throw new ExperimentOperationException(
							"Cannot move down because it is on the limit");
				}
			}
		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void takeImage(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {

			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			String urlParameter = (String) operationArguments[2];

			String formatParameter = (String) operationArguments[3];
			String format = (String) operationContext.getExperimentContext()
					.getParameterValue(formatParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String url = null;
			String imageId = null;
			// double exposure = 0.0;

			try {

				// exposure = this.getCCDTeleoperation().getExposureTime(rtName,
				// camName);
				imageId = this.getCCDTeleoperation().startExposure(rtName,
						camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			int retries = 0;

			while (retries < 10 && url == null) {
				ImageInformation imageInfo = this.getImageRepository()
						.getImageInformationByRTLocaId(rtName, imageId);

				url = imageInfo.getUrl();

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}

				retries++;
			}

			/*
			 * 
			 * int retries = 0;
			 * 
			 * while (retries < 10 && url == null) {
			 * 
			 * try { url = this.getCCDTeleoperation().getImageURL(rtName,
			 * camName, imageId, ImageExtensionFormat.valueOf(format)); } catch
			 * (CCDTeleoperationException e) {
			 * 
			 * System.out.println(e.getMessage()); if
			 * (e.getMessage().contains("yet")) { try { Thread.sleep((int)
			 * (exposure * 1000 + 100)); } catch (InterruptedException s) { } }
			 * else throw new ExperimentOperationException(e.getMessage()); }
			 * 
			 * retries++; }
			 */

			if (url == null) {
				throw new ExperimentOperationException(
						"Cannot recover the instantaneous image url from the camera");
			}

			int rid = operationContext.getExperimentContext().getReservation();

			this.getImageRepository().setExperimentReservationByUrl(url, rid);

			ReservationInformation resInfo = this.getAdapter()
					.getReservationInformation(rid);

			this.getImageRepository().setUserByUrl(url, resInfo.getUser());

			operationContext.getExperimentContext().setParameterValue(
					urlParameter, url);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException
				| ImageRepositoryException | ExperimentDatabaseException
				| NoSuchReservationException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

	}

	private void getStream(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		try {
			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			String urlParameter = (String) operationArguments[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String url = this.getSCamTeleoperation().getImageURL(rtName,
					camName);

			operationContext.getExperimentContext().setParameterValue(
					urlParameter, url);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| SCamTeleoperationException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void park(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		try {

			String rtNameParameter = (String) operationArguments[0];

			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			List<String> mounts;
			List<String> domes;

			try {
				mounts = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.MOUNT);
				domes = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.DOME);
			} catch (RTRepositoryException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			if (domes != null && domes.size() > 0) {
				String domeName = domes.get(0);
				try {
					this.getDomeTeleoperation().close(rtName, domeName);
				} catch (DomeTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				}
			}

			if (mounts != null && mounts.size() > 0) {

				String mountName = mounts.get(0);
				try {
					this.getMountTeleoperation().setTracking(rtName, mountName,
							false);
				} catch (MountTeleoperationException e) {
					if (!e.getMessage().contains("not tracking")) {
						throw new ExperimentOperationException(e.getMessage());
					}
				}
				try {
					this.getMountTeleoperation().park(rtName, mountName);
				} catch (MountTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				}
			} else {
				throw new ExperimentOperationException(
						"No mount available on the '" + rtName + "' RT");
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void pointToObject(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		try {
			String rtNameParameter = (String) operationArguments[0];
			String objectParameter = (String) operationArguments[1];

			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);
			String object = (String) operationContext.getExperimentContext()
					.getParameterValue(objectParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			List<String> mounts;
			List<String> domes;

			try {
				mounts = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.MOUNT);
				domes = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.DOME);
			} catch (RTRepositoryException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			if (domes != null && domes.size() > 0) {
				String domeName = domes.get(0);
				try {
					DomeOpeningState domeState = this.getDomeTeleoperation()
							.getState(rtName, domeName);

					System.out.println(domeState.name());

					// if (domeState.equals(DomeOpeningState.UNDEFINED)
					// || domeState.equals(DomeOpeningState.CLOSED)) {
					this.getDomeTeleoperation().open(rtName, domeName);
					// }
				} catch (DomeTeleoperationException e) {
					if (!e.getMessage().contains("already open")) {
						throw new ExperimentOperationException(e.getMessage());
					}
				}
			}

			if (mounts != null && mounts.size() > 0) {

				String mountName = mounts.get(0);

				try {

					this.getMountTeleoperation().setTrackingRate(rtName,
							mountName, TrackingRate.DRIVE_SOLAR);

					this.getMountTeleoperation().setTracking(rtName, mountName,
							true);

					this.getMountTeleoperation().slewToObject(rtName,
							mountName, object);

				} catch (MountTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				}
			}

			else {
				throw new ExperimentOperationException(
						"No mount available on the '" + rtName + "' RT");
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}
}
