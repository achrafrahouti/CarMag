-- Initialize Garage data
INSERT INTO garage (id, name, address, telephone, email, opening_hours) 
VALUES (1, 'Downtown Auto Service', '123 Main Street, Downtown', '555-1234', 'contact@downtownauto.com', 
  '{
    "MONDAY": [
      {"startTime": "08:00:00", "endTime": "12:00:00"},
      {"startTime": "13:00:00", "endTime": "18:00:00"}
    ],
    "TUESDAY": [
      {"startTime": "08:00:00", "endTime": "12:00:00"},
      {"startTime": "13:00:00", "endTime": "18:00:00"}
    ],
    "WEDNESDAY": [
      {"startTime": "08:00:00", "endTime": "12:00:00"},
      {"startTime": "13:00:00", "endTime": "18:00:00"}
    ],
    "THURSDAY": [
      {"startTime": "08:00:00", "endTime": "12:00:00"},
      {"startTime": "13:00:00", "endTime": "18:00:00"}
    ],
    "FRIDAY": [
      {"startTime": "08:00:00", "endTime": "12:00:00"},
      {"startTime": "13:00:00", "endTime": "17:00:00"}
    ],
    "SATURDAY": [
      {"startTime": "09:00:00", "endTime": "14:00:00"}
    ]
  }'
);

INSERT INTO garage (id, name, address, telephone, email, opening_hours) 
VALUES (2, 'Eastside Car Repair', '456 Oak Avenue, Eastside', '555-5678', 'service@eastsiderepair.com', 
  '{
    "MONDAY": [
      {"startTime": "07:30:00", "endTime": "19:00:00"}
    ],
    "TUESDAY": [
      {"startTime": "07:30:00", "endTime": "19:00:00"}
    ],
    "WEDNESDAY": [
      {"startTime": "07:30:00", "endTime": "19:00:00"}
    ],
    "THURSDAY": [
      {"startTime": "07:30:00", "endTime": "19:00:00"}
    ],
    "FRIDAY": [
      {"startTime": "07:30:00", "endTime": "19:00:00"}
    ]
  }'
);

INSERT INTO garage (id, name, address, telephone, email, opening_hours) 
VALUES (3, 'Westside Auto Care', '789 Pine Road, Westside', '555-9012', 'info@westsideauto.com', 
  '{
    "MONDAY": [
      {"startTime": "09:00:00", "endTime": "17:00:00"}
    ],
    "TUESDAY": [
      {"startTime": "09:00:00", "endTime": "17:00:00"}
    ],
    "WEDNESDAY": [
      {"startTime": "09:00:00", "endTime": "17:00:00"}
    ],
    "THURSDAY": [
      {"startTime": "09:00:00", "endTime": "17:00:00"}
    ],
    "FRIDAY": [
      {"startTime": "09:00:00", "endTime": "17:00:00"}
    ],
    "SATURDAY": [
      {"startTime": "10:00:00", "endTime": "15:00:00"}
    ],
    "SUNDAY": [
      {"startTime": "11:00:00", "endTime": "14:00:00"}
    ]
  }'
);

-- Initialize Vehicle data
INSERT INTO vehicle (id, brand, model, manufacturing_year, fuel_type, garage_id)
VALUES (1, 'Toyota', 'Corolla', 2018, 'Gasoline', 1);

INSERT INTO vehicle (id, brand, model, manufacturing_year, fuel_type, garage_id)
VALUES (2, 'Honda', 'Civic', 2019, 'Gasoline', 1);

INSERT INTO vehicle (id, brand, model, manufacturing_year, fuel_type, garage_id)
VALUES (3, 'Ford', 'Focus', 2017, 'Diesel', 1);

INSERT INTO vehicle (id, brand, model, manufacturing_year, fuel_type, garage_id)
VALUES (4, 'Volkswagen', 'Golf', 2020, 'Hybrid', 2);

INSERT INTO vehicle (id, brand, model, manufacturing_year, fuel_type, garage_id)
VALUES (5, 'BMW', '3 Series', 2021, 'Gasoline', 2);

INSERT INTO vehicle (id, brand, model, manufacturing_year, fuel_type, garage_id)
VALUES (6, 'Tesla', 'Model 3', 2022, 'Electric', 3);

INSERT INTO vehicle (id, brand, model, manufacturing_year, fuel_type, garage_id)
VALUES (7, 'Audi', 'A4', 2019, 'Diesel', 3);

-- Initialize Accessory data
INSERT INTO accessory (id, name, description, price, type, vehicle_id)
VALUES (1, 'GPS Navigation System', 'Advanced GPS with real-time traffic updates', 299.99, 'Electronics', 1);

INSERT INTO accessory (id, name, description, price, type, vehicle_id)
VALUES (2, 'Leather Seat Covers', 'Premium leather seat covers with heating function', 199.50, 'Interior', 1);

INSERT INTO accessory (id, name, description, price, type, vehicle_id)
VALUES (3, 'Roof Rack', 'Aluminum roof rack for extra storage', 149.99, 'Exterior', 2);

INSERT INTO accessory (id, name, description, price, type, vehicle_id)
VALUES (4, 'Tinted Windows', 'UV protection window tinting', 250.00, 'Exterior', 3);

INSERT INTO accessory (id, name, description, price, type, vehicle_id)
VALUES (5, 'Premium Sound System', 'High-end audio system with subwoofer', 499.99, 'Electronics', 4);

INSERT INTO accessory (id, name, description, price, type, vehicle_id)
VALUES (6, 'Alloy Wheels', '18-inch lightweight alloy wheels', 799.99, 'Exterior', 5);

INSERT INTO accessory (id, name, description, price, type, vehicle_id)
VALUES (7, 'Home Charging Station', 'Level 2 home charging station', 899.99, 'Electronics', 6);

INSERT INTO accessory (id, name, description, price, type, vehicle_id)
VALUES (8, 'Winter Tire Set', 'Set of 4 premium winter tires', 650.00, 'Exterior', 7);