package pdaa.learning.connectfour

var columns = 7
var rows = 6
var firstPlayer = ""
var secondPlayer = ""
var playerOneScore = 0
var playerTwoScore = 0
var numberOfGames = 0U
var currentTurn = 0U
var lastPLayer = LastPlayer.PLAYER_TWO

enum class StatusGame {
    FIRST_PLAYER_WINS, SECOND_PLAYER_WINS, DRAW, CONTINUE_PLAYING
}

enum class LastPlayer {
    PLAYER_ONE, PLAYER_TWO
}

fun main() {

    //Game begins
    println("Connect Four")

    //get players nicknames
    println("First player's name:")
    firstPlayer = readln()
    println("Second player's name:")
    secondPlayer = readln()

    //Ask for the board dimension
    getBoardDimensions()

    //Ask for game mode
    askForGameMode()

    //Printing game configuration
    printConfigurationGame()

    //Board definition
    var board: MutableList<MutableList<Char>>


    //Start game
    currentTurn = numberOfGames
    repeat(numberOfGames.toInt()) {
        currentTurn--
        if (numberOfGames > 1U) {
            println("Game #${numberOfGames-currentTurn}")
        }

        board = MutableList(rows) { MutableList(columns) { ' ' } }
        printBoard(board)

        lastPLayer = if (lastPLayer == LastPlayer.PLAYER_TWO) {
            if (startGamePlayerOneFirst(firstPlayer, secondPlayer, board) == -1) return
            LastPlayer.PLAYER_ONE
        } else {
            if(startGamePlayerTwoFirst(firstPlayer, secondPlayer, board)== -1 ) return
            LastPlayer.PLAYER_TWO
        }

    }
}

fun printConfigurationGame() {
    println("$firstPlayer VS $secondPlayer")
    println("$rows X $columns board")
    if (numberOfGames == 1U) {
        println("Single game")
    } else {
        println("Total $numberOfGames games")
    }
}

fun askForGameMode() {
    println(
        "Do you want to play single or multiple games?\n" +
                "For a single game, input 1 or press Enter\n" +
                "Input a number of games:"
    )
    val numberOfGamesRow = readln()
    if (numberOfGamesRow.isEmpty()) {
        numberOfGames = 1U
        return
    }
    try {
        numberOfGames = numberOfGamesRow.toUInt()
        if (numberOfGames == 0U) {
            println("Invalid input")
            askForGameMode()
        }
    } catch (ex: Exception) {
        println("Invalid input")
        askForGameMode()
    }
}

fun startGamePlayerOneFirst(
    firstPlayer: String,
    secondPlayer: String,
    board: MutableList<MutableList<Char>>
) : Int{

    while (true) {
        //First player playing
        firstPlayerActive@ while (true) {
            val firstPLayerSelectedPoint = askPlayerInput(firstPlayer)
            if (firstPLayerSelectedPoint == -1) {
                println("Game over!")
                return -1
            } else if (firstPLayerSelectedPoint == -2) {
                println("Incorrect column number")
                continue@firstPlayerActive
            } else {
                val firstPLayerFirstAvailableRow =
                    validateIfUserInputIsAvailable(firstPLayerSelectedPoint - 1, board)
                when (firstPLayerFirstAvailableRow) {
                    -2 -> {
                        continue@firstPlayerActive
                    }
                    -1 -> {
                        continue@firstPlayerActive
                    }
                    else -> {
                        setAsSelected(
                            board,
                            firstPLayerSelectedPoint - 1,
                            firstPLayerFirstAvailableRow,
                            'o'
                        )
                        break@firstPlayerActive
                    }
                }
            }
        }

        //Verify if first player wins
        if (verifyIfThereIsAnyWinner(board)) {
            return 1
        }

        //Second player playing
        secondPlayerActive@ while (true) {
            val secondPLayerSelectedPoint = askPlayerInput(secondPlayer)
            if (secondPLayerSelectedPoint == -1) {
                println("Game over!")
                return -1
            } else if (secondPLayerSelectedPoint == -2) {
                println("Incorrect column number")
                continue@secondPlayerActive
            } else {
                val secondPLayerFirstAvailableRow =
                    validateIfUserInputIsAvailable(secondPLayerSelectedPoint - 1, board)
                when (secondPLayerFirstAvailableRow) {
                    -2 -> {
                        continue@secondPlayerActive
                    }
                    -1 -> {
                        continue@secondPlayerActive
                    }
                    else -> {
                        setAsSelected(
                            board,
                            secondPLayerSelectedPoint - 1,
                            secondPLayerFirstAvailableRow,
                            '*'
                        )
                        break@secondPlayerActive
                    }
                }
            }
        }

        if (verifyIfThereIsAnyWinner(board)) {
            return 1
        }

    }

}

