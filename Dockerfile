#################################################
# Imagen base para el contenedor de compilación
#################################################
FROM maven:3.8.5-openjdk-17 as builder

# Define el directorio de trabajo donde ejecutar comandos
WORKDIR /project

# Copia las dependencias del proyecto
COPY pom.xml /project/

# Descarga las dependencias del proyecto
RUN mvn clean verify

# Copia el código del proyecto
COPY /src /project/src

# Compila proyecto
RUN mvn package -o -DskipTests=true

#################################################
# Imagen base para el contenedor de la aplicación
#################################################
FROM openjdk:17

# Define el directorio de trabajo donde se encuentra el JAR
WORKDIR /usr/app/

# Copia el JAR del contenedor de compilación
COPY --from=builder /project/target/*.jar /usr/app/app.jar

# Indica el puerto que expone el contenedor
EXPOSE 8080

# Comando que se ejecuta al hacer docker run
CMD [ "java", "-jar", "app.jar" ]