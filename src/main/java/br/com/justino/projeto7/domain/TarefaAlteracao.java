package br.com.justino.projeto7.domain;

import br.com.justino.projeto7.validators.IntegerValues;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
public class TarefaAlteracao  extends PanacheEntity {
    @Transient
    public static final int TIPOMOVIMENTO_INCLUSAO = 1;
    @Transient
    public static final int TIPOMOVIMENTO_ALTERACAO = 2;
    @Transient
    public static final int TIPOMOVIMENTO_CONCLUSAO = 3;

    /* Refactory para usar o PanacheEntity
     @Id
     @GeneratedValue(strategy=GenerationType.IDENTITY)
     Long id;
 */
    @ManyToOne
    @JoinColumn(name="idTarefa", nullable=true)
    Tarefa tarefa;

    @NotBlank(message="O campo Descricao deve ser informado na Alteração")
    String descricao;

    @NotNull(message="O campo datamovimento não pode ser vazio")
    LocalDateTime datamovimento;

    @IntegerValues(values = {TIPOMOVIMENTO_INCLUSAO, TIPOMOVIMENTO_ALTERACAO, TIPOMOVIMENTO_CONCLUSAO}, message = "Tipo de movimento não permitido")
    Integer tipomovimento;

    public TarefaAlteracao() {
        datamovimento = LocalDateTime.now();
    }

    public TarefaAlteracao(Tarefa tarefa, @NotBlank(message = "O campo Descricao deve ser informado na Alteração") String descricao, Integer tipomovimento) {
        this();
        this.tarefa = tarefa;
        this.descricao = descricao;
        this.tipomovimento = tipomovimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDatamovimento() {
        return datamovimento;
    }

    public void setDatamovimento(LocalDateTime datamovimento) {
        this.datamovimento = datamovimento;
    }

    public Integer getTipomovimento() {
        return tipomovimento;
    }

    public void setTipomovimento(Integer tipomovimento) {
        this.tipomovimento = tipomovimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TarefaAlteracao tarefaAlteracao = (TarefaAlteracao) o;
        return Objects.equals(id, tarefaAlteracao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
