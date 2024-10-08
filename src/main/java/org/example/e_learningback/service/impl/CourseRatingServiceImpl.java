package org.example.e_learningback.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.e_learningback.dto.CourseRatingDto;
import org.example.e_learningback.dto.UserDto;
import org.example.e_learningback.entity.Course;
import org.example.e_learningback.entity.CourseRating;
import org.example.e_learningback.entity.User;
import org.example.e_learningback.exception.CourseNotFoundException;
import org.example.e_learningback.exception.CourseRatingNotFoundException;
import org.example.e_learningback.exception.UserNotFoundException;
import org.example.e_learningback.repository.CourseRatingRepository;
import org.example.e_learningback.repository.CourseRepository;
import org.example.e_learningback.repository.UserRepository;
import org.example.e_learningback.service.CourseRatingService;
import org.example.e_learningback.utils.GenericMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseRatingServiceImpl implements CourseRatingService {

    private final CourseRatingRepository courseRatingRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final GenericMapper genericMapper;

    @Override
    public List<CourseRatingDto> findAllCourseRatings() {
        List<CourseRating> courseRatings = courseRatingRepository.findAll();

        List<CourseRatingDto> courseRatingDtos = genericMapper.mapList(courseRatings, CourseRatingDto.class);
        courseRatingDtos.forEach(courseRatingDto -> {
            courseRatings.forEach(courseRating -> {
                if (courseRatingDto.getId().equals(courseRating.getId())) {
                    courseRatingDto.setCourseId(courseRating.getCourse().getId());
                }
            });
        });
        return courseRatingDtos;
    }

    @Override
    public CourseRatingDto findCourseRatingById(Long id) {
        Optional<CourseRating> courseRating = courseRatingRepository.findById(id);

        if (courseRating.isEmpty()) {
            throw new CourseRatingNotFoundException("Course rating does not exist");
        }

        CourseRatingDto courseRatingDto = genericMapper.map(courseRating.get(), CourseRatingDto.class);

        // Set UserDto
        UserDto userDto = genericMapper.map(courseRating.get().getUser(), UserDto.class);
        courseRatingDto.setUser(userDto);

        return courseRatingDto;
    }

    @Override
    public List<CourseRatingDto> findAllCourseRatingsByCourseId(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (courseOptional.isEmpty()) {
            throw new CourseNotFoundException("Course does not exist");
        }

        List<CourseRating> courseRatings = courseOptional.get().getCourseRatings();
        List<CourseRatingDto> courseRatingDtos = new ArrayList<>();

        for (CourseRating courseRating : courseRatings) {
            CourseRatingDto courseRatingDto = genericMapper.map(courseRating, CourseRatingDto.class);

            // Set UserDto
            UserDto userDto = genericMapper.map(courseRating.getUser(), UserDto.class);
            courseRatingDto.setUser(userDto);

            courseRatingDtos.add(courseRatingDto);
        }

        return courseRatingDtos;
    }

    @Override
    @Transactional
    public CourseRatingDto createCourseRating(CourseRatingDto newCourseRatingDto, Long courseId, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User does not exist");
        }
        if (courseOptional.isEmpty()) {
            throw new CourseNotFoundException("Course does not exist");
        }

        CourseRating courseRating = genericMapper.map(newCourseRatingDto, CourseRating.class);

        // Set ID to null to ensure a new course rating is created
        courseRating.setId(null);

        courseRating.setUser(userOptional.get());
        courseRating.setCourse(courseOptional.get());
        courseRating.setCreated_at(new Date());

        CourseRatingDto createdCourseRatingDto = genericMapper.map(courseRatingRepository.save(courseRating), CourseRatingDto.class);

        // Set UserDto
        UserDto userDto = genericMapper.map(userOptional.get(), UserDto.class);
        createdCourseRatingDto.setUser(userDto);

        return createdCourseRatingDto;
    }

    @Override
    @Transactional
    public CourseRatingDto updateCourseRating(Long courseRatingId, CourseRatingDto newCourseRatingDto) {
        Optional<CourseRating> optionalCourseRating = courseRatingRepository.findById(courseRatingId);

        if (optionalCourseRating.isEmpty()) {
            throw new CourseRatingNotFoundException("Course rating not found");
        }

        CourseRating courseRating = optionalCourseRating.get();
        courseRating.setRating(newCourseRatingDto.getRating());
        courseRating.setReview(newCourseRatingDto.getReview());
        courseRating.setCreated_at(newCourseRatingDto.getCreated_at());

        CourseRatingDto updatedCourseRatingDto = genericMapper.map(courseRating, CourseRatingDto.class);

        // Set UserDto
        UserDto userDto = genericMapper.map(courseRating.getUser(), UserDto.class);
        updatedCourseRatingDto.setUser(userDto);

        return updatedCourseRatingDto;
    }

    @Override
    @Transactional
    public void deleteCourseRating(Long courseRatingId) {
        if (courseRatingRepository.existsById(courseRatingId)) {
            courseRatingRepository.deleteById(courseRatingId);
        } else {
            throw new CourseRatingNotFoundException("Course rating not found");
        }
    }
}
