FROM node:20-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 4200

# ──► CORREGIDO: Eliminamos el argumento conflictivo del final
CMD ["npx", "ng", "serve", "--host", "0.0.0.0", "--port", "4200"]
