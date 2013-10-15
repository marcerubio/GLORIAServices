/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.OperationContext;
import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;
import eu.gloria.gs.services.repository.rt.RTRepositoryInterface;
import eu.gloria.gs.services.repository.user.UserRepositoryInterface;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.dome.DomeTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.focuser.FocuserTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.fw.FilterWheelTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.scam.SCamTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationInterface;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public abstract class ServiceOperation {

	private OperationContext context;
	private Object[] arguments;
	private ExperimentDBAdapter adapter;
	private String username;
	private String password;
	private RTRepositoryInterface rt;
	private UserRepositoryInterface user;
	private CCDTeleoperationInterface ccd;
	private DomeTeleoperationInterface dome;
	private SCamTeleoperationInterface scam;
	private MountTeleoperationInterface mount;
	private FocuserTeleoperationInterface focus;
	private WeatherTeleoperationInterface weather;
	private FilterWheelTeleoperationInterface fw;
	private ImageRepositoryInterface image;
	
	public ServiceOperation() {
		
	}	
	
	public ExperimentDBAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setRTRepository(RTRepositoryInterface rtRepository) {
		this.rt = rtRepository;
	}

	public RTRepositoryInterface getRTRepository() {
		return this.rt;
	}

	public void setUserRepository(UserRepositoryInterface userRepository) {
		this.user = userRepository;
	}

	public UserRepositoryInterface getUserRepository() {
		return this.user;
	}

	public void setCCDTeleoperation(CCDTeleoperationInterface ccd) {
		this.ccd = ccd;
	}

	public CCDTeleoperationInterface getCCDTeleoperation() {
		return this.ccd;
	}

	public void setDomeTeleoperation(DomeTeleoperationInterface dome) {
		this.dome = dome;
	}

	public DomeTeleoperationInterface getDomeTeleoperation() {
		return this.dome;
	}

	public void setSCamTeleoperation(SCamTeleoperationInterface scam) {
		this.scam = scam;
	}

	public SCamTeleoperationInterface getSCamTeleoperation() {
		return this.scam;
	}

	public void setMountTeleoperation(MountTeleoperationInterface mount) {
		this.mount = mount;
	}

	public MountTeleoperationInterface getMountTeleoperation() {
		return this.mount;
	}

	public void setFocuserTeleoperation(FocuserTeleoperationInterface focus) {
		this.focus = focus;
	}

	public FocuserTeleoperationInterface getFocuserTeleoperation() {
		return this.focus;
	}

	public void setFilterWheelTeleoperation(FilterWheelTeleoperationInterface fw) {
		this.fw = fw;
	}

	public FilterWheelTeleoperationInterface getFilterWheelTeleoperation() {
		return this.fw;
	}

	public ImageRepositoryInterface getImageRepository() {
		return image;
	}

	public void setImageRepository(ImageRepositoryInterface image) {
		this.image = image;
	}
	
	public WeatherTeleoperationInterface getWeatherTeleoperation() {
		return weather;
	}

	public void setWeatherTeleoperation(WeatherTeleoperationInterface weather) {
		this.weather = weather;
	}

	public void setContext(OperationContext context) {
		this.context = context;
	}
	
	public void setArguments(Object[] args) {
		this.arguments = args;
	}

	public OperationContext getContext() {
		return context;
	}

	public Object[] getArguments() {
		return arguments;
	}
	
	public abstract void execute() throws ExperimentOperationException;
}
