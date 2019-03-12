package br.inf.ufes.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.inf.ufes.domain.Entidade;
import br.inf.ufes.service.EntidadeService;
import br.inf.ufes.web.rest.errors.BadRequestAlertException;
import br.inf.ufes.web.rest.util.HeaderUtil;
import br.inf.ufes.web.rest.util.PaginationUtil;
import br.inf.ufes.service.dto.EntidadeCriteria;
import br.inf.ufes.service.EntidadeQueryService;
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
 * REST controller for managing Entidade.
 */
@RestController
@RequestMapping("/api")
public class EntidadeResource {

    private final Logger log = LoggerFactory.getLogger(EntidadeResource.class);

    private static final String ENTITY_NAME = "entidade";

    private final EntidadeService entidadeService;

    private final EntidadeQueryService entidadeQueryService;

    public EntidadeResource(EntidadeService entidadeService, EntidadeQueryService entidadeQueryService) {
        this.entidadeService = entidadeService;
        this.entidadeQueryService = entidadeQueryService;
    }

    /**
     * POST  /entidades : Create a new entidade.
     *
     * @param entidade the entidade to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entidade, or with status 400 (Bad Request) if the entidade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entidades")
    @Timed
    public ResponseEntity<Entidade> createEntidade(@RequestBody Entidade entidade) throws URISyntaxException {
        log.debug("REST request to save Entidade : {}", entidade);
        if (entidade.getId() != null) {
            throw new BadRequestAlertException("A new entidade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Entidade result = entidadeService.save(entidade);
        return ResponseEntity.created(new URI("/api/entidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entidades : Updates an existing entidade.
     *
     * @param entidade the entidade to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entidade,
     * or with status 400 (Bad Request) if the entidade is not valid,
     * or with status 500 (Internal Server Error) if the entidade couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entidades")
    @Timed
    public ResponseEntity<Entidade> updateEntidade(@RequestBody Entidade entidade) throws URISyntaxException {
        log.debug("REST request to update Entidade : {}", entidade);
        if (entidade.getId() == null) {
            return createEntidade(entidade);
        }
        Entidade result = entidadeService.save(entidade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entidade.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entidades : get all the entidades.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of entidades in body
     */
    @GetMapping("/entidades")
    @Timed
    public ResponseEntity<List<Entidade>> getAllEntidades(EntidadeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Entidades by criteria: {}", criteria);
        Page<Entidade> page = entidadeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/entidades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /entidades/:id : get the "id" entidade.
     *
     * @param id the id of the entidade to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entidade, or with status 404 (Not Found)
     */
    @GetMapping("/entidades/{id}")
    @Timed
    public ResponseEntity<Entidade> getEntidade(@PathVariable Long id) {
        log.debug("REST request to get Entidade : {}", id);
        Entidade entidade = entidadeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entidade));
    }

    /**
     * DELETE  /entidades/:id : delete the "id" entidade.
     *
     * @param id the id of the entidade to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entidades/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntidade(@PathVariable Long id) {
        log.debug("REST request to delete Entidade : {}", id);
        entidadeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
