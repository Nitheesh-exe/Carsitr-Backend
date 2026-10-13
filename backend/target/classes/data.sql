-- Seed Data for Nash Car Rentals (PostgreSQL)

-- Sample Cars with high-resolution Unsplash automotive photos
INSERT INTO cars (brand, model, year, category, daily_rate, status, image_url, transmission, fuel_type, seating_capacity)
VALUES
('Porsche', '911 GT3 RS', 2024, 'Sports', 850.00, 'AVAILABLE',
 'https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?auto=format&fit=crop&w=1200&q=80',
 'Automatic (PDK)', 'Petrol', 2),

('Mercedes-Benz', 'G 63 AMG', 2024, 'Luxury', 650.00, 'AVAILABLE',
 'https://images.unsplash.com/photo-1520050206274-a1ae44613e6d?auto=format&fit=crop&w=1200&q=80',
 'Automatic', 'Petrol', 5),

('BMW', 'M8 Competition Gran Coupe', 2023, 'Sports', 520.00, 'AVAILABLE',
 'https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&w=1200&q=80',
 'Automatic', 'Petrol', 5),

('Audi', 'RS7 Sportback', 2024, 'Luxury', 480.00, 'AVAILABLE',
 'https://images.unsplash.com/photo-1603584173870-7f23fdae1b7a?auto=format&fit=crop&w=1200&q=80',
 'Automatic', 'Hybrid', 5),

('Range Rover', 'Autobiography V8', 2024, 'SUV', 590.00, 'AVAILABLE',
 'https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=1200&q=80',
 'Automatic', 'Petrol', 5),

('Tesla', 'Model S Plaid', 2023, 'Sedan', 380.00, 'AVAILABLE',
 'https://images.unsplash.com/photo-1617788138017-80ad40651399?auto=format&fit=crop&w=1200&q=80',
 'Automatic', 'Electric', 5),

('Ferrari', 'F8 Tributo', 2023, 'Sports', 1250.00, 'AVAILABLE',
 'https://images.unsplash.com/photo-1583121274602-3e2820c69888?auto=format&fit=crop&w=1200&q=80',
 'Automatic', 'Petrol', 2),

('Lamborghini', 'Urus Performante', 2024, 'SUV', 1100.00, 'AVAILABLE',
 'https://images.unsplash.com/photo-1544829099-b9a0c07fad1a?auto=format&fit=crop&w=1200&q=80',
 'Automatic', 'Petrol', 5),

('Mercedes-Benz', 'S 580 4MATIC', 2024, 'Sedan', 420.00, 'AVAILABLE',
 'https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?auto=format&fit=crop&w=1200&q=80',
 'Automatic', 'Mild Hybrid', 5),

('Cadillac', 'Escalade V-Series', 2023, 'SUV', 460.00, 'AVAILABLE',
 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=1200&q=80',
 'Automatic', 'Petrol', 7)
ON CONFLICT (brand, model) DO NOTHING;
