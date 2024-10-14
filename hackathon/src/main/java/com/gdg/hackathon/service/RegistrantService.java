package com.gdg.hackathon.service;

import com.gdg.hackathon.domain.Registrant;
import com.gdg.hackathon.dto.RegistrantRequest;
import com.gdg.hackathon.repository.RegistrantRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrantService {
    @Autowired
    private RegistrantRepository registrantRepository;
    private final JavaMailSender emailSender;

    public String register(RegistrantRequest request)throws BadRequestException{
        request.setPhoneNumber(removeHyphen(request.getPhoneNumber()));
        validation(request);
        sendRegisterEmail(request.getEmail());
        registrantRepository.save(request.to());
        return "접수 완료";
    }

    private String removeHyphen(String phoneNumber){
        return phoneNumber.replaceAll("-", "");
    }

    private void validation(RegistrantRequest request) throws BadRequestException{
        if(registrantRepository.existsByStudentId(request.getStudentId())){
            throw new BadRequestException("이미 신청한 학번입니다.");
        }
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
                        3. 입금 기한: 10월 30일까지
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
