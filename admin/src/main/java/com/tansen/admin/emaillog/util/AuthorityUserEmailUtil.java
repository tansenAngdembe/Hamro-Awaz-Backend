package com.tansen.admin.emaillog.util;


import com.tansen.admin.emaillog.dto.AuthorityUserEmailContent;
import com.tansen.common.constant.FreeMarkerTemplateConstant;
import com.tansen.entity.EmailTemplate;
import com.tansen.repository.EmailTemplateRepository;
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

    public String prepareAdminEmail(AuthorityUserEmailContent emailContent, String link) {
        EmailTemplate emailTemplate = emailTemplateRepository.findEmailTemplateByName(emailContent.getTemplate());
        Map<String, Object> model = new HashMap<>();
        model.put(FreeMarkerTemplateConstant.NAME, emailContent.getName());
        model.put(FreeMarkerTemplateConstant.EXPIRATION_TIME, emailContent.getExpirationTime());
        model.put(FreeMarkerTemplateConstant.VERIFICATION_LINK,link+emailContent.getUuid());
        model.put(FreeMarkerTemplateConstant.CURRENT_YEAR, Year.now().getValue());
        String content;
        try{
            Template template = new Template("emailTemplate",emailTemplate.getTemplate(),freeMarkerConfig);
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
        }
        catch(Exception e){
            throw new RuntimeException("Error processing email template"+e.getMessage());
        }
        return content;
    }

}
