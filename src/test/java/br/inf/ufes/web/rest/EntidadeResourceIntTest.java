package br.inf.ufes.web.rest;

import br.inf.ufes.PrototipoRioDoceApp;

import br.inf.ufes.domain.Entidade;
import br.inf.ufes.domain.DataSource;
import br.inf.ufes.repository.EntidadeRepository;
import br.inf.ufes.service.EntidadeService;
import br.inf.ufes.web.rest.errors.ExceptionTranslator;
import br.inf.ufes.service.dto.EntidadeCriteria;
import br.inf.ufes.service.EntidadeQueryService;

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
 * Test class for the EntidadeResource REST controller.
 *
 * @see EntidadeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrototipoRioDoceApp.class)
public class EntidadeResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private EntidadeRepository entidadeRepository;

    @Autowired
    private EntidadeService entidadeService;

    @Autowired
    private EntidadeQueryService entidadeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntidadeMockMvc;

    private Entidade entidade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EntidadeResource entidadeResource = new EntidadeResource(entidadeService, entidadeQueryService);
        this.restEntidadeMockMvc = MockMvcBuilders.standaloneSetup(entidadeResource)
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
    public static Entidade createEntity(EntityManager em) {
        Entidade entidade = new Entidade()
            .nome(DEFAULT_NOME);
        return entidade;
    }

    @Before
    public void initTest() {
        entidade = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntidade() throws Exception {
        int databaseSizeBeforeCreate = entidadeRepository.findAll().size();

        // Create the Entidade
        restEntidadeMockMvc.perform(post("/api/entidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entidade)))
            .andExpect(status().isCreated());

        // Validate the Entidade in the database
        List<Entidade> entidadeList = entidadeRepository.findAll();
        assertThat(entidadeList).hasSize(databaseSizeBeforeCreate + 1);
        Entidade testEntidade = entidadeList.get(entidadeList.size() - 1);
        assertThat(testEntidade.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createEntidadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entidadeRepository.findAll().size();

        // Create the Entidade with an existing ID
        entidade.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntidadeMockMvc.perform(post("/api/entidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entidade)))
            .andExpect(status().isBadRequest());

        // Validate the Entidade in the database
        List<Entidade> entidadeList = entidadeRepository.findAll();
        assertThat(entidadeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntidades() throws Exception {
        // Initialize the database
        entidadeRepository.saveAndFlush(entidade);

        // Get all the entidadeList
        restEntidadeMockMvc.perform(get("/api/entidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getEntidade() throws Exception {
        // Initialize the database
        entidadeRepository.saveAndFlush(entidade);

        // Get the entidade
        restEntidadeMockMvc.perform(get("/api/entidades/{id}", entidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entidade.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getAllEntidadesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        entidadeRepository.saveAndFlush(entidade);

        // Get all the entidadeList where nome equals to DEFAULT_NOME
        defaultEntidadeShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the entidadeList where nome equals to UPDATED_NOME
        defaultEntidadeShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllEntidadesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        entidadeRepository.saveAndFlush(entidade);

        // Get all the entidadeList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultEntidadeShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the entidadeList where nome equals to UPDATED_NOME
        defaultEntidadeShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllEntidadesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        entidadeRepository.saveAndFlush(entidade);

        // Get all the entidadeList where nome is not null
        defaultEntidadeShouldBeFound("nome.specified=true");

        // Get all the entidadeList where nome is null
        defaultEntidadeShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllEntidadesByDatasourceIsEqualToSomething() throws Exception {
        // Initialize the database
        DataSource datasource = DataSourceResourceIntTest.createEntity(em);
        em.persist(datasource);
        em.flush();
        entidade.addDatasource(datasource);
        entidadeRepository.saveAndFlush(entidade);
        Long datasourceId = datasource.getId();

        // Get all the entidadeList where datasource equals to datasourceId
        defaultEntidadeShouldBeFound("datasourceId.equals=" + datasourceId);

        // Get all the entidadeList where datasource equals to datasourceId + 1
        defaultEntidadeShouldNotBeFound("datasourceId.equals=" + (datasourceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEntidadeShouldBeFound(String filter) throws Exception {
        restEntidadeMockMvc.perform(get("/api/entidades?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEntidadeShouldNotBeFound(String filter) throws Exception {
        restEntidadeMockMvc.perform(get("/api/entidades?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingEntidade() throws Exception {
        // Get the entidade
        restEntidadeMockMvc.perform(get("/api/entidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntidade() throws Exception {
        // Initialize the database
        entidadeService.save(entidade);

        int databaseSizeBeforeUpdate = entidadeRepository.findAll().size();

        // Update the entidade
        Entidade updatedEntidade = entidadeRepository.findOne(entidade.getId());
        // Disconnect from session so that the updates on updatedEntidade are not directly saved in db
        em.detach(updatedEntidade);
        updatedEntidade
            .nome(UPDATED_NOME);

        restEntidadeMockMvc.perform(put("/api/entidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntidade)))
            .andExpect(status().isOk());

        // Validate the Entidade in the database
        List<Entidade> entidadeList = entidadeRepository.findAll();
        assertThat(entidadeList).hasSize(databaseSizeBeforeUpdate);
        Entidade testEntidade = entidadeList.get(entidadeList.size() - 1);
        assertThat(testEntidade.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingEntidade() throws Exception {
        int databaseSizeBeforeUpdate = entidadeRepository.findAll().size();

        // Create the Entidade

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntidadeMockMvc.perform(put("/api/entidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entidade)))
            .andExpect(status().isCreated());

        // Validate the Entidade in the database
        List<Entidade> entidadeList = entidadeRepository.findAll();
        assertThat(entidadeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntidade() throws Exception {
        // Initialize the database
        entidadeService.save(entidade);

        int databaseSizeBeforeDelete = entidadeRepository.findAll().size();

        // Get the entidade
        restEntidadeMockMvc.perform(delete("/api/entidades/{id}", entidade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Entidade> entidadeList = entidadeRepository.findAll();
        assertThat(entidadeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entidade.class);
        Entidade entidade1 = new Entidade();
        entidade1.setId(1L);
        Entidade entidade2 = new Entidade();
        entidade2.setId(entidade1.getId());
        assertThat(entidade1).isEqualTo(entidade2);
        entidade2.setId(2L);
        assertThat(entidade1).isNotEqualTo(entidade2);
        entidade1.setId(null);
        assertThat(entidade1).isNotEqualTo(entidade2);
    }
}
