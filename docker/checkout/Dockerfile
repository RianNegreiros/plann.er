FROM node:16-alpine

WORKDIR /app/clients/checkout

COPY clients/checkout/package.json clients/checkout/package-lock.json ./
RUN npm run install

COPY . .

RUN npm run build

CMD ["npm", "start"]