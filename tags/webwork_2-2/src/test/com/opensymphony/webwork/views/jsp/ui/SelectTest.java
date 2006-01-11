/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jsp.ui;

import com.opensymphony.webwork.TestAction;
import com.opensymphony.webwork.views.jsp.AbstractUITagTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author Matt Ho <a href="mailto:matt@enginegreen.com">&lt;matt@enginegreen.com&gt;</a>
 * @author tm_jee
 * @version $Id$
 */
public class SelectTest extends AbstractUITagTest {

    /**
     * Tests WW-455: Select tag template does not work properly for Object like BigDecimal.
     */
    public void testBigDecimal() throws Exception {
        BigDecimalObject hello = new BigDecimalObject("hello", new BigDecimal(1));
        BigDecimalObject foo = new BigDecimalObject("foo", new BigDecimal(2));

        TestAction testAction = (TestAction) action;

        Collection collection = new ArrayList(2);
        // expect strings to be returned, we're still dealing with HTTP here!
        collection.add("hello");
        collection.add("foo");
        testAction.setCollection(collection);

        List list2 = new ArrayList();
        list2.add(hello);
        list2.add(foo);
        list2.add(new BigDecimalObject("<cat>", new BigDecimal(1.500)));
        testAction.setList2(list2);

        SelectTag tag = new SelectTag();
        tag.setPageContext(pageContext);
        tag.setLabel("mylabel");
        tag.setName("collection");
        tag.setList("list2");
        tag.setListKey("name");
        tag.setListValue("bigDecimal");
        tag.setMultiple("true");
        tag.setOnmousedown("alert('onmousedown');");
        tag.setOnmousemove("alert('onmousemove');");
        tag.setOnmouseout("alert('onmouseout');");
        tag.setOnmouseover("alert('onmouseover');");
        tag.setOnmouseup("alert('onmouseup');");

        tag.doStartTag();
        tag.doEndTag();

        verify(SelectTag.class.getResource("Select-3.txt"));
    }

    public class BigDecimalObject {
        private String name;
        private BigDecimal bigDecimal;

        public BigDecimalObject(String name, BigDecimal bigDecimal) {
            this.name = name;
            this.bigDecimal = bigDecimal;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getBigDecimal() {
            return bigDecimal;
        }
    }
    
    public void testNullList() throws Exception {
    	TestAction testAction = (TestAction) action;
    	testAction.setList2(null);
    	
    	SelectTag tag = new SelectTag();
    	tag.setName("collection");
    	tag.setList("list2");
    	tag.setLabel("tmjee_name");
    	
    	tag.setPageContext(pageContext);
    	try {
    		tag.doStartTag();
    		tag.doEndTag();
    		fail("exception should have been thrown value of select tag is null");
    	}
    	catch(Exception e) {
    		assertTrue(true);
    	}
    }
    

    public void testEmptyList() throws Exception {
    	TestAction testAction = (TestAction) action;
    	testAction.setList2(new ArrayList());
    	
    	SelectTag tag = new SelectTag();
    	tag.setName("collection");
    	tag.setList("list2");
    	tag.setLabel("tmjee_name");
    	
    	tag.setPageContext(pageContext);
    	tag.doStartTag();
    	tag.doEndTag();
    	
    	verify(SelectTag.class.getResource("Select-4.txt"));
    }
    
    public void testMultiple() throws Exception {
        TestAction testAction = (TestAction) action;
        Collection collection = new ArrayList(2);
        collection.add("hello");
        collection.add("foo");
        testAction.setCollection(collection);
        testAction.setList(new String[][]{
                {"hello", "world"},
                {"foo", "bar"},
                {"cat", "dog"}
        });

        SelectTag tag = new SelectTag();
        tag.setPageContext(pageContext);
        tag.setLabel("mylabel");
        tag.setName("collection");
        tag.setList("list");
        tag.setListKey("top[0]");
        tag.setListValue("top[1]");
        tag.setMultiple("true");
        tag.setOnmousedown("alert('onmousedown');");
        tag.setOnmousemove("alert('onmousemove');");
        tag.setOnmouseout("alert('onmouseout');");
        tag.setOnmouseover("alert('onmouseover');");
        tag.setOnmouseup("alert('onmouseup');");

        tag.doStartTag();
        tag.doEndTag();

        verify(SelectTag.class.getResource("Select-2.txt"));
    }

    public void testSimple() throws Exception {
        TestAction testAction = (TestAction) action;
        testAction.setFoo("hello");
        testAction.setList(new String[][]{
                {"hello", "world"},
                {"foo", "bar"}
        });

        SelectTag tag = new SelectTag();
        tag.setPageContext(pageContext);
        tag.setEmptyOption("true");
        tag.setLabel("mylabel");
        tag.setName("foo");
        tag.setList("list");
        tag.setListKey("top[0]");
        tag.setListValue("top[1]");

        // header stuff
        tag.setHeaderKey("headerKey");
        tag.setHeaderValue("headerValue");

        // empty option
        tag.setEmptyOption("true");

        tag.doStartTag();
        tag.doEndTag();

        verify(SelectTag.class.getResource("Select-1.txt"));
    }
}