fun startGamePlayerTwoFirst(
    firstPlayer: String,
    secondPlayer: String,
    board: MutableList<MutableList<Char>>
): Int {

    while (true) {

        //Second player playing
        secondPlayerActive@ while (true) {
            val secondPLayerSelectedPoint = askPlayerInput(secondPlayer)
            if (secondPLayerSelectedPoint == -1) {
                println("Game over!")
                return -1
            } else if (secondPLayerSelectedPoint == -2) {
                println("Incorrect column number")
                continue@secondPlayerActive
            } else {
                val secondPLayerFirstAvailableRow =
                    validateIfUserInputIsAvailable(secondPLayerSelectedPoint - 1, board)
                when (secondPLayerFirstAvailableRow) {
                    -2 -> {
                        continue@secondPlayerActive
                    }
                    -1 -> {
                        continue@secondPlayerActive
                    }
                    else -> {
                        setAsSelected(
                            board,
                            secondPLayerSelectedPoint - 1,
                            secondPLayerFirstAvailableRow,
                            '*'
                        )
                        break@secondPlayerActive
                    }
                }
            }
        }

        if (verifyIfThereIsAnyWinner(board)) {
            return 1
        }

        //First player playing
        firstPlayerActive@ while (true) {
            val firstPLayerSelectedPoint = askPlayerInput(firstPlayer)
            if (firstPLayerSelectedPoint == -1) {
                println("Game over!")
                return -1
            } else if (firstPLayerSelectedPoint == -2) {
                println("Incorrect column number")
                continue@firstPlayerActive
            } else {
                val firstPLayerFirstAvailableRow =
                    validateIfUserInputIsAvailable(firstPLayerSelectedPoint - 1, board)
                when (firstPLayerFirstAvailableRow) {
                    -2 -> {
                        continue@firstPlayerActive
                    }
                    -1 -> {
                        continue@firstPlayerActive
                    }
                    else -> {
                        setAsSelected(
                            board,
                            firstPLayerSelectedPoint - 1,
                            firstPLayerFirstAvailableRow,
                            'o'
                        )
                        break@firstPlayerActive
                    }
                }
            }
        }

        //Verify if first player wins
        if (verifyIfThereIsAnyWinner(board)) {
            return 1
        }

    }

}

fun verifyIfThereIsAnyWinner(board: MutableList<MutableList<Char>>): Boolean {

    var statusGame = StatusGame.CONTINUE_PLAYING

    //Verify if first player has won
    if (hasTheCharacterWon('o', board)) {
        playerOneScore += 2
        println("Player $firstPlayer won")
        printScoreAndGameStatus()
        return true
    }

    //Verify if second player has won
    if (hasTheCharacterWon('*', board)) {
        playerTwoScore += 2
        println("Player $secondPlayer won")
        printScoreAndGameStatus()
        return true
    }

    //verify a draft
    if (isTheGameDraw(board)) {
        playerOneScore++
        playerTwoScore++
        println("It is a draw")
        printScoreAndGameStatus()
        return true
    }

    return false
}

