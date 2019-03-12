package br.inf.ufes.service;

import br.inf.ufes.domain.Propriedade;
import br.inf.ufes.repository.PropriedadeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Propriedade.
 */
@Service
@Transactional
public class PropriedadeService {

    private final Logger log = LoggerFactory.getLogger(PropriedadeService.class);

    private final PropriedadeRepository propriedadeRepository;

    public PropriedadeService(PropriedadeRepository propriedadeRepository) {
        this.propriedadeRepository = propriedadeRepository;
    }

    /**
     * Save a propriedade.
     *
     * @param propriedade the entity to save
     * @return the persisted entity
     */
    public Propriedade save(Propriedade propriedade) {
        log.debug("Request to save Propriedade : {}", propriedade);
        return propriedadeRepository.save(propriedade);
    }

    /**
     * Get all the propriedades.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Propriedade> findAll(Pageable pageable) {
        log.debug("Request to get all Propriedades");
        return propriedadeRepository.findAll(pageable);
    }

    /**
     * Get one propriedade by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Propriedade findOne(Long id) {
        log.debug("Request to get Propriedade : {}", id);
        return propriedadeRepository.findOne(id);
    }

    /**
     * Delete the propriedade by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Propriedade : {}", id);
        propriedadeRepository.delete(id);
    }
}
