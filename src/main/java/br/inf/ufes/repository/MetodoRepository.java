package br.inf.ufes.repository;

import br.inf.ufes.domain.Metodo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Metodo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetodoRepository extends JpaRepository<Metodo, Long>, JpaSpecificationExecutor<Metodo> {

}
