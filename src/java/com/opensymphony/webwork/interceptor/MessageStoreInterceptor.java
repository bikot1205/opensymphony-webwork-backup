/*
 * Copyright (c) 2002-2007 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.interceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ValidationAware;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * <!-- START SNIPPET: description -->
 * 
 * An interceptor to store {@link ValidationAware} action's messages / errors and field errors into 
 * Http Session, such that it will be retrieveable at a later stage. This allows the action's message /
 * errors and field errors to be available longer that just the particular http request.
 * 
 * <p/>
 * 
 * In the 'STORE' mode, the interceptor will store the {@link ValidationAware} action's message / errors 
 * and field errors into Http session.
 * 
 * <p/>
 * 
 * In the 'RETRIEVE' mode, the interceptor will retrieve the stored action's message / errors  and field
 * errors and put them back into the {@link ValidationAware} action.
 * 
 * <p/>
 * 
 * The interceptor does nothing in the 'NONE' mode, which is the default.
 * 
 * <p/>
 * 
 * The operation mode could be switched using :- <p/>
 * 1] Setting the iterceptor parameter eg.
 * <pre>
 *   &lt;action name="submitApplication" ...&gt;
 *      &lt;interceptor-ref name="store"&gt;
 *         &lt;param name="operationMode"&gtl;STORE&lt;/param&gt;
 *      &lt;/interceptor-ref&gt;
 *      &lt;interceptor-ref name="defaultStack" /&gt;
 *      ....
 *   &lt;/action&gt;
 * </pre>
 * 
 * 2] Through request parameter (allowRequestParameterSwitch must be 'true' which is the default)
 * <pre>
 *   // the request will have the operation mode in 'STORE'
 *   http://localhost:8080/context/submitApplication.action?operationMode=STORE
 * </pre>
 * 
 * <!-- END SNIPPET: description -->
 * 
 * 
 * <!-- START SNIPPET: parameters -->
 * 
 * <ul>
 * 		<li>allowRequestParameterSwitch - To enable request parameter that could switch the operation mode 
 * 										  of this interceptor. </li>
 * 		<li>requestParameterSwitch - The request parameter that will indicate what mode this 
 * 									 interceptor is in. </li>
 * 		<li>operationMode - The operation mode this interceptor should be in 
 * 							(either 'STORE', 'RETRIEVE' or 'NONE'). 'NONE' being the default.</li>
 * </ul>
 * 
 * <!-- END SNIPPET: parameters -->
 * 
 * <p/>
 * 
 * <!-- START SNIPPET: extending -->
 * 
 * The following method could be overriden :-
 * <ul>
 * 	<li>getRequestOperationMode - get the operation mode of this interceptor based on the request parameters</li>
 *  <li>mergeCollection - merge two collections</li>
 *  <li>mergeMap - merge two map</li>
 * </ul>
 * 
 * <!-- END SNIPPET: extending -->
 * 
 * <pre>
 * <!-- START SNIPPET: example -->
 * 
 * &lt;action name="submitApplication" ....&gt;
 *    &lt;interceptor-ref name="store"&gt;
 *    	&lt;param name="operationMode">STORE&lt;/param&gt;
 *    &lt;/interceptor-ref&gt;
 *    &lt;interceptor-ref name="defaultStack" /&gt;
 *    &lt;result name="input" type="redirect">applicationFailed.action&lt;/result&gt;
 *    &lt;result type="dispatcher"&gt;applicationSuccess.jsp&lt;/result&gt;
 * &lt;/action&gt;
 * 
 * &lt;action name="applicationFailed" ....&gt;
 *    &lt;interceptor-ref name="store"&gt;
 *       &lt;param name="operationMode"&gt;RETRIEVE&lt;/param&gt;
 *    &lt;/interceptor-ref&gt;
 *    &lt;-- this result will get executed if its field/error message(s) --&gt;
 *    &lt;result name="input"&gt;applicationFailed.jsp&lt;/result&gt;
 *    &lt;-- this result will get executed if its action message(s) --&gt;
 *    &lt;result&gt;applicationFailed.jsp&lt;/result&gt;
 * &lt;/action&gt;
 * 
 * <!-- END SNIPPET: example -->
 * </pre>
 * 
 * <!-- START SNIPPET: exampleDescription -->
 * 
 * With the example above, 'submitApplication.action' will have the action messages / errors / field errors stored
 * in the Http Session. Later when needed, (in this case, when 'applicationFailed.action' is fired, it
 * will get the action messages / errors / field errors stored in the Http Session and put them back into 
 * the action. 
 * 
 * <!-- END SNIPPET: exampleDescription -->
 * 
 * @author tmjee
 * @version $Date$ $Id$
 */
