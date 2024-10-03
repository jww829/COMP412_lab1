package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Scanner {

    private final int[][] transTable = new int[47][30]; // 47 is the number of states; 30 is the number of characters(after compression)
    private BufferedReader buffer;
    private int lineNum = 0;
    private String line = "";
    private int idx = 0;
    private final int BADSTATE = 100;
    private final Set<Integer> acceptedState = new HashSet<>(Arrays.asList(6, 10, 11, 16, 18, 22, 28, 31, 33, 34, 36, 38, 39, 40, 43, 44, 45, 46));
    private boolean EOF = false;
    public int maxSR = -1;
    public int errNum;

    public Scanner(BufferedReader buffer){
        this.buffer = buffer;
        initTransTable();
    }
    /**
     * Process the next token
     * @return Token
     * @throws IOException
     */
    public Token nextToken() throws IOException {
        if (EOF) { // Deal with EOF 
            return Token.EOF(lineNum);
        }
        int state = 0;
        String lexeme = "";
        Stack<Integer> stack = new Stack<>();
        stack.clear();
        stack.push(BADSTATE);
        while (state != -1 && !EOF) {
            char c = nextChar();
            lexeme = lexeme + c;
            if (lexeme.isBlank() && !lexeme.contains("\n")){ // Skip space
                return nextToken();
            }
            if (this.acceptedState.contains(state)) {
                stack.clear();
            }
            stack.push(state);
            state = this.transTable[state][charMap(c)];
        
        }
        
        String lexeme_err = lexeme; 
        int idx_err = idx; // For error handling

        // Rollback to last accepted state
        while (!this.acceptedState.contains(state) && state != BADSTATE) {
            state = stack.pop();
            if (lexeme.length() > 1) {
                lexeme = lexeme.substring(0, lexeme.length()-1);
            }
            if (this.idx > 0) {
                this.idx--; // Rollback
            }
        }

        if (this.acceptedState.contains(state)) { 
            int PoS = stateToPOS(state);
            if (PoS == 9) { // Comment
                idx = line.length() - 1;
            }
            if (PoS == 6) { // Register
                int r = Integer.parseInt(lexeme.substring(1));
                if (r >= this.maxSR) {
                    this.maxSR = r;
                }
            }
            Token t = new Token(PoS, lexeme, PoS == 11 ? lineNum-1 : lineNum);
            return t;
        } else {
            System.err.println("ERROR " + this.lineNum + ": \"" + lexeme_err + "\" is not a valid word.");
            this.idx = idx_err;
            errNum++;
            return new Token(10, lexeme_err, lineNum); // INVALID
        }
    }

   
    private void initTransTable() {
        for (int i=0; i<this.transTable.length; i++) {
            for (int j=0; j<this.transTable[0].length; j++) {
                this.transTable[i][j] = -1;
            }
        }

        // Initial states
        transTable[0][charMap('r')]=1;
        transTable[0][charMap('l')]=7;
        transTable[0][charMap('s')]=12;
        transTable[0][charMap('m')]=19;
        transTable[0][charMap('o')]=23;
        transTable[0][charMap('n')]=29;
        transTable[0][charMap('=')]=35;
        transTable[0][charMap('/')]=37;
        transTable[0][charMap(',')]=39; //
        transTable[0][charMap('\n')]=40; //
        transTable[0][charMap('\r')]=40; //
        transTable[0][charMap('a')]=41; 
        transTable[0][charMap('1')]=33; //
        transTable[0][charMap('0')]=46; //
        // transTable[0][charMap('\0')]=46; //

        // lshift & rshift
        transTable[7][charMap('s')]=2; 
        transTable[1][charMap('s')]=2;
        transTable[2][charMap('h')]=3;
        transTable[3][charMap('i')]=4;
        transTable[4][charMap('f')]=5;
        transTable[5][charMap('t')]=6; //

        // register name
        transTable[1][charMap('1')]=44;
        transTable[1][charMap('0')]=45;
        transTable[44][charMap('1')]=44;
        transTable[44][charMap('0')]=45;
        transTable[45][charMap('0')]=45; //
        transTable[45][charMap('1')]=44; //

        // constant 
        transTable[33][charMap('0')]=34; 
        transTable[33][charMap('1')]=33; 
        transTable[34][charMap('0')]=34; //
        transTable[34][charMap('1')]=33; //


        // load & loadI
        transTable[7][charMap('o')]=8;
        transTable[8][charMap('a')]=9;
        transTable[9][charMap('d')]=10; //
        transTable[10][charMap('I')]=11; //

        // store
        transTable[12][charMap('t')]=13;
        transTable[13][charMap('o')]=14;
        transTable[14][charMap('r')]=15;
        transTable[15][charMap('e')]=16; //

        // add
        transTable[41][charMap('d')]=42; 
        transTable[42][charMap('d')]=43; //

        // sub
        transTable[12][charMap('u')]=17;
        transTable[17][charMap('b')]=18; //

        // mult
        transTable[19][charMap('u')]=20;
        transTable[20][charMap('l')]=21;
        transTable[21][charMap('t')]=22; //

        // output
        transTable[23][charMap('u')]=24;
        transTable[24][charMap('t')]=25;
        transTable[25][charMap('p')]=26; 
        transTable[26][charMap('u')]=27;
        transTable[27][charMap('t')]=28; //

        // nop
        transTable[29][charMap('o')]=30;
        transTable[30][charMap('p')]=31; //

        // =>
        transTable[35][charMap('>')]=36; //

        // comment
        transTable[37][charMap('/')]=38; //

    }
    
    /**
     * Get next char from the input
     * @return char
     * @throws IOException
     */
    private char nextChar() throws IOException {
        if (this.idx >= this.line.length()) {
            line = buffer.readLine();
            if (line == null) {
                this.EOF = true;
            }
            idx = 0;
            lineNum++;
            line += "\n"; // Add the newline character back 
        }
        char c = this.line.charAt(idx++);
        return c;
    }

    private int charMap(char c) {
        switch (c) {
            case 'a': return 1;
            case 'b': return 2;
            case 'd': return 3;
            case 'e': return 4;
            case 'f': return 5;
            case 'h': return 6;
            case 'i': return 7;
            case 'l': return 8;
            case 'm': return 9;
            case 'n': return 10;
            case 'o': return 11;
            case 'p': return 12;
            case 'q': return 13;
            case 'r': return 14;
            case 's': return 15;
            case 't': return 16;
            case 'u': return 17;
            case 'x': return 18;
            case 'y': return 19;
            case 'z': return 20;
            case '0': return 21;
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': return 22;
            case '=': return 23;
            case '\n': return 24;
            case '\r': return 25;
            case '/': return 26;
            case ',': return 27;
            case 'I': return 28;
            // case '\0': return 29;
            default: return 0;
        }
    }

    private int stateToPOS(int state) {
        switch (state) {
            case 10:
            case 16: return 0;
            case 6:
            case 18:
            case 22:
            case 43: return 2;
            case 11: return 1;
            case 28: return 3;
            case 31: return 4;
            case 46:
            case 33:
            case 34: return 5;
            case 44:
            case 45: return 6;
            case 39: return 7;
            case 36: return 8;
            case 38: return 9;
            case 40: return 11;
            // case 46: return "EOF";

            default: return 10; 
        }
    }

}

