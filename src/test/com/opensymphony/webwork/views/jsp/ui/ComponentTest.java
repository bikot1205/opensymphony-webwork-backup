/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jsp.ui;

import com.opensymphony.webwork.TestAction;
import com.opensymphony.webwork.views.jsp.AbstractUITagTest;


/**
 * @author Matt Ho <a href="mailto:matt@enginegreen.com">&lt;matt@enginegreen.com&gt;</a>
 * @version $Id$
 */
public class ComponentTest extends AbstractUITagTest {
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Note -- this test uses empty.vm, so it's basically clear
     */
    public void testSimple() throws Exception {
        TestAction testAction = (TestAction) action;
        testAction.setFoo("bar");

        ComponentTag tag = new ComponentTag();
        tag.setPageContext(pageContext);
        tag.setLabel("'mylabel'");
        tag.setName("'myname'");
        tag.setValue("foo");

        int result = tag.doEndTag();

        verify(ComponentTag.class.getResource("Component-1.txt"));
    }

    /**
     * executes a component test passing in a custom parameter. it also executes calling a custom template using an
     * absolute reference.
     */
    public void testWithParam() throws Exception {
        TestAction testAction = (TestAction) action;
        testAction.setFoo("bar");

        ComponentTag tag = new ComponentTag();
        tag.setPageContext(pageContext);
        tag.setLabel("mylabel");
        tag.setName("myname");
        tag.setValue("foo");
        tag.setTheme("'test'");
        tag.setTemplate("'Component.vm'");

        tag.doStartTag();
        tag.addParameter("hello", "world");
        tag.addParameter("argle", "bargle");
        tag.addParameter("glip", "glop");
        tag.addParameter("array", new String[]{"a", "b", "c"});
        tag.addParameter("obj", tag);

        int result = tag.doEndTag();

        //        System.out.println(writer);
        verify(ComponentTag.class.getResource("Component-param.txt"));
    }
}
