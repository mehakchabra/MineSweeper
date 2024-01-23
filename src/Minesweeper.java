import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class Minesweeper extends JFrame {
    private static class  MineTile extends JButton{
        int r;
        int c;
        public MineTile(int r, int c){
            this.r = r;
            this.c = c;
        }
    }
    int tileSize = 50;
    int numRows = 10;
    int numColumns = numRows;
    int boardWidth = numRows * tileSize;
    int boardHeight = numColumns * tileSize;
    MineTile [][] board = new MineTile[numRows][numColumns];
    ArrayList<MineTile> mineList;
    int clickedTile = 0;
    boolean gameOver = false;
    int mineCount = 10;
    Random random = new Random();

    JLabel boardLabel;
    JPanel textPanel, boardPanel;

    Minesweeper(){
        super("Minesweeper");

        setSize(boardWidth,boardHeight);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardLabel = new JLabel("Minesweeper: " + Integer.toString(mineCount));
        boardLabel.setFont(new Font("Ariel",Font.BOLD, 25));
        boardLabel.setHorizontalAlignment(JLabel.CENTER);
        boardLabel.setOpaque(true);

        textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(boardLabel);
        add(textPanel,BorderLayout.NORTH);

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(numRows,numColumns));
//        boardPanel.setBackground(Color.green);
        add(boardPanel);

        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numColumns; j++){
                MineTile tile = new MineTile(i,j);
                board[i][j] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0,0,0,0));
                tile.setFont(new Font("Ariel Unicode MS", Font.PLAIN, 30));
                tile.setBackground(new Color(193, 192, 192));
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(gameOver){
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource();

                        //left click
                        if(e.getButton() == MouseEvent.BUTTON1){
                            if(tile.getText() == ""){
                                if(mineList.contains(tile)){
                                    revealMines();
                                }
                                else{
                                    checkMine(tile.r,tile.c);
                                }
                            }
                        }

                        //right click
                        else if(e.getButton() == MouseEvent.BUTTON3){
                            if(tile.getText() == "" && tile.isEnabled()){
                                tile.setText("🚩");
                            }
                            else if(tile.getText() == "🚩"){
                                tile.setText("");
                            }
                        }
                    }
                });
//                tile.setText("1");
                boardPanel.add(tile);
            }
        }
        
        setMines();

        setVisible(true);
    }

    void setMines() {
        mineList = new ArrayList<>();

        int mineLeft = mineCount;
        while(mineLeft > 0){
            int r = random.nextInt(numRows);
            int c = random.nextInt(numColumns);

            MineTile tile = board[r][c];
            if(!mineList.contains(tile)){
                mineList.add(tile);
                mineLeft -= 1;
            }
        }

//        mineList.add(board[2][2]);
//        mineList.add(board[2][3]);
//        mineList.add(board[5][6]);
//        mineList.add(board[3][4]);
//        mineList.add(board[1][1]);
    }

    void checkMine(int r, int c){
        if(r < 0 || r >= numRows || c < 0 || c >= numColumns){
            return;
        }

        MineTile tile = board[r][c];

        if(!tile.isEnabled()){
            return;
        }

        tile.setEnabled(false);
        tile.setBackground(new Color(65,69,69,255));
        clickedTile += 1;

        int minesFound = 0;

        //top 3
        minesFound += countMines(r-1,c-1);
        minesFound += countMines(r-1,c);
        minesFound += countMines(r-1,c+1);

        //left and right
        minesFound += countMines(r,c-1);
        minesFound += countMines(r,c+1);

        //bottom 3
        minesFound += countMines(r+1,c-1);
        minesFound += countMines(r+1,c);
        minesFound += countMines(r+1,c+1);

        if(minesFound > 0){
            tile.setText(Integer.toString(minesFound));
        }
        else{
            tile.setText("");

            //top3
            checkMine(r-1,c-1);
            checkMine(r-1,c);
            checkMine(r-1,c+1);

            //left and right
            checkMine(r,c-1);
            checkMine(r,c+1);

            //bottom 3
            checkMine(r+1,c-1);
            checkMine(r+1,c);
            checkMine(r+1,c+1);
        }
        if(clickedTile == numRows * numColumns - mineList.size()){
            gameOver = true;
            boardLabel.setText("Mines Cleared!!");
        }

    }

    int countMines(int r, int c) {
        if(r < 0 || r >= numRows || c < 0 || c >= numColumns){
            return 0;
        }
        else if(mineList.contains(board[r][c])){
            return 1;
        }
        else return 0;
    }

    void revealMines() {
        for(int i = 0; i < mineList.size(); i++){
            MineTile tile = mineList.get(i);
            tile.setText("💣");
//            tile.setBackground(new Color(248, 23, 23, 158));
        }
        gameOver = true;
        boardLabel.setText("Game Over");
    }

    public static void main(String[] args) {
        new Minesweeper();
    }
}