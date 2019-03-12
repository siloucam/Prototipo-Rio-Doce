package br.inf.ufes.repository;

import br.inf.ufes.domain.Proveniencia;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Proveniencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvenienciaRepository extends JpaRepository<Proveniencia, Long>, JpaSpecificationExecutor<Proveniencia> {

}
