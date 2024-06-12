package hardik.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTopicService {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTopics() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("spring", "Spring Framework", "Spring Framework Description"));
        topics.add(new Topic("java", "Core Java", "Core Java Description"));
        topics.add(new Topic("javascript", "JavaScript", "JavaScript Description"));
        
        when(topicRepository.findAll()).thenReturn(topics);

        List<Topic> result = topicService.getAllTopics();

        assertEquals(topics, result);
    }

    @Test
    public void testGetTopic() {
        Topic topic = new Topic("spring", "Spring Framework", "Spring Framework Description");
        
        when(topicRepository.findById("spring")).thenReturn(Optional.of(topic));

        Topic result = topicService.getTopic("spring");

        assertEquals(topic, result);
    }

    @Test
    public void testGetTopicNotFound() {
        when(topicRepository.findById("unknown")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            topicService.getTopic("unknown");
        });
    }

    @Test
    public void testAddTopic() {
        Topic topic = new Topic("spring", "Spring Framework", "Spring Framework Description");

        topicService.addTopic(topic);

        verify(topicRepository).save(topic);
    }

    @Test
    public void testUpdateTopic() {
        Topic topic = new Topic("spring", "Spring Framework", "Spring Framework Description");

        topicService.updateTopic("spring", topic);

        verify(topicRepository).save(topic);
    }

    @Test
    public void testDeleteTopic() {
        String id = "spring";

        topicService.deleteTopic(id);

        verify(topicRepository).deleteById(id);
    }
}
