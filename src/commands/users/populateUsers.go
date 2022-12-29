package main

import (
	"ambassador/src/database"
	"ambassador/src/models"

	"github.com/go-faker/faker/v4"
)

func main() {
	database.Connect()

	for i := 0; i < 10; i++ {
		ambassador := models.User{
			FirstName:    faker.FirstName(),
			LastName:     faker.LastName(),
			Email:        faker.Email(),
			IsAmbassador: true,
		}

		ambassador.SetPassword(faker.Password())

		database.DB.Create(&ambassador)
	}
}
