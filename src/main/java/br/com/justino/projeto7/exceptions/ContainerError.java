package br.com.justino.projeto7.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;
import java.io.Serializable;

public class ContainerError implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    @Transient
    @JsonIgnore
    private Object obj;

    @Transient
    @JsonIgnore
    private Exception ex;

    public ContainerError() {
    }

    public ContainerError(String message, Exception ex) {
        this.message = message;
        this.ex = ex;
    }

    public ContainerError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }
}
