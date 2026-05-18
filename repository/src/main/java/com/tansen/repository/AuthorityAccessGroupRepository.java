package com.tansen.repository;

import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.AuthorityAccessGroup;
import com.tansen.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityAccessGroupRepository extends JpaRepository<AuthorityAccessGroup,Long> {
Optional<AuthorityAccessGroup> findByName(String name);

    List<AuthorityAccessGroup> findByStatusAndMunicipality(Status status, AdministrativeUnit vendor);

    List<AuthorityAccessGroup> findByMunicipalityOrderByCreatedAtDesc(AdministrativeUnit municipality);
}
