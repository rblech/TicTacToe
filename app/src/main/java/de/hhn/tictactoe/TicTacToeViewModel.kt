package de.hhn.tictactoe

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.hhn.tictactoe.model.Field
import de.hhn.tictactoe.model.GameModel
import de.hhn.tictactoe.model.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class TicTacToeViewModel : ViewModel() {

    /**Es wird eine neue private val deklariert die _gameModel heist und ein MutableStateFlow(GameModel()) ist*/
    private val _gameModel = MutableStateFlow(GameModel())
    /**Es wird eine neu val gameModel deklariert die eine instanz von StateFlow<GameModel> = _gameModel.asStateFlow() ist*/
    val gameModel: StateFlow<GameModel> = _gameModel.asStateFlow()

    /**Die funktion resetGame() startet das Spiel neu
     * in dem sie ne neue Variable gamField deklariert welche ein 2 Diminsonales Array objekt ist vom Typ field
     *  und die größe 3 * 3 hat und diese apply und den staus = Satus Empty setzt
     *  danach wird das _gameModel.value = dem GameModel gesetzt*/
    fun resetGame() {
        _gameModel.value.gameField = Array(3) {
            Array(3) {
                Field().apply { status = Status.Empty }
            }
        }
        _gameModel.value = GameModel()
    }

    /**Die funktion selectedField erhält den paramter field: Field übergeben
     * sie überprüft ob das aktuelle ausgewählte feld leer ist
     * fals ja dan setzt sie den field.status gleich dem _gameModel.value.currentPlayer
     * es wird eine neue val deklariert die updatedGameModel heißt die eine copie des aktuellen nexten Spielers ist und überprüft
     * ob das Spiel zuende ist in dem sie checkEndingGame() aufruft
     * Das updatedGameModel.gameField erhält die field.indexRow und field.indexColumn des fields
     * und sezt das _gameModel.value gleich dem updatedGameModel
     * */
    fun selectField(field: Field) {
        if (field.status == Status.Empty) {
            field.status = _gameModel.value.currentPlayer
            val updatedGameModel = _gameModel.value.copy(
                currentPlayer = _gameModel.value.currentPlayer.next(),
                isGameEnding = checkEndingGame()
            )
            updatedGameModel.gameField[field.indexRow][field.indexColumn] = field
            _gameModel.value = updatedGameModel
        }
    }

    /**Die checkEndingGame überprüft ob das Spiel zuende ist
     * sie returnt einen Boolean-wert
     * Es wird eine neue variable deklariert die gameField heist und gleich dem _gameModel.value.gameField gesetzt wird
     * zuerst werden die horizontalen und vertikalen reihen abgefragt
     * es wird eine forschleife erstellt die von 0 bis 3 zählt
     * es wird überprüft ob 3 in einer reihe sind indem man die checkLine funktion aufruft und ihr eine listof übergibt
     * mit den werten von gameField[i][0], gameField[i][1], gameField[i][2])
     * oder es wird überprüft ob 3 in einer reihe sind indem man die checkLine funktion aufruft und ihr eine listof übergibt
     * mit den werten von gameField[0][i], gameField[1][i], gameField[2][i]
     * das _gameModel.value.winningPlayer wird dem Status des PlayerX gesetzt
     * _gameModel.value.isGameEnding wird true gesetzt
     * und true zurückgegeben
     * danach werden die diagonalen abgefragt in dem man die checkLine aufruft und ihr eine listof übergibt
     * mit den werten von gameField[0][0], gameField[1][1], gameField[2][2] oder
     * gameField[0][2], gameField[1][1], gameField[2][0])
     * sollte es so sein wird der aktuelle _gameModel.value.winningPlayer gleich dem Status des PLayerO gesetzt
     * das _gameModel.value.isGameEnding wird true gesetzt
     * es wird überprüft ob alle Spielfelder belegt sind
     * wenn ja setzt es _gameModel.value.isGameEnding auf true
     * und returnt true
     * und am ende wird fals nochmal zurückgegeben*/
    private fun checkEndingGame(): Boolean {
        val gameField = _gameModel.value.gameField

        for (i in 0 until 3) {
            if (checkLine(listOf(gameField[i][0], gameField[i][1], gameField[i][2])) ||
                checkLine(listOf(gameField[0][i], gameField[1][i], gameField[2][i]))
            )  {
                _gameModel.value.winningPlayer = Status.PlayerX
                _gameModel.value.isGameEnding = true
                return true
            }
        }

        if (checkLine(listOf(gameField[0][0], gameField[1][1], gameField[2][2])) ||
            checkLine(listOf(gameField[0][2], gameField[1][1], gameField[2][0]))
        )  {
            _gameModel.value.winningPlayer = Status.PlayerO
            _gameModel.value.isGameEnding = true
            return true
        }

        if (gameField.flatten().all { it.status != Status.Empty }) {
            _gameModel.value.isGameEnding = true
            return true
        }

        return false
    }

    /**Die funktion checkLine erhält den Paramter übergebn fields: List<Field> und gibt einen Boolean zurück
     * wenn das fields leer seien sollt gibt er fals zurück
     * ansonsten setzt er den status dem fields[0].staus
     * und gibt zurüch das der Stats ungleich ist dem leerenstatus und überprüft ob alle fields dem status des ersten
     * elements entsprechen*/
    private fun checkLine(fields: List<Field>): Boolean {
        if (fields.isEmpty()) return false

        val status = fields[0].status
        return status != Status.Empty && fields.all { it.status == status }
    }
}