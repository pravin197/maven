package org.apache.maven.xml.sax.filter;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import static org.xmlunit.assertj.XmlAssert.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.maven.xml.sax.filter.ReactorDependencyXMLFilter;
import org.junit.Test;
import org.xml.sax.SAXException;

public class ReactorDependencyXMLFilterTest extends AbstractXMLFilterTests
{
    @Override
    protected ReactorDependencyXMLFilter getFilter()
        throws TransformerException, SAXException, ParserConfigurationException
    {
        return new ReactorDependencyXMLFilter( r -> "1.0.0" );
    }

    @Test
    public void testDefaultDependency() throws Exception
    {
        String input = "<dependency>"
            + "<groupId>GROUPID</groupId>"
            + "<artifactId>ARTIFACTID</artifactId>"
            + "<version>VERSION</version>"
            + "</dependency>";
        String expected = input;
        
        String actual = transform( input );
        
        assertThat( actual ).isEqualTo( expected );
    }

    @Test
    public void testManagedDependency() throws Exception
    {
        ReactorDependencyXMLFilter filter = new ReactorDependencyXMLFilter( r -> null );
        
        String input = "<dependency>"
            + "<groupId>GROUPID</groupId>"
            + "<artifactId>ARTIFACTID</artifactId>"
            + "</dependency>";
        String expected = input;
        
        String actual = transform( input, filter );
        
        assertThat( actual ).isEqualTo( expected );
    }

    @Test
    public void testReactorDependency() throws Exception
    {
        String input = "<dependency>"
                        + "<groupId>GROUPID</groupId>"
                        + "<artifactId>ARTIFACTID</artifactId>"
                        + "</dependency>";
        String expected = "<dependency>"
                        + "<groupId>GROUPID</groupId>"
                        + "<artifactId>ARTIFACTID</artifactId>"
                        + "<version>1.0.0</version>"
                        + "</dependency>";
        
        String actual = transform( input );
        
        assertThat( actual ).isEqualTo( expected );
    }

    @Test
    public void testReactorDependencyLF() throws Exception
    {
        String input = "<dependency>\n"
                        + "  <groupId>GROUPID</groupId>\n"
                        + "  <artifactId>ARTIFACTID</artifactId>\n"
                        + "  <!-- include version here --> " 
                        + "</dependency>";
        String expected = "<dependency>\n"
                        + "  <groupId>GROUPID</groupId>\n"
                        + "  <artifactId>ARTIFACTID</artifactId>\n"
                        + "  <!-- include version here -->\n" 
                        + "  <version>1.0.0</version>\n"
                        + "</dependency>";
        
        String actual = transform( input );
        
        assertThat( actual ).and( expected ).ignoreWhitespace().areIdentical();
    }

}