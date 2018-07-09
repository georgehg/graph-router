package com.avenuecode.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

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
public class RouteControllerIT {

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
	public void shouldFindAvailableRoute() throws Exception {
		// Arrange
		String graph = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":5},{\"source\":\"B\",\"target\":\"C\",\"distance\":4},{\"source\":\"C\",\"target\":\"D\",\"distance\":8},{\"source\":\"D\",\"target\":\"C\",\"distance\":8},{\"source\":\"D\",\"target\":\"E\",\"distance\":6},{\"source\":\"A\",\"target\":\"D\",\"distance\":5},{\"source\":\"C\",\"target\":\"E\",\"distance\":2},{\"source\":\"E\",\"target\":\"B\",\"distance\":3},{\"source\":\"A\",\"target\":\"E\",\"distance\":7}]}";

		// Act
		ResultActions result = this.mvc.perform(post("/routes/from/{town1}/to/{town2}", "A", "C")
										.contentType(contentType)
										.content(graph));
		
		// Assert
		result.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.routes.[*]", hasSize(4)));
	}
	
	@Test
	public void shouldFindAvailableRoute_MaxStops() throws Exception {
		// Arrange
		String graph = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":5},{\"source\":\"B\",\"target\":\"C\",\"distance\":4},{\"source\":\"C\",\"target\":\"D\",\"distance\":8},{\"source\":\"D\",\"target\":\"C\",\"distance\":8},{\"source\":\"D\",\"target\":\"E\",\"distance\":6},{\"source\":\"A\",\"target\":\"D\",\"distance\":5},{\"source\":\"C\",\"target\":\"E\",\"distance\":2},{\"source\":\"E\",\"target\":\"B\",\"distance\":3},{\"source\":\"A\",\"target\":\"E\",\"distance\":7}]}";

		// Act
		ResultActions result = this.mvc.perform(post("/routes/from/{town1}/to/{town2}?maxStops={maxStops}", "A", "C", 3)
										.contentType(contentType)
										.content(graph));
		
		// Assert
		result.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.routes.[*]", hasSize(3)));
	}
	
	@Test
	public void shouldFindAvailableRoute_ForGraph() throws Exception {
		// Arrange
		String graph = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":5},{\"source\":\"B\",\"target\":\"C\",\"distance\":4},{\"source\":\"C\",\"target\":\"D\",\"distance\":8},{\"source\":\"D\",\"target\":\"C\",\"distance\":8},{\"source\":\"D\",\"target\":\"E\",\"distance\":6},{\"source\":\"A\",\"target\":\"D\",\"distance\":5},{\"source\":\"C\",\"target\":\"E\",\"distance\":2},{\"source\":\"E\",\"target\":\"B\",\"distance\":3},{\"source\":\"A\",\"target\":\"E\",\"distance\":7}]}";

		// Act
		ResultActions result = this.mvc.perform(post("/graph").contentType(contentType)
										.content(graph));
		
		Integer id = Integer.valueOf(result.andReturn().getResponse().getHeader("location").split("/")[2]);
				
		// Act
		result = this.mvc.perform(post("/routes/{graphId}/from/{town1}/to/{town2}", id, "A", "C")
						.contentType(contentType)
						.content(graph));
		
		// Assert
		result.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.routes.[*]", hasSize(4)));
	}
	
	@Test
	public void shouldFindAvailableRoute_ForGraph_MaxStops() throws Exception {
		// Arrange
		String graph = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":5},{\"source\":\"B\",\"target\":\"C\",\"distance\":4},{\"source\":\"C\",\"target\":\"D\",\"distance\":8},{\"source\":\"D\",\"target\":\"C\",\"distance\":8},{\"source\":\"D\",\"target\":\"E\",\"distance\":6},{\"source\":\"A\",\"target\":\"D\",\"distance\":5},{\"source\":\"C\",\"target\":\"E\",\"distance\":2},{\"source\":\"E\",\"target\":\"B\",\"distance\":3},{\"source\":\"A\",\"target\":\"E\",\"distance\":7}]}";

		// Act
		ResultActions result = this.mvc.perform(post("/graph").contentType(contentType)
										.content(graph));
		
		Integer id = Integer.valueOf(result.andReturn().getResponse().getHeader("location").split("/")[2]);
				
		// Act
		result = this.mvc.perform(post("/routes/{graphId}/from/{town1}/to/{town2}?maxStops={maxStops}", id, "A", "C", 3)
						.contentType(contentType)
						.content(graph));
		
		// Assert
		result.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.routes.[*]", hasSize(3)));
	}
	
	@Test
	public void shouldFindAvailableRoute_GraphNotFoud() throws Exception {
		// Arrange
		String graph = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":5},{\"source\":\"B\",\"target\":\"C\",\"distance\":4},{\"source\":\"C\",\"target\":\"D\",\"distance\":8},{\"source\":\"D\",\"target\":\"C\",\"distance\":8},{\"source\":\"D\",\"target\":\"E\",\"distance\":6},{\"source\":\"A\",\"target\":\"D\",\"distance\":5},{\"source\":\"C\",\"target\":\"E\",\"distance\":2},{\"source\":\"E\",\"target\":\"B\",\"distance\":3},{\"source\":\"A\",\"target\":\"E\",\"distance\":7}]}";

		// Act
		ResultActions result = this.mvc.perform(post("/graph").contentType(contentType)
										.content(graph));
						
		// Act
		result = this.mvc.perform(post("/routes/{graphId}/from/{town1}/to/{town2}?maxStops={maxStops}", 99, "A", "C", 3)
						.contentType(contentType)
						.content(graph));
		
		// Assert
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath("message", is("Graph not found with id: 99")));
	}


}
