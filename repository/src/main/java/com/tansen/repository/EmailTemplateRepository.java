package com.tansen.repository;

import com.tansen.entity.EmailTemplate;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate,Long> {
    EmailTemplate findEmailTemplateByName(@NotBlank(message = "Email template is required") String attr0);
}
