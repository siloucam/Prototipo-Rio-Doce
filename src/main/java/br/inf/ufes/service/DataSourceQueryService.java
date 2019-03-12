package br.inf.ufes.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import br.inf.ufes.domain.DataSource;
import br.inf.ufes.domain.*; // for static metamodels
import br.inf.ufes.repository.DataSourceRepository;
import br.inf.ufes.service.dto.DataSourceCriteria;


/**
 * Service for executing complex queries for DataSource entities in the database.
 * The main input is a {@link DataSourceCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DataSource} or a {@link Page} of {@link DataSource} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DataSourceQueryService extends QueryService<DataSource> {

    private final Logger log = LoggerFactory.getLogger(DataSourceQueryService.class);


    private final DataSourceRepository dataSourceRepository;

    public DataSourceQueryService(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    /**
     * Return a {@link List} of {@link DataSource} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataSource> findByCriteria(DataSourceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<DataSource> specification = createSpecification(criteria);
        return dataSourceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DataSource} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DataSource> findByCriteria(DataSourceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<DataSource> specification = createSpecification(criteria);
        return dataSourceRepository.findAll(specification, page);
    }

    /**
     * Function to convert DataSourceCriteria to a {@link Specifications}
     */
    private Specifications<DataSource> createSpecification(DataSourceCriteria criteria) {
        Specifications<DataSource> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DataSource_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), DataSource_.nome));
            }
            if (criteria.getPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPath(), DataSource_.path));
            }
            if (criteria.getDtinicial() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDtinicial(), DataSource_.dtinicial));
            }
            if (criteria.getDtfinal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDtfinal(), DataSource_.dtfinal));
            }
            if (criteria.getMetodoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMetodoId(), DataSource_.metodos, Metodo_.id));
            }
            if (criteria.getEntidadeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEntidadeId(), DataSource_.entidades, Entidade_.id));
            }
            if (criteria.getAtividadeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAtividadeId(), DataSource_.atividades, Atividade_.id));
            }
            if (criteria.getPropriedadeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPropriedadeId(), DataSource_.propriedades, Propriedade_.id));
            }
            if (criteria.getProvenienciaId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProvenienciaId(), DataSource_.proveniencias, Proveniencia_.id));
            }
        }
        return specification;
    }

}
