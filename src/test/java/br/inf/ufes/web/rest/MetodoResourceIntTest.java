package br.inf.ufes.web.rest;

import br.inf.ufes.PrototipoRioDoceApp;

import br.inf.ufes.domain.Metodo;
import br.inf.ufes.domain.DataSource;
import br.inf.ufes.repository.MetodoRepository;
import br.inf.ufes.service.MetodoService;
import br.inf.ufes.web.rest.errors.ExceptionTranslator;
import br.inf.ufes.service.dto.MetodoCriteria;
import br.inf.ufes.service.MetodoQueryService;

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
import java.util.List;

import static br.inf.ufes.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MetodoResource REST controller.
 *
 * @see MetodoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrototipoRioDoceApp.class)
public class MetodoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private MetodoRepository metodoRepository;

    @Autowired
    private MetodoService metodoService;

    @Autowired
    private MetodoQueryService metodoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMetodoMockMvc;

    private Metodo metodo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MetodoResource metodoResource = new MetodoResource(metodoService, metodoQueryService);
        this.restMetodoMockMvc = MockMvcBuilders.standaloneSetup(metodoResource)
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
    public static Metodo createEntity(EntityManager em) {
        Metodo metodo = new Metodo()
            .nome(DEFAULT_NOME);
        return metodo;
    }

    @Before
    public void initTest() {
        metodo = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetodo() throws Exception {
        int databaseSizeBeforeCreate = metodoRepository.findAll().size();

        // Create the Metodo
        restMetodoMockMvc.perform(post("/api/metodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metodo)))
            .andExpect(status().isCreated());

        // Validate the Metodo in the database
        List<Metodo> metodoList = metodoRepository.findAll();
        assertThat(metodoList).hasSize(databaseSizeBeforeCreate + 1);
        Metodo testMetodo = metodoList.get(metodoList.size() - 1);
        assertThat(testMetodo.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createMetodoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metodoRepository.findAll().size();

        // Create the Metodo with an existing ID
        metodo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetodoMockMvc.perform(post("/api/metodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metodo)))
            .andExpect(status().isBadRequest());

        // Validate the Metodo in the database
        List<Metodo> metodoList = metodoRepository.findAll();
        assertThat(metodoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMetodos() throws Exception {
        // Initialize the database
        metodoRepository.saveAndFlush(metodo);

        // Get all the metodoList
        restMetodoMockMvc.perform(get("/api/metodos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metodo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getMetodo() throws Exception {
        // Initialize the database
        metodoRepository.saveAndFlush(metodo);

        // Get the metodo
        restMetodoMockMvc.perform(get("/api/metodos/{id}", metodo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metodo.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getAllMetodosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        metodoRepository.saveAndFlush(metodo);

        // Get all the metodoList where nome equals to DEFAULT_NOME
        defaultMetodoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the metodoList where nome equals to UPDATED_NOME
        defaultMetodoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllMetodosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        metodoRepository.saveAndFlush(metodo);

        // Get all the metodoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultMetodoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the metodoList where nome equals to UPDATED_NOME
        defaultMetodoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllMetodosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        metodoRepository.saveAndFlush(metodo);

        // Get all the metodoList where nome is not null
        defaultMetodoShouldBeFound("nome.specified=true");

        // Get all the metodoList where nome is null
        defaultMetodoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllMetodosByDatasourceIsEqualToSomething() throws Exception {
        // Initialize the database
        DataSource datasource = DataSourceResourceIntTest.createEntity(em);
        em.persist(datasource);
        em.flush();
        metodo.addDatasource(datasource);
        metodoRepository.saveAndFlush(metodo);
        Long datasourceId = datasource.getId();

        // Get all the metodoList where datasource equals to datasourceId
        defaultMetodoShouldBeFound("datasourceId.equals=" + datasourceId);

        // Get all the metodoList where datasource equals to datasourceId + 1
        defaultMetodoShouldNotBeFound("datasourceId.equals=" + (datasourceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMetodoShouldBeFound(String filter) throws Exception {
        restMetodoMockMvc.perform(get("/api/metodos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metodo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMetodoShouldNotBeFound(String filter) throws Exception {
        restMetodoMockMvc.perform(get("/api/metodos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMetodo() throws Exception {
        // Get the metodo
        restMetodoMockMvc.perform(get("/api/metodos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetodo() throws Exception {
        // Initialize the database
        metodoService.save(metodo);

        int databaseSizeBeforeUpdate = metodoRepository.findAll().size();

        // Update the metodo
        Metodo updatedMetodo = metodoRepository.findOne(metodo.getId());
        // Disconnect from session so that the updates on updatedMetodo are not directly saved in db
        em.detach(updatedMetodo);
        updatedMetodo
            .nome(UPDATED_NOME);

        restMetodoMockMvc.perform(put("/api/metodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMetodo)))
            .andExpect(status().isOk());

        // Validate the Metodo in the database
        List<Metodo> metodoList = metodoRepository.findAll();
        assertThat(metodoList).hasSize(databaseSizeBeforeUpdate);
        Metodo testMetodo = metodoList.get(metodoList.size() - 1);
        assertThat(testMetodo.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingMetodo() throws Exception {
        int databaseSizeBeforeUpdate = metodoRepository.findAll().size();

        // Create the Metodo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMetodoMockMvc.perform(put("/api/metodos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metodo)))
            .andExpect(status().isCreated());

        // Validate the Metodo in the database
        List<Metodo> metodoList = metodoRepository.findAll();
        assertThat(metodoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMetodo() throws Exception {
        // Initialize the database
        metodoService.save(metodo);

        int databaseSizeBeforeDelete = metodoRepository.findAll().size();

        // Get the metodo
        restMetodoMockMvc.perform(delete("/api/metodos/{id}", metodo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Metodo> metodoList = metodoRepository.findAll();
        assertThat(metodoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metodo.class);
        Metodo metodo1 = new Metodo();
        metodo1.setId(1L);
        Metodo metodo2 = new Metodo();
        metodo2.setId(metodo1.getId());
        assertThat(metodo1).isEqualTo(metodo2);
        metodo2.setId(2L);
        assertThat(metodo1).isNotEqualTo(metodo2);
        metodo1.setId(null);
        assertThat(metodo1).isNotEqualTo(metodo2);
    }
}
