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

import br.inf.ufes.domain.Propriedade;
import br.inf.ufes.domain.*; // for static metamodels
import br.inf.ufes.repository.PropriedadeRepository;
import br.inf.ufes.service.dto.PropriedadeCriteria;


/**
 * Service for executing complex queries for Propriedade entities in the database.
 * The main input is a {@link PropriedadeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Propriedade} or a {@link Page} of {@link Propriedade} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PropriedadeQueryService extends QueryService<Propriedade> {

    private final Logger log = LoggerFactory.getLogger(PropriedadeQueryService.class);


    private final PropriedadeRepository propriedadeRepository;

    public PropriedadeQueryService(PropriedadeRepository propriedadeRepository) {
        this.propriedadeRepository = propriedadeRepository;
    }

    /**
     * Return a {@link List} of {@link Propriedade} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Propriedade> findByCriteria(PropriedadeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Propriedade> specification = createSpecification(criteria);
        return propriedadeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Propriedade} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Propriedade> findByCriteria(PropriedadeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Propriedade> specification = createSpecification(criteria);
        return propriedadeRepository.findAll(specification, page);
    }

    /**
     * Function to convert PropriedadeCriteria to a {@link Specifications}
     */
    private Specifications<Propriedade> createSpecification(PropriedadeCriteria criteria) {
        Specifications<Propriedade> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Propriedade_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Propriedade_.nome));
            }
            if (criteria.getDatasourceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDatasourceId(), Propriedade_.datasources, DataSource_.id));
            }
        }
        return specification;
    }

}
