package com.gdg.hackathon.service;

import com.gdg.hackathon.dto.RegistrantRequest;
import com.gdg.hackathon.exception.ResponseMessage;
import com.gdg.hackathon.repository.RegistrantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RegistrantService {
    @Autowired
    private RegistrantRepository registrantRepository;
    private final JavaMailSender emailSender;

    @Transactional
    public ResponseMessage register(RegistrantRequest request)throws BadRequestException{
        request.setPhoneNumber(removeHyphen(request.getPhoneNumber()));

        if(!isValidRequest(request)){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "입력하지 않은 필드가 존재합니다.");
        }

        if(!isRegistrationPeriod()){
            return new ResponseMessage(HttpStatus.SERVICE_UNAVAILABLE.value(), "아직 신청기간이 아닙니다.");
        }

        if(!isRegistrationDuplicate(request.getStudentId())){
            return new ResponseMessage(HttpStatus.CONFLICT.value(), "이미 신청한 학번입니다.")
        }
        registrantRepository.save(request.to());
        sendRegisterEmail(request.getEmail());
        return new ResponseMessage(HttpStatus.CREATED.value(), "접수 완료");
    }

    private String removeHyphen(String phoneNumber){
        return phoneNumber.replaceAll("-", "");
    }

    // 입력하지 않은 필드 존재
    private boolean isValidRequest(RegistrantRequest request) {
        return request.getName() != null && request.getStudentId() != null &&
                request.getMajor() != null && request.getPhoneNumber() != null &&
                request.getPosition() != null && request.getGithubId() != null &&
                request.getTeamName() != null && request.getEmail() != null;
    }

    // 중복 확인
    private boolean isRegistrationDuplicate(Long studentId) throws BadRequestException{
        return registrantRepository.existsByStudentId(studentId);
    }

    // 신청 기간 확인
    private boolean isRegistrationPeriod() {
        // 예시: 현재 날짜가 특정 범위 내인지 확인하는 로직
        LocalDate now = LocalDate.now();
        // 신청 시작일과 종료일 설정
        LocalDate startDate = LocalDate.of(2024, 10, 16); // 예시 시작일
        LocalDate endDate = LocalDate.of(2024, 10, 31); // 예시 종료일
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    @Async
    public void sendRegisterEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[GDG KNU] 비전 챌린지 해커톤 참가 안내");
        message.setText(
                """
                        안녕하세요.

                        비전 챌린지 해커톤 참가와 관련하여 다음과 같이 안내드립니다.

                        1. 참가비: 10,000원
                        2. 참가비 납부: 참가자 본인 이름으로 아래 계좌에 입금
                           3333312689606 카카오뱅크 GDG KNU
                        3. 입금 기한: 10월 31일까지
                        4. 문의사항: GDG 운영진 (KakaoTalk ID: k.gu_wae)

                        팀장님이 대표로 신청하는 것이 아니라 각 팀원들도 개별적으로 신청해 주시기 바랍니다.

                        입금이 확인된 분들께는 해커톤 입장 QR 코드가 이메일로 발송될 예정입니다.

                        행사 관련 자세한 사항은 홈페이지를 참고해 주시기 바랍니다.

                        감사합니다.

                        GDG KNU 운영진 드림"""
        );
        emailSender.send(message);
    }
}
