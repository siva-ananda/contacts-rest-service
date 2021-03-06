package be.steformations.sivananda.service.contacts.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import be.steformations.java_data.contacts.interfaces.beans.Contact;
import be.steformations.java_data.contacts.interfaces.beans.Tag;
import be.steformations.java_data.contacts.interfaces.dao.ContactDao;
import be.steformations.sivananda.data.contacts.dto.ContactDto;
import be.steformations.sivananda.data.contacts.dto.CountryDto;
import be.steformations.sivananda.data.contacts.dto.TagDto;

@javax.ws.rs.Path("contact")
public class ContactRestService {

	private ContactDao contactDao;

	public ContactRestService() {
		super();
		this.contactDao = ContactRestDaoFactory.getInstance().getContactDao();
	}

	private ContactDto createDto(Contact c) {
		CountryDto countryDto = null;
		if (c.getCountry() != null) {
			countryDto = new CountryDto();
			countryDto.setId(c.getCountry().getId());
			countryDto.setAbbreviation(c.getCountry().getAbbreviation());
			countryDto.setName(c.getCountry().getName());
		}
		ContactDto dto = new ContactDto();
		dto.setId(c.getId());
		dto.setFirstname(c.getFirstname());
		dto.setName(c.getName());
		dto.setEmail(c.getEmail());
		dto.setCountry(countryDto);

		for (Tag t : this.contactDao.getTagsByContact(c.getId())) {
			TagDto tagDto = new TagDto();
			tagDto.setId(t.getId());
			tagDto.setValue(t.getValue());
			dto.getTags().add(tagDto);
		}
		return dto;
	}

	// http://localhost:8080/contacts-rest/rs/contact/{id}
	@javax.ws.rs.GET
	@javax.ws.rs.Path("{id}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public Response getContactById(@javax.ws.rs.PathParam("id") int id) {
		Response response = null;
		Contact c = this.contactDao.getContactById(id);
		if (c == null) {
			response = Response.status(404).build();
		} else {
			ContactDto dto = this.createDto(c);
			response = Response.ok(dto).build();
		}
		return response;
	}

	// http://localhost:8080/contacts-rest/rs/contact
	@javax.ws.rs.GET
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public Response getAllContacts() {
		Response response = null;
		List<? extends Contact> contacts = this.contactDao.getAllContacts();
		List<ContactDto> dtos = new ArrayList<>();
		for (Contact c : contacts) {
			dtos.add(this.createDto(c));
		}
		GenericEntity<List<ContactDto>> entity = new GenericEntity<List<ContactDto>>(dtos) {
		};
		response = Response.ok(entity).build();
		return response;
	}

	// http://localhost:8080/contacts-rest/rs/contact/{id}
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("{id}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public Response removeContact(@javax.ws.rs.PathParam("id") int id) {
		Response response = null;
		Contact c = this.contactDao.getContactById(id);
		if (c == null) {
			response = Response.status(404).build();
		} else {
			this.contactDao.removeContact(id);
			ContactDto dto = this.createDto(c);
			response = Response.ok(dto).build();
		}
		return response;
	}

	// http://localhost:8080/contacts-rest/rs/contact
	@javax.ws.rs.POST
	@javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_XML)
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	// autre manière de faire le post : reçoit et envoie du xml
	public Response createAndSaveContact(ContactDto input) {
		Response response = null;

		String countryAbbreviation = input.getCountry() == null ? null : input.getCountry().getAbbreviation();
		List<String> tagValues = new ArrayList<>();
		for (TagDto t : input.getTags()) {
			tagValues.add(t.getValue());
		}

		Contact c = this.contactDao.createAndSaveContact(input.getFirstname(), input.getName(), input.getEmail(),
				countryAbbreviation, tagValues);

		if (c == null) {
			response = Response.status(500).build();
		} else {
			ContactDto dto = this.createDto(c);
			response = Response.ok(dto).build();
		}
		return response;
	}

}
