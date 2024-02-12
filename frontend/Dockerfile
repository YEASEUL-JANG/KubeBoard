FROM node:lts-alpine3.16 as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install --force
COPY . .
RUN npm run build

# production stage
FROM nginx:1.23.1 as production-stage
COPY  ./dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]