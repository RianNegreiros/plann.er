## Plann.er

A simple trip planning API.

### Overview

Plann.er is a REST API to plan trips collaboratively. You can create a trip with a destination and dates, invite
participants by email, have invitees confirm their participation, and attach activities and useful links to the trip.
The provided Postman collection demonstrates the typical workflow end-to-end using a local server on port 8080.

### Core concepts

- **Trip**: The main entity that holds `destination`, `starts_at`, `ends_at`, and confirmation status. Typical
  endpoints: `POST /trips`, `GET /trips/{tripId}`, `PUT /trips/{tripId}`, `PATCH /trips/{tripId}` (confirm).
- **Participant**: People invited to join a trip via email. They can confirm their participation. Typical endpoints:
  `POST /trips/{tripId}/invite`, `POST /participants/{participantId}` (confirm), `GET /trips/{tripId}/participants`.
- **Activity**: A scheduled item within a trip (e.g., “Art Museum” at a specific time). Endpoint:
  `POST /trips/{tripId}/activities`.
- **Link**: Helpful references related to the trip (e.g., booking links). Endpoint: `POST /trips/{tripId}/links`.

### Typical workflow

1. **Create a trip**: `POST /trips` with `destination`, `starts_at`, and `ends_at`.
  - The Postman tests store the returned `tripId` in a collection variable for subsequent requests.
2. **Invite participants**: `POST /trips/{tripId}/invite` with an email.
  - The Postman tests store `participantId` for confirmation.
3. **Confirm a participant**: `POST /participants/{participantId}` with participant details.
4. **Add activities**: `POST /trips/{tripId}/activities` with `title` and `occurs_at`.
5. **Add helpful links**: `POST /trips/{tripId}/links` with `title` and `url`.
6. **Review trip details**: `GET /trips/{tripId}` to see the current state and participants.
7. **Confirm or update the trip**: `PATCH /trips/{tripId}` to confirm; `PUT /trips/{tripId}` to update `destination` or
   dates.

### Postman collection

The Postman collection is included in the repo:

- `Plann.er.postman_collection.json`

#### Import into Postman

- Open Postman
- Click Import → Choose File
- Select `Plann.er.postman_collection.json`

The collection uses local URLs (http://localhost:8080). Some requests set collection variables like `tripId` and
`participantId` automatically from responses.

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/15917186-b60e01f4-acb2-476b-821b-4ac249aed034?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D15917186-b60e01f4-acb2-476b-821b-4ac249aed034%26entityType%3Dcollection%26workspaceId%3D76ff1811-a0af-4935-a423-2e5bb926aa1d)

### Development

Ensure the API is running locally on port 8080 before using the collection.