fun printScoreAndGameStatus() {
    if (currentTurn < 1U) {
        println("Score")
        println("$firstPlayer: $playerOneScore $secondPlayer: $playerTwoScore")
        println("Game over!")
    }else{
        println("Score")
        println("$firstPlayer: $playerOneScore $secondPlayer: $playerTwoScore")
    }
}

fun isTheGameDraw(board: MutableList<MutableList<Char>>): Boolean {
    board.forEach { currentRow ->
        currentRow.forEach {
            if (it == ' ') return false
        }
    }
    return true
}

fun hasTheCharacterWon(caracter: Char, board: MutableList<MutableList<Char>>): Boolean {
    if (checkCoincidences(board, caracter)) return true
    return false
}

fun checkCoincidences(board: MutableList<MutableList<Char>>, caracter: Char): Boolean {

    //Lets go trought all the elements in the board
    for (rowIndex in board.indices) {
        for (columnIndex in board[rowIndex].indices) {
            if (board[rowIndex][columnIndex] == caracter) {
                var consecutiveForwardCoincidences = 0
                return when {
                    //Forward coincidence
                    try {
                        ((board[rowIndex][columnIndex] == caracter) &&
                                (board[rowIndex][columnIndex + 1] == caracter)) &&
                                ((board[rowIndex][columnIndex + 2] == caracter) &&
                                        (board[rowIndex][columnIndex + 3] == caracter))
                    } catch (ex: IndexOutOfBoundsException) {
                        false
                    } -> true

                    //Backward coincidence
                    try {
                        ((board[rowIndex][columnIndex] == caracter) &&
                                (board[rowIndex][columnIndex - 1] == caracter)) &&
                                ((board[rowIndex][columnIndex - 2] == caracter) &&
                                        (board[rowIndex][columnIndex - 3] == caracter))
                    } catch (ex: IndexOutOfBoundsException) {
                        false
                    } -> true

                    //Up coincidence
                    try {
                        ((board[rowIndex][columnIndex] == caracter) &&
                                (board[rowIndex + 1][columnIndex] == caracter)) &&
                                ((board[rowIndex + 2][columnIndex] == caracter) &&
                                        (board[rowIndex + 3][columnIndex] == caracter))
                    } catch (ex: IndexOutOfBoundsException) {
                        false
                    } -> true

                    //Down coincidence
                    try {
                        ((board[rowIndex][columnIndex] == caracter) &&
                                (board[rowIndex - 1][columnIndex] == caracter)) &&
                                ((board[rowIndex - 2][columnIndex] == caracter) &&
                                        (board[rowIndex - 3][columnIndex] == caracter))
                    } catch (ex: IndexOutOfBoundsException) {
                        false
                    } -> true

                    //++
                    try {
                        ((board[rowIndex][columnIndex] == caracter) &&
                                (board[rowIndex + 1][columnIndex + 1] == caracter)) &&
                                ((board[rowIndex + 2][columnIndex + 2] == caracter) &&
                                        (board[rowIndex + 3][columnIndex + 3] == caracter))
                    } catch (ex: IndexOutOfBoundsException) {
                        false
                    } -> true

                    //-+
                    try {
                        ((board[rowIndex][columnIndex] == caracter) &&
                                (board[rowIndex - 1][columnIndex + 1] == caracter)) &&
                                ((board[rowIndex - 2][columnIndex + 2] == caracter) &&
                                        (board[rowIndex - 3][columnIndex + 3] == caracter))
                    } catch (ex: IndexOutOfBoundsException) {
                        false
                    } -> true

                    //--
                    try {
                        ((board[rowIndex][columnIndex] == caracter) &&
                                (board[rowIndex - 1][columnIndex - 1] == caracter)) &&
                                ((board[rowIndex - 2][columnIndex - 2] == caracter) &&
                                        (board[rowIndex - 3][columnIndex - 3] == caracter))
                    } catch (ex: IndexOutOfBoundsException) {
                        false
                    } -> true

                    //+-
                    try {
                        ((board[rowIndex][columnIndex] == caracter) &&
                                (board[rowIndex + 1][columnIndex - 1] == caracter)) &&
                                ((board[rowIndex + 2][columnIndex - 2] == caracter) &&
                                        (board[rowIndex + 3][columnIndex - 3] == caracter))
                    } catch (ex: IndexOutOfBoundsException) {
                        false
                    } -> true

                    else -> false
                }

            }
        }
    }
    return false
}


