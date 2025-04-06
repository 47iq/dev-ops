package org.iq47.devops.repository;

import org.iq47.devops.model.SkiPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkiPassRepository extends JpaRepository<SkiPass, Long> {
}
