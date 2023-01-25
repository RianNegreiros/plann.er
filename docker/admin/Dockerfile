FROM node:16-alpine

WORKDIR /app/clients/admin

COPY clients/admin/package.json .

RUN npm install

COPY . .

RUN npm run build

CMD [ "npx", "serve", "build" ]