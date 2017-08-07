OneSevenHome (for java 1.8)
-------------------

Java open source e-commerce software

- Shopping cart
- Catalogue
- Search
- Checkout
- Administration


To get the code:
-------------------
Clone the repository:
$ git clone git://github.com/rambabu0006/onesevenhome.git

If this is your first time using Github, review http://help.github.com to learn the basics.

You can also download the zip file containing the code from https://github.com/rambabu0006/onesevenhome 

To build the application:
-------------------	
From the command line with Maven installed:

	$ cd onesevenhome 
	$ mvn clean install
	

Run the application from Tomcat 
-------------------
copy one7homeWar/target/onesevenhome.war to tomcat or any other application server deployment dir

Increase heap space to 1024 m or at least 512 m

### Heap space configuration in Tomcat:


If you are using Tomcat, edit catalina.bat for windows users or catalina.sh for linux / Mac users

	in Windows
	set JAVA_OPTS="-Xms1024m -Xmx1024m -XX:MaxPermSize=256m" 
	
	in Linux / Mac
	export JAVA_OPTS="-Xms1024m -Xmx1024m -XX:MaxPermSize=256m" 

Run the application from Spring boot 
-------------------

       $ cd one7homeWar
       $ mvn spring-boot:run

Run the application from Spring boot in eclipse
-------------------

Right click on com.salesmanager.shop.application.ShopApplication

run as Java Application

### Access the application:
-------------------

Access the deployed web application at: http://localhost:8080/onesevenhome/mappings 
 or 
 
 http://103.92.235.45/shop/mappings