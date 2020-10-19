package br.com.justino.projeto7.services;

import br.com.justino.projeto7.domain.Tarefa;
import br.com.justino.projeto7.domain.TarefaAlteracao;
import br.com.justino.projeto7.domain.Usuario;
import br.com.justino.projeto7.exceptions.JustinoException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.List;

@Path("/tarefa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TarefaResource {
    @Inject
    EntityManager em;

    @GET
    @Path("/{userid}/all")
    public List<Tarefa> getAllTask(@PathParam("userid") Long idUsuario){
        return Tarefa.find("usuario.id", idUsuario).list();
    }

    @POST
    @Transactional
    @Path("/{userid}/create")
    public Tarefa newTask(@PathParam("userid") Long userId, Tarefa tarefa){
        Usuario u = Usuario.findById(userId);
        tarefa.setDatainclusao(LocalDateTime.now());
        tarefa.setUsuario(u);
        tarefa.getAlteracoes().add(new TarefaAlteracao(tarefa,"Inclusao", TarefaAlteracao.TIPOMOVIMENTO_INCLUSAO));
        if (tarefa.isConcluida() == null) tarefa.setConcluida(false);
        tarefa.persistAndFlush();
        return tarefa;
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Tarefa updateTask(@PathParam("id") Long idTarefa, Tarefa tarefanova){
        Tarefa tarefa = Tarefa.findById(idTarefa);
        tarefa.setDescricao(tarefanova.getDescricao());
        tarefa.setTitulo(tarefanova.getTitulo());
        tarefa.setConcluida(tarefanova.isConcluida());
        tarefa.getAlteracoes().add(new TarefaAlteracao(tarefa,"Alteracao da tarefa", tarefa.isConcluida() ? TarefaAlteracao.TIPOMOVIMENTO_CONCLUSAO : TarefaAlteracao.TIPOMOVIMENTO_ALTERACAO));
        return tarefa;
    }
    @GET
    @Path("/{id}")
    public Tarefa getTask(@PathParam("id") Long idTarefa){
        Tarefa tarefa = Tarefa.findById(idTarefa);
        return tarefa;
    }

    @GET
    @Path("/{userid}/{id}/alteracoes")
    public List<TarefaAlteracao> getAllChanges(@PathParam("userid") Long userId, @PathParam("id") Long idTarefa) throws JustinoException {
        Tarefa tarefa = Tarefa.findById(idTarefa);
        if (tarefa.getUsuario().getId().equals(userId))
            return tarefa.getAlteracoes();
        else
            throw new JustinoException("Tarefa não pertence ao Usuário");
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public void deleteTask(@PathParam("id") Long idTarefa){
        Tarefa tarefa = Tarefa.findById(idTarefa);
        tarefa.delete();
    }


}
