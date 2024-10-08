package com.gdg.hackathon.dto;

import com.gdg.hackathon.domain.Position;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
public class RegistrantRequest {

    @NotBlank(message = "학번은 필수입니다.")
    @Column(nullable = false, unique = true)
    private Long studentId;

    @NotBlank(message = "이름은 필수입니다.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "전공은 필수입니다.")
    @Column(nullable = false)
    private String major;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Size(min = 10, max = 15) // 전화번호 길이에 대한 제약 조건 예시
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @NotBlank(message = "직열은 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @Column(unique = true, nullable = true)
    private String githubId;

    @NotBlank(message = "팀명은 필수입니다.")
    @Column(nullable = false)
    private String teamName;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식을 확인해주세요.")
    @Column(nullable = false, unique = true)
    private String email;
}
