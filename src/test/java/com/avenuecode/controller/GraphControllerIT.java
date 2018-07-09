package com.avenuecode.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class GraphControllerIT {

	@Autowired
	private GraphCommandRepository repo;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Before
	public void setup() throws Exception {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).alwaysDo(MockMvcResultHandlers.print()).build();
		repo.deleteAll();
	}

	@Test
	public void shouldSaveGraph() throws Exception {
		// Arrange
		String graph = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":6},{\"source\":\"A\",\"target\":\"E\",\"distance\":4},{\"source\":\"B\",\"target\":\"A\",\"distance\":6},{\"source\":\"B\",\"target\":\"C\",\"distance\":2},{\"source\":\"B\",\"target\":\"D\",\"distance\":4},{\"source\":\"C\",\"target\":\"B\",\"distance\":3},{\"source\":\"C\",\"target\":\"D\",\"distance\":1},{\"source\":\"C\",\"target\":\"E\",\"distance\":7},{\"source\":\"B\",\"target\":\"D\",\"distance\":8},{\"source\":\"E\",\"target\":\"B\",\"distance\":5},{\"source\":\"E\",\"target\":\"D\",\"distance\":7}]}";

		// Act
		ResultActions result = this.mvc.perform(post("/graph").contentType(contentType)
										.content(graph));

		// Assert
		result.andExpect(status().isCreated())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.data.[*]", hasSize(11)));
	}
	
	@Test
	public void shouldSaveGraph_AndReturnSaved() throws Exception {
		// Arrange
		String graph = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":6},{\"source\":\"A\",\"target\":\"E\",\"distance\":4},{\"source\":\"B\",\"target\":\"A\",\"distance\":6},{\"source\":\"B\",\"target\":\"C\",\"distance\":2},{\"source\":\"B\",\"target\":\"D\",\"distance\":4},{\"source\":\"C\",\"target\":\"B\",\"distance\":3},{\"source\":\"C\",\"target\":\"D\",\"distance\":1},{\"source\":\"C\",\"target\":\"E\",\"distance\":7},{\"source\":\"B\",\"target\":\"D\",\"distance\":8},{\"source\":\"E\",\"target\":\"B\",\"distance\":5},{\"source\":\"E\",\"target\":\"D\",\"distance\":7}]}";
		
		// Act
		ResultActions result = this.mvc.perform(post("/graph").contentType(contentType)
										.content(graph));

		// Assert
		result.andExpect(status().isCreated())
				.andExpect(content().contentType(contentType));
		
		// Act
		Integer id = Integer.valueOf(result.andReturn().getResponse().getHeader("location").split("/")[2]);
		result = this.mvc.perform(get("/graph/{id}", id));
		
		// Assert
		result.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.data.[*]", hasSize(11)));
	}
	
	@Test
	public void shouldReturnError_GraphNotFound() throws Exception {
		// Act
		ResultActions result = this.mvc.perform(get("/graph/{id}", 99));

		// Assert
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath("message", is("Graph not found with id: 99")));
	}

}
