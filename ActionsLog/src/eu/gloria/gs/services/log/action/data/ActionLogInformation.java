/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.log.action.data;

import java.util.Date;

/**
 * @author fernando
 * 
 */
public class ActionLogInformation {
	private String user;
	private Date date;
	private Integer rid;
	private Object action;
	private String rt;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public Object getAction() {
		return action;
	}

	public void setAction(Object action) {
		this.action = action;
	}

	/**
	 * @return the rt
	 */
	public String getRt() {
		return rt;
	}

	/**
	 * @param rt
	 *            the rt to set
	 */
	public void setRt(String rt) {
		this.rt = rt;
	}

}
