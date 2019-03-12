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

import br.inf.ufes.domain.Atividade;
import br.inf.ufes.domain.*; // for static metamodels
import br.inf.ufes.repository.AtividadeRepository;
import br.inf.ufes.service.dto.AtividadeCriteria;


/**
 * Service for executing complex queries for Atividade entities in the database.
 * The main input is a {@link AtividadeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Atividade} or a {@link Page} of {@link Atividade} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AtividadeQueryService extends QueryService<Atividade> {

    private final Logger log = LoggerFactory.getLogger(AtividadeQueryService.class);


    private final AtividadeRepository atividadeRepository;

    public AtividadeQueryService(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    /**
     * Return a {@link List} of {@link Atividade} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Atividade> findByCriteria(AtividadeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Atividade> specification = createSpecification(criteria);
        return atividadeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Atividade} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Atividade> findByCriteria(AtividadeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Atividade> specification = createSpecification(criteria);
        return atividadeRepository.findAll(specification, page);
    }

    /**
     * Function to convert AtividadeCriteria to a {@link Specifications}
     */
    private Specifications<Atividade> createSpecification(AtividadeCriteria criteria) {
        Specifications<Atividade> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Atividade_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Atividade_.nome));
            }
            if (criteria.getDatasourceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDatasourceId(), Atividade_.datasources, DataSource_.id));
            }
        }
        return specification;
    }

}
