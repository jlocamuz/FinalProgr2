package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Orden;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Orden entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {}
