package br.com.justino.projeto7.exceptions;

import java.io.Serializable;

public class JustinoException extends Exception implements Serializable
{
    private static final long serialVersionUID = 1L;

    public JustinoException() {
        super();
    }
    public JustinoException(String msg)   {
        super(msg);
    }
    public JustinoException(String msg, Exception e)  {
        super(msg, e);
    }

}
