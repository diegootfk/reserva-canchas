CREATE TABLE sedes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    direccion VARCHAR(150),
    comuna VARCHAR(100),
    telefono VARCHAR(20),
    estado VARCHAR(50)
);

INSERT INTO sedes(nombre, direccion, comuna, telefono, estado)
VALUES
('Sede Centro', 'Av Central 123', 'Santiago', '123456789', 'ACTIVA'),

('Sede Norte', 'Calle Norte 456', 'Recoleta', '987654321', 'ACTIVA'),

('Sede Sur', 'Av Sur 789', 'La Florida', '111222333', 'ACTIVA'),

('Sede Oriente', 'Camino Oriente 321', 'Las Condes', '444555666', 'ACTIVA'),

('Sede Poniente', 'Pasaje Poniente 654', 'Maipu', '777888999', 'ACTIVA'),

('Sede Maipu', 'Av Maipu 100', 'Maipu', '123123123', 'ACTIVA'),

('Sede Providencia', 'Av Providencia 200', 'Providencia', '456456456', 'ACTIVA'),

('Sede Ñuñoa', 'Calle Ñuñoa 300', 'Ñuñoa', '789789789', 'ACTIVA'),

('Sede Puente Alto', 'Av Concha y Toro 400', 'Puente Alto', '159357258', 'ACTIVA'),

('Sede Quilicura', 'Camino Quilicura 500', 'Quilicura', '951753852', 'ACTIVA'),

('Sede Estacion Central', 'Alameda 600', 'Estacion Central', '357159456', 'ACTIVA'),

('Sede San Miguel', 'Gran Avenida 700', 'San Miguel', '852456123', 'ACTIVA');