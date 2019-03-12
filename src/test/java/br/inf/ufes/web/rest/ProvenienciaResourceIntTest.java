package br.inf.ufes.web.rest;

import br.inf.ufes.PrototipoRioDoceApp;

import br.inf.ufes.domain.Proveniencia;
import br.inf.ufes.domain.DataSource;
import br.inf.ufes.repository.ProvenienciaRepository;
import br.inf.ufes.service.ProvenienciaService;
import br.inf.ufes.web.rest.errors.ExceptionTranslator;
import br.inf.ufes.service.dto.ProvenienciaCriteria;
import br.inf.ufes.service.ProvenienciaQueryService;

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
 * Test class for the ProvenienciaResource REST controller.
 *
 * @see ProvenienciaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrototipoRioDoceApp.class)
public class ProvenienciaResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private ProvenienciaRepository provenienciaRepository;

    @Autowired
    private ProvenienciaService provenienciaService;

    @Autowired
    private ProvenienciaQueryService provenienciaQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProvenienciaMockMvc;

    private Proveniencia proveniencia;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProvenienciaResource provenienciaResource = new ProvenienciaResource(provenienciaService, provenienciaQueryService);
        this.restProvenienciaMockMvc = MockMvcBuilders.standaloneSetup(provenienciaResource)
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
    public static Proveniencia createEntity(EntityManager em) {
        Proveniencia proveniencia = new Proveniencia()
            .nome(DEFAULT_NOME);
        return proveniencia;
    }

    @Before
    public void initTest() {
        proveniencia = createEntity(em);
    }

    @Test
    @Transactional
    public void createProveniencia() throws Exception {
        int databaseSizeBeforeCreate = provenienciaRepository.findAll().size();

        // Create the Proveniencia
        restProvenienciaMockMvc.perform(post("/api/proveniencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proveniencia)))
            .andExpect(status().isCreated());

        // Validate the Proveniencia in the database
        List<Proveniencia> provenienciaList = provenienciaRepository.findAll();
        assertThat(provenienciaList).hasSize(databaseSizeBeforeCreate + 1);
        Proveniencia testProveniencia = provenienciaList.get(provenienciaList.size() - 1);
        assertThat(testProveniencia.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createProvenienciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = provenienciaRepository.findAll().size();

        // Create the Proveniencia with an existing ID
        proveniencia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvenienciaMockMvc.perform(post("/api/proveniencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proveniencia)))
            .andExpect(status().isBadRequest());

        // Validate the Proveniencia in the database
        List<Proveniencia> provenienciaList = provenienciaRepository.findAll();
        assertThat(provenienciaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProveniencias() throws Exception {
        // Initialize the database
        provenienciaRepository.saveAndFlush(proveniencia);

        // Get all the provenienciaList
        restProvenienciaMockMvc.perform(get("/api/proveniencias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proveniencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getProveniencia() throws Exception {
        // Initialize the database
        provenienciaRepository.saveAndFlush(proveniencia);

        // Get the proveniencia
        restProvenienciaMockMvc.perform(get("/api/proveniencias/{id}", proveniencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proveniencia.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getAllProvenienciasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        provenienciaRepository.saveAndFlush(proveniencia);

        // Get all the provenienciaList where nome equals to DEFAULT_NOME
        defaultProvenienciaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the provenienciaList where nome equals to UPDATED_NOME
        defaultProvenienciaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllProvenienciasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        provenienciaRepository.saveAndFlush(proveniencia);

        // Get all the provenienciaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultProvenienciaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the provenienciaList where nome equals to UPDATED_NOME
        defaultProvenienciaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllProvenienciasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        provenienciaRepository.saveAndFlush(proveniencia);

        // Get all the provenienciaList where nome is not null
        defaultProvenienciaShouldBeFound("nome.specified=true");

        // Get all the provenienciaList where nome is null
        defaultProvenienciaShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllProvenienciasByDatasourceIsEqualToSomething() throws Exception {
        // Initialize the database
        DataSource datasource = DataSourceResourceIntTest.createEntity(em);
        em.persist(datasource);
        em.flush();
        proveniencia.addDatasource(datasource);
        provenienciaRepository.saveAndFlush(proveniencia);
        Long datasourceId = datasource.getId();

        // Get all the provenienciaList where datasource equals to datasourceId
        defaultProvenienciaShouldBeFound("datasourceId.equals=" + datasourceId);

        // Get all the provenienciaList where datasource equals to datasourceId + 1
        defaultProvenienciaShouldNotBeFound("datasourceId.equals=" + (datasourceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProvenienciaShouldBeFound(String filter) throws Exception {
        restProvenienciaMockMvc.perform(get("/api/proveniencias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proveniencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProvenienciaShouldNotBeFound(String filter) throws Exception {
        restProvenienciaMockMvc.perform(get("/api/proveniencias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProveniencia() throws Exception {
        // Get the proveniencia
        restProvenienciaMockMvc.perform(get("/api/proveniencias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProveniencia() throws Exception {
        // Initialize the database
        provenienciaService.save(proveniencia);

        int databaseSizeBeforeUpdate = provenienciaRepository.findAll().size();

        // Update the proveniencia
        Proveniencia updatedProveniencia = provenienciaRepository.findOne(proveniencia.getId());
        // Disconnect from session so that the updates on updatedProveniencia are not directly saved in db
        em.detach(updatedProveniencia);
        updatedProveniencia
            .nome(UPDATED_NOME);

        restProvenienciaMockMvc.perform(put("/api/proveniencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProveniencia)))
            .andExpect(status().isOk());

        // Validate the Proveniencia in the database
        List<Proveniencia> provenienciaList = provenienciaRepository.findAll();
        assertThat(provenienciaList).hasSize(databaseSizeBeforeUpdate);
        Proveniencia testProveniencia = provenienciaList.get(provenienciaList.size() - 1);
        assertThat(testProveniencia.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingProveniencia() throws Exception {
        int databaseSizeBeforeUpdate = provenienciaRepository.findAll().size();

        // Create the Proveniencia

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProvenienciaMockMvc.perform(put("/api/proveniencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proveniencia)))
            .andExpect(status().isCreated());

        // Validate the Proveniencia in the database
        List<Proveniencia> provenienciaList = provenienciaRepository.findAll();
        assertThat(provenienciaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProveniencia() throws Exception {
        // Initialize the database
        provenienciaService.save(proveniencia);

        int databaseSizeBeforeDelete = provenienciaRepository.findAll().size();

        // Get the proveniencia
        restProvenienciaMockMvc.perform(delete("/api/proveniencias/{id}", proveniencia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Proveniencia> provenienciaList = provenienciaRepository.findAll();
        assertThat(provenienciaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proveniencia.class);
        Proveniencia proveniencia1 = new Proveniencia();
        proveniencia1.setId(1L);
        Proveniencia proveniencia2 = new Proveniencia();
        proveniencia2.setId(proveniencia1.getId());
        assertThat(proveniencia1).isEqualTo(proveniencia2);
        proveniencia2.setId(2L);
        assertThat(proveniencia1).isNotEqualTo(proveniencia2);
        proveniencia1.setId(null);
        assertThat(proveniencia1).isNotEqualTo(proveniencia2);
    }
}
