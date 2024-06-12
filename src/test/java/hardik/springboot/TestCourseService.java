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
public class TestCourseService {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("1", "Course 1", "Course 1 Description", "topic1"));
        courses.add(new Course("2", "Course 2", "Course 2 Description", "topic1"));
        
        when(courseRepository.findByTopicId("topic1")).thenReturn(courses);

        List<Course> result = courseService.getAllCourses("topic1");

        assertEquals(courses, result);
    }

    @Test
    public void testGetCourse() {
        Course course = new Course("1", "Course 1", "Course 1 Description", "topic1");
        
        when(courseRepository.findById("1")).thenReturn(Optional.of(course));

        Course result = courseService.getCourse("1");

        assertEquals(course, result);
    }

    @Test
    public void testGetCourseNotFound() {
        when(courseRepository.findById("unknown")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            courseService.getCourse("unknown");
        });
    }

    @Test
    public void testAddCourse() {
        Course course = new Course("1", "Course 1", "Course 1 Description", "topic1");

        courseService.addCourse(course);

        verify(courseRepository).save(course);
    }

    @Test
    public void testUpdateCourse() {
        Course course = new Course("1", "Course 1", "Course 1 Description", "topic1");

        courseService.updateCourse(course);

        verify(courseRepository).save(course);
    }

    @Test
    public void testDeleteCourse() {
        String id = "1";

        courseService.deleteCourse(id);

        verify(courseRepository).deleteById(id);
    }
}
