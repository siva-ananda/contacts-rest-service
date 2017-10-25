package be.steformations.sivananda.service.contacts.rest;

import javax.ws.rs.core.Response;

import be.steformations.java_data.contacts.interfaces.beans.Tag;
import be.steformations.java_data.contacts.interfaces.dao.TagDao;
import be.steformations.sivananda.data.contacts.dto.ContactsDtoFactory;
import be.steformations.sivananda.data.contacts.dto.TagDto;

/*
http://localhost:8080/contacts-rest/rs/tag
 */

@javax.ws.rs.Path("tag")
public class TagRestService {

	private TagDao tagDao;
	private ContactsDtoFactory contactsDtoFactory;

	public TagRestService() {
		super();
		this.tagDao = ContactRestDaoFactory.getInstance().getTagDao();
		this.contactsDtoFactory = new ContactsDtoFactory();
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

	// GET http://localhost:8080/contacts-rest/rs/tag/1
	@javax.ws.rs.GET
	@javax.ws.rs.Path("{pk:[1-9]+}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String getTagByValueAsText(@javax.ws.rs.PathParam("pk") int id) {
		String xml = null;
		Tag tag = this.tagDao.getTagById(id);
		TagDto dto = new TagDto();

		if (tag != null) {
			dto.setId(tag.getId());
			dto.setValue(tag.getValue());
		}
		xml = this.contactsDtoFactory.tagToXml(dto);
		return xml;
	}

	// POST http://localhost:8080/contacts-rest/rs/tag/{value}
	@javax.ws.rs.POST
	@javax.ws.rs.Path("{value}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public TagDto createAndSaveTag(@javax.ws.rs.PathParam("value") String value) {
		TagDto dto = new TagDto();
		Tag tag = this.tagDao.createAndSaveTag(value);
		if (tag != null) {
			dto.setId(tag.getId());
			dto.setValue(tag.getValue());
		}
		return dto;
	}

	// GET http://localhost:8080/contacts-rest/rs/tag/{value}
	@javax.ws.rs.GET
	@javax.ws.rs.Path("{value}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public Response getTagByValue(@javax.ws.rs.PathParam("value") String value) {
		Response response = null;// a l'avantage de pouvoir contenir un objet
									// TagDto mais aussi des messages d'erreur
									// => ici on renvoie un message d'erreur
									// plutôt qu'un xml possiblement mal formé
		Tag tag = this.tagDao.getTagByValue(value);
		if (tag == null) {
			response = Response.status(404).build();
		} else {
			TagDto dto = new TagDto();
			dto.setId(tag.getId());
			dto.setValue(tag.getValue());
			response = Response.ok(dto).build();
		}
		return response;
	}
}
