package br.com.justino.projeto7;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.com.justino.projeto7.common.SenhaHash;
import br.com.justino.projeto7.domain.Usuario;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import java.util.HashMap;

@ApplicationScoped
public class AppLifecycleBean {

    @Inject
    EntityManager em;

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Iniciando a aplicação...");
        int qtd = Usuario.find("select u from Usuario u where u.login = 'justino'" ).list().size();
        if (qtd == 0) padrao();
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("Parando a aplicação...");
    }

    @Transactional
    private void padrao() {
        Usuario usuario = new Usuario();
        usuario.setLogin("justino");
        usuario.setEmail("carlos.justino08@gmail.com");
        usuario.setNome("Carlos Justino");
        SenhaHash snh = new SenhaHash();
        snh.setSenhaNova("123456");
        snh.verificaAtualizacaoDeSenha(true,2,100);
        usuario.setSenha(snh);
        usuario.persist();
    }
}