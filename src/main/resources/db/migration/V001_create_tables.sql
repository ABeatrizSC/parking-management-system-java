CREATE TABLE IF NOT EXISTS vehicles (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(100) NULL,
    slotSize INT NULL,
    accessType VARCHAR(50) NULL,
    entranceGate INT NULL,
    entranceGatesAvailable VARCHAR(100) NULL,
    exitGate INT NULL,
    exitGatesAvailable VARCHAR(100) NULL
);

CREATE TABLE IF NOT EXISTS monthlyPayers (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    licensePlate VARCHAR(100) NULL,
    valuePerMonth DOUBLE NULL,
    vehicle_id INT NOT NULL
);

ALTER TABLE monthlyPayers
ADD FOREIGN KEY (vehicle_id) REFERENCES vehicles(id);

CREATE TABLE IF NOT EXISTS deliveryTrucks (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    licensePlate VARCHAR(100) NULL,
    vehicle_id INT NOT NULL
);

ALTER TABLE deliveryTrucks
ADD FOREIGN KEY (vehicle_id) REFERENCES vehicles(id);

CREATE TABLE IF NOT EXISTS parkingSpaces (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    isOccupied BIT(2) NULL,
    slotType VARCHAR(100) NULL,
    vehicle_id INT
);

ALTER TABLE parkingSpaces
ADD FOREIGN KEY (vehicle_id) REFERENCES vehicles(id);

CREATE TABLE IF NOT EXISTS tickets (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    startHour TIME NULL,
    finishHour TIME NULL,
    totalValue DOUBLE NULL,
    parkingSpaces VARCHAR(100) NULL,
    vehicle_id INT NOT NULL
);

ALTER TABLE tickets
ADD FOREIGN KEY (vehicle_id) REFERENCES vehicles(id);