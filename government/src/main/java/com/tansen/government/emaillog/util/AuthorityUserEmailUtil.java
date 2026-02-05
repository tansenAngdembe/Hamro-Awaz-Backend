package com.tansen.government.emaillog.util;

import com.tansen.common.constant.FreeMarkerTemplateConstant;
import com.tansen.entity.EmailTemplate;
import com.tansen.government.emaillog.dto.AuthorityEscalationContent;
import com.tansen.government.emaillog.dto.AuthorityUserEmailContent;
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
public class AuthorityUserEmailUtil {
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    @Autowired
    private freemarker.template.Configuration freeMarkerConfig;

    public String prepareVendorUserEmail(AuthorityUserEmailContent emailContent, String link) {
        EmailTemplate emailTemplate = emailTemplateRepository.findEmailTemplateByName(emailContent.getTemplate());
        Map<String, Object> model = new HashMap<>();
        model.put(FreeMarkerTemplateConstant.NAME, emailContent.getName());
        model.put(FreeMarkerTemplateConstant.EXPIRATION_TIME, emailContent.getExpirationTime());
        model.put(FreeMarkerTemplateConstant.VERIFICATION_LINK,link+emailContent.getUuid());
        model.put(FreeMarkerTemplateConstant.CURRENT_YEAR, Year.now().getValue());

        return getString(emailTemplate, model, freeMarkerConfig);

    }

    public String prepareEscalationEmail(AuthorityEscalationContent escalationContent) {
        EmailTemplate emailTemplate = emailTemplateRepository.findEmailTemplateByName(escalationContent.getTemplate());
        Map<String, Object> model = new HashMap<>();
        model.put(FreeMarkerTemplateConstant.COMPLAINTTITLE, escalationContent.getComplaintTitle());
        model.put(FreeMarkerTemplateConstant.CATEGORY, escalationContent.getCategory());
        model.put(FreeMarkerTemplateConstant.CREATEDON, String.valueOf(escalationContent.getCreatedDate()));
        model.put(FreeMarkerTemplateConstant.COMPLAINTRULE, escalationContent.getComplaintRule());
        model.put(FreeMarkerTemplateConstant.ASSIGNEDTO, escalationContent.getAssignedTo());
        model.put(FreeMarkerTemplateConstant.ESCALATIONON, escalationContent.getEscalationAt());
        return getString(emailTemplate, model, freeMarkerConfig);
    }

    static String getString(EmailTemplate emailTemplate, Map<String, Object> model, Configuration freeMarkerConfig) {
        String content;
        try {
            Template template = new Template("emailTemplate", emailTemplate.getTemplate(), freeMarkerConfig);
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            throw new RuntimeException("Error processing email template" + e.getMessage());
        }
        return content;
    }

}
