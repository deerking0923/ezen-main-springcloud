package com.example.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "A domain object for user detail information")
public class ResponseUser {

    @Schema(title = "사용자 Email", description = "사용자 ID로 사용되는 Email 정보로써 로그인 시 사용")
    private String email;

    @Schema(title = "사용자 이름", description = "사용자 이름")
    private String name;

    @Schema(title = "사용자 User ID", description = "사용자 회원 가입 시 자동으로 부여 되는 사용자 고유한 ID (UUID로 Random하게 생성)")
    private String userId;

    @Schema(title = "리뷰 목록", description = "사용자의 리뷰 목록")
    private List<ResponseReview> reviews;

    // 새로 추가: 사용자 작성 게시글 목록
    private List<ResponsePost> posts;

    // 새로 추가: 사용자 작성 댓글 목록
    private List<ResponseComment> comments;
}
