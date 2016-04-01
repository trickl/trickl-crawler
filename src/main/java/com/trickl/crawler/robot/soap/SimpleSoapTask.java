/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trickl.crawler.robot.soap;

import com.trickl.crawler.protocol.soap.SoapProtocol;
import java.beans.PropertyEditor;
import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import org.apache.droids.api.Protocol;
import org.springframework.ws.client.core.WebServiceTemplate;

public class SimpleSoapTask<RequestType> implements SoapTask, Serializable {

    private URI uri;
    private final Date started;
    private boolean aborted = false;
    private URI action;
    private WebServiceTemplate webServiceTemplate = null;
    private RequestType request;
    private PropertyEditor propertyEditor;

    public SimpleSoapTask() {
        this.started = new Date();
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public String getId() {
        return uri.toString();
    }

    @Override
    public Date getTaskDate() {
        return started;
    }

    @Override
    public void abort() {
        aborted = true;
    }

    @Override
    public boolean isAborted() {
        return aborted;
    }

    @Override
    public URI getSOAPAction() {
        return action;
    }

    /**
     * @return the uri
     */
    public URI getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * @return the action
     */
    public URI getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(URI action) {
        this.action = action;
    }

    /**
     * @return the webServiceTemplate
     */
    public WebServiceTemplate getWebServiceTemplate() {
        return webServiceTemplate;
    }

    public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    @Override
    public <T> T getSOAPRequest(Class<T> cls) {
        return (T) request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(RequestType request) {
        this.request = request;
    }

    public void setPropertyEditor(PropertyEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
    }

    @Override
    public Protocol getProtocol() {
        SoapProtocol soapProtocol = new SoapProtocol();
        soapProtocol.setAction(action);
        soapProtocol.setRequest(request);
        soapProtocol.setPropertyEditor(propertyEditor);
        soapProtocol.setWebServiceTemplate(webServiceTemplate);
        return soapProtocol;
    }
}
