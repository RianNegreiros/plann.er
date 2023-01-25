FROM node:16-alpine

WORKDIR /app/clients/ambassador

COPY clients/ambassador/package.json ./

RUN npm install

COPY . .

RUN npm run build

CMD [ "npx", "serve", "build" ]