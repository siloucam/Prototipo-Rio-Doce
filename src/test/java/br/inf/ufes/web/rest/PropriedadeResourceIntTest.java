package br.inf.ufes.web.rest;

import br.inf.ufes.PrototipoRioDoceApp;

import br.inf.ufes.domain.Propriedade;
import br.inf.ufes.domain.DataSource;
import br.inf.ufes.repository.PropriedadeRepository;
import br.inf.ufes.service.PropriedadeService;
import br.inf.ufes.web.rest.errors.ExceptionTranslator;
import br.inf.ufes.service.dto.PropriedadeCriteria;
import br.inf.ufes.service.PropriedadeQueryService;

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
 * Test class for the PropriedadeResource REST controller.
 *
 * @see PropriedadeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrototipoRioDoceApp.class)
public class PropriedadeResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private PropriedadeRepository propriedadeRepository;

    @Autowired
    private PropriedadeService propriedadeService;

    @Autowired
    private PropriedadeQueryService propriedadeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPropriedadeMockMvc;

    private Propriedade propriedade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PropriedadeResource propriedadeResource = new PropriedadeResource(propriedadeService, propriedadeQueryService);
        this.restPropriedadeMockMvc = MockMvcBuilders.standaloneSetup(propriedadeResource)
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
    public static Propriedade createEntity(EntityManager em) {
        Propriedade propriedade = new Propriedade()
            .nome(DEFAULT_NOME);
        return propriedade;
    }

    @Before
    public void initTest() {
        propriedade = createEntity(em);
    }

    @Test
    @Transactional
    public void createPropriedade() throws Exception {
        int databaseSizeBeforeCreate = propriedadeRepository.findAll().size();

        // Create the Propriedade
        restPropriedadeMockMvc.perform(post("/api/propriedades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propriedade)))
            .andExpect(status().isCreated());

        // Validate the Propriedade in the database
        List<Propriedade> propriedadeList = propriedadeRepository.findAll();
        assertThat(propriedadeList).hasSize(databaseSizeBeforeCreate + 1);
        Propriedade testPropriedade = propriedadeList.get(propriedadeList.size() - 1);
        assertThat(testPropriedade.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createPropriedadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propriedadeRepository.findAll().size();

        // Create the Propriedade with an existing ID
        propriedade.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropriedadeMockMvc.perform(post("/api/propriedades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propriedade)))
            .andExpect(status().isBadRequest());

        // Validate the Propriedade in the database
        List<Propriedade> propriedadeList = propriedadeRepository.findAll();
        assertThat(propriedadeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPropriedades() throws Exception {
        // Initialize the database
        propriedadeRepository.saveAndFlush(propriedade);

        // Get all the propriedadeList
        restPropriedadeMockMvc.perform(get("/api/propriedades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propriedade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getPropriedade() throws Exception {
        // Initialize the database
        propriedadeRepository.saveAndFlush(propriedade);

        // Get the propriedade
        restPropriedadeMockMvc.perform(get("/api/propriedades/{id}", propriedade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(propriedade.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getAllPropriedadesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        propriedadeRepository.saveAndFlush(propriedade);

        // Get all the propriedadeList where nome equals to DEFAULT_NOME
        defaultPropriedadeShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the propriedadeList where nome equals to UPDATED_NOME
        defaultPropriedadeShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllPropriedadesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        propriedadeRepository.saveAndFlush(propriedade);

        // Get all the propriedadeList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultPropriedadeShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the propriedadeList where nome equals to UPDATED_NOME
        defaultPropriedadeShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllPropriedadesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        propriedadeRepository.saveAndFlush(propriedade);

        // Get all the propriedadeList where nome is not null
        defaultPropriedadeShouldBeFound("nome.specified=true");

        // Get all the propriedadeList where nome is null
        defaultPropriedadeShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllPropriedadesByDatasourceIsEqualToSomething() throws Exception {
        // Initialize the database
        DataSource datasource = DataSourceResourceIntTest.createEntity(em);
        em.persist(datasource);
        em.flush();
        propriedade.addDatasource(datasource);
        propriedadeRepository.saveAndFlush(propriedade);
        Long datasourceId = datasource.getId();

        // Get all the propriedadeList where datasource equals to datasourceId
        defaultPropriedadeShouldBeFound("datasourceId.equals=" + datasourceId);

        // Get all the propriedadeList where datasource equals to datasourceId + 1
        defaultPropriedadeShouldNotBeFound("datasourceId.equals=" + (datasourceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPropriedadeShouldBeFound(String filter) throws Exception {
        restPropriedadeMockMvc.perform(get("/api/propriedades?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propriedade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPropriedadeShouldNotBeFound(String filter) throws Exception {
        restPropriedadeMockMvc.perform(get("/api/propriedades?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPropriedade() throws Exception {
        // Get the propriedade
        restPropriedadeMockMvc.perform(get("/api/propriedades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePropriedade() throws Exception {
        // Initialize the database
        propriedadeService.save(propriedade);

        int databaseSizeBeforeUpdate = propriedadeRepository.findAll().size();

        // Update the propriedade
        Propriedade updatedPropriedade = propriedadeRepository.findOne(propriedade.getId());
        // Disconnect from session so that the updates on updatedPropriedade are not directly saved in db
        em.detach(updatedPropriedade);
        updatedPropriedade
            .nome(UPDATED_NOME);

        restPropriedadeMockMvc.perform(put("/api/propriedades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPropriedade)))
            .andExpect(status().isOk());

        // Validate the Propriedade in the database
        List<Propriedade> propriedadeList = propriedadeRepository.findAll();
        assertThat(propriedadeList).hasSize(databaseSizeBeforeUpdate);
        Propriedade testPropriedade = propriedadeList.get(propriedadeList.size() - 1);
        assertThat(testPropriedade.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingPropriedade() throws Exception {
        int databaseSizeBeforeUpdate = propriedadeRepository.findAll().size();

        // Create the Propriedade

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPropriedadeMockMvc.perform(put("/api/propriedades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propriedade)))
            .andExpect(status().isCreated());

        // Validate the Propriedade in the database
        List<Propriedade> propriedadeList = propriedadeRepository.findAll();
        assertThat(propriedadeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePropriedade() throws Exception {
        // Initialize the database
        propriedadeService.save(propriedade);

        int databaseSizeBeforeDelete = propriedadeRepository.findAll().size();

        // Get the propriedade
        restPropriedadeMockMvc.perform(delete("/api/propriedades/{id}", propriedade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Propriedade> propriedadeList = propriedadeRepository.findAll();
        assertThat(propriedadeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Propriedade.class);
        Propriedade propriedade1 = new Propriedade();
        propriedade1.setId(1L);
        Propriedade propriedade2 = new Propriedade();
        propriedade2.setId(propriedade1.getId());
        assertThat(propriedade1).isEqualTo(propriedade2);
        propriedade2.setId(2L);
        assertThat(propriedade1).isNotEqualTo(propriedade2);
        propriedade1.setId(null);
        assertThat(propriedade1).isNotEqualTo(propriedade2);
    }
}
