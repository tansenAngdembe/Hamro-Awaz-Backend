package com.tansen.common.service;

import com.tansen.common.dto.model.SendEmailRequest;

public interface MailService {
    void sendEmail(SendEmailRequest request);

}
