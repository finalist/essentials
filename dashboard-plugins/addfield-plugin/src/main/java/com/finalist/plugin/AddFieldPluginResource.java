package com.finalist.plugin;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.util.string.Strings;
import org.hippoecm.repository.api.NodeNameCodec;
import org.onehippo.cms7.essentials.dashboard.ctx.PluginContext;
import org.onehippo.cms7.essentials.dashboard.rest.BaseResource;
import org.onehippo.cms7.essentials.dashboard.rest.ErrorMessageRestful;
import org.onehippo.cms7.essentials.dashboard.rest.MessageRestful;
import org.onehippo.cms7.essentials.dashboard.rest.PostPayloadRestful;
import org.onehippo.cms7.essentials.dashboard.utils.GlobalUtils;
import org.onehippo.cms7.essentials.dashboard.utils.TemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;


@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/add-field/")
public class AddFieldPluginResource extends BaseResource {

    private static Logger log = LoggerFactory.getLogger(AddFieldPluginResource.class);

    @POST
    @Path("/")
    public MessageRestful runAddFieldPlugin(final PostPayloadRestful payloadRestful,
                                            @Context ServletContext servletContext, @Context HttpServletResponse response) {
        final Session session = getContext(servletContext).createSession();
            final Map<String, String> values = payloadRestful.getValues();
            final String fieldName = values.get("fieldName");
            if (!Strings.isEmpty(fieldName)) {
                try{
                    return addField(session,payloadRestful.getValues(),response);
                }catch (RepositoryException | IOException e){
                    log.warn("Exception trying to add a text field to a document type", e);
                    return createErrorMessage("Failed to add new text field to document type. Check logs.", response);
                }
            }
        return new ErrorMessageRestful("Failed to receive field");
    }


    /**
     * Add a new text field to a document type.
     *
     * @param session JCR session for persisting the changes
     * @param values  parameters of new selection field (See addFieldPlugin.js for keys).
     * @return        message to be sent back to front-end.
     */
    private MessageRestful addField(Session session, LinkedHashMap<String, String> values, final HttpServletResponse response) throws RepositoryException, IOException{
        final String docTypeBase = MessageFormat.format("/hippo:namespaces/{0}/{1}/",
                values.get("namespace"), values.get("documentType"));
        final String documentType = values.get("namespace") + ":" + values.get("documentType");

        final Node editorTemplate = session.getNode(docTypeBase + "editor:templates/_default_");
        final Node nodeTypeHandle = session.getNode(docTypeBase + "hipposysedit:nodetype");
        if (nodeTypeHandle.getNodes().getSize() > 1) {
            return createErrorMessage("Document type '" + documentType + "' is currently being edited in the CMS, "
                    + "please commit any pending changes before adding a selection field.", response);
        }
        final Node nodeType = nodeTypeHandle.getNode("hipposysedit:nodetype");

        // Check if the field name is valid. If so, normalize it.
        final String normalized = NodeNameCodec.encode(values.get("fieldName").toLowerCase().replaceAll("\\s", ""));
        values.put("normalizedFieldName", normalized);

        // Check if the fieldName is already in use
        if (nodeType.hasNode(normalized)
                || editorTemplate.hasNode(normalized)
                || isPropertyNameInUse(nodeType, values.get("namespace"), normalized)) {
            return createErrorMessage("Field name is already in use for this document type.", response);
        }
        importXml("/xml/single-text-editor-template.xml", values, editorTemplate);
        importXml("/xml/single-text-node-type.xml", values, nodeType);
        session.save();

        final String successMessage = MessageFormat.format("Successfully added new single text field {0} to document type {1}.",
                values.get("fieldName"), documentType);
        return new MessageRestful(successMessage);
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

    /**
     * Check if a certain namespace/fieldname combination is already in use for a certain document type
     *
     * @param nodeType  JCR node representing the hipposysedit:nodetype node.
     * @param namespace JCR namespace for the selected document type.
     * @param fieldName Candidate normalized field name.
     * @return          true if already in use, false otherwise.
     * @throws RepositoryException
     */
    private boolean isPropertyNameInUse(final Node nodeType, final String namespace, final String fieldName)
            throws RepositoryException {
        final NodeIterator fields = nodeType.getNodes();
        final String path = namespace + ":" + fieldName;
        while (fields.hasNext()) {
            final Node field = fields.nextNode();
            if (field.hasProperty("hipposysedit:path") && path.equals(field.getProperty("hipposysedit:path").getString())) {
                return true;
            }
        }
        return false;
    }

}
