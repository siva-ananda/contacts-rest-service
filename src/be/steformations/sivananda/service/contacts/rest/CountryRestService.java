package be.steformations.sivananda.service.contacts.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import be.steformations.java_data.contacts.interfaces.beans.Country;
import be.steformations.java_data.contacts.interfaces.dao.CountryDao;
import be.steformations.sivananda.data.contacts.dto.CountryDto;

@javax.ws.rs.Path("country")
public class CountryRestService {

	private CountryDao countryDao;

	public CountryRestService() {
		super();
		this.countryDao = ContactRestDaoFactory.getInstance().getCountryDao();
	}

	// http://localhost:8080/contacts-rest/rs/country/{abbr}
	@javax.ws.rs.GET
	@javax.ws.rs.Path("{abbr}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Response getCountryByAbbreviation(@javax.ws.rs.PathParam("abbr") String abbreviation) {
		Response response = null;
		Country country = this.countryDao.getCountryByAbbreviation(abbreviation);
		if (country == null) {
			response = Response.status(404).build();
		} else {
			CountryDto dto = new CountryDto();
			dto.setId(country.getId());
			dto.setAbbreviation(country.getAbbreviation());
			dto.setName(country.getName());
			response = Response.ok(dto).build();
		}
		return response;
	}

	// http://localhost:8080/contacts-rest/rs/country
	@javax.ws.rs.GET
	@javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Response getAllCountries() {
		Response response = null;
		List<? extends Country> countries = this.countryDao.getAllCountries();
		List<CountryDto> dtos = new ArrayList<>();
		for (Country country : countries) {
			CountryDto dto = new CountryDto();
			dto.setId(country.getId());
			dto.setAbbreviation(country.getAbbreviation());
			dto.setName(country.getName());
			dtos.add(dto);
		}
		GenericEntity<List<CountryDto>> entity = new GenericEntity<List<CountryDto>>(dtos) {
		};
		response = Response.ok(entity).build();
		return response;
	}

	@javax.ws.rs.POST
	@javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED)
	// ne renvoie plus les infos par l'url mais par un formulaire html
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Response createAndSaveCountry(@javax.ws.rs.FormParam("abbr") String abbreviation,
			@javax.ws.rs.FormParam("name") String name) {
		Response response = null;
		Country country = this.countryDao.createAndSaveCountry(abbreviation, name);
		if (country != null) {
			CountryDto dto = new CountryDto();
			dto.setId(country.getId());
			dto.setAbbreviation(country.getAbbreviation());
			dto.setName(country.getName());
			response = Response.ok(dto).build();
		} else {
			response = Response.serverError().build();
		}
		return response;
	}

}
