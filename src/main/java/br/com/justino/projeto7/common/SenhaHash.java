package br.com.justino.projeto7.common;

import br.com.justino.projeto7.helper.Hashing;
import br.com.justino.projeto7.helper.StaticFunctions;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

@Embeddable
public final class SenhaHash implements Serializable {



    private String senha;
    @Transient
    private boolean senhaAlterada;
    @Transient
    private String senhaAntiga;
    @Transient
    private String senhaNova;
    @Transient
    private boolean senhaJaHasheada;

    public SenhaHash() {
        senha = "";
    }

    public SenhaHash(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
        if (!isSenhaJaHasheada()) {
            setSenhaJaHasheada(true);
            this.senha = Hashing.hash(senha);
        }
    }

    public boolean getSenhaAlterada() {
        return senhaAlterada;
    }

    public void setSenhaAlterada(boolean senhaAlterada) {
        this.senhaAlterada = senhaAlterada;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    public boolean isSenhaJaHasheada() {
        return senhaJaHasheada;
    }

    public void setSenhaJaHasheada(boolean senhaJaHasheada) {
        this.senhaJaHasheada = senhaJaHasheada;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }

    public void verificaAtualizacaoDeSenha(boolean onCreate, int minSize, int maxSize) {
        if (onCreate || getSenhaAlterada()) {
            if (!onCreate
                    && !getSenhaAntiga().equals(getSenha()) // senhas antigas nao hasheadas
                    && !senhaEhValida(getSenhaAntiga())) {

                StaticFunctions.throwConstraintViolationException("Senha antiga não confere", this);
            }else{
                if (getSenha() != null && !getSenha().isEmpty() && getSenhaNova() == null)
                    setSenhaNova(getSenha());
            }

            if (getSenhaNova().length() < minSize || getSenhaNova().length() > maxSize)
                StaticFunctions.throwConstraintViolationException(String.format("Senha deve conter entre %s e %s caracteres", minSize, maxSize), this);

            setSenhaJaHasheada(false);
            setSenha(getSenhaNova());
        }
    }

    public boolean senhaEhValida(String senhaAVerificar) {
        return Hashing.verifyHash(senhaAVerificar, getSenha());
    }
}

