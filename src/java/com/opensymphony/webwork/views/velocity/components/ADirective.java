package com.opensymphony.webwork.views.velocity.components;

import com.opensymphony.webwork.components.Component;
import com.opensymphony.webwork.components.Href;
import com.opensymphony.xwork.util.OgnlValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see Href
 * @see HrefDirective
 * Function same as HrefDirective
 */
public class ADirective extends AbstractDirective {
    public String getBeanName() {
        return "a";
    }

    protected Component getBean(OgnlValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Href(stack, req, res);
    }

    public int getType() {
        return BLOCK;
    }
}
