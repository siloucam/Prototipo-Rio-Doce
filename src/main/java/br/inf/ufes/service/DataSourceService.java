package br.inf.ufes.service;

import br.inf.ufes.domain.DataSource;
import br.inf.ufes.repository.DataSourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing DataSource.
 */
@Service
@Transactional
public class DataSourceService {

    private final Logger log = LoggerFactory.getLogger(DataSourceService.class);

    private final DataSourceRepository dataSourceRepository;

    public DataSourceService(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    /**
     * Save a dataSource.
     *
     * @param dataSource the entity to save
     * @return the persisted entity
     */
    public DataSource save(DataSource dataSource) {
        log.debug("Request to save DataSource : {}", dataSource);
        return dataSourceRepository.save(dataSource);
    }

    /**
     * Get all the dataSources.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DataSource> findAll(Pageable pageable) {
        log.debug("Request to get all DataSources");
        return dataSourceRepository.findAll(pageable);
    }

    /**
     * Get one dataSource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public DataSource findOne(Long id) {
        log.debug("Request to get DataSource : {}", id);
        return dataSourceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the dataSource by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DataSource : {}", id);
        dataSourceRepository.delete(id);
    }
}
