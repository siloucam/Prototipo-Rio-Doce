package br.inf.ufes.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the DataSource entity. This class is used in DataSourceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /data-sources?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DataSourceCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter nome;

    private StringFilter path;

    private LocalDateFilter dtinicial;

    private LocalDateFilter dtfinal;

    private LongFilter metodoId;

    private LongFilter entidadeId;

    private LongFilter atividadeId;

    private LongFilter propriedadeId;

    private LongFilter provenienciaId;

    public DataSourceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getPath() {
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public LocalDateFilter getDtinicial() {
        return dtinicial;
    }

    public void setDtinicial(LocalDateFilter dtinicial) {
        this.dtinicial = dtinicial;
    }

    public LocalDateFilter getDtfinal() {
        return dtfinal;
    }

    public void setDtfinal(LocalDateFilter dtfinal) {
        this.dtfinal = dtfinal;
    }

    public LongFilter getMetodoId() {
        return metodoId;
    }

    public void setMetodoId(LongFilter metodoId) {
        this.metodoId = metodoId;
    }

    public LongFilter getEntidadeId() {
        return entidadeId;
    }

    public void setEntidadeId(LongFilter entidadeId) {
        this.entidadeId = entidadeId;
    }

    public LongFilter getAtividadeId() {
        return atividadeId;
    }

    public void setAtividadeId(LongFilter atividadeId) {
        this.atividadeId = atividadeId;
    }

    public LongFilter getPropriedadeId() {
        return propriedadeId;
    }

    public void setPropriedadeId(LongFilter propriedadeId) {
        this.propriedadeId = propriedadeId;
    }

    public LongFilter getProvenienciaId() {
        return provenienciaId;
    }

    public void setProvenienciaId(LongFilter provenienciaId) {
        this.provenienciaId = provenienciaId;
    }

    @Override
    public String toString() {
        return "DataSourceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (path != null ? "path=" + path + ", " : "") +
                (dtinicial != null ? "dtinicial=" + dtinicial + ", " : "") +
                (dtfinal != null ? "dtfinal=" + dtfinal + ", " : "") +
                (metodoId != null ? "metodoId=" + metodoId + ", " : "") +
                (entidadeId != null ? "entidadeId=" + entidadeId + ", " : "") +
                (atividadeId != null ? "atividadeId=" + atividadeId + ", " : "") +
                (propriedadeId != null ? "propriedadeId=" + propriedadeId + ", " : "") +
                (provenienciaId != null ? "provenienciaId=" + provenienciaId + ", " : "") +
            "}";
    }

}
