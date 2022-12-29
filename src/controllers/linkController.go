package controllers

import (
	"ambassador/src/database"
	"ambassador/src/models"
	"strconv"

	"github.com/gofiber/fiber/v2"
)

func Links(c *fiber.Ctx) error {
	id, _ := strconv.Atoi(c.Params("id"))

	var links []models.Link

	database.DB.Where("user_id = ?", id).Find(&links)

	return c.JSON(links)
}
