package be.steformations.sivananda.service.contacts.rest;

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

}
