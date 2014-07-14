/*
 * Copyright 2014 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onehippo.cms7.essentials.plugins.relateddocuments;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.onehippo.cms7.essentials.dashboard.ctx.PluginContext;
import org.onehippo.cms7.essentials.dashboard.ctx.PluginContextFactory;
import org.onehippo.cms7.essentials.dashboard.rest.BaseResource;
import org.onehippo.cms7.essentials.dashboard.rest.MessageRestful;
import org.onehippo.cms7.essentials.dashboard.rest.PostPayloadRestful;
import org.onehippo.cms7.essentials.dashboard.utils.DocumentTemplateUtils;
import org.onehippo.cms7.essentials.dashboard.utils.GlobalUtils;
import org.onehippo.cms7.essentials.dashboard.utils.PayloadUtils;
import org.onehippo.cms7.essentials.dashboard.utils.TemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * @version "$Id$"
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("related-documents")
public class RelatedDocumentsResource extends BaseResource {


    private static Logger log = LoggerFactory.getLogger(RelatedDocumentsResource.class);

    public static final String MIXIN_NAME = "relateddocs:relatabledocs";

    @POST
    @Path("/")
    public MessageRestful addDocuments(final PostPayloadRestful payloadRestful, @Context ServletContext servletContext) {
        final PluginContext context = PluginContextFactory.getContext();
        final Session session = context.createSession();
        try {
            final Map<String, String> values = payloadRestful.getValues();
            final String documents = values.get("documents");
            final String prefix = context.getProjectNamespacePrefix();

            final String templateRelatedDocs = GlobalUtils.readStreamAsText(getClass().getResourceAsStream("/related_documents_template.xml"));
            final String templateSuggestDocs = GlobalUtils.readStreamAsText(getClass().getResourceAsStream("/related_documents_suggestion_template.xml"));

            final Collection<String> changedDocuments = new HashSet<>();

            if (!Strings.isNullOrEmpty(documents)) {

                final String[] suggestions = PayloadUtils.extractValueArray(values.get("numberOfSuggestions"));
                final String[] searchPaths = PayloadUtils.extractValueArray(values.get("searchPaths"));
                final String[] docs = PayloadUtils.extractValueArray(values.get("documents"));

                int idx = 0;
                for (final String document : docs) {
                    final String fieldImportPath = MessageFormat.format("/hippo:namespaces/{0}/{1}/editor:templates/_default_", prefix, document);
                    final String suggestFieldPath = MessageFormat.format("{0}/relateddocs", fieldImportPath);
                    if (session.nodeExists(suggestFieldPath)) {
                        log.info("Suggest field path: [{}] already exists.", fieldImportPath);
                        continue;
                    }
                    DocumentTemplateUtils.addMixinToTemplate(context, document, MIXIN_NAME, true);
                    // add place holders:
                    final Map<String, String> templateData = new HashMap<>(values);
                    final Node editorTemplate = session.getNode(fieldImportPath);
                    templateData.put("fieldLocation", DocumentTemplateUtils.getDefaultPosition(editorTemplate));
                    templateData.put("searchPaths", searchPaths[idx]);
                    templateData.put("numberOfSuggestions", suggestions[idx]);
                    // import field:
                    final String fieldData = TemplateUtils.replaceStringPlaceholders(templateRelatedDocs, templateData);
                    session.importXML(fieldImportPath, IOUtils.toInputStream(fieldData), ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
                    // import suggest field:
                    final String suggestData = TemplateUtils.replaceStringPlaceholders(templateSuggestDocs, templateData);
                    session.importXML(fieldImportPath, IOUtils.toInputStream(suggestData), ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
                    session.save();
                    changedDocuments.add(document);
                    idx++;
                }


            }
            if (changedDocuments.size() > 0) {
                final String join = Joiner.on(',').join(changedDocuments);
                return new MessageRestful("Added related document fields to following documents: " + join);
            }


        } catch (RepositoryException | IOException e) {
            log.error("Error adding related documents field", e);
        } finally {
            GlobalUtils.cleanupSession(session);
        }
        return new MessageRestful("No related document fields were added");

    }

}
