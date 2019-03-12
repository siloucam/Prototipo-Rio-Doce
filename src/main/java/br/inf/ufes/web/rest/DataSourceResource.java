package br.inf.ufes.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.inf.ufes.domain.DataSource;
import br.inf.ufes.service.DataSourceService;
import br.inf.ufes.web.rest.errors.BadRequestAlertException;
import br.inf.ufes.web.rest.util.HeaderUtil;
import br.inf.ufes.web.rest.util.PaginationUtil;
import br.inf.ufes.service.dto.DataSourceCriteria;
import br.inf.ufes.service.DataSourceQueryService;
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
 * REST controller for managing DataSource.
 */
@RestController
@RequestMapping("/api")
public class DataSourceResource {

    private final Logger log = LoggerFactory.getLogger(DataSourceResource.class);

    private static final String ENTITY_NAME = "dataSource";

    private final DataSourceService dataSourceService;

    private final DataSourceQueryService dataSourceQueryService;

    public DataSourceResource(DataSourceService dataSourceService, DataSourceQueryService dataSourceQueryService) {
        this.dataSourceService = dataSourceService;
        this.dataSourceQueryService = dataSourceQueryService;
    }

    /**
     * POST  /data-sources : Create a new dataSource.
     *
     * @param dataSource the dataSource to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataSource, or with status 400 (Bad Request) if the dataSource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-sources")
    @Timed
    public ResponseEntity<DataSource> createDataSource(@RequestBody DataSource dataSource) throws URISyntaxException {
        log.debug("REST request to save DataSource : {}", dataSource);
        if (dataSource.getId() != null) {
            throw new BadRequestAlertException("A new dataSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataSource result = dataSourceService.save(dataSource);
        return ResponseEntity.created(new URI("/api/data-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-sources : Updates an existing dataSource.
     *
     * @param dataSource the dataSource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataSource,
     * or with status 400 (Bad Request) if the dataSource is not valid,
     * or with status 500 (Internal Server Error) if the dataSource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-sources")
    @Timed
    public ResponseEntity<DataSource> updateDataSource(@RequestBody DataSource dataSource) throws URISyntaxException {
        log.debug("REST request to update DataSource : {}", dataSource);
        if (dataSource.getId() == null) {
            return createDataSource(dataSource);
        }
        DataSource result = dataSourceService.save(dataSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataSource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-sources : get all the dataSources.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of dataSources in body
     */
    @GetMapping("/data-sources")
    @Timed
    public ResponseEntity<List<DataSource>> getAllDataSources(DataSourceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DataSources by criteria: {}", criteria);
        Page<DataSource> page = dataSourceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/data-sources");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /data-sources/:id : get the "id" dataSource.
     *
     * @param id the id of the dataSource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataSource, or with status 404 (Not Found)
     */
    @GetMapping("/data-sources/{id}")
    @Timed
    public ResponseEntity<DataSource> getDataSource(@PathVariable Long id) {
        log.debug("REST request to get DataSource : {}", id);
        DataSource dataSource = dataSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dataSource));
    }

    /**
     * DELETE  /data-sources/:id : delete the "id" dataSource.
     *
     * @param id the id of the dataSource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-sources/{id}")
    @Timed
    public ResponseEntity<Void> deleteDataSource(@PathVariable Long id) {
        log.debug("REST request to delete DataSource : {}", id);
        dataSourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
