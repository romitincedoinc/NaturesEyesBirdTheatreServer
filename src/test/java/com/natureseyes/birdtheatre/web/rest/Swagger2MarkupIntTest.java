package com.natureseyes.birdtheatre.web.rest;

import com.natureseyes.birdtheatre.BirdtheatreApp;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springfox.documentation.staticdocs.SwaggerResultHandler;

import javax.inject.Inject;
import java.io.IOException;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BirdtheatreApp.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles("dev")
public class Swagger2MarkupIntTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("target/asciidoc");

    @Inject
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
.apply(documentationConfiguration(this.restDocumentation))        .build();
    }

    @Test
    public void getAllUsersSamples() throws Exception {
        this.mockMvc.perform(get("/api/users")
          .accept(MediaType.APPLICATION_JSON))
          .andDo(document("getallusers", preprocessResponse(prettyPrint())))
          .andExpect(status().isOk());
    }
    @Test
    public void convertSwaggerToAsciiDoc() throws Exception {
        this.mockMvc.perform(get("/v2/api-docs")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(SwaggerResultHandler.outputDirectory("target/swagger")
            .build())
            .andExpect(status().isOk());
    }
}
