package com.tansen.repository;

import com.tansen.entity.User;
import com.tansen.entity.UserDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDocumentsRepository extends JpaRepository<UserDocuments, Long> {
    Optional<UserDocuments> findByUser(User user);

    Optional<UserDocuments> findByUniqueId(String uniqueId);
}
