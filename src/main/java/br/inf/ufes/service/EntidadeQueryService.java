package br.inf.ufes.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import br.inf.ufes.domain.Entidade;
import br.inf.ufes.domain.*; // for static metamodels
import br.inf.ufes.repository.EntidadeRepository;
import br.inf.ufes.service.dto.EntidadeCriteria;


/**
 * Service for executing complex queries for Entidade entities in the database.
 * The main input is a {@link EntidadeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Entidade} or a {@link Page} of {@link Entidade} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EntidadeQueryService extends QueryService<Entidade> {

    private final Logger log = LoggerFactory.getLogger(EntidadeQueryService.class);


    private final EntidadeRepository entidadeRepository;

    public EntidadeQueryService(EntidadeRepository entidadeRepository) {
        this.entidadeRepository = entidadeRepository;
    }

    /**
     * Return a {@link List} of {@link Entidade} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Entidade> findByCriteria(EntidadeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Entidade> specification = createSpecification(criteria);
        return entidadeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Entidade} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Entidade> findByCriteria(EntidadeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Entidade> specification = createSpecification(criteria);
        return entidadeRepository.findAll(specification, page);
    }

    /**
     * Function to convert EntidadeCriteria to a {@link Specifications}
     */
    private Specifications<Entidade> createSpecification(EntidadeCriteria criteria) {
        Specifications<Entidade> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Entidade_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Entidade_.nome));
            }
            if (criteria.getDatasourceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDatasourceId(), Entidade_.datasources, DataSource_.id));
            }
        }
        return specification;
    }

}
