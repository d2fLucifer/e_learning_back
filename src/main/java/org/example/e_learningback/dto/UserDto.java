package org.example.e_learningback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.e_learningback.entity.Course;
import org.example.e_learningback.entity.CourseRating;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String avatar;
    private  String bio;
    private  String occupation;
    private String name;
    private String email;
    private String password;

    private RoleDto role;
    private LocalDateTime joinedAt;
}
