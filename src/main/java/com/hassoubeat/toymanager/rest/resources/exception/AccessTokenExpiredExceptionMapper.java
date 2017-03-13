/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resources.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author hassoubeat
 */
@Provider
public class AccessTokenExpiredExceptionMapper implements ExceptionMapper<AccessTokenExpiredException>{

    @Override
    public Response toResponse(AccessTokenExpiredException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(exception.getMessage()).type("text/plain").build();
    }
    
}
