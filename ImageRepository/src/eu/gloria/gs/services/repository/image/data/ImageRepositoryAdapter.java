package eu.gloria.gs.services.repository.image.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.gloria.gs.services.repository.image.data.dbservices.ImageDBService;
import eu.gloria.gs.services.repository.image.data.dbservices.ImageEntry;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ImageRepositoryAdapter {

	private ImageDBService imageService;

	/**
	 * 
	 */
	public ImageRepositoryAdapter() {

	}

	/**
	 * 
	 */
	public void init() {

		imageService.create();
	}

	public void saveImage(String rt, String ccd, String user, Date when,
			String lid) throws ImageDatabaseException {

		ImageEntry entry = new ImageEntry();
		entry.setUser(user);
		entry.setDate(when);
		entry.setLocal_id(lid);
		entry.setRt(rt);
		entry.setCcd(ccd);

		imageService.save(entry);
	}

	public void removeImage(int id) throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			throw new ImageDatabaseException("The image '" + id
					+ "' does not exist");
		}

		imageService.remove(id);
	}

	public void setExperimentReservation(int id, int rid)
			throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			throw new ImageDatabaseException("The image '" + id
					+ "' does not exist");
		}

		imageService.setReservation(id, rid);
	}

	public void setExperimentReservationByUrl(String url, int rid)
			throws ImageDatabaseException {
		if (!imageService.containsUrl(url)) {
			throw new ImageDatabaseException("The image with URL= '"
					+ url + "' does not exist");
		}

		ImageEntry entry = imageService.getByUrl(url);
		imageService.setReservation(entry.getIdimage(), rid);
	}

	public void setUrl(int id, String url)
			throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			throw new ImageDatabaseException("The image with URL= '"
					+ url + "' does not exist");
		}

		imageService.setUrl(id, url);
	}

	public void setUrlByRT(String rt, String lid, String url)
			throws ImageDatabaseException {
		if (!imageService.containsRTLocalId(rt, lid)) {
			throw new ImageDatabaseException("The image with URL= '"
					+ url + "' does not exist");
		}

		imageService.setUrlByRTLocalId(rt, lid, url);
	}

	public void setUser(int id, String user)
			throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			throw new ImageDatabaseException("The image '" + id
					+ "' does not exist");
		}

		imageService.setUser(id, user);
	}

	public void setUserByUrl(String url, String user)
			throws ImageDatabaseException {
		if (!imageService.containsUrl(url)) {
			throw new ImageDatabaseException("The image with URL= '"
					+ url + "' does not exist");
		}

		ImageEntry entry = imageService.getByUrl(url);
		imageService.setUser(entry.getIdimage(), user);
	}

	public ImageInformation getImageInformation(int id)
			throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			throw new ImageDatabaseException("The image '" + id
					+ "' does not exist");
		}

		ImageEntry entry = imageService.get(id);

		ImageInformation imageInfo = new ImageInformation();

		imageInfo.setCreationDate(entry.getDate());
		imageInfo.setId(id);
		imageInfo.setLocalid(entry.getLocal_id());
		imageInfo.setRt(entry.getRt());
		imageInfo.setCcd(entry.getCcd());
		imageInfo.setRid(entry.getRid());
		imageInfo.setUrl(entry.getUrl());
		imageInfo.setUser(entry.getUser());

		return imageInfo;
	}

	public ImageInformation getImageInformationByRTLocalId(String rt, String lid)
			throws ImageDatabaseException {
		if (!imageService.containsRTLocalId(rt, lid)) {
			throw new ImageDatabaseException("The image '" + lid
					+ "' does not exist");
		}

		ImageEntry entry = imageService.getByRTLocalId(rt, lid);

		ImageInformation imageInfo = new ImageInformation();

		imageInfo.setCreationDate(entry.getDate());
		imageInfo.setId(entry.getIdimage());
		imageInfo.setLocalid(lid);
		imageInfo.setRt(rt);
		imageInfo.setCcd(entry.getCcd());
		imageInfo.setRid(entry.getRid());
		imageInfo.setUrl(entry.getUrl());
		imageInfo.setUser(entry.getUser());

		return imageInfo;
	}

	public List<ImageInformation> getAllWithoutUrl()
			throws ImageDatabaseException {

		List<ImageInformation> imageInfos = new ArrayList<ImageInformation>();
		List<ImageEntry> entries = imageService.getAllWithoutUrl();

		for (ImageEntry entry : entries) {
			ImageInformation imageInfo = new ImageInformation();

			imageInfo.setId(entry.getIdimage());
			imageInfo.setCreationDate(entry.getDate());
			imageInfo.setLocalid(entry.getLocal_id());
			imageInfo.setRid(entry.getRid());
			imageInfo.setRt(entry.getRt());
			imageInfo.setCcd(entry.getCcd());

			imageInfos.add(imageInfo);
		}

		return imageInfos;
	}
	
	public List<Integer> getAllImagesBetween(Date from, Date to) {
		
		List<Integer> imageIds = new ArrayList<Integer>();
		List<ImageEntry> entries = imageService.getAllBetweenDates(from, to);

		System.out.println(from);
		System.out.println(to);
		
		for (ImageEntry entry : entries) {
			imageIds.add(entry.getIdimage());
		}

		System.out.println(entries.size());
		return imageIds;
	}
	
	/**
	 * @param service
	 */
	public void setImageDBService(ImageDBService service) {
		this.imageService = service;
	}
}
