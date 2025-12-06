-- Insert a test user (password: password123 hashed with SHA-256)
-- SHA-256 hash of "password123" = ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255),
    phone VARCHAR(255) ,
    verified BOOLEAN NOT NULL DEFAULT FALSE
) ;
INSERT INTO users (email, password, username, phone, verified) 
VALUES ('test@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'testuser', '1234567890', true)
ON CONFLICT (email) DO NOTHING;

