package br.inf.ufes.service;

import br.inf.ufes.domain.Entidade;
import br.inf.ufes.repository.EntidadeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Entidade.
 */
@Service
@Transactional
public class EntidadeService {

    private final Logger log = LoggerFactory.getLogger(EntidadeService.class);

    private final EntidadeRepository entidadeRepository;

    public EntidadeService(EntidadeRepository entidadeRepository) {
        this.entidadeRepository = entidadeRepository;
    }

    /**
     * Save a entidade.
     *
     * @param entidade the entity to save
     * @return the persisted entity
     */
    public Entidade save(Entidade entidade) {
        log.debug("Request to save Entidade : {}", entidade);
        return entidadeRepository.save(entidade);
    }

    /**
     * Get all the entidades.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Entidade> findAll(Pageable pageable) {
        log.debug("Request to get all Entidades");
        return entidadeRepository.findAll(pageable);
    }

    /**
     * Get one entidade by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Entidade findOne(Long id) {
        log.debug("Request to get Entidade : {}", id);
        return entidadeRepository.findOne(id);
    }

    /**
     * Delete the entidade by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Entidade : {}", id);
        entidadeRepository.delete(id);
    }
}