public class MessageStoreInterceptor implements Interceptor {

	private static final long serialVersionUID = 4491997514314242420L;
	
	private static final Log _log = LogFactory.getLog(MessageStoreInterceptor.class);
	
	
	public static final String STORE_MODE = "STORE";
	public static final String RETRIEVE_MODE = "RETRIEVE";
	public static final String NONE = "NONE";
	
	private boolean allowRequestParameterSwitch = true;
	private String requestParameterSwitch = "operationMode";
	private String operationMode = NONE;
	
	public static String fieldErrorsSessionKey = "__MessageStoreInterceptor_FieldErrors_SessionKey";
	public static String actionErrorsSessionKey = "__MessageStoreInterceptor_ActionErrors_SessionKey";
	public static String actionMessagesSessionKey = "__MessageStoreInterceptor_ActionMessages_SessionKey";
	
	
	/**
	 * If true allows request parameter (default to operationMode) to decide which mode
	 * this interceptor should operates in. 
	 * 
	 * @param allowRequestParameterSwitch
	 */
	public void setAllowRequestParameterSwitch(boolean allowRequestParameterSwitch) {
		this.allowRequestParameterSwitch = allowRequestParameterSwitch;
	}
	
	/**
	 * Returns true if the request parameter (default to operationMode) is used to decide
	 * which mode this interceptor should operates in.
	 *
	 * @return boolean
	 */
	public boolean getAllowRequestParameterSwitch() {
		return this.allowRequestParameterSwitch;
	}
	
	
	/**
	 * Set the request parameter string that is used to determine the mode this
	 * interceptor should operates in .
	 * 
	 * @see setRequestParameterSwitch
	 * @param requestParameterSwitch
	 */
	public void setRequestParameterSwitch(String requestParameterSwitch) {
		this.requestParameterSwitch = requestParameterSwitch;
	}
	
	/**
	 * Returns the request parameter string that is used to determine the mode this 
	 * interceptor should operatoes in.
	 * 
	 * @see setRequestParameterSwitch
	 * @return String
	 */
	public String getRequestParameterSwitch() {
		return this.requestParameterSwitch;
	}
	
	
	/**
	 * Set the operation mode this interceptor is operating in (valid values are)
	 * <ul>
	 * 	<li>STORE</li>
	 * 	<li>RETRIEVE</li>
	 * 	<li>NONE (this is the default)</li>
	 * </ul>
	 * @param operationMode
	 */
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	
	/**
	 * Returns the operation mode this interceptor is operating in (valid values are)
	 * <ul>
	 * 	<li>STORE</li>
	 * 	<li>RETRIEVE</li>
	 * 	<li>NONE (this is the default)</li>
	 * </ul>
	 * 
	 * @return String
	 */
	public String getOperationModel() {
		return this.operationMode;
	}
	

	/**
	 * No operation.
	 */
	public void destroy() {
	}

	/**
	 * No operatio9n.
	 */
	public void init() {
	}

	/**
	 * Intercept the incomming request and store messages into HttpSession / 
	 * retrieve messages from HttpSession / do nothing 
	 * depending on the mode this interceptor is operating in.
	 * 
	 * @return String
	 */
	public String intercept(ActionInvocation invocation) throws Exception {
		_log.debug("entering MessageStoreInterceptor ...");
		
		before(invocation);
		String result = invocation.invoke();
		after(invocation, result);
		
		_log.debug("exit executing MessageStoreInterceptor");
		return result;
	}
	
