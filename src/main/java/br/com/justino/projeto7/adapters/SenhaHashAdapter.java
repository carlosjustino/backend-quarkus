package br.com.justino.projeto7.adapters;



import br.com.justino.projeto7.common.SenhaHash;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SenhaHashAdapter extends XmlAdapter<SenhaHash, SenhaHash> {

    @Override
    public SenhaHash unmarshal(SenhaHash v) throws Exception {
        return v;
    }

    @Override
    public SenhaHash marshal(SenhaHash v) throws Exception {
        SenhaHash senha = new SenhaHash();
        senha.setSenhaJaHasheada(true);
        return senha;
    }

}
