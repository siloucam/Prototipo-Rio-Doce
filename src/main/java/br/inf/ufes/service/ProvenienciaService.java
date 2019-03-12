package br.inf.ufes.service;

import br.inf.ufes.domain.Proveniencia;
import br.inf.ufes.repository.ProvenienciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Proveniencia.
 */
@Service
@Transactional
public class ProvenienciaService {

    private final Logger log = LoggerFactory.getLogger(ProvenienciaService.class);

    private final ProvenienciaRepository provenienciaRepository;

    public ProvenienciaService(ProvenienciaRepository provenienciaRepository) {
        this.provenienciaRepository = provenienciaRepository;
    }

    /**
     * Save a proveniencia.
     *
     * @param proveniencia the entity to save
     * @return the persisted entity
     */
    public Proveniencia save(Proveniencia proveniencia) {
        log.debug("Request to save Proveniencia : {}", proveniencia);
        return provenienciaRepository.save(proveniencia);
    }

    /**
     * Get all the proveniencias.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Proveniencia> findAll(Pageable pageable) {
        log.debug("Request to get all Proveniencias");
        return provenienciaRepository.findAll(pageable);
    }

    /**
     * Get one proveniencia by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Proveniencia findOne(Long id) {
        log.debug("Request to get Proveniencia : {}", id);
        return provenienciaRepository.findOne(id);
    }

    /**
     * Delete the proveniencia by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Proveniencia : {}", id);
        provenienciaRepository.delete(id);
    }
}
