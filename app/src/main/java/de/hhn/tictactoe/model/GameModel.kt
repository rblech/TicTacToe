package de.hhn.tictactoe.model

data class GameModel(
    var currentPlayer: Status = Status.PlayerX,
    var winningPlayer: Status = Status.Empty,
    var isGameEnding: Boolean = false,
    /**Es wird eine neue Variable gamField deklariert welche ein 2 Diminsonales Array objekt ist vom Typ field
     * und die größe 3 * 3 hat*/
    var gameField: Array<Array<Field>> = Array(3) {
        Array(3) {
            Field()
        }
    }

) {
}