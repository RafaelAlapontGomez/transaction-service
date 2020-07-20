Spring Boot, RESTful, Transaction Service
El principal propósito de este desarrollo es la creación de un micro servicio usando Java, Spring Boot.
En este microservicio, se aplican los siguientes principios:
•	SOLID
•	Hace ATDD
•	El autoservicio es autocontenido, No depende de ningún servicio externo para ser ejecutado
Prerrequisitos 
Para usar este proyecto, vamos a usar:
•	Java JDK 8 (1.8)
•	Maven compatible con JDK 8 (yo he usado apache-maven-3.6.3)
•	Cualquier Java IDE (yo he usado eclipse con el plugin STS 4 e intellij IDEA)
•	Postman tool (opcional, pero si se quiere usar tengo una collection)
Creación Spring Boot Project
Fue creado desde eclipse con la opción de Spring Starter Project. La versión de spring boot es 2.2.7.RELEASE 
Usamos los siguientes starters
•	Lombok
•	Spring Data JPA
•	H2 Database
•	Spring Web
También he usado las siguientes dependencias:
•	Swagger 2
•	Joda-time
•	Dozer
•	Cucumber
Este microservicio se identifica por:
	<groupId>com.example</groupId>
	<artifactId>transaction-service</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>transaction-service</name>
Compilando y ejecutando con Maven
Para obtener el jar del proyecto, se puede usar el comando Maven siguiente desde una consola del sistema operativo:
mvn clean package
Este comando compilara y ejecutara los test y creara el jar.
Si solo se quieren ejecutar los test hay que invocar al comando mvn test
Si se quiere ejecutar el microservicio hay que invocar al comando mvn spring-boot:run

Endpoints del microservicio Transacción
/transaction/créate  Crea una transacción en el sistema.
/transaction/search  Busca transacciones en el sistema, tiene como parámetros una cuenta en formato Iban y el orden (ascendente o descendente por fecha) que queremos ver los resultados
/transaction/status  Consulta el status de una transacción, tiene como parámetros la referencia de la transacción y el channel.
/transaction/{reference}  Obtiene una transacción por su referencia
Pruebas con Postman
1.	Ejecutar el microservicio con mvn spring-boot:run
2.	Ejecutar postman e importar la collection Transaccion Service que esta en resources de test, carpeta postman
3.	Ejecutar los test con la opción de postman
Pruebas con Cucumber
Cucumber genera un fichero en el directorio target con los resultados de los test. Cucumber.json y un directorio target/cucumber dentro del cual esta index.html que también contiene los resultados del test.
Acceso a la consola de h2
Con la aplicación ejecutada, puede acceder a la base de datos desde un browser con la url http://localhost:8080/h2-console
