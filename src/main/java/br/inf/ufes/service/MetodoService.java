package br.inf.ufes.service;

import br.inf.ufes.domain.Metodo;
import br.inf.ufes.repository.MetodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Metodo.
 */
@Service
@Transactional
public class MetodoService {

    private final Logger log = LoggerFactory.getLogger(MetodoService.class);

    private final MetodoRepository metodoRepository;

    public MetodoService(MetodoRepository metodoRepository) {
        this.metodoRepository = metodoRepository;
    }

    /**
     * Save a metodo.
     *
     * @param metodo the entity to save
     * @return the persisted entity
     */
    public Metodo save(Metodo metodo) {
        log.debug("Request to save Metodo : {}", metodo);
        return metodoRepository.save(metodo);
    }

    /**
     * Get all the metodos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Metodo> findAll(Pageable pageable) {
        log.debug("Request to get all Metodos");
        return metodoRepository.findAll(pageable);
    }

    /**
     * Get one metodo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Metodo findOne(Long id) {
        log.debug("Request to get Metodo : {}", id);
        return metodoRepository.findOne(id);
    }

    /**
     * Delete the metodo by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Metodo : {}", id);
        metodoRepository.delete(id);
    }
}
