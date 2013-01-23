/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.tut.fast.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

/**
 *
 * @author Johannes
 */
public class NamespaceContextImpl implements NamespaceContext{

        Map<String,String> namespaceMap = new HashMap<String,String>();

        public NamespaceContextImpl(){
            
        }
        
        public NamespaceContextImpl(Map<String,String> namespaces){
            namespaceMap.putAll(namespaces);
        }
        
        public void addNamespace(String prefix, String namespaceUri){
            if(namespaceMap.containsKey(prefix)){
                return;
            }
            if(prefix.length() > 0){
                namespaceMap.put(prefix,namespaceUri);
            }
        }
        public void addNamespace(QName name){
            addNamespace(name.getPrefix(),name.getNamespaceURI());
        }

        @Override
        public String getNamespaceURI(String prefix) {
            return namespaceMap.get(prefix);
        }

        @Override
        public String getPrefix(String namespaceURI) {
            for(Entry<String,String> pair : namespaceMap.entrySet()){
                if(pair.getValue().equals(namespaceURI)){
                    return pair.getKey();
                }
            }
            return null;
        }

        @Override
        public Iterator getPrefixes(String namespaceURI) {
            List<String> prefixes = new ArrayList<String>();
            for(Entry<String,String> pair : namespaceMap.entrySet()){
                if(pair.getValue().equals(namespaceURI)){
                    prefixes.add(pair.getKey());
                }
            }
            return prefixes.listIterator();
        }

}
