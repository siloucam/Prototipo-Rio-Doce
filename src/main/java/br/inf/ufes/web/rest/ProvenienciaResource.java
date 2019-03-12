package br.inf.ufes.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.inf.ufes.domain.Proveniencia;
import br.inf.ufes.service.ProvenienciaService;
import br.inf.ufes.web.rest.errors.BadRequestAlertException;
import br.inf.ufes.web.rest.util.HeaderUtil;
import br.inf.ufes.web.rest.util.PaginationUtil;
import br.inf.ufes.service.dto.ProvenienciaCriteria;
import br.inf.ufes.service.ProvenienciaQueryService;
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
 * REST controller for managing Proveniencia.
 */
@RestController
@RequestMapping("/api")
public class ProvenienciaResource {

    private final Logger log = LoggerFactory.getLogger(ProvenienciaResource.class);

    private static final String ENTITY_NAME = "proveniencia";

    private final ProvenienciaService provenienciaService;

    private final ProvenienciaQueryService provenienciaQueryService;

    public ProvenienciaResource(ProvenienciaService provenienciaService, ProvenienciaQueryService provenienciaQueryService) {
        this.provenienciaService = provenienciaService;
        this.provenienciaQueryService = provenienciaQueryService;
    }

    /**
     * POST  /proveniencias : Create a new proveniencia.
     *
     * @param proveniencia the proveniencia to create
     * @return the ResponseEntity with status 201 (Created) and with body the new proveniencia, or with status 400 (Bad Request) if the proveniencia has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/proveniencias")
    @Timed
    public ResponseEntity<Proveniencia> createProveniencia(@RequestBody Proveniencia proveniencia) throws URISyntaxException {
        log.debug("REST request to save Proveniencia : {}", proveniencia);
        if (proveniencia.getId() != null) {
            throw new BadRequestAlertException("A new proveniencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Proveniencia result = provenienciaService.save(proveniencia);
        return ResponseEntity.created(new URI("/api/proveniencias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /proveniencias : Updates an existing proveniencia.
     *
     * @param proveniencia the proveniencia to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated proveniencia,
     * or with status 400 (Bad Request) if the proveniencia is not valid,
     * or with status 500 (Internal Server Error) if the proveniencia couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/proveniencias")
    @Timed
    public ResponseEntity<Proveniencia> updateProveniencia(@RequestBody Proveniencia proveniencia) throws URISyntaxException {
        log.debug("REST request to update Proveniencia : {}", proveniencia);
        if (proveniencia.getId() == null) {
            return createProveniencia(proveniencia);
        }
        Proveniencia result = provenienciaService.save(proveniencia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, proveniencia.getId().toString()))
            .body(result);
    }

    /**
     * GET  /proveniencias : get all the proveniencias.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of proveniencias in body
     */
    @GetMapping("/proveniencias")
    @Timed
    public ResponseEntity<List<Proveniencia>> getAllProveniencias(ProvenienciaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Proveniencias by criteria: {}", criteria);
        Page<Proveniencia> page = provenienciaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/proveniencias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /proveniencias/:id : get the "id" proveniencia.
     *
     * @param id the id of the proveniencia to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the proveniencia, or with status 404 (Not Found)
     */
    @GetMapping("/proveniencias/{id}")
    @Timed
    public ResponseEntity<Proveniencia> getProveniencia(@PathVariable Long id) {
        log.debug("REST request to get Proveniencia : {}", id);
        Proveniencia proveniencia = provenienciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(proveniencia));
    }

    /**
     * DELETE  /proveniencias/:id : delete the "id" proveniencia.
     *
     * @param id the id of the proveniencia to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/proveniencias/{id}")
    @Timed
    public ResponseEntity<Void> deleteProveniencia(@PathVariable Long id) {
        log.debug("REST request to delete Proveniencia : {}", id);
        provenienciaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
