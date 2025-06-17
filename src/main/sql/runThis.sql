INSERT INTO roles (name, permissions) VALUES ('USER', 'READ');
INSERT INTO roles (name, permissions) VALUES ('ADMIN', 'READ,WRITE,ADMIN');

-- Insert perifericos products
INSERT INTO products (name, description, price, image, category, seller_id) VALUES
('Auriculares SteelSeries', 'Auriculares con sonido nítido y diseño cómodo para largas sesiones.', 299.99, '/images/auricular.jpeg', 'perifericos', 1),
('Teclado Hypermagnetico', 'Teclado mecánico con gran sensibilidad y retroiluminación RGB.', 389.99, '/images/teclado1.jpg', 'perifericos', 1),
('Mouse Logitech Pro', 'Mouse gamer de alta precisión con diseño ergonómico.', 249.99, '/images/mouse2.jpeg', 'perifericos', 1),
('Teclado Razer Chroma', 'Teclado con iluminación personalizable y respuesta rápida.', 249.99, '/images/teclado2.jpg', 'perifericos', 1),
('Teclado Razer Blackwidow', 'Modelo clásico con switches mecánicos ideales para gaming.', 299.99, '/images/teclado3.jpeg', 'perifericos', 1),
('Auriculares Sony', 'Auriculares con cancelación de ruido y gran calidad de sonido.', 389.99, '/images/auricular3.jpeg', 'perifericos', 1),
('Mouse Logitech G503', 'Mouse con sensor óptico avanzado y botones programables.', 249.99, '/images/mouse4.jpeg', 'perifericos', 1),
('Mouse Logitech G303', 'Compacto, liviano y preciso. Ideal para juegos de reacción rápida.', 249.99, '/images/mouse5.jpeg', 'perifericos', 1),
('Auriculares HyperX', 'Audio inmersivo con almohadillas suaves para mayor confort.', 299.99, '/images/auricular2.jpeg', 'perifericos', 1),
('Auricular Jnl tune', 'Diseño moderno con excelente rendimiento de graves.', 389.99, '/images/auricular1.jpg', 'perifericos', 1),
('Mouse Logitech Lightspeed', 'Mouse inalámbrico ultrarrápido con tecnología Lightspeed.', 249.99, '/images/mouse1.jpeg', 'perifericos', 1),
('Mouse Logitech Wireless', 'Mouse inalámbrico confiable con gran duración de batería.', 249.99, '/images/mouse.jpg', 'perifericos', 1);

-- Insert electrodomesticos products
INSERT INTO products (name, description, price, image, category, seller_id) VALUES
('Heladera No Frost 320L', 'Heladera con gran capacidad, eficiencia energética y sistema no frost.', 799.99, '/images/heladera.jpeg', 'electrodomesticos', 1),
('Heladera Siam 90L', 'Compacta y eficiente, ideal para espacios reducidos.', 219.99, '/images/heladera1.jpeg', 'electrodomesticos', 1),
('Heladera Bajo Mesada', 'Diseñada para cocina funcional y con estilo moderno.', 659.99, '/images/heladera2.jpeg', 'electrodomesticos', 1),
('Heladera Philco 110L', 'Heladera pequeña de bajo consumo y excelente rendimiento.', 249.99, '/images/heladera4.jpeg', 'electrodomesticos', 1),
('Microondas Digital Sansei', 'Microondas moderno con funciones digitales intuitivas.', 299.99, '/images/microondas.jpeg', 'electrodomesticos', 1),
('Microondas Philco 700w', 'Potente y eficiente, perfecto para cocinar rápidamente.', 389.99, '/images/microondas1.jpeg', 'electrodomesticos', 1),
('Microondas Grill Samsung', 'Microondas con función grill para resultados dorados.', 249.99, '/images/microondas2.jpeg', 'electrodomesticos', 1),
('Microondas Atma 20L', 'Diseño compacto, ideal para calentar y cocinar fácilmente.', 249.99, '/images/microondas3.jpeg', 'electrodomesticos', 1),
('Lavarropas automatico', 'Lavado automático eficiente y silencioso.', 299.99, '/images/lavarropas.jpeg', 'electrodomesticos', 1),
('Lavarropas Semiautomatico', 'Ideal para quienes buscan control en el lavado.', 389.99, '/images/lavarropas1.jpeg', 'electrodomesticos', 1),
('Lavarropas Drean', 'Tecnología nacional con programas automáticos.', 249.99, '/images/lavarropas2.jpeg', 'electrodomesticos', 1),
('Lavarropas 6,5kg', 'Capacidad justa para hogares pequeños o medianos.', 249.99, '/images/lavarropas3.jpeg', 'electrodomesticos', 1);

-- Insert computacion products
INSERT INTO products (name, description, price, image, category, seller_id) VALUES
('Placa de Video NVIDIA RTX 4060', 'Placa gráfica NVIDIA ideal para gaming en alta resolución.', 549.99, '/images/tarjeta1.jpeg', 'computacion', 1),
('Mother GIGABYTE 9', 'Motherboard GIGABYTE confiable y compatible con Intel.', 399.99, '/images/tarjeta2.jpeg', 'computacion', 1),
('Procesador Intel 5', 'Procesador Intel de alto rendimiento para tareas múltiples.', 249.99, '/images/procesador1.jpeg', 'computacion', 1),
('Procesador gamer Intel Core i5-12400F', 'Ideal para gamers que buscan un buen balance price/calidad.', 299.99, '/images/procesador.jpeg', 'computacion', 1),
('Procesador gamer Intel Core i9-14900K', 'Máximo rendimiento para juegos exigentes y streaming.', 389.99, '/images/procesador2.jpeg', 'computacion', 1),
('Tarjeta Gráfica Asrock Amd Radeon Rx6600', 'Excelente relación price-rendimiento en resolución 1080p.', 249.99, '/images/grafica.jpeg', 'computacion', 1),
('Tarjeta Gráfica Dual Nvidia Geforce Asus Rtx 3050', 'Desempeño sólido para gaming y creación de contenido.', 249.99, '/images/grafica1.jpeg', 'computacion', 1),
('Tarjeta gráfica Sapphire Pulse Amd Radeon Rx 7700', 'Ideal para juegos AAA con excelente disipación térmica.', 299.99, '/images/grafica3.jpeg', 'computacion', 1),
('Tarjeta Gráfica Gigabyte Geforce Rtx 3050', 'GPU potente con arquitectura NVIDIA Ampere.', 389.99, '/images/grafica4.jpeg', 'computacion', 1),
('Tarjeta gráfica Sapphire Pulse Rx 7600', 'GPU versátil con excelente rendimiento térmico.', 249.99, '/images/grafica5.jpeg', 'computacion', 1),
('Gigabyte Tarjeta Gráfica Geforce Rtx 4070', 'Una de las mejores GPUs para gaming 2K y productividad.', 249.99, '/images/grafica6.jpeg', 'computacion', 1);
