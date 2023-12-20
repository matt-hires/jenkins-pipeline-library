package at.matthires.jenkins.tests;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import groovy.lang.*;
import groovy.util.*;

public class VersionTest
  extends at.matthires.jenkins.tests.BaseDeclarativePipelineTest {
;
@org.junit.jupiter.params.ParameterizedTest() @org.junit.jupiter.params.provider.CsvSource(value={"1.0.0-SNAPSHOT, 1.0.1-SNAPSHOT","1.1.1-SNAPSHOT,1.1.2","1.2.1,1.2.2-SNAPSHOT","1,1.0.1","1.0,1.0.1","1.1,1.1.1","1.2.2,1.2.3"}) public  void testIsPatchVersionUpgrade(java.lang.String versionA, java.lang.String versionB) { }
@org.junit.jupiter.params.ParameterizedTest() @org.junit.jupiter.params.provider.CsvSource(value={"1.0.0-SNAPSHOT, 1.1.0-SNAPSHOT","1.0.0-SNAPSHOT, 2.0.0-SNAPSHOT","1.1.1-SNAPSHOT,1.2.0","1.2.1,1.0.1-SNAPSHOT","2.0.0,1.0.0-SNAPSHOT","3.0.0,2.1.0","1,2","1.0,2.1","20,11"}) public  void testNoPatchVersionUpgrade(java.lang.String versionA, java.lang.String versionB) { }
@org.junit.jupiter.params.ParameterizedTest() @org.junit.jupiter.params.provider.CsvSource(value={"1.0.0-SNAPSHOT, 1.1.0-SNAPSHOT","1.1.1-SNAPSHOT,1.2.0","1.2.1,1.3.2-SNAPSHOT","1,1.1","1.0,1.1","1.1,1.2.1","1.2.3,1.3"}) public  void testIsMinorVersionUpgrade(java.lang.String versionA, java.lang.String versionB) { }
@org.junit.jupiter.params.ParameterizedTest() @org.junit.jupiter.params.provider.CsvSource(value={"1.0.0-SNAPSHOT, 1.0.1-SNAPSHOT","1.0.0-SNAPSHOT,2.1.0","1.2.1,1.1.2-SNAPSHOT","1,1.0.1","1.0,1.0.1","1,2.0.0","1.2.3,2.2.3"}) public  void testNoMinorVersionUpgrade(java.lang.String versionA, java.lang.String versionB) { }
@org.junit.jupiter.params.ParameterizedTest() @org.junit.jupiter.params.provider.CsvSource(value={"1.0.0-SNAPSHOT, 2.0.0-SNAPSHOT","1.0.0-SNAPSHOT,2.1.0","1.2.1,2.2.2-SNAPSHOT","1,2.0.1","1.0,2.0.1","53,54","1.2.3,2.3.3"}) public  void testMajorVersionUpgrade(java.lang.String versionA, java.lang.String versionB) { }
@org.junit.jupiter.params.ParameterizedTest() @org.junit.jupiter.params.provider.CsvSource(value={"1.0.0-SNAPSHOT, 1.0.0-SNAPSHOT","1.0.0-SNAPSHOT,1.1.0","1.2.1,1.2.2-SNAPSHOT","1,1.0.1","1.0,1.0.1","54,54-SNAPSHOT","2.2.3,1.3.3"}) public  void testNoMajorVersionUpgrade(java.lang.String versionA, java.lang.String versionB) { }
}
