package br.inf.ufes.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DataSource.
 */
@Entity
@Table(name = "data_source")
public class DataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "path")
    private String path;

    @Column(name = "dtinicial")
    private LocalDate dtinicial;

    @Column(name = "dtfinal")
    private LocalDate dtfinal;

    @ManyToMany
    @JoinTable(name = "data_source_metodo",
               joinColumns = @JoinColumn(name="data_sources_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="metodos_id", referencedColumnName="id"))
    private Set<Metodo> metodos = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "data_source_entidade",
               joinColumns = @JoinColumn(name="data_sources_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="entidades_id", referencedColumnName="id"))
    private Set<Entidade> entidades = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "data_source_atividade",
               joinColumns = @JoinColumn(name="data_sources_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="atividades_id", referencedColumnName="id"))
    private Set<Atividade> atividades = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "data_source_propriedade",
               joinColumns = @JoinColumn(name="data_sources_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="propriedades_id", referencedColumnName="id"))
    private Set<Propriedade> propriedades = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "data_source_proveniencia",
               joinColumns = @JoinColumn(name="data_sources_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="proveniencias_id", referencedColumnName="id"))
    private Set<Proveniencia> proveniencias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public DataSource nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPath() {
        return path;
    }

    public DataSource path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDate getDtinicial() {
        return dtinicial;
    }

    public DataSource dtinicial(LocalDate dtinicial) {
        this.dtinicial = dtinicial;
        return this;
    }

    public void setDtinicial(LocalDate dtinicial) {
        this.dtinicial = dtinicial;
    }

    public LocalDate getDtfinal() {
        return dtfinal;
    }

    public DataSource dtfinal(LocalDate dtfinal) {
        this.dtfinal = dtfinal;
        return this;
    }

    public void setDtfinal(LocalDate dtfinal) {
        this.dtfinal = dtfinal;
    }

    public Set<Metodo> getMetodos() {
        return metodos;
    }

    public DataSource metodos(Set<Metodo> metodos) {
        this.metodos = metodos;
        return this;
    }

    public DataSource addMetodo(Metodo metodo) {
        this.metodos.add(metodo);
        metodo.getDatasources().add(this);
        return this;
    }

    public DataSource removeMetodo(Metodo metodo) {
        this.metodos.remove(metodo);
        metodo.getDatasources().remove(this);
        return this;
    }

    public void setMetodos(Set<Metodo> metodos) {
        this.metodos = metodos;
    }

    public Set<Entidade> getEntidades() {
        return entidades;
    }

    public DataSource entidades(Set<Entidade> entidades) {
        this.entidades = entidades;
        return this;
    }

    public DataSource addEntidade(Entidade entidade) {
        this.entidades.add(entidade);
        entidade.getDatasources().add(this);
        return this;
    }

    public DataSource removeEntidade(Entidade entidade) {
        this.entidades.remove(entidade);
        entidade.getDatasources().remove(this);
        return this;
    }

    public void setEntidades(Set<Entidade> entidades) {
        this.entidades = entidades;
    }

    public Set<Atividade> getAtividades() {
        return atividades;
    }

    public DataSource atividades(Set<Atividade> atividades) {
        this.atividades = atividades;
        return this;
    }

    public DataSource addAtividade(Atividade atividade) {
        this.atividades.add(atividade);
        atividade.getDatasources().add(this);
        return this;
    }

    public DataSource removeAtividade(Atividade atividade) {
        this.atividades.remove(atividade);
        atividade.getDatasources().remove(this);
        return this;
    }

    public void setAtividades(Set<Atividade> atividades) {
        this.atividades = atividades;
    }

    public Set<Propriedade> getPropriedades() {
        return propriedades;
    }

    public DataSource propriedades(Set<Propriedade> propriedades) {
        this.propriedades = propriedades;
        return this;
    }

    public DataSource addPropriedade(Propriedade propriedade) {
        this.propriedades.add(propriedade);
        propriedade.getDatasources().add(this);
        return this;
    }

    public DataSource removePropriedade(Propriedade propriedade) {
        this.propriedades.remove(propriedade);
        propriedade.getDatasources().remove(this);
        return this;
    }

    public void setPropriedades(Set<Propriedade> propriedades) {
        this.propriedades = propriedades;
    }

    public Set<Proveniencia> getProveniencias() {
        return proveniencias;
    }

    public DataSource proveniencias(Set<Proveniencia> proveniencias) {
        this.proveniencias = proveniencias;
        return this;
    }

    public DataSource addProveniencia(Proveniencia proveniencia) {
        this.proveniencias.add(proveniencia);
        proveniencia.getDatasources().add(this);
        return this;
    }

    public DataSource removeProveniencia(Proveniencia proveniencia) {
        this.proveniencias.remove(proveniencia);
        proveniencia.getDatasources().remove(this);
        return this;
    }

    public void setProveniencias(Set<Proveniencia> proveniencias) {
        this.proveniencias = proveniencias;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataSource dataSource = (DataSource) o;
        if (dataSource.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataSource.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataSource{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", path='" + getPath() + "'" +
            ", dtinicial='" + getDtinicial() + "'" +
            ", dtfinal='" + getDtfinal() + "'" +
            "}";
    }
}
