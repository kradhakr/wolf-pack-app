package com.spring.wolves.packs;

import com.spring.wolves.packs.model.Packs;
import com.spring.wolves.packs.model.Wolf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PacksControllerIntegrationTest {

	private static final Log logger = LogFactory.getLog(PacksControllerIntegrationTest.class);

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void testGetPacks() throws URISyntaxException{
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Packs[]> response = restTemplate.exchange(getRootUrl() + "/packs",
				HttpMethod.GET, entity, Packs[].class);
		assertEquals(200, response.getStatusCodeValue());
		assertNotNull(response.getBody());
	}



	@Test
	public void testGetPacksById() {
		Packs packs = restTemplate.getForObject(getRootUrl() + "/packs/193", Packs.class);
		assertNotNull(packs);
	}

	@Test
	public void testCreatePacks() throws ParseException {
		Wolf wolf = new Wolf();
		wolf.setWolfName("Kenny");
		wolf.setGender("Male");
		wolf.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-06-02"));
		wolf.setLocation("Amsterdam");

		Set<Wolf> wolfList = new HashSet<Wolf>();
		wolfList.add(wolf);

		Packs packs = new Packs();
		packs.setPackName("Packs555");
		packs.setWolfList(wolfList);

		ResponseEntity<Packs> postResponse = restTemplate.postForEntity(getRootUrl() + "/packs", packs, Packs.class);
		assertEquals(200, postResponse.getStatusCodeValue());
		assertNotNull(postResponse);
	}


	@Test
	public void testDeletePacks() {
		int id = 193;
		Packs packs = restTemplate.getForObject(getRootUrl() + "/packs/" + id, Packs.class);
		assertNotNull(packs);
		restTemplate.delete(getRootUrl() + "/packs/" + id);
		try {
			packs = restTemplate.getForObject(getRootUrl() + "/packs/" + id, Packs.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}


	@Test
	public void testdDeleteAllWolvesFromPack() {
		int id = 391;
		Packs packs = restTemplate.getForObject(getRootUrl() + "/packs/" + id,Packs.class);
		assertNotNull(packs);
		restTemplate.delete(getRootUrl() + "/packs/" +id+"/wolves");
		try {
			packs = restTemplate.getForObject(getRootUrl() + "/packs/" + id, Packs.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}


	@Test
	public void testDeleteWolvesByPack() {
		int packsId = 393;
		int wolvesId = 394;
		Packs packs = restTemplate.getForObject(getRootUrl() + "/packs/" + packsId, Packs.class);
		assertNotNull(packs);
		restTemplate.delete(getRootUrl() + "/packs/" +packsId+"/wolves/"+wolvesId);
		try {
			packs = restTemplate.getForObject(getRootUrl() + "/packs/" + packsId, Packs.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}


	@Test
	public void testUpdatePacks() {
		int id = 161;
		Packs packs = restTemplate.getForObject(getRootUrl() + "/packs/" + id, Packs.class);
		packs.setPackName("Pack100");

		restTemplate.put(getRootUrl() + "/packs/" + id, packs);
		Packs updatedPacks = restTemplate.getForObject(getRootUrl() + "/packs/" + id, Packs.class);
		assertNotNull(updatedPacks);
		assertEquals("Pack100", packs.getPackName());
	}
}
