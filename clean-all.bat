@echo off

echo =====================================
echo LIMPIANDO PROYECTOS MAVEN
echo =====================================

cd api-gateway
call .\mvnw clean

cd ..\auth-service
call .\mvnw clean

cd ..\usuario-service
call .\mvnw clean

cd ..\sede-service
call .\mvnw clean

cd ..\cancha-service
call .\mvnw clean

cd ..\horario-service
call .\mvnw clean

cd ..\disponibilidad-service
call .\mvnw clean

cd ..\reserva-service
call .\mvnw clean

cd ..\pago-service
call .\mvnw clean

cd ..\resena-service
call .\mvnw clean

cd ..\notificacion-service
call .\mvnw clean

cd ..\mantenimiento-service
call .\mvnw clean

cd ..

echo.
echo =====================================
echo LIMPIEZA COMPLETADA
echo =====================================

pause