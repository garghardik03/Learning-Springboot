package hardik.springboot;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TopicsController.class)
public class TestTopicsController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService topicService;

    @InjectMocks
    private TopicsController topicsController;

    private List<Topic> topics;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(topicsController).build();

        topics = Arrays.asList(
            new Topic("1", "Topic 1", "Description 1"),
            new Topic("2", "Topic 2", "Description 2")
        );
    }

    @Test
    public void testGetAllTopics() throws Exception {
        when(topicService.getAllTopics()).thenReturn(topics);

        mockMvc.perform(get("/topics"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(topics.size()))
            .andExpect(jsonPath("$[0].id").value(topics.get(0).getId()))
            .andExpect(jsonPath("$[1].id").value(topics.get(1).getId()));
    }

    @Test
    public void testGetTopic() throws Exception {
        Topic topic = topics.get(0);
        when(topicService.getTopic("1")).thenReturn(topic);

        mockMvc.perform(get("/topics/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(topic.getId()))
            .andExpect(jsonPath("$.name").value(topic.getName()))
            .andExpect(jsonPath("$.description").value(topic.getDescription()));
    }

    @Test
    public void testAddTopic() throws Exception {
        Topic topic = new Topic("3", "Topic 3", "Description 3");

        mockMvc.perform(post("/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(topic)))
            .andExpect(status().isOk());

        verify(topicService).addTopic(topic);
    }

    @Test
    public void testUpdateTopic() throws Exception {
        Topic topic = new Topic("1", "Updated Topic", "Updated Description");

        mockMvc.perform(put("/topics/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(topic)))
            .andExpect(status().isOk());

        verify(topicService).updateTopic("1", topic);
    }

    @Test
    public void testDeleteTopic() throws Exception {
        mockMvc.perform(delete("/topics/1"))
            .andExpect(status().isOk());

        verify(topicService).deleteTopic("1");
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
