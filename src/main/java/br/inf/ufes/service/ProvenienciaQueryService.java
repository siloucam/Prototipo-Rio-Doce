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

import br.inf.ufes.domain.Proveniencia;
import br.inf.ufes.domain.*; // for static metamodels
import br.inf.ufes.repository.ProvenienciaRepository;
import br.inf.ufes.service.dto.ProvenienciaCriteria;


/**
 * Service for executing complex queries for Proveniencia entities in the database.
 * The main input is a {@link ProvenienciaCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Proveniencia} or a {@link Page} of {@link Proveniencia} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProvenienciaQueryService extends QueryService<Proveniencia> {

    private final Logger log = LoggerFactory.getLogger(ProvenienciaQueryService.class);


    private final ProvenienciaRepository provenienciaRepository;

    public ProvenienciaQueryService(ProvenienciaRepository provenienciaRepository) {
        this.provenienciaRepository = provenienciaRepository;
    }

    /**
     * Return a {@link List} of {@link Proveniencia} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Proveniencia> findByCriteria(ProvenienciaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Proveniencia> specification = createSpecification(criteria);
        return provenienciaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Proveniencia} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Proveniencia> findByCriteria(ProvenienciaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Proveniencia> specification = createSpecification(criteria);
        return provenienciaRepository.findAll(specification, page);
    }

    /**
     * Function to convert ProvenienciaCriteria to a {@link Specifications}
     */
    private Specifications<Proveniencia> createSpecification(ProvenienciaCriteria criteria) {
        Specifications<Proveniencia> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Proveniencia_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Proveniencia_.nome));
            }
            if (criteria.getDatasourceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDatasourceId(), Proveniencia_.datasources, DataSource_.id));
            }
        }
        return specification;
    }

}
