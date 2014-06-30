package com.finalist.plugin;

import org.apache.wicket.util.string.Strings;
import org.onehippo.cms7.essentials.dashboard.ctx.PluginContext;
import org.onehippo.cms7.essentials.dashboard.rest.BaseResource;
import org.onehippo.cms7.essentials.dashboard.rest.ErrorMessageRestful;
import org.onehippo.cms7.essentials.dashboard.rest.MessageRestful;
import org.onehippo.cms7.essentials.dashboard.rest.PostPayloadRestful;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;


@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/add-field/")
public class AddFieldPluginResource extends BaseResource {

    private static Logger log = LoggerFactory.getLogger(AddFieldPluginResource.class);

    @POST
    @Path("/")
    public MessageRestful runAddFieldPlugin(final PostPayloadRestful payloadRestful, @Context ServletContext servletContext) {
        final PluginContext context = getContext(servletContext);
            final Map<String, String> values = payloadRestful.getValues();
            final String fieldName = values.get("fieldName");
            final String documentType = values.get("selectedDocumentType");
            if (!Strings.isEmpty(fieldName)) {
                return new MessageRestful("Field to be added: " + fieldName + " to documentType "+ documentType);
            }
        return new ErrorMessageRestful("Failed to receive field");
    }

}
