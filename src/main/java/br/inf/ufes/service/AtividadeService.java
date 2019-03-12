package br.inf.ufes.service;

import br.inf.ufes.domain.Atividade;
import br.inf.ufes.repository.AtividadeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Atividade.
 */
@Service
@Transactional
public class AtividadeService {

    private final Logger log = LoggerFactory.getLogger(AtividadeService.class);

    private final AtividadeRepository atividadeRepository;

    public AtividadeService(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    /**
     * Save a atividade.
     *
     * @param atividade the entity to save
     * @return the persisted entity
     */
    public Atividade save(Atividade atividade) {
        log.debug("Request to save Atividade : {}", atividade);
        return atividadeRepository.save(atividade);
    }

    /**
     * Get all the atividades.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Atividade> findAll(Pageable pageable) {
        log.debug("Request to get all Atividades");
        return atividadeRepository.findAll(pageable);
    }

    /**
     * Get one atividade by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Atividade findOne(Long id) {
        log.debug("Request to get Atividade : {}", id);
        return atividadeRepository.findOne(id);
    }

    /**
     * Delete the atividade by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Atividade : {}", id);
        atividadeRepository.delete(id);
    }
}
