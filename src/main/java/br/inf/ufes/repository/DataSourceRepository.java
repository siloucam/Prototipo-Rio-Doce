package br.inf.ufes.repository;

import br.inf.ufes.domain.DataSource;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the DataSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long>, JpaSpecificationExecutor<DataSource> {
    @Query("select distinct data_source from DataSource data_source left join fetch data_source.metodos left join fetch data_source.entidades left join fetch data_source.atividades left join fetch data_source.propriedades left join fetch data_source.proveniencias")
    List<DataSource> findAllWithEagerRelationships();

    @Query("select data_source from DataSource data_source left join fetch data_source.metodos left join fetch data_source.entidades left join fetch data_source.atividades left join fetch data_source.propriedades left join fetch data_source.proveniencias where data_source.id =:id")
    DataSource findOneWithEagerRelationships(@Param("id") Long id);

}
