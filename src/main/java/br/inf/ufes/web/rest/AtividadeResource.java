package br.inf.ufes.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.inf.ufes.domain.Atividade;
import br.inf.ufes.service.AtividadeService;
import br.inf.ufes.web.rest.errors.BadRequestAlertException;
import br.inf.ufes.web.rest.util.HeaderUtil;
import br.inf.ufes.web.rest.util.PaginationUtil;
import br.inf.ufes.service.dto.AtividadeCriteria;
import br.inf.ufes.service.AtividadeQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Atividade.
 */
@RestController
@RequestMapping("/api")
public class AtividadeResource {

    private final Logger log = LoggerFactory.getLogger(AtividadeResource.class);

    private static final String ENTITY_NAME = "atividade";

    private final AtividadeService atividadeService;

    private final AtividadeQueryService atividadeQueryService;

    public AtividadeResource(AtividadeService atividadeService, AtividadeQueryService atividadeQueryService) {
        this.atividadeService = atividadeService;
        this.atividadeQueryService = atividadeQueryService;
    }

    /**
     * POST  /atividades : Create a new atividade.
     *
     * @param atividade the atividade to create
     * @return the ResponseEntity with status 201 (Created) and with body the new atividade, or with status 400 (Bad Request) if the atividade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/atividades")
    @Timed
    public ResponseEntity<Atividade> createAtividade(@RequestBody Atividade atividade) throws URISyntaxException {
        log.debug("REST request to save Atividade : {}", atividade);
        if (atividade.getId() != null) {
            throw new BadRequestAlertException("A new atividade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Atividade result = atividadeService.save(atividade);
        return ResponseEntity.created(new URI("/api/atividades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /atividades : Updates an existing atividade.
     *
     * @param atividade the atividade to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated atividade,
     * or with status 400 (Bad Request) if the atividade is not valid,
     * or with status 500 (Internal Server Error) if the atividade couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/atividades")
    @Timed
    public ResponseEntity<Atividade> updateAtividade(@RequestBody Atividade atividade) throws URISyntaxException {
        log.debug("REST request to update Atividade : {}", atividade);
        if (atividade.getId() == null) {
            return createAtividade(atividade);
        }
        Atividade result = atividadeService.save(atividade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, atividade.getId().toString()))
            .body(result);
    }

    /**
     * GET  /atividades : get all the atividades.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of atividades in body
     */
    @GetMapping("/atividades")
    @Timed
    public ResponseEntity<List<Atividade>> getAllAtividades(AtividadeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Atividades by criteria: {}", criteria);
        Page<Atividade> page = atividadeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/atividades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /atividades/:id : get the "id" atividade.
     *
     * @param id the id of the atividade to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the atividade, or with status 404 (Not Found)
     */
    @GetMapping("/atividades/{id}")
    @Timed
    public ResponseEntity<Atividade> getAtividade(@PathVariable Long id) {
        log.debug("REST request to get Atividade : {}", id);
        Atividade atividade = atividadeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(atividade));
    }

    /**
     * DELETE  /atividades/:id : delete the "id" atividade.
     *
     * @param id the id of the atividade to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/atividades/{id}")
    @Timed
    public ResponseEntity<Void> deleteAtividade(@PathVariable Long id) {
        log.debug("REST request to delete Atividade : {}", id);
        atividadeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
