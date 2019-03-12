package br.inf.ufes.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.inf.ufes.domain.Propriedade;
import br.inf.ufes.service.PropriedadeService;
import br.inf.ufes.web.rest.errors.BadRequestAlertException;
import br.inf.ufes.web.rest.util.HeaderUtil;
import br.inf.ufes.web.rest.util.PaginationUtil;
import br.inf.ufes.service.dto.PropriedadeCriteria;
import br.inf.ufes.service.PropriedadeQueryService;
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
 * REST controller for managing Propriedade.
 */
@RestController
@RequestMapping("/api")
public class PropriedadeResource {

    private final Logger log = LoggerFactory.getLogger(PropriedadeResource.class);

    private static final String ENTITY_NAME = "propriedade";

    private final PropriedadeService propriedadeService;

    private final PropriedadeQueryService propriedadeQueryService;

    public PropriedadeResource(PropriedadeService propriedadeService, PropriedadeQueryService propriedadeQueryService) {
        this.propriedadeService = propriedadeService;
        this.propriedadeQueryService = propriedadeQueryService;
    }

    /**
     * POST  /propriedades : Create a new propriedade.
     *
     * @param propriedade the propriedade to create
     * @return the ResponseEntity with status 201 (Created) and with body the new propriedade, or with status 400 (Bad Request) if the propriedade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/propriedades")
    @Timed
    public ResponseEntity<Propriedade> createPropriedade(@RequestBody Propriedade propriedade) throws URISyntaxException {
        log.debug("REST request to save Propriedade : {}", propriedade);
        if (propriedade.getId() != null) {
            throw new BadRequestAlertException("A new propriedade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Propriedade result = propriedadeService.save(propriedade);
        return ResponseEntity.created(new URI("/api/propriedades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /propriedades : Updates an existing propriedade.
     *
     * @param propriedade the propriedade to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated propriedade,
     * or with status 400 (Bad Request) if the propriedade is not valid,
     * or with status 500 (Internal Server Error) if the propriedade couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/propriedades")
    @Timed
    public ResponseEntity<Propriedade> updatePropriedade(@RequestBody Propriedade propriedade) throws URISyntaxException {
        log.debug("REST request to update Propriedade : {}", propriedade);
        if (propriedade.getId() == null) {
            return createPropriedade(propriedade);
        }
        Propriedade result = propriedadeService.save(propriedade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, propriedade.getId().toString()))
            .body(result);
    }

    /**
     * GET  /propriedades : get all the propriedades.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of propriedades in body
     */
    @GetMapping("/propriedades")
    @Timed
    public ResponseEntity<List<Propriedade>> getAllPropriedades(PropriedadeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Propriedades by criteria: {}", criteria);
        Page<Propriedade> page = propriedadeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/propriedades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /propriedades/:id : get the "id" propriedade.
     *
     * @param id the id of the propriedade to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the propriedade, or with status 404 (Not Found)
     */
    @GetMapping("/propriedades/{id}")
    @Timed
    public ResponseEntity<Propriedade> getPropriedade(@PathVariable Long id) {
        log.debug("REST request to get Propriedade : {}", id);
        Propriedade propriedade = propriedadeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(propriedade));
    }

    /**
     * DELETE  /propriedades/:id : delete the "id" propriedade.
     *
     * @param id the id of the propriedade to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/propriedades/{id}")
    @Timed
    public ResponseEntity<Void> deletePropriedade(@PathVariable Long id) {
        log.debug("REST request to delete Propriedade : {}", id);
        propriedadeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
