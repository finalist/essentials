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

package org.onehippo.cms7.essentials.plugins.contentblocks.model.contentblocks;

import javax.xml.bind.annotation.XmlRootElement;

import org.onehippo.cms7.essentials.dashboard.model.Restful;
import org.onehippo.cms7.essentials.dashboard.rest.KeyValueRestful;

/**
 * @version "$Id$"
 */
@XmlRootElement(name = "items")
public class Compound extends KeyValueRestful implements Restful {

    private static final long serialVersionUID = 1L;

    private String path;

    public Compound() {
    }

    public Compound(final String key, final String value, final String path) {
        super(key, value);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }
}