# Example of using: Spring, JSF 2 and XStream

		~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
							 
		  Instructions how to enable this sample  
		                                         
		~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This is currency conversion web application. 
To build this sample used technologies: 
- XStream - XML to JAVA domain deserialization;
- JSF 2 (mostly primefaces) - UI view;
- Spring - set up services etc.;


~~~~~~~~~~~
 1. Tools 
~~~~~~~~~~~

1.1. Building project: download maven and place under 'C:\' directory, i.e.: apache-maven-3.0.4.
1.2. Starting application:download tomcat and place under 'C:\' directory, i.e.: apache-tomcat-7.0.68.
1.3. Coding: download java and eclipse, i.e.: jdk1.7, Mars.2.


~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
2. Prepare sample application 
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Run following maven commands (from sample directory) to prepare sample application:
2.1. mvn clean install
2.2. mvn package


~~~~~~~~~~~~~~~~~~~~
3. Run application 
~~~~~~~~~~~~~~~~~~~~

3.1. click on startup.bat under tomcat directory.
3.1. when tomcat is up, enter http://localhost:8080/demo-app/default.xhtml to your browser.


~~~~~~
Note 
~~~~~~
1. Services functionality covered by unit tests;

