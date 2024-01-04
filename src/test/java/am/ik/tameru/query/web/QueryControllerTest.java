package am.ik.tameru.query.web;

import am.ik.tameru.event.LogEventQuery;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QueryController.class)
class QueryControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	LogEventQuery logEventQuery;

	@Test
	void eventsOK() throws Exception {
		mockMvc.perform(get(("/"))).andExpect(status().isOk());
	}

	@Test
	void eventsBadRequest() throws Exception {
		mockMvc.perform(get("/").param("size", "0").param("query", "fo")).andExpect(content().json("""
				{
				  "type": "about:blank",
				  "title": "Bad Request",
				  "status": 400,
				  "detail": "Constraint violations found!",
				  "instance": "/",
				  "violations": [
				    {
				      "type": "container.greaterThanOrEqual",
				      "message": "The size of \\"query\\" must be greater than or equal to 3. The given size is 2"
				    },
				    {
				      "type": "numeric.greaterThanOrEqual",
				      "message": "\\"size\\" must be greater than or equal to 1"
				    }
				  ]
				}
				""")).andExpect(status().isBadRequest());
	}

}