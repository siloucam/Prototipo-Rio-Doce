package br.inf.ufes.repository;

import br.inf.ufes.domain.Entidade;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Entidade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntidadeRepository extends JpaRepository<Entidade, Long>, JpaSpecificationExecutor<Entidade> {

}
