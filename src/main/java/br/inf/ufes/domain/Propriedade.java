package br.inf.ufes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Propriedade.
 */
@Entity
@Table(name = "propriedade")
public class Propriedade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @ManyToMany(mappedBy = "propriedades")
    @JsonIgnore
    private Set<DataSource> datasources = new HashSet<>();

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

    public Propriedade nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<DataSource> getDatasources() {
        return datasources;
    }

    public Propriedade datasources(Set<DataSource> dataSources) {
        this.datasources = dataSources;
        return this;
    }

    public Propriedade addDatasource(DataSource dataSource) {
        this.datasources.add(dataSource);
        dataSource.getPropriedades().add(this);
        return this;
    }

    public Propriedade removeDatasource(DataSource dataSource) {
        this.datasources.remove(dataSource);
        dataSource.getPropriedades().remove(this);
        return this;
    }

    public void setDatasources(Set<DataSource> dataSources) {
        this.datasources = dataSources;
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
        Propriedade propriedade = (Propriedade) o;
        if (propriedade.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), propriedade.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Propriedade{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
