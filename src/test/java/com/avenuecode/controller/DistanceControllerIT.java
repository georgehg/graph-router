package com.avenuecode.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avenuecode.repository.GraphCommandRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DistanceControllerIT {

	@Autowired
	private GraphCommandRepository repo;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Before
	public void setup() throws Exception {
		this.mvc = MockMvcBuilders.
					webAppContextSetup(this.context)
					.alwaysDo(MockMvcResultHandlers.print()).build();
		
		repo.deleteAll();
	}

	@Test
	public void shouldFindDistanceForPath() throws Exception {
		// Arrange
		String graph = "{\"path\":[\"A\",\"B\",\"C\",\"D\"],\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":6},{\"source\":\"A\",\"target\":\"E\",\"distance\":4},{\"source\":\"B\",\"target\":\"A\",\"distance\":6},{\"source\":\"B\",\"target\":\"C\",\"distance\":2},{\"source\":\"B\",\"target\":\"D\",\"distance\":4},{\"source\":\"C\",\"target\":\"B\",\"distance\":3},{\"source\":\"C\",\"target\":\"D\",\"distance\":1},{\"source\":\"C\",\"target\":\"E\",\"distance\":7},{\"source\":\"B\",\"target\":\"D\",\"distance\":8},{\"source\":\"E\",\"target\":\"B\",\"distance\":5},{\"source\":\"E\",\"target\":\"D\",\"distance\":7}]}";

		// Act
		ResultActions result = this.mvc.perform(post("/distance")
										.contentType(contentType)
										.content(graph));
		
		// Assert
		result.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.distance", is(9)));
	}
	
	@Test
	public void shouldFindDistanceForPath_SavedGraph() throws Exception {
		// Arrange
		String graph = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":6},{\"source\":\"A\",\"target\":\"E\",\"distance\":4},{\"source\":\"B\",\"target\":\"A\",\"distance\":6},{\"source\":\"B\",\"target\":\"C\",\"distance\":2},{\"source\":\"B\",\"target\":\"D\",\"distance\":4},{\"source\":\"C\",\"target\":\"B\",\"distance\":3},{\"source\":\"C\",\"target\":\"D\",\"distance\":1},{\"source\":\"C\",\"target\":\"E\",\"distance\":7},{\"source\":\"B\",\"target\":\"D\",\"distance\":8},{\"source\":\"E\",\"target\":\"B\",\"distance\":5},{\"source\":\"E\",\"target\":\"D\",\"distance\":7}]}";
		String path = "{\"path\":[\"A\",\"B\",\"C\",\"D\"]}";
		
		// Act
		ResultActions result = this.mvc.perform(post("/graph").contentType(contentType)
										.content(graph));
		
		Integer id = Integer.valueOf(result.andReturn().getResponse().getHeader("location").split("/")[2]);

		// Act
		result = this.mvc.perform(post("/distance/{graphId}", id)
						.contentType(contentType)
						.content(path));
		
		// Assert
		result.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.distance", is(9)));
	}
	
	@Test
	public void shouldFindDistanceBetweenTowns() throws Exception {
		// Arrange
		String graph = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":6},{\"source\":\"A\",\"target\":\"E\",\"distance\":4},{\"source\":\"B\",\"target\":\"A\",\"distance\":6},{\"source\":\"B\",\"target\":\"C\",\"distance\":2},{\"source\":\"B\",\"target\":\"D\",\"distance\":4},{\"source\":\"C\",\"target\":\"B\",\"distance\":3},{\"source\":\"C\",\"target\":\"D\",\"distance\":1},{\"source\":\"C\",\"target\":\"E\",\"distance\":7},{\"source\":\"B\",\"target\":\"D\",\"distance\":8},{\"source\":\"E\",\"target\":\"B\",\"distance\":5},{\"source\":\"E\",\"target\":\"D\",\"distance\":7}]}";

		// Act
		ResultActions result = this.mvc.perform(post("/distance/from/{town1}/to/{town2}", "A", "C")
										.contentType(contentType)
										.content(graph));
		
		// Assert
		result.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.distance", is(8)))
				.andExpect(jsonPath("$.path", is(Arrays.asList("A","B","C"))));
	}
	
	@Test
	public void shouldFindDistanceBetweenTowns_FromGraph() throws Exception {
		// Arrange
		String graph = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":6},{\"source\":\"A\",\"target\":\"E\",\"distance\":4},{\"source\":\"B\",\"target\":\"A\",\"distance\":6},{\"source\":\"B\",\"target\":\"C\",\"distance\":2},{\"source\":\"B\",\"target\":\"D\",\"distance\":4},{\"source\":\"C\",\"target\":\"B\",\"distance\":3},{\"source\":\"C\",\"target\":\"D\",\"distance\":1},{\"source\":\"C\",\"target\":\"E\",\"distance\":7},{\"source\":\"B\",\"target\":\"D\",\"distance\":8},{\"source\":\"E\",\"target\":\"B\",\"distance\":5},{\"source\":\"E\",\"target\":\"D\",\"distance\":7}]}";

		// Act
		ResultActions result = this.mvc.perform(post("/graph").contentType(contentType)
										.content(graph));
		
		Integer id = Integer.valueOf(result.andReturn().getResponse().getHeader("location").split("/")[2]);
		
		
		// Act
		result = this.mvc.perform(post("/distance/{graphId}/from/{town1}/to/{town2}", id, "A", "C")
						.contentType(contentType)
						.content(graph));
		
		// Assert
		result.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.distance", is(8)))
				.andExpect(jsonPath("$.path", is(Arrays.asList("A","B","C"))));
	}

}
