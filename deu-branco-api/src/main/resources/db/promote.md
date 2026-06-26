docker compose exec postgres psql -U myuser -d mydatabase -c "UPDATE jogador SET role = 'ADMIN' WHERE email = 'joao@email.com';
