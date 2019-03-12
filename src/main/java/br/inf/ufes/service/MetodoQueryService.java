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

import br.inf.ufes.domain.Metodo;
import br.inf.ufes.domain.*; // for static metamodels
import br.inf.ufes.repository.MetodoRepository;
import br.inf.ufes.service.dto.MetodoCriteria;


/**
 * Service for executing complex queries for Metodo entities in the database.
 * The main input is a {@link MetodoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Metodo} or a {@link Page} of {@link Metodo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetodoQueryService extends QueryService<Metodo> {

    private final Logger log = LoggerFactory.getLogger(MetodoQueryService.class);


    private final MetodoRepository metodoRepository;

    public MetodoQueryService(MetodoRepository metodoRepository) {
        this.metodoRepository = metodoRepository;
    }

    /**
     * Return a {@link List} of {@link Metodo} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Metodo> findByCriteria(MetodoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Metodo> specification = createSpecification(criteria);
        return metodoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Metodo} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Metodo> findByCriteria(MetodoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Metodo> specification = createSpecification(criteria);
        return metodoRepository.findAll(specification, page);
    }

    /**
     * Function to convert MetodoCriteria to a {@link Specifications}
     */
    private Specifications<Metodo> createSpecification(MetodoCriteria criteria) {
        Specifications<Metodo> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Metodo_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Metodo_.nome));
            }
            if (criteria.getDatasourceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDatasourceId(), Metodo_.datasources, DataSource_.id));
            }
        }
        return specification;
    }

}
