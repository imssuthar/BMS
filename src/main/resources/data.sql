-- Insert a test user (password: password123 hashed with SHA-256)
-- SHA-256 hash of "password123" = ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f
INSERT INTO users (email, password, username, phone) 
VALUES ('test@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'testuser', '1234567890')
ON CONFLICT (email) DO NOTHING;

