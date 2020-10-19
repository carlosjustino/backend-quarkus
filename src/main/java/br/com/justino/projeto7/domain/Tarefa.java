package br.com.justino.projeto7.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REFRESH;

@Entity
public class Tarefa  extends PanacheEntity {

    /* Refactory para usar o PanacheEntity
     @Id
     @GeneratedValue(strategy=GenerationType.IDENTITY)
     Long id;
 */
    Long numero;

    @NotBlank(message="O campo Titulo deve ser informado em Tarefa")
    String titulo;

    @NotBlank(message="O campo Descricao deve ser informado em Tarefa")
    String descricao;

    @NotNull(message="O campo Usuário deve ser informado em Tarefa")
    @OneToOne(cascade = REFRESH, orphanRemoval = false)
    @JoinColumn(name="idUsuario")
    Usuario usuario;


    @NotNull(message="O campo datainclusao não pode ser vazio")
    LocalDateTime datainclusao;

    @OneToMany(fetch = FetchType.LAZY, cascade=ALL, orphanRemoval=true, mappedBy="tarefa")
    @JsonIgnoreProperties("tarefa")
    List<TarefaAlteracao> alteracoes;

    Boolean concluida = Boolean.FALSE;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column(name = "numero", nullable = true, insertable = false, updatable = false)
    @Generated(GenerationTime.ALWAYS)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDatainclusao() {
        return datainclusao;
    }

    public void setDatainclusao(LocalDateTime datainclusao) {
        this.datainclusao = datainclusao;
    }

    public Boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }

    public List<TarefaAlteracao> getAlteracoes() {
        if (alteracoes == null ) alteracoes = new ArrayList<>();
        return alteracoes;
    }

    public void setAlteracoes(List<TarefaAlteracao> alteracoes) {
        this.alteracoes = alteracoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarefa tarefa = (Tarefa) o;
        return Objects.equals(id, tarefa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