	/**
	 * Handle the retrieving of field errors / action messages / field errors, which is
	 * done before action invocation, and the <code>operationMode</code> is 'RETRIEVE'.
	 * 
	 * @param invocation
	 * @throws Exception
	 */
	protected void before(ActionInvocation invocation) throws Exception {
		String reqOperationMode = getRequestOperationMode(invocation);
		
		if (RETRIEVE_MODE.equalsIgnoreCase(reqOperationMode) || 
				RETRIEVE_MODE.equalsIgnoreCase(operationMode)) {
			
			Object action = invocation.getAction();
			if (action instanceof ValidationAware) {
				// retrieve error / message from session
				Map session = (Map) invocation.getInvocationContext().get(ActionContext.SESSION);
				ValidationAware validationAwareAction = (ValidationAware) action;
				
				_log.debug("retrieve error / message from session to populate into action ["+action+"]");
				
				Collection actionErrors = (Collection) session.get(actionErrorsSessionKey);
				Collection actionMessages = (Collection) session.get(actionMessagesSessionKey);
				Map fieldErrors = (Map) session.get(fieldErrorsSessionKey);
				
				if (actionErrors != null && actionErrors.size() > 0) {
					Collection mergedActionErrors = mergeCollection(validationAwareAction.getActionErrors(), actionErrors);
					validationAwareAction.setActionErrors(mergedActionErrors);
				}
				
				if (actionMessages != null && actionMessages.size() > 0) {
					Collection mergedActionMessages = mergeCollection(validationAwareAction.getActionMessages(), actionMessages);
					validationAwareAction.setActionMessages(mergedActionMessages);
				}
				
				if (fieldErrors != null && fieldErrors.size() > 0) {
					Map mergedFieldErrors = mergeMap(validationAwareAction.getFieldErrors(), fieldErrors);
					validationAwareAction.setFieldErrors(mergedFieldErrors);
				}
				session.remove(actionErrorsSessionKey);
				session.remove(actionMessagesSessionKey);
				session.remove(fieldErrorsSessionKey);
			}
		}
	}
	
	/**
	 * Handle the storing of field errors / action messages / field errors, which is
	 * done after action invocation, and the <code>operationMode</code> is in 'STORE'.
	 * 
	 * @param invocation
	 * @param result
	 * @throws Exception
	 */
	protected void after(ActionInvocation invocation, String result) throws Exception {
		
		String reqOperationMode = getRequestOperationMode(invocation);
		if (STORE_MODE.equalsIgnoreCase(reqOperationMode) ||
				STORE_MODE.equalsIgnoreCase(operationMode)) {
			
			Object action = invocation.getAction();
			if (action instanceof ValidationAware) {
				// store error / messages into session
				Map session = (Map) invocation.getInvocationContext().get(ActionContext.SESSION);
				
				_log.debug("store action ["+action+"] error/messages into session ");
				
				ValidationAware validationAwareAction = (ValidationAware) action;
				session.put(actionErrorsSessionKey, validationAwareAction.getActionErrors());
				session.put(actionMessagesSessionKey, validationAwareAction.getActionMessages());
				session.put(fieldErrorsSessionKey, validationAwareAction.getFieldErrors());
			}
			else {
				_log.debug("Action ["+action+"] is not ValidationAware, no message / error that are storeable");
			}
		}
	}
	
	
	/**
	 * Get the operationMode through request paramter, if <code>allowRequestParameterSwitch</code>
	 * is 'true', else it simply returns 'NONE', meaning its neither in the 'STORE_MODE' nor
	 * 'RETRIEVE_MODE'.
	 * 
	 * @return String
	 */
	protected String getRequestOperationMode(ActionInvocation invocation) {
		String reqOperationMode = NONE;
		if (allowRequestParameterSwitch) {
			Map reqParams = (Map) invocation.getInvocationContext().get(ActionContext.PARAMETERS);
			boolean containsParameter = reqParams.containsKey(requestParameterSwitch);
			if (containsParameter) {
				String[] reqParamsArr = (String[]) reqParams.get(requestParameterSwitch);
				if (reqParamsArr != null && reqParamsArr.length > 0) {
					reqOperationMode = reqParamsArr[0];
				}
			}
		}
		return reqOperationMode;
	}
	
	/**
	 * Merge <code>col1</code> and <code>col2</code> and return the composite 
	 * <code>Collection</code>.
	 * 
	 * @param col1
	 * @param col2
	 * @return Collection
	 */
	protected Collection mergeCollection(Collection col1, Collection col2) {
		Collection _col1 = (col1 == null ? new ArrayList() : col1);
		Collection _col2 = (col2 == null ? new ArrayList() : col2);
		_col1.addAll(_col2);
		return _col1;
	}
	
	/**
	 * Merge <code>map1</code> and <code>map2</code> and return the composite
	 * <code>Map</code>
	 * 
	 * @param map1
	 * @param map2
	 * @return Map
	 */
	protected Map mergeMap(Map map1, Map map2) {
		Map _map1 = (map1 == null ? new LinkedHashMap() : map1);
		Map _map2 = (map2 == null ? new LinkedHashMap() : map2);
		_map1.putAll(_map2);
		return _map1;
	}

}