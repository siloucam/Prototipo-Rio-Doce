package br.inf.ufes.repository;

import br.inf.ufes.domain.Propriedade;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Propriedade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropriedadeRepository extends JpaRepository<Propriedade, Long>, JpaSpecificationExecutor<Propriedade> {

}
