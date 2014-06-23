package com.finalist.plugin;

import org.onehippo.cms7.essentials.dashboard.rest.BaseResource;
import org.onehippo.cms7.essentials.dashboard.rest.MessageRestful;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/textblockPlugin/")
public class TextBlockPluginResource extends BaseResource {


    @GET
    public MessageRestful runTextblockPlugin(@Context ServletContext servletContext) {
       return new MessageRestful("Not implemented yet");
    }
}
