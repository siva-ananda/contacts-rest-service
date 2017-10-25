package be.steformations.sivananda.service.contacts.rest;

import be.steformations.java_data.contacts.interfaces.beans.Tag;
import be.steformations.java_data.contacts.interfaces.dao.TagDao;

/*
http://localhost:8080/contacts-rest/rs/tag
 */

@javax.ws.rs.Path("tag")
public class TagRestService {

	private TagDao tagDao;

	public TagRestService() {
		super();
		this.tagDao = ContactRestDaoFactory.getInstance().getTagDao();
	}

	// GET http://localhost:8080/contacts-rest/rs/tag/1/value
	@javax.ws.rs.GET
	@javax.ws.rs.Path("{pk:[1-9]+}/value")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String getTagValueById(@javax.ws.rs.PathParam("pk") int id) {
		Tag tag = this.tagDao.getTagById(id);
		if (tag == null) {
			return "not found";
		}
		return tag.getValue();
	}
}
