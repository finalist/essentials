package com.finalist.plugin;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.util.string.Strings;
import org.onehippo.cms7.essentials.dashboard.ctx.PluginContext;
import org.onehippo.cms7.essentials.dashboard.rest.BaseResource;
import org.onehippo.cms7.essentials.dashboard.rest.ErrorMessageRestful;
import org.onehippo.cms7.essentials.dashboard.rest.MessageRestful;
import org.onehippo.cms7.essentials.dashboard.rest.PostPayloadRestful;
import org.onehippo.cms7.essentials.dashboard.utils.GlobalUtils;
import org.onehippo.cms7.essentials.dashboard.utils.TemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * Import an XML resource into the repository, after mustache-processing.
     *
     * @param resourcePath   path to obtain resource.
     * @param placeholderMap map with placeholder values
     * @param destination    target parent JCR node for the import
     * @throws java.io.IOException
     * @throws javax.jcr.RepositoryException
     */
    private void importXml(final String resourcePath, final Map<String, String> placeholderMap, final Node destination)
            throws IOException, RepositoryException
    {
        final InputStream stream = getClass().getResourceAsStream(resourcePath);
        final String processedXml = TemplateUtils.replaceStringPlaceholders(GlobalUtils.readStreamAsText(stream), placeholderMap);
        destination.getSession().importXML(destination.getPath(), IOUtils.toInputStream(processedXml),
                ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
    }

}
