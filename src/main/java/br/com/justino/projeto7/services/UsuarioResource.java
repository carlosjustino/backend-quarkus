package br.com.justino.projeto7.services;

import br.com.justino.projeto7.exceptions.JustinoException;
import br.com.justino.projeto7.domain.ContainerUsuario;
import br.com.justino.projeto7.domain.Usuario;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {
    @Inject
    EntityManager em;

    @GET
    @Path("/all")
    public List<Usuario> getAllUsers(){
        return Usuario.listAll();
    }

    @POST
    @Transactional
    @Path("/create")
    public Usuario create(ContainerUsuario usuario){
        Usuario u = new Usuario(usuario);
        u.getSenha().verificaAtualizacaoDeSenha(true,6,100);
        u.persistAndFlush();
        return u;
    }

    @POST
    @Transactional
    @Path("/autenticar")
    public Usuario autenticar(ContainerUsuario usuario) throws JustinoException {
        Usuario u = Usuario.find("login", usuario.getLogin()).firstResult();
        if (u.getSenha().senhaEhValida(usuario.getSenha()))
            return u;
        else throw new JustinoException("Usuário ou senha invalidos");
    }

    @GET
    public Usuario getUser(@PathParam("id") Long id){
        return Usuario.findById(id);
    }

}
