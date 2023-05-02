import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final char[][] board = new char[3][3];
    private static final char USER = 'X';
    private static final char COMPUTER = 'O';
    private static final char EMPTY = '-';

    public static void main(String[] args) {
        helloPanel();
        initializeBoard();
        printBoard();
        if (playerStarts()) {
            while (!isGameOver()) {
                userTurn();
                printBoard();
                if (isGameOver()) {
                    break;
                }
                computerTurn();
                printBoard();
            }
        } else {
            while (!isGameOver()) {
                computerTurn();
                printBoard();
                if (isGameOver()) {
                    break;
                }
                userTurn();
                printBoard();
            }
        }
        winner();
    }

    private static void helloPanel() {
        System.out.println("Witaj w grze kółko krzyżyk!");
        System.out.println("Twój symbol to X");
        System.out.println();
    }

    private static void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private static void printBoard() {
        System.out.println("Plansza:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print("|" + board[i][j] + "|");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean playerStarts() {
        System.out.print("Wybierz kto ma zacząć: Gracz czy Komputer (G/K): ");
        return scanner.next().equalsIgnoreCase("G");
    }

    private static boolean isGameOver() {
        return isBoardFull() || isLosing(USER) || isLosing(COMPUTER);
    }

    private static void userTurn() {
        int[] userMove = getUserMove();
        board[userMove[0]][userMove[1]] = USER;
    }

    private static void computerTurn() {
        int[] computerMove = getComputerMove();
        board[computerMove[0]][computerMove[1]] = COMPUTER;
        System.out.printf("Komputer postawił swój znak na polu: %d, %d\n", computerMove[0] + 1, computerMove[1] + 1);
        System.out.println();
    }

    private static int[] getUserMove() {
        int row, col;
        boolean indexException, areaUsed;
        do {
            indexException = false;
            areaUsed = false;
            System.out.print("Podaj wiersz od 1 do 3: ");
            row = scanner.nextInt() - 1;
            System.out.print("Podaj kolumnę od 1 do 3: ");
            col = scanner.nextInt() - 1;
            if (row < 0 || row > 2 || col < 0 || col > 2) {
                indexException = true;
                System.out.println("Wyszedłeś poza planszę!");
                System.out.println();
            } else if (board[row][col] != EMPTY) {
                areaUsed = true;
                System.out.println("To pole jest już zajęte!");
                System.out.println();
            }
        } while (indexException || areaUsed);
        return new int[]{row, col};
    }

    private static int[] getComputerMove() {
        int[] bestMove = {-1, -1};
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = COMPUTER;
                    int score = minmax(0, false);
                    board[i][j] = EMPTY;
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    private static int minmax(int depth, boolean isMaximizing) {
        int score = evaluate();
        if (score == 1) {
            return score - depth;
        }
        if (score == -1) {
            return score + depth;
        }
        if (isBoardFull()) {
            return 0;
        }
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = COMPUTER;
                        int currentScore = minmax(depth + 1, false);
                        board[i][j] = EMPTY;
                        bestScore = Math.max(bestScore, currentScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = USER;
                        int currentScore = minmax(depth + 1, true);
                        board[i][j] = EMPTY;
                        bestScore = Math.min(bestScore, currentScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private static boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isLosing(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        return false;
    }

    private static int evaluate() {
        if (isLosing(USER)) {
            return 1;
        } else if (isLosing(COMPUTER)) {
            return -1;
        } else {
            return 0;
        }
    }

    private static void winner() {
        if (isLosing(USER)) {
            System.out.println("Niestety Przegrałeś!");
        } else if (isLosing(COMPUTER)) {
            System.out.println("Gratulacje, wygrałeś!");
        } else {
            System.out.println("Remis!");
        }
    }
}