package br.com.justino.projeto7.exceptions;

import br.com.justino.projeto7.helper.StaticFunctions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JustinoExceptionHandler implements ExceptionMapper<Exception>
{
    @Override
    public Response toResponse(Exception exception)
    {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ContainerError(StaticFunctions.getMessageConstraintViolationException(exception), exception)).build();
    }
}
