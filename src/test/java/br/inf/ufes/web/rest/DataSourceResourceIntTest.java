package br.inf.ufes.web.rest;

import br.inf.ufes.PrototipoRioDoceApp;

import br.inf.ufes.domain.DataSource;
import br.inf.ufes.domain.Metodo;
import br.inf.ufes.domain.Entidade;
import br.inf.ufes.domain.Atividade;
import br.inf.ufes.domain.Propriedade;
import br.inf.ufes.domain.Proveniencia;
import br.inf.ufes.repository.DataSourceRepository;
import br.inf.ufes.service.DataSourceService;
import br.inf.ufes.web.rest.errors.ExceptionTranslator;
import br.inf.ufes.service.dto.DataSourceCriteria;
import br.inf.ufes.service.DataSourceQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static br.inf.ufes.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DataSourceResource REST controller.
 *
 * @see DataSourceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrototipoRioDoceApp.class)
public class DataSourceResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DTINICIAL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DTINICIAL = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DTFINAL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DTFINAL = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private DataSourceQueryService dataSourceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataSourceMockMvc;

    private DataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataSourceResource dataSourceResource = new DataSourceResource(dataSourceService, dataSourceQueryService);
        this.restDataSourceMockMvc = MockMvcBuilders.standaloneSetup(dataSourceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataSource createEntity(EntityManager em) {
        DataSource dataSource = new DataSource()
            .nome(DEFAULT_NOME)
            .path(DEFAULT_PATH)
            .dtinicial(DEFAULT_DTINICIAL)
            .dtfinal(DEFAULT_DTFINAL);
        return dataSource;
    }

    @Before
    public void initTest() {
        dataSource = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataSource() throws Exception {
        int databaseSizeBeforeCreate = dataSourceRepository.findAll().size();

        // Create the DataSource
        restDataSourceMockMvc.perform(post("/api/data-sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataSource)))
            .andExpect(status().isCreated());

        // Validate the DataSource in the database
        List<DataSource> dataSourceList = dataSourceRepository.findAll();
        assertThat(dataSourceList).hasSize(databaseSizeBeforeCreate + 1);
        DataSource testDataSource = dataSourceList.get(dataSourceList.size() - 1);
        assertThat(testDataSource.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDataSource.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testDataSource.getDtinicial()).isEqualTo(DEFAULT_DTINICIAL);
        assertThat(testDataSource.getDtfinal()).isEqualTo(DEFAULT_DTFINAL);
    }

    @Test
    @Transactional
    public void createDataSourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataSourceRepository.findAll().size();

        // Create the DataSource with an existing ID
        dataSource.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataSourceMockMvc.perform(post("/api/data-sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataSource)))
            .andExpect(status().isBadRequest());

        // Validate the DataSource in the database
        List<DataSource> dataSourceList = dataSourceRepository.findAll();
        assertThat(dataSourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataSources() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList
        restDataSourceMockMvc.perform(get("/api/data-sources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].dtinicial").value(hasItem(DEFAULT_DTINICIAL.toString())))
            .andExpect(jsonPath("$.[*].dtfinal").value(hasItem(DEFAULT_DTFINAL.toString())));
    }

    @Test
    @Transactional
    public void getDataSource() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get the dataSource
        restDataSourceMockMvc.perform(get("/api/data-sources/{id}", dataSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataSource.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.dtinicial").value(DEFAULT_DTINICIAL.toString()))
            .andExpect(jsonPath("$.dtfinal").value(DEFAULT_DTFINAL.toString()));
    }

    @Test
    @Transactional
    public void getAllDataSourcesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where nome equals to DEFAULT_NOME
        defaultDataSourceShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the dataSourceList where nome equals to UPDATED_NOME
        defaultDataSourceShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDataSourcesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultDataSourceShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the dataSourceList where nome equals to UPDATED_NOME
        defaultDataSourceShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDataSourcesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where nome is not null
        defaultDataSourceShouldBeFound("nome.specified=true");

        // Get all the dataSourceList where nome is null
        defaultDataSourceShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataSourcesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where path equals to DEFAULT_PATH
        defaultDataSourceShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the dataSourceList where path equals to UPDATED_PATH
        defaultDataSourceShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllDataSourcesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where path in DEFAULT_PATH or UPDATED_PATH
        defaultDataSourceShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the dataSourceList where path equals to UPDATED_PATH
        defaultDataSourceShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllDataSourcesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where path is not null
        defaultDataSourceShouldBeFound("path.specified=true");

        // Get all the dataSourceList where path is null
        defaultDataSourceShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataSourcesByDtinicialIsEqualToSomething() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where dtinicial equals to DEFAULT_DTINICIAL
        defaultDataSourceShouldBeFound("dtinicial.equals=" + DEFAULT_DTINICIAL);

        // Get all the dataSourceList where dtinicial equals to UPDATED_DTINICIAL
        defaultDataSourceShouldNotBeFound("dtinicial.equals=" + UPDATED_DTINICIAL);
    }

    @Test
    @Transactional
    public void getAllDataSourcesByDtinicialIsInShouldWork() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where dtinicial in DEFAULT_DTINICIAL or UPDATED_DTINICIAL
        defaultDataSourceShouldBeFound("dtinicial.in=" + DEFAULT_DTINICIAL + "," + UPDATED_DTINICIAL);

        // Get all the dataSourceList where dtinicial equals to UPDATED_DTINICIAL
        defaultDataSourceShouldNotBeFound("dtinicial.in=" + UPDATED_DTINICIAL);
    }

    @Test
    @Transactional
    public void getAllDataSourcesByDtinicialIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where dtinicial is not null
        defaultDataSourceShouldBeFound("dtinicial.specified=true");

        // Get all the dataSourceList where dtinicial is null
        defaultDataSourceShouldNotBeFound("dtinicial.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataSourcesByDtinicialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where dtinicial greater than or equals to DEFAULT_DTINICIAL
        defaultDataSourceShouldBeFound("dtinicial.greaterOrEqualThan=" + DEFAULT_DTINICIAL);

        // Get all the dataSourceList where dtinicial greater than or equals to UPDATED_DTINICIAL
        defaultDataSourceShouldNotBeFound("dtinicial.greaterOrEqualThan=" + UPDATED_DTINICIAL);
    }

    @Test
    @Transactional
    public void getAllDataSourcesByDtinicialIsLessThanSomething() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where dtinicial less than or equals to DEFAULT_DTINICIAL
        defaultDataSourceShouldNotBeFound("dtinicial.lessThan=" + DEFAULT_DTINICIAL);

        // Get all the dataSourceList where dtinicial less than or equals to UPDATED_DTINICIAL
        defaultDataSourceShouldBeFound("dtinicial.lessThan=" + UPDATED_DTINICIAL);
    }


    @Test
    @Transactional
    public void getAllDataSourcesByDtfinalIsEqualToSomething() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where dtfinal equals to DEFAULT_DTFINAL
        defaultDataSourceShouldBeFound("dtfinal.equals=" + DEFAULT_DTFINAL);

        // Get all the dataSourceList where dtfinal equals to UPDATED_DTFINAL
        defaultDataSourceShouldNotBeFound("dtfinal.equals=" + UPDATED_DTFINAL);
    }

    @Test
    @Transactional
    public void getAllDataSourcesByDtfinalIsInShouldWork() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where dtfinal in DEFAULT_DTFINAL or UPDATED_DTFINAL
        defaultDataSourceShouldBeFound("dtfinal.in=" + DEFAULT_DTFINAL + "," + UPDATED_DTFINAL);

        // Get all the dataSourceList where dtfinal equals to UPDATED_DTFINAL
        defaultDataSourceShouldNotBeFound("dtfinal.in=" + UPDATED_DTFINAL);
    }

    @Test
    @Transactional
    public void getAllDataSourcesByDtfinalIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where dtfinal is not null
        defaultDataSourceShouldBeFound("dtfinal.specified=true");

        // Get all the dataSourceList where dtfinal is null
        defaultDataSourceShouldNotBeFound("dtfinal.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataSourcesByDtfinalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where dtfinal greater than or equals to DEFAULT_DTFINAL
        defaultDataSourceShouldBeFound("dtfinal.greaterOrEqualThan=" + DEFAULT_DTFINAL);

        // Get all the dataSourceList where dtfinal greater than or equals to UPDATED_DTFINAL
        defaultDataSourceShouldNotBeFound("dtfinal.greaterOrEqualThan=" + UPDATED_DTFINAL);
    }

    @Test
    @Transactional
    public void getAllDataSourcesByDtfinalIsLessThanSomething() throws Exception {
        // Initialize the database
        dataSourceRepository.saveAndFlush(dataSource);

        // Get all the dataSourceList where dtfinal less than or equals to DEFAULT_DTFINAL
        defaultDataSourceShouldNotBeFound("dtfinal.lessThan=" + DEFAULT_DTFINAL);

        // Get all the dataSourceList where dtfinal less than or equals to UPDATED_DTFINAL
        defaultDataSourceShouldBeFound("dtfinal.lessThan=" + UPDATED_DTFINAL);
    }


    @Test
    @Transactional
    public void getAllDataSourcesByMetodoIsEqualToSomething() throws Exception {
        // Initialize the database
        Metodo metodo = MetodoResourceIntTest.createEntity(em);
        em.persist(metodo);
        em.flush();
        dataSource.addMetodo(metodo);
        dataSourceRepository.saveAndFlush(dataSource);
        Long metodoId = metodo.getId();

        // Get all the dataSourceList where metodo equals to metodoId
        defaultDataSourceShouldBeFound("metodoId.equals=" + metodoId);

        // Get all the dataSourceList where metodo equals to metodoId + 1
        defaultDataSourceShouldNotBeFound("metodoId.equals=" + (metodoId + 1));
    }


    @Test
    @Transactional
    public void getAllDataSourcesByEntidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        Entidade entidade = EntidadeResourceIntTest.createEntity(em);
        em.persist(entidade);
        em.flush();
        dataSource.addEntidade(entidade);
        dataSourceRepository.saveAndFlush(dataSource);
        Long entidadeId = entidade.getId();

        // Get all the dataSourceList where entidade equals to entidadeId
        defaultDataSourceShouldBeFound("entidadeId.equals=" + entidadeId);

        // Get all the dataSourceList where entidade equals to entidadeId + 1
        defaultDataSourceShouldNotBeFound("entidadeId.equals=" + (entidadeId + 1));
    }


    @Test
    @Transactional
    public void getAllDataSourcesByAtividadeIsEqualToSomething() throws Exception {
        // Initialize the database
        Atividade atividade = AtividadeResourceIntTest.createEntity(em);
        em.persist(atividade);
        em.flush();
        dataSource.addAtividade(atividade);
        dataSourceRepository.saveAndFlush(dataSource);
        Long atividadeId = atividade.getId();

        // Get all the dataSourceList where atividade equals to atividadeId
        defaultDataSourceShouldBeFound("atividadeId.equals=" + atividadeId);

        // Get all the dataSourceList where atividade equals to atividadeId + 1
        defaultDataSourceShouldNotBeFound("atividadeId.equals=" + (atividadeId + 1));
    }


    @Test
    @Transactional
    public void getAllDataSourcesByPropriedadeIsEqualToSomething() throws Exception {
        // Initialize the database
        Propriedade propriedade = PropriedadeResourceIntTest.createEntity(em);
        em.persist(propriedade);
        em.flush();
        dataSource.addPropriedade(propriedade);
        dataSourceRepository.saveAndFlush(dataSource);
        Long propriedadeId = propriedade.getId();

        // Get all the dataSourceList where propriedade equals to propriedadeId
        defaultDataSourceShouldBeFound("propriedadeId.equals=" + propriedadeId);

        // Get all the dataSourceList where propriedade equals to propriedadeId + 1
        defaultDataSourceShouldNotBeFound("propriedadeId.equals=" + (propriedadeId + 1));
    }


    @Test
    @Transactional
    public void getAllDataSourcesByProvenienciaIsEqualToSomething() throws Exception {
        // Initialize the database
        Proveniencia proveniencia = ProvenienciaResourceIntTest.createEntity(em);
        em.persist(proveniencia);
        em.flush();
        dataSource.addProveniencia(proveniencia);
        dataSourceRepository.saveAndFlush(dataSource);
        Long provenienciaId = proveniencia.getId();

        // Get all the dataSourceList where proveniencia equals to provenienciaId
        defaultDataSourceShouldBeFound("provenienciaId.equals=" + provenienciaId);

        // Get all the dataSourceList where proveniencia equals to provenienciaId + 1
        defaultDataSourceShouldNotBeFound("provenienciaId.equals=" + (provenienciaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDataSourceShouldBeFound(String filter) throws Exception {
        restDataSourceMockMvc.perform(get("/api/data-sources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].dtinicial").value(hasItem(DEFAULT_DTINICIAL.toString())))
            .andExpect(jsonPath("$.[*].dtfinal").value(hasItem(DEFAULT_DTFINAL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDataSourceShouldNotBeFound(String filter) throws Exception {
        restDataSourceMockMvc.perform(get("/api/data-sources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingDataSource() throws Exception {
        // Get the dataSource
        restDataSourceMockMvc.perform(get("/api/data-sources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataSource() throws Exception {
        // Initialize the database
        dataSourceService.save(dataSource);

        int databaseSizeBeforeUpdate = dataSourceRepository.findAll().size();

        // Update the dataSource
        DataSource updatedDataSource = dataSourceRepository.findOne(dataSource.getId());
        // Disconnect from session so that the updates on updatedDataSource are not directly saved in db
        em.detach(updatedDataSource);
        updatedDataSource
            .nome(UPDATED_NOME)
            .path(UPDATED_PATH)
            .dtinicial(UPDATED_DTINICIAL)
            .dtfinal(UPDATED_DTFINAL);

        restDataSourceMockMvc.perform(put("/api/data-sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataSource)))
            .andExpect(status().isOk());

        // Validate the DataSource in the database
        List<DataSource> dataSourceList = dataSourceRepository.findAll();
        assertThat(dataSourceList).hasSize(databaseSizeBeforeUpdate);
        DataSource testDataSource = dataSourceList.get(dataSourceList.size() - 1);
        assertThat(testDataSource.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDataSource.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testDataSource.getDtinicial()).isEqualTo(UPDATED_DTINICIAL);
        assertThat(testDataSource.getDtfinal()).isEqualTo(UPDATED_DTFINAL);
    }

    @Test
    @Transactional
    public void updateNonExistingDataSource() throws Exception {
        int databaseSizeBeforeUpdate = dataSourceRepository.findAll().size();

        // Create the DataSource

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataSourceMockMvc.perform(put("/api/data-sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataSource)))
            .andExpect(status().isCreated());

        // Validate the DataSource in the database
        List<DataSource> dataSourceList = dataSourceRepository.findAll();
        assertThat(dataSourceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataSource() throws Exception {
        // Initialize the database
        dataSourceService.save(dataSource);

        int databaseSizeBeforeDelete = dataSourceRepository.findAll().size();

        // Get the dataSource
        restDataSourceMockMvc.perform(delete("/api/data-sources/{id}", dataSource.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataSource> dataSourceList = dataSourceRepository.findAll();
        assertThat(dataSourceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataSource.class);
        DataSource dataSource1 = new DataSource();
        dataSource1.setId(1L);
        DataSource dataSource2 = new DataSource();
        dataSource2.setId(dataSource1.getId());
        assertThat(dataSource1).isEqualTo(dataSource2);
        dataSource2.setId(2L);
        assertThat(dataSource1).isNotEqualTo(dataSource2);
        dataSource1.setId(null);
        assertThat(dataSource1).isNotEqualTo(dataSource2);
    }
}
