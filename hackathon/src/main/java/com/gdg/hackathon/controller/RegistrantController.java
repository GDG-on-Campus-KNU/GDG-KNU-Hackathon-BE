package com.gdg.hackathon.controller;

import com.gdg.hackathon.dto.RegistrantRequest;
import com.gdg.hackathon.exception.ResponseMessage;
import com.gdg.hackathon.service.RegistrantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/registrants")
@AllArgsConstructor
public class RegistrantController {
    private final RegistrantService registrantService;

    @PostMapping()
    public ResponseEntity<ResponseMessage> register(@Valid @RequestBody RegistrantRequest registrant) {
        try {
            ResponseMessage message = new ResponseMessage(HttpStatus.CREATED.value(), registrantService.register(registrant));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(message);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseMessage(e.getStatusCode().value(), e.getReason()));
        }
    }
}
