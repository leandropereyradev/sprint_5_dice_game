# DiceGame - APIRest

The DiceGame is played with two dice. If the sum of the two dice equals 7, the game is won; otherwise, it is lost. A player can view a list of all the rolls they have made and their success percentage.

To play the game and make a roll, a user must register with a unique email. Upon registration, each user is assigned a unique numeric identifier and a registration date. If the user chooses not to add a name, they will be called "ANONYMOUS". Multiple players can have the name "ANONYMOUS".

Each player can see a list of all the rolls they have made, including the value of each dice and whether they won the game.

Players cannot delete specific games but can delete their entire list of rolls.

The software allows listing all players in the system and provides the average success percentage of all players.

The software adheres to main design patterns.

## Notes

### Construction Details

#### URL - Player

- **POST /api/players/register:** Create a player (no authentication required)


- **POST /api/players/login:** Log in a player (no authentication required)


- **POST /api/players/logout:** Log out a player (authentication required)


- **PUT /api/players/{id}:** Update a player (authentication required)


- **GET /api/players/{id}:** Get a player by ID (authentication required)


- **GET /api/players:** Get all players (authentication required)


- **DELETE /api/players/{id}:** Delete a player (ADMIN role only)

#### URL - Game

- **POST /api/players/{id}/games:** Roll the dice for a specific player (authentication required)


- **DELETE /api/players/{id}/games:** Delete all rolls of a player (authentication required)


- **GET /api/players/{id}/games:** List all games for a player (authentication required)



#### URL - Player / Game

- **GET /api/players/ranking:** Get the average ranking of all players (average success percentage, authentication required)


- **GET /api/players/ranking/loser:** Get the player with the worst success rate (authentication required)


- **GET /api/players/ranking/winner:** Get the player with the best success rate (authentication required)
---

**Swagger:** [Swagger UI](http://localhost:8080/swagger)

### Phase 1
**Persistence:** Uses MySQL as a database.

### Phase 2
Modify the project to use MongoDB for data persistence.

### Phase 3
**Security:** Include JWT authentication for all accesses to microservice URLs.

**Testing:** Add unit, component, and integration tests to the project using jUnit, AssertJ, or Hamcrest libraries. Include mocks for project testing (Mockito) and contract tests.

**Design Enhancement:** Diversify persistence to use both MySQL and MongoDB simultaneously.
