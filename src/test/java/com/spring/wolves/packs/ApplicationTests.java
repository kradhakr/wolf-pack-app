package com.spring.wolves.packs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {

	@Test
	public void applicationContextLoaded() {
	}

	@Test
	public void applicationContextTest() {
		Application.main(new String[] {});
	}

}