package com.spring.wolves.packs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.spring.wolves.packs.model.Wolf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WolfControllerIntegrationTest {

	private static final Log logger = LogFactory.getLog(WolfControllerIntegrationTest.class);

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
	public void testGetAllWolves() throws URISyntaxException{
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Wolf[]> response = restTemplate.exchange(getRootUrl() + "/wolves",
				HttpMethod.GET, entity, Wolf[].class);
		assertEquals(200, response.getStatusCodeValue());
		assertNotNull(response.getBody());
	}



	@Test
	public void testGetWolvesById() {
		Wolf wolf = restTemplate.getForObject(getRootUrl() + "/wolves/130", Wolf.class);
		assertNotNull(wolf);
	}

	@Test
	public void testCreateWolves() throws ParseException {
		Wolf wolf = new Wolf();
		wolf.setWolfName("Kenny");
		wolf.setGender("Male");
		wolf.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-06-02"));
		wolf.setLocation("Amsterdam");
		ResponseEntity<Wolf> postResponse = restTemplate.postForEntity(getRootUrl() + "/wolves", wolf, Wolf.class);
		assertEquals(200, postResponse.getStatusCodeValue());
		assertNotNull(postResponse);
	}

	@Test
	public void testUpdateWolves() {
		int id = 161;
		Wolf wolf = restTemplate.getForObject(getRootUrl() + "/wolves/" + id, Wolf.class);
		wolf.setWolfName("Jerry");
		wolf.setLocation("Amsterdam");
		restTemplate.put(getRootUrl() + "/wolves/" + id, wolf);
		Wolf updatedWolf = restTemplate.getForObject(getRootUrl() + "/wolves/" + id, Wolf.class);
		assertNotNull(updatedWolf);
	}

	@Test
	public void testDeleteWolves() {
		int id = 161;
		Wolf wolf = restTemplate.getForObject(getRootUrl() + "/wolves/" + id, Wolf.class);
		assertNotNull(wolf);
		restTemplate.delete(getRootUrl() + "/wolves/" + id);
		try {
			wolf = restTemplate.getForObject(getRootUrl() + "/wolves/" + id, Wolf.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}