fun setAsSelected(
    board: MutableList<MutableList<Char>>,
    selectedColumn: Int,
    selectedRow: Int,
    caracter: Char
) {
    board[selectedRow][selectedColumn] = caracter
    printBoard(board)
}

/**
 * Returns the first available column
 * When the column is not available it will return -1
 * And -2 if the user's input is out of range
 */
fun validateIfUserInputIsAvailable(
    pLayerSelectedPoint: Int,
    board: MutableList<MutableList<Char>>
): Int {
    for (rowIndex in board.size - 1 downTo 0) {
        if (try {
                board[rowIndex][pLayerSelectedPoint]
            } catch (ex: IndexOutOfBoundsException) {
                //When the selected column is out fo range
                println("The column number is out of range (1 - ${board.first().size})")
                return -2
            } == ' '
        ) {
            //returning a valid number
            return rowIndex
        }
    }
    //When the column is full
    println("Column ${pLayerSelectedPoint + 1} is full")
    return -1
}

/**
 * Returns the user serlected number
 * If the user wants to end the game, will be returned -1
 * If there is any other input, -2 will be returned
 */
fun askPlayerInput(playerName: String): Int {
    println("$playerName's turn:")
    return when (val userInput = readln()) {
        "end" -> -1
        else -> try {
            userInput.toUInt().toInt()
        } catch (ex: Exception) {
            -2
        }
    }
}

fun printBoard(board: MutableList<MutableList<Char>>) {
    //Table head
    for (columnIndex in board.first().indices) {
        print(" ${columnIndex + 1}")
    }
    println("")

    //table content
    for (rowIndex in board.indices) {
        for (columnIndex in board[rowIndex].indices) {
            print("║${board[rowIndex][columnIndex]}")
        }
        println("║")
    }

    //Print botton table
    for (columnIndex in board.first().indices) {
        if (columnIndex == 0) {
            print("╚═")
        } else if (columnIndex == board.first().indices.last()) {
            println("╩═╝")
        } else {
            print("╩═")
        }
    }
}

fun getBoardDimensions() {
    val keepAsking = true

    while (keepAsking) {
        println(
            "Set the board dimensions (Rows x Columns)\n" +
                    "Press Enter for default (6 x 7)"
        )

        val userInput = readln().uppercase().trim().replace(" ".toRegex(), "").replace("\t", "")
        var rowUnprocessed = 6
        var columnUnprocessed = 7
        if (userInput.isEmpty()) {
            return
        }

        val referenceFormatRegex = Regex("..? ?X ?..?")

        if (!referenceFormatRegex.matches(userInput)) {
            println("Invalid input")
            continue
        }

        try {
            rowUnprocessed = userInput.first().digitToInt()
        } catch (ex: Exception) {
            println("Invalid input")
            continue
        }

        try {
            columnUnprocessed = userInput.last().digitToInt()
        } catch (ex: Exception) {
            println("Invalid input")
            continue
        }

        if (rowUnprocessed !in 5..9) {
            println("Board rows should be from 5 to 9")
            continue
        }
        if (columnUnprocessed !in 5..9) {
            println("Board columns should be from 5 to 9")
            continue
        }

        rows = rowUnprocessed
        columns = columnUnprocessed

        return
    }

}