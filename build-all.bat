@echo off

echo =====================================
echo LIMPIANDO CONTENEDORES E IMAGENES
echo =====================================

FOR /f %%i IN ('docker ps -aq') DO docker rm -f %%i
FOR /f %%i IN ('docker images -aq') DO docker rmi -f %%i

echo.
echo =====================================
echo COMPILANDO MICROSERVICIOS
echo =====================================

cd api-gateway
call .\mvnw clean package -DskipTests

cd ..\auth-service
call .\mvnw clean package -DskipTests

cd ..\usuario-service
call .\mvnw clean package -DskipTests

cd ..\sede-service
call .\mvnw clean package -DskipTests

cd ..\cancha-service
call .\mvnw clean package -DskipTests

cd ..\horario-service
call .\mvnw clean package -DskipTests

cd ..\disponibilidad-service
call .\mvnw clean package -DskipTests

cd ..\reserva-service
call .\mvnw clean package -DskipTests

cd ..\pago-service
call .\mvnw clean package -DskipTests

cd ..\resena-service
call .\mvnw clean package -DskipTests

cd ..\notificacion-service
call .\mvnw clean package -DskipTests

cd ..\mantenimiento-service
call .\mvnw clean package -DskipTests

cd ..

echo.
echo =====================================
echo CREANDO IMAGENES DOCKER
echo =====================================

docker compose build

echo.
echo =====================================
echo LEVANTANDO CONTENEDORES
echo =====================================

docker compose up -d

pause