package br.com.justino.projeto7;

import br.com.justino.projeto7.common.SenhaHash;
import br.com.justino.projeto7.domain.Usuario;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(Projeto7App.class, args);
    }

    public static class Projeto7App implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {

            Quarkus.waitForExit();
            return 0;
        }

    }
}