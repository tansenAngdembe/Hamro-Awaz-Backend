package com.tansen.repository;

import com.tansen.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  ComplainStatusRepository extends JpaRepository<ComplaintStatus,Long> {
   ComplaintStatus findByName(String name);
}
