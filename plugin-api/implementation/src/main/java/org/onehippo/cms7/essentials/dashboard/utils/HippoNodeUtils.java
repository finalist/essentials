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

package org.onehippo.cms7.essentials.dashboard.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.repository.api.HippoNodeType;
import org.hippoecm.repository.api.HippoSession;
import org.hippoecm.repository.api.NodeNameCodec;
import org.onehippo.cms7.essentials.dashboard.rest.NodeRestful;
import org.onehippo.cms7.essentials.dashboard.rest.PropertyRestful;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

/**
 * @version "$Id: HippoNodeUtils.java 169724 2013-07-05 08:32:08Z dvandiepen $"
 */
public final class HippoNodeUtils {


    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]+$");
    private static final Pattern URL_PATTERN = Pattern.compile("^http:.*/[0-9].[0-9]$");

    public static final Multimap<Integer, Class<?>> TYPE_MAPPINGS = new ImmutableMultimap.Builder<Integer, Class<?>>()
            .put(PropertyType.LONG, Integer.class)
            .put(PropertyType.LONG, int.class)
            .put(PropertyType.LONG, Long.class)
            .put(PropertyType.LONG, long.class)
            .put(PropertyType.DOUBLE, Double.class)
            .put(PropertyType.DOUBLE, double.class)
            .put(PropertyType.DOUBLE, Number.class)
            .put(PropertyType.BOOLEAN, Boolean.class)
            .put(PropertyType.BOOLEAN, boolean.class)
            .put(PropertyType.STRING, String.class)
            .put(PropertyType.DATE, Calendar.class)
            .put(PropertyType.BINARY, Binary.class)

            .build();
    public static final String HIPPOSYSEDIT_PATH = HippoNodeType.HIPPO_PATH;
    public static final String HIPPO_NAMESPACE_PREFIX = "hippo:";
    public static final String HIPPOSYS_NAMESPACE_PREFIX = "hipposys:";
    public static final String HIPPOSYSEDIT_NAMESAPCE_PREFIX = "hipposysedit:";
    public static final String REPORTING_NAMESPACE_PREFIX = "reporting:";
    private static final Predicate<String> INTERNAL_TYPES_PREDICATE = new Predicate<String>() {
        @Override
        public boolean apply(String documentType) {
            return documentType != null && notInternalType(documentType);
        }
    };
    public static final String HIPPO_TRANSLATION = "hippo:translation";
    public static final String NT_UNSTRUCTURED = "nt:unstructured";
    public static final String HIPPOSYSEDIT_SUPERTYPE = "hipposysedit:supertype";

    private static boolean notInternalType(final String documentType) {
        return !documentType.startsWith(HIPPO_NAMESPACE_PREFIX)
                && !documentType.startsWith(HIPPOSYS_NAMESPACE_PREFIX)
                && !documentType.startsWith(HIPPOSYSEDIT_NAMESAPCE_PREFIX)
                && !documentType.startsWith(REPORTING_NAMESPACE_PREFIX)
                && !documentType.equals(NT_UNSTRUCTURED)
                && !documentType.startsWith("hippogallery:");
    }

    private static final Predicate<String> NAMESPACE_PREDICATE = new Predicate<String>() {
        @Override
        public boolean apply(String namespace) {
            return EssentialConst.HIPPO_BUILT_IN_NAMESPACES.contains(namespace);
        }
    };
    private static Logger log = LoggerFactory.getLogger(HippoNodeUtils.class);

    private HippoNodeUtils() {
    }

    public static List<String> getProjectNamespaces(final Session session) {
        try {
            final Node rootNode = session.getRootNode();
            final Node namespace = rootNode.getNode("hippo:namespaces");
            final NodeIterator nodes = namespace.getNodes();
            final Collection<String> namespaceNames = new HashSet<>();
            while (nodes.hasNext()) {
                final Node node = nodes.nextNode();
                final String name = node.getName();
                namespaceNames.add(name);
            }
            return ImmutableList.copyOf(Iterables.filter(namespaceNames, Predicates.not(NAMESPACE_PREDICATE)));
        } catch (RepositoryException e) {
            log.error("Error fetching namespaces", e);
        }
        return Collections.emptyList();

    }

    public static void setSupertype(final Node namespaceNode, final Collection<String> values) throws RepositoryException {
        Node node = getSupertypeNode(namespaceNode);
        final String[] array = values.toArray(new String[values.size()]);
        node.setProperty(HIPPOSYSEDIT_SUPERTYPE, array);
    }

    public static void setSupertype(final Node namespaceNode, final String... values) throws RepositoryException {
        Node node = getSupertypeNode(namespaceNode);
        node.setProperty(HIPPOSYSEDIT_SUPERTYPE, values);
    }

    public static void setUri(final Node namespaceNode, final String uri) throws RepositoryException {
        Node node = getSupertypeNode(namespaceNode);
        node.setProperty("hipposysedit:uri", uri);
    }

    public static void setNodeType(final Node namespaceNode, final String value) throws RepositoryException {
        Node node = getPrototypeNode(namespaceNode);
        node.setPrimaryType(value);
    }

    public static String getStringProperty(final Node node, final String property) throws RepositoryException {
        if (node.hasProperty(property)) {
            return node.getProperty(property).getString();
        } else {
            return null;
        }

    }  public static String getStringProperty(final Node node, final String property, final String defaultValue) throws RepositoryException {
        if (node.hasProperty(property)) {
            return node.getProperty(property).getString();
        } else {
            return defaultValue;
        }
    }

    public static boolean getBooleanProperty(final Node node, final String property) throws RepositoryException {
        return node.hasProperty(property) && node.getProperty(property).getBoolean();
    }

    public static Long getLongProperty(final Node node, final String property, final Long defaultValue) throws RepositoryException {
        final Long value = getLongProperty(node, property);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    public static double getDoubleProperty(final Node node, final String property, final double defaultValue) throws RepositoryException {
        final Double value = getDoubleProperty(node, property);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public static Long getLongProperty(final Node node, final String property) throws RepositoryException {
        if (node.hasProperty(property)) {
            return node.getProperty(property).getLong();
        } else {
            return null;
        }
    }
    public static Double getDoubleProperty(final Node node, final String property) throws RepositoryException {
        if (node.hasProperty(property)) {
            return node.getProperty(property).getDouble();
        } else {
            return null;
        }
    }

    public static Node getNode(final Session session, final String path) throws RepositoryException {
        if (session.nodeExists(path)) {
            return session.getNode(path);
        }
        return null;
    }

    public static Node retrieveExistingNodeOrCreateCopy(final Session session, final String path, final String pathToCopy) throws RepositoryException {
        if (session.nodeExists(path)) {
            return session.getNode(path);
        }

        return copyNode(session, pathToCopy, path);
    }

    public static Node retrieveExistingNodeOrCreateCopy(final Session session, final String path, final Node nodeToCopy) throws RepositoryException {
        if (session.nodeExists(path)) {
            return session.getNode(path);
        }

        return copyNode(session, nodeToCopy, path);
    }

    /**
     * Copy the node at the source path to the destination. When there exists no node at the source path, null will be
     * returned.
     *
     * @param session     the JCR session
     * @param source      the absolute path to the node to copy
     * @param destination the absolute path to copy node to
     * @return the copied destination node or null when source is not available
     * @throws RepositoryException generic repository exception
     */
    public static Node copyNode(final Session session, final String source, final String destination) throws RepositoryException {
        if (!session.nodeExists(source)) {
            return null;
        }

        return copyNode(session, session.getNode(source), destination);
    }

    /**
     * Copy the source node to the destination. When the source node is null, null will be returned.
     *
     * @param session     the JCR session
     * @param source      the node to copy
     * @param destination the absolute path to copy node to
     * @return the copied destination node or null when source is not available
     * @throws RepositoryException generic repository exception
     */
    public static Node copyNode(final Session session, final Node source, final String destination) throws RepositoryException {
        if (source == null) {
            return null;
        }
        final HippoSession hs = (HippoSession) session;
        return hs.copy(source, destination);
    }

    /**
     * @param node
     * @param child
     * @throws RepositoryException
     */
    public static void removeChildNode(final Node node, final String child) throws RepositoryException {
        if (node == null) {
            return;
        }

        // TODO check for multiple deletes?
        if (node.hasNode(child)) {
            final Node childNode = node.getNode(child);
            childNode.remove();
        }
    }

    /**
     * Retrieve the namespace prefix from a prefixed node type. This method will return null when the type is not
     * prefixed.
     *
     * @param type the namespace type, e.g. 'hippo:type'
     * @return the namespace prefix or null, e.g. 'hippo'
     */
    public static String getPrefixFromType(final String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        final int i = type.indexOf(':');
        if (i < 0) {
            return null;
        }
        return type.substring(0, i);
    }

    /**
     * Retrieve the namespace from a prefixed node type. This method will return null when the type empty. When the type
     * is not emtpy, but not prefixed as well, it will return the original type.
     *
     * @param type the namespace type, e.g. 'hippo:type'
     * @return the non prefixed name or null, e.g. 'type'
     */
    public static String getNameFromType(final String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        final int i = type.indexOf(':');
        if (i < 0) {
            return type;
        }
        return type.substring(i + 1);
    }

    public static String getTypeFromPrefixAndName(final String prefix, final String name) {
        if (StringUtils.isBlank(prefix)) {
            return name;
        } else if(StringUtils.isBlank(name)) {
            return prefix;
        } else {
            return new StringBuilder()
                    .append(prefix.trim())
                    .append(':')
                    .append(name.trim())
                    .toString();
        }

    }

    //############################################
    // UTIL
    //############################################

    private static Node getPrototypeNode(final Node namespaceNode) throws RepositoryException {
        return namespaceNode.getNode("hipposysedit:prototypes").getNode(EssentialConst.HIPPOSYSEDIT_PROTOTYPE);
    }

    private static Node getSupertypeNode(final Node namespaceNode) throws RepositoryException {
        return namespaceNode.getNode(EssentialConst.HIPPOSYSEDIT_NODETYPE).getNode(EssentialConst.HIPPOSYSEDIT_NODETYPE);
    }


    /**
     * Partially ripped from org.hippoecm.repository.standardworkflow.FolderWorkflowImpl#prototypes() to retrieve a list
     * of all used hippo document. Which match to the rule according to the specified implementation of the
     * org.onehippo.cms7.essentials.dashboard.utils.JcrMatcher
     *
     * @param session
     * @param matcher
     * @param templates
     * @return
     * @throws RepositoryException
     */
    private static Map<String, Set<String>> prototypes(final Session session, JcrMatcher matcher, final String... templates) {
        Map<String, Set<String>> types = new LinkedHashMap<>();
        if (session == null) {
            // WHEN RUNNING WITHOUT CMS
            log.warn("Session was null, returning empty types");
            return types;
        }
        try {
            QueryManager qmgr = session.getWorkspace().getQueryManager();
            List<Node> queryTemplateNodes = getQueryTemplateNodes(session, templates);
            for (Node queryTemplate : queryTemplateNodes) {
                extractPrototype(matcher, types, qmgr, queryTemplate);
            }
        } catch (RepositoryException ex) {
            log.error(ex.getClass().getName() + ": " + ex.getMessage(), ex);
        }
        return types;
    }

    private static void extractPrototype(final JcrMatcher matcher, final Map<String, Set<String>> types, final QueryManager qmgr, final Node queryTemplate) throws RepositoryException {
        try {
            Set<String> prototypes = new TreeSet<>();
            if (queryTemplate.isNodeType("nt:query")) {
                Query query = qmgr.getQuery(queryTemplate);
                query = qmgr.createQuery(queryTemplate.getProperty("jcr:statement").getString(), query.getLanguage()); // HREPTWO-1266
                QueryResult rs = query.execute();
                for (NodeIterator iter = rs.getNodes(); iter.hasNext(); ) {
                    Node typeNode = iter.nextNode();
                    if (typeNode.getName().equals(EssentialConst.HIPPOSYSEDIT_PROTOTYPE)) {
                        String documentType = typeNode.getPrimaryNodeType().getName();
                        final boolean isTemplate = INTERNAL_TYPES_PREDICATE.apply(documentType);
                        if (isTemplate && (matcher == null || matcher.matches(typeNode))) {
                            prototypes.add(documentType);
                        }
                    } else {
                        prototypes.add(typeNode.getName());
                    }
                }
            }
            types.put(queryTemplate.getName(), prototypes);
        } catch (InvalidQueryException ex) {
            log.error(MessageFormat.format("{0}: {1}", ex.getClass().getName(), ex.getMessage()), ex);
        }
    }

    private static List<Node> getQueryTemplateNodes(Session session, String[] templates) throws RepositoryException {
        List<Node> queryTemplates = new ArrayList<>();
        Node hippoTemplates = session.getRootNode().getNode("hippo:configuration/hippo:queries/hippo:templates");
        for (String template : templates) {
            if (hippoTemplates.hasNode(template)) {
                queryTemplates.add(hippoTemplates.getNode(template));
            }
        }
        return queryTemplates;
    }

    /**
     * Similar to the org.onehippo.cms7.essentials.dashboard.utils.HippoNodeUtils#prototypes(javax.jcr.Session,
     * java.lang.String...) but instead of retrieving document. This method retrieves all available compound types
     *
     * @param session
     * @return
     * @throws RepositoryException
     */
    public static Set<String> getCompounds(final Session session) throws RepositoryException {
        return getCompounds(session, null);
    }

    /**
     * Similar to the org.onehippo.cms7.essentials.dashboard.utils.HippoNodeUtils#prototypes(javax.jcr.Session,
     * java.lang.String...) but instead of retrieving document. This method retrieves all available compound types
     *
     * @param session
     * @param matcher
     * @return a Set of compound names
     * @throws RepositoryException
     */
    public static Set<String> getCompounds(final Session session, final JcrMatcher matcher) throws RepositoryException {
        String query = "//element(*,hipposysedit:namespacefolder)/element(*,mix:referenceable)/element(*,hipposysedit:templatetype)/hipposysedit:prototypes/element(hipposysedit:prototype,hippo:compound)";
        final QueryManager queryManager = session.getWorkspace().getQueryManager();
        @SuppressWarnings("deprecation")
        final QueryResult queryResult = queryManager.createQuery(query, EssentialConst.XPATH).execute();
        Set<String> prototypes = new TreeSet<>();
        for (NodeIterator iter = queryResult.getNodes(); iter.hasNext(); ) {
            Node typeNode = iter.nextNode();
            if (typeNode.getName().equals(EssentialConst.HIPPOSYSEDIT_PROTOTYPE)) {
                String documentType = typeNode.getPrimaryNodeType().getName();
                if (documentType != null && notReservedType(documentType) && (matcher != null && matcher.matches(typeNode))) {
                    prototypes.add(documentType);
                }
            } else {
                prototypes.add(typeNode.getName());
            }
        }
        return prototypes;
    }

    private static boolean notReservedType(final String documentType) {
        return !documentType.startsWith(HIPPO_NAMESPACE_PREFIX) && !documentType.startsWith(HIPPOSYS_NAMESPACE_PREFIX) && !documentType.startsWith(HIPPOSYSEDIT_NAMESAPCE_PREFIX) && !documentType.startsWith(REPORTING_NAMESPACE_PREFIX)
                && !documentType.equals(NT_UNSTRUCTURED) && !documentType.startsWith("hippogallery:");
    }

    /**
     * Similar to the org.onehippo.cms7.essentials.dashboard.utils.HippoNodeUtils#prototypes(javax.jcr.Session,
     * java.lang.String...) but instead of retrieving document. This method retrieves all available compound types
     *
     * @param session
     * @param matcher
     * @return
     * @throws RepositoryException
     */
    public static Set<String> getContentBlocksProviders(final Session session, final JcrMatcher matcher) throws RepositoryException {
        String query = "//element(*,hipposysedit:namespacefolder)/element(*,mix:referenceable)/element(*,hipposysedit:templatetype)[@editor:templates/_default_/cbprovider='true']";
        final QueryManager queryManager = session.getWorkspace().getQueryManager();
        @SuppressWarnings("deprecation")
        final QueryResult queryResult = queryManager.createQuery(query, EssentialConst.XPATH).execute();
        Set<String> prototypes = new TreeSet<>();
        for (NodeIterator iter = queryResult.getNodes(); iter.hasNext(); ) {
            Node typeNode = iter.nextNode();

            if (!Strings.isNullOrEmpty(typeNode.getName()) && typeNode.getParent().getName().equals(EssentialConst.HIPPOSYSEDIT_PROTOTYPE)) {
                String documentType = typeNode.getPrimaryNodeType().getName();
                if (!documentType.startsWith("hippo:") && !documentType.startsWith("hipposys:") && !documentType.startsWith("hipposysedit:") && !documentType.startsWith("reporting:")
                        && !documentType.equals(NT_UNSTRUCTURED) && !documentType.startsWith("hippogallery:") && (matcher != null && matcher.matches(typeNode))) {
                    prototypes.add(documentType);
                }
            } else {
                prototypes.add(typeNode.getName());
            }
        }
        return prototypes;
    }

    /**
     * Retrieves a list of available primary types which are retrieved with the #prototype method
     *
     * @param session
     * @param templates
     * @return
     * @throws RepositoryException
     */
    public static List<String> getPrimaryTypes(final Session session, final String... templates) throws RepositoryException {
        final Map<String, Set<String>> prototypes = prototypes(session, null, templates);
        return extractTypes(prototypes);
    }

    private static List<String> extractTypes(final Map<String, Set<String>> prototypes) {
        final List<String> set = new ArrayList<>();
        final Collection<Set<String>> values = prototypes.values();
        for (Set<String> collection : values) {
            set.addAll(collection);
        }
        return set;
    }

    /**
     * Retrieves a list of available primary types which are retrieved with the #prototype method  and filtererd with
     * the org.onehippo.cms7.essentials.dashboard.utils.JcrMatcher implementation
     *
     * @param session
     * @param templates
     * @return
     * @throws RepositoryException
     */
    public static List<String> getPrimaryTypes(final Session session, final JcrMatcher matcher, final String... templates) throws RepositoryException {
        final Map<String, Set<String>> prototypes = prototypes(session, matcher, templates);
        return extractTypes(prototypes);
    }


    public static String getDisplayValue(final Session session, final String type) throws RepositoryException {
        String name = type;
        final String resolvedPath = resolvePath(type);
        if (session.itemExists(resolvedPath)) {
            Node node = session.getNode(resolvedPath);
            try {
                name = NodeNameCodec.decode(node.getName());
                if (node.isNodeType("hippo:translated")) {
                    Locale locale = Locale.getDefault();
                    NodeIterator nodes = node.getNodes(HIPPO_TRANSLATION);
                    while (nodes.hasNext()) {
                        Node child = nodes.nextNode();
                        if (child.isNodeType(HIPPO_TRANSLATION) && !child.hasProperty("hippo:property")) {
                            String language = child.getProperty("hippo:language").getString();
                            if (locale.getLanguage().equals(language)) {
                                return child.getProperty("hippo:message").getString();
                            }
                        }
                    }
                }
            } catch (RepositoryException ex) {
                log.error(ex.getMessage());
            }
        }
        return name;
    }

    public static String resolvePath(String type) {
        if (type.contains(":")) {
            return "/hippo:namespaces/" + type.replace(':', '/');
        } else {
            return "/hippo:namespaces/system/" + type;
        }
    }


    public static void checkName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("No name specified");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid name; only alphabetic characters allowed in lower- or uppercase");
        }
    }

    public static void checkURI(String name) {
        if (name == null) {
            throw new IllegalArgumentException("No URI specified");
        }
        if (!URL_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid URL; ");
        }
    }

    /**
     * Populates properties of given node
     *
     * @param node    node
     * @param restful restful representation
     */
    public static void populateProperties(final Node node, final NodeRestful restful) {
        try {
            restful.setName(node.getName());
            restful.setPath(node.getPath());
            final PropertyIterator properties = node.getProperties();
            while (properties.hasNext()) {
                final Property property = properties.nextProperty();
                restful.addProperty(extractProperty(property));
            }


        } catch (RepositoryException e) {
            log.error("Error populating node", e);
        }
    }

    private static PropertyRestful extractProperty(final Property property) throws RepositoryException {
        final PropertyRestful restful = new PropertyRestful(property.getName());
        restful.setName(property.getName());
        if (property.isMultiple()) {
            restful.setMultivalue(true);
            final Value[] values = property.getValues();
            for (Value value : values) {
                restful.addValue(value.getString());
                restful.setType(value.getType());
            }

        } else {
            restful.setValue(property.getValue().getString());
            restful.setType(property.getType());
        }

        return restful;
    }


}
