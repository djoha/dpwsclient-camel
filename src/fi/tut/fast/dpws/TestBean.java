/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.tut.fast.dpws;

import org.apache.camel.Exchange;

/**
 *
 * @author Johannes
 */
public class TestBean {
 
    public void stepOne(Exchange ex){
        String body = ex.getIn().getBody(String.class);
        ex.getIn().setBody(body + " - StepOne");
    }
    
    public void stepTwo(Exchange ex){
        
        String body = ex.getIn().getBody(String.class);
        ex.getIn().setBody(body + " - StepTwo");
        
    }
    
        
    public void stepThree(Exchange ex){
        
        String body = ex.getIn().getBody(String.class);
        ex.getIn().setBody(body + " - stepThree");
        
    }
    
}
