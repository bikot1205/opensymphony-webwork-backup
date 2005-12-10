/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork;


/**
 * Constants used by WebWork. The constants can be used to get or set objects out of the action context
 * or other collections.<p>
 * <p/>
 * Example:
 * <ul><code>ActionContext.getContext().put(HTTP_REQUEST, request);</code></ul>
 * <p/>
 * or
 * <p/>
 * <ul><code>
 * ActionContext context = ActionContext.getContext();<br>
 * HttpServletRequest request = (HttpServletRequest)context.get(HTTP_REQUEST);</code></ul>
 *
 * @author Patrick Lightbody
 * @author Bill Lynch (docs)
 */
public interface WebWorkStatics {
    //~ Static fields/initializers /////////////////////////////////////////////

    /**
     * Constant for the HTTP request object.
     */
    public static final String HTTP_REQUEST = "com.opensymphony.xwork.dispatcher.HttpServletRequest";

    /**
     * Constant for the HTTP response object.
     */
    public static final String HTTP_RESPONSE = "com.opensymphony.xwork.dispatcher.HttpServletResponse";

    /**
     * Constant for an HTTP {@link javax.servlet.RequestDispatcher request dispatcher}.
     */
    public static final String SERLVET_DISPATCHER = "com.opensymphony.xwork.dispatcher.ServletDispatcher";

    /**
     * Constant for the {@link javax.servlet.ServletConfig servlet config} object.
     */
    public static final String SERVLET_CONFIG = "com.opensymphony.xwork.dispatcher.ServletConfig";

    /**
     * Constant for the XWork compontent interceptor.
     */
    public static final String COMPONENT_MANAGER = "com.opensymphony.xwork.interceptor.component.ComponentManager";

    /**
     * Constant for the JSP {@link javax.servlet.jsp.PageContext page context}.
     */
    public static final String PAGE_CONTEXT = "com.opensymphony.xwork.dispatcher.PageContext";
}