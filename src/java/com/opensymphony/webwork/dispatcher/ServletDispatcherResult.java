/*
 * Copyright (c) 2002-2004 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.dispatcher;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * Pulls the HttpServletRequest and HttpServletResponse objects from the action context and creates a
 * RequestDispatcher to the location specified as the parameter "location" and then forwards it. The
 * following parameter is required:
 *
 * <ul><li>location - the page or resource to dispatch to</li></ul>
 *
 * @see javax.servlet.RequestDispatcher
 * @author Patrick Lightbody
 */
public class ServletDispatcherResult extends WebWorkResultSupport {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Log log = LogFactory.getLog(ServletDispatcherResult.class);

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Dispatches to the given location. Does its forward via a RequestDispatcher. If the
     * dispatch fails a 404 error will be sent back in the http response.
     *
     * @param finalLocation the location to dispatch to.
     * @param invocation the execution state of the action
     * @throws Exception if an error occurs. If the dispatch fails the error will go back via the
     *      HTTP request.
     */
    public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Forwarding to location " + finalLocation);
        }

        PageContext pageContext = ServletActionContext.getPageContext();

        if (pageContext != null) {
            pageContext.include(finalLocation);
        } else {
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpServletResponse response = ServletActionContext.getResponse();
            RequestDispatcher dispatcher = request.getRequestDispatcher(finalLocation);

            // if the view doesn't exist, let's do a 404
            if (dispatcher == null) {
                response.sendError(404, "result '" + finalLocation + "' not found");
                return;
            }

            // If we're included, then include the view
            // Otherwise do forward 
            // This allow the page to, for example, set content type 
            if (!response.isCommitted() && (request.getAttribute("javax.servlet.include.servlet_path") == null)) {
                request.setAttribute("webwork.view_uri", finalLocation);
                request.setAttribute("webwork.request_uri", request.getRequestURI());

                dispatcher.forward(request, response);
            } else {
                dispatcher.include(request, response);
            }
        }
    }
}
