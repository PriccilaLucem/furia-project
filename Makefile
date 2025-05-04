.PHONY: up

up:
	@for dir in */ ; do \
		if [ -f $$dir/docker-compose.yaml ] || [ -f $$dir/docker-compose.yml ]; then \
			echo "📦 Subindo serviço em $$dir"; \
			cd $$dir && docker compose up -d && cd ..; \
		else \
			echo "⚠️  Nenhum docker-compose encontrado em $$dir, ignorando..."; \
		fi \
	done
