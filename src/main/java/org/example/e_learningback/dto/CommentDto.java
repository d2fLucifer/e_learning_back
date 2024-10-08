
package org.example.e_learningback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String body;
    private Date created_at;
    private List<ReplyCommentDto> replyComments;

    // private CourseSession courseSession;
    private UserDto user;
}
