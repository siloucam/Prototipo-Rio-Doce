package br.inf.ufes.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.inf.ufes.domain.Metodo;
import br.inf.ufes.service.MetodoService;
import br.inf.ufes.web.rest.errors.BadRequestAlertException;
import br.inf.ufes.web.rest.util.HeaderUtil;
import br.inf.ufes.web.rest.util.PaginationUtil;
import br.inf.ufes.service.dto.MetodoCriteria;
import br.inf.ufes.service.MetodoQueryService;
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
 * REST controller for managing Metodo.
 */
@RestController
@RequestMapping("/api")
public class MetodoResource {

    private final Logger log = LoggerFactory.getLogger(MetodoResource.class);

    private static final String ENTITY_NAME = "metodo";

    private final MetodoService metodoService;

    private final MetodoQueryService metodoQueryService;

    public MetodoResource(MetodoService metodoService, MetodoQueryService metodoQueryService) {
        this.metodoService = metodoService;
        this.metodoQueryService = metodoQueryService;
    }

    /**
     * POST  /metodos : Create a new metodo.
     *
     * @param metodo the metodo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metodo, or with status 400 (Bad Request) if the metodo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/metodos")
    @Timed
    public ResponseEntity<Metodo> createMetodo(@RequestBody Metodo metodo) throws URISyntaxException {
        log.debug("REST request to save Metodo : {}", metodo);
        if (metodo.getId() != null) {
            throw new BadRequestAlertException("A new metodo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Metodo result = metodoService.save(metodo);
        return ResponseEntity.created(new URI("/api/metodos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metodos : Updates an existing metodo.
     *
     * @param metodo the metodo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metodo,
     * or with status 400 (Bad Request) if the metodo is not valid,
     * or with status 500 (Internal Server Error) if the metodo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/metodos")
    @Timed
    public ResponseEntity<Metodo> updateMetodo(@RequestBody Metodo metodo) throws URISyntaxException {
        log.debug("REST request to update Metodo : {}", metodo);
        if (metodo.getId() == null) {
            return createMetodo(metodo);
        }
        Metodo result = metodoService.save(metodo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, metodo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /metodos : get all the metodos.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of metodos in body
     */
    @GetMapping("/metodos")
    @Timed
    public ResponseEntity<List<Metodo>> getAllMetodos(MetodoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Metodos by criteria: {}", criteria);
        Page<Metodo> page = metodoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/metodos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /metodos/:id : get the "id" metodo.
     *
     * @param id the id of the metodo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metodo, or with status 404 (Not Found)
     */
    @GetMapping("/metodos/{id}")
    @Timed
    public ResponseEntity<Metodo> getMetodo(@PathVariable Long id) {
        log.debug("REST request to get Metodo : {}", id);
        Metodo metodo = metodoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(metodo));
    }

    /**
     * DELETE  /metodos/:id : delete the "id" metodo.
     *
     * @param id the id of the metodo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/metodos/{id}")
    @Timed
    public ResponseEntity<Void> deleteMetodo(@PathVariable Long id) {
        log.debug("REST request to delete Metodo : {}", id);
        metodoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
