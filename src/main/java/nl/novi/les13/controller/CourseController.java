package nl.novi.les13.controller;

import nl.novi.les13.dto.CourseDto;
import nl.novi.les13.model.Course;
import nl.novi.les13.model.Teacher;
import nl.novi.les13.repository.CourseRepository;
import nl.novi.les13.repository.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    // ALWAYS USE SERVICES INSTEAD OF REPOSITORIES IN CONTROLLERS!!!
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public CourseController(CourseRepository courseRepos, TeacherRepository teacherRepos) {
        this.courseRepository = courseRepos;
        this.teacherRepository = teacherRepos;
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
        Course course = new Course();
        course.setTitle(courseDto.title);
        course.setSp(courseDto.sp);
        for (Long id : courseDto.teacherIds) {
            Optional<Teacher> ot = teacherRepository.findById(id);
            if (ot.isPresent()) {
                Teacher teacher = ot.get();
                course.getTeachers().add(teacher);
            }
        }
        courseRepository.save(course);
        courseDto.id = course.getId();
        return new ResponseEntity<>(courseDto, HttpStatus.CREATED);
    }
}
