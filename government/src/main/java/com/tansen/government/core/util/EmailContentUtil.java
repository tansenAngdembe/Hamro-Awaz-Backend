package com.tansen.government.core.util;

import com.tansen.common.constant.FreeMarkerTemplateConstant;
import com.tansen.entity.EmailTemplate;
import com.tansen.government.municipality.dto.EmailOtpSendDto;
import com.tansen.repository.EmailTemplateRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailContentUtil {
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    @Autowired
    private Configuration configuration;

    public String prepareOtpEmailContent(EmailOtpSendDto prepareEmailContentDto) {
        EmailTemplate emailTemplate = emailTemplateRepository.findEmailTemplateByName(prepareEmailContentDto.getTemplateName());

        Map<String, Object> model = new HashMap<>();
        model.put(FreeMarkerTemplateConstant.NAME, prepareEmailContentDto.getUserFullName());
        model.put(FreeMarkerTemplateConstant.OTP, prepareEmailContentDto.getOtp());
        model.put(FreeMarkerTemplateConstant.EXPIRATION_TIME, prepareEmailContentDto.getExpirationTime());
        model.put(FreeMarkerTemplateConstant.CURRENT_YEAR, Year.now().getValue());

        String emailContent;
        try{
            Template template = new Template("emailTemplate", emailTemplate.getTemplate(), configuration);
            emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            throw new RuntimeException("Error processing email template: " + e.getMessage());
        }
        return emailContent;
    }

}
