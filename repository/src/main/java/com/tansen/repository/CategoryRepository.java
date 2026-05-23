package com.tansen.repository;

import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.Category;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByUniqueId(@NotNull(message = "Category shouldn't be blank") String categoryId);


    @Query(
            value = """
        SELECT *
        FROM categories
        WHERE category_name = :categoryName
          AND municipality_id = :municipality
    """,
            nativeQuery = true
    )
    Optional<Category> findByCategoryNameAndMunicipalityId(
            @Param("categoryName") String categoryName,
            @Param("municipality") Long municipality
    );


    // Spring Data JPA auto-implements this from the method name
    List<Category> findAllByMunicipality(AdministrativeUnit municipality);

    // Alternative: filter by municipality ID directly
    @Query("SELECT c FROM Category c WHERE c.municipality.id = :municipalityId")
    List<Category> findAllByMunicipalityId(@Param("municipalityId") Long municipalityId);

}
