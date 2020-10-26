

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Minesweeper extends JFrame
{

    public static Minesweeper Game;
    static int ROWS,COLS;
    public int MINES;
    public int tilesLeft;
    public int Gtemp =0;
    private int TotalMines;
    static boolean MineArray[][];
    static boolean IsPressed [][];
    static JToggleButton grid[];
    int X = 0;
    int Y = 0;
    static int adjacentGrid[][];
    static int HEIGHT = 500;
    static int WIDTH = 500;
    JPanel gameContentPane = null;


    public static void main(String[] args)
    {
        Game = new Minesweeper();
    }
    public Minesweeper()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ROWS = 10;
            COLS = 10;
            MINES = 20;
            WIDTH = 500;
            HEIGHT = 500;

        buildJFrame();
    }

    private void buildJFrame()
    {
        this.setSize(WIDTH,HEIGHT);
        this.setTitle("Minesweeper	       Total Mines: "+ TotalMines);
        MineArray = new boolean[ROWS][COLS];
        IsPressed = new boolean[ROWS][COLS];
        adjacentGrid = new int[ROWS][COLS];

        grid = new JToggleButton[ROWS*COLS];
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(ROWS);
        gridLayout.setColumns(COLS);
        gameContentPane = new JPanel();
        gameContentPane.setLayout(gridLayout);
        this.setContentPane(gameContentPane);


        StartGame();
        this.setVisible(true);
    }
    private void StartGame()
    {
        BuildGrid();
        PlaceMines(MINES);
    }

    private void BuildGrid()
    {
        int t = 0;
        for(int x=0;x<ROWS;x++)
        {
            for(int y = 0;y<COLS;y++)
            {
                X = x;
                Y = y;
                grid[t] = new JToggleButton("");
                grid[t].addMouseListener(new java.awt.event.MouseAdapter()
                {
                    public void mouseReleased(java.awt.event.MouseEvent e)
                    {
                        if(e.getModifiers() == InputEvent.BUTTON3_MASK)
                        {
                            RightClick(e);
                        }
                        else if(e.getModifiers() == InputEvent.BUTTON1_MASK)
                        {
                            LeftClick(e);
                        }
                    }
                });
                gameContentPane.add(grid[t]);
                t++;
            }
        }
    }

    private void PlaceMines(int mines)
    {
        for(int x = 0; x < ROWS; x++)
        {
            for(int y = 0; y < COLS; y++)
            {
                MineArray[x][y] = false;
            }
        }
        int t,z,count = 0,Mines = mines;
        while(count<=Mines)
        {
            t = (int)(Math.random()*ROWS);
            z = (int)(Math.random()*COLS);
            if(MineArray[t][z] == false)
            {
                MineArray[t][z] = true;
                count++;
            }
        }
       for(int x = 0; x < ROWS; x++)
        {
            for(int y = 0; y < COLS; y++)
            {
                adjacentGrid[x][y] = getMines(x,y);
                IsPressed[x][y] = false;
            }
        }
        totalMines();
        gameContentPane.setEnabled(true);
    }

    private void LeftClick(java.awt.event.MouseEvent e)
    {
        JToggleButton mineButton = (JToggleButton)e.getSource();
        if(mineButton.isEnabled())
        {
            mineButton.setEnabled(false);
            if(isMine(mineButton))
            {
                showMines();
                JOptionPane.showMessageDialog(null,"Game Over");
            }
            else
            {
                mineButton.setBackground(null);
                if(mineButton.getText() == "|>")
                {
                    tilesLeft = tilesLeft - 1;
                    mineButton.setText("");
                }
                if(adjacentGrid[X][Y] > 0)
                {
                    mineButton.setText(Integer.toString(adjacentGrid[X][Y]));
                    tilesLeft = tilesLeft - 1;
                }
                else if(adjacentGrid[X][Y] == 0)
                {
                    clearGrid(X,Y);
                    tilesLeft = tilesLeft - Gtemp;
                    Gtemp = 0;
                }

                if(tilesLeft == MINES + 1)
                {
                    JOptionPane.showMessageDialog(null,"Winner");
                }
            }
        }
    }

    private void showMines()
    {
        for(int x = 0;x < ROWS; x++)
        {
            for(int y = 0;y < COLS; y++)
            {
                if(isMine(grid[x * ROWS + y]))
                {
                    grid[x * ROWS + y].setText("*");
                    grid[x * ROWS + y].setBackground(Color.GREEN);
                }
            }
        }
    }

    private void clearGrid(int i, int j)
    {
        if(isAround(i,j))
        {
            if(!IsPressed[i][j] && !MineArray[i][j])
            {

                IsPressed[i][j] = true;
                JToggleButton mineButton = check(i,j);
                if(adjacentGrid[i][j] > 0)
                {
                    mineButton.setText(Integer.toString(adjacentGrid[i][j]));
                }
                else
                {
                    mineButton.setText("");
                }
                mineButton.setSelected(true);
                mineButton.setEnabled(false);
                if(adjacentGrid[i][j] == 0)
                {
                    for(int x = -1; x<= 1; x++)
                    {
                        for(int y = -1;y<=1; y++)
                        {
                            clearGrid(i + x,j + y);
                        }
                    }
                }
            }
        }
    }
    private JToggleButton check(int x, int y)
    {
        return grid[(x*ROWS+y)];
    }

    private void RightClick(java.awt.event.MouseEvent e)
    {
        JToggleButton mineButton = (JToggleButton)e.getSource();
        if(mineButton.isEnabled())
        {
            if(mineButton.getText() != "|>")
            {
                mineButton.setText("|>");
                mineButton.setBackground(Color.BLUE);
            }
            else if(mineButton.getText() == "|>")
            {
                mineButton.setText("");
                mineButton.setBackground(null);
            }
            else mineButton.setText("");
        }
    }

    private int getMines(int x,int y)
    {
        int mines = 0;
        for(int i = -1; i <= 1; i++)
        {
            for(int j = -1; j <= 1; j++)
            {
                int new_x = x + i;
                int new_y = y + j;
                if(isAround(new_x, new_y) && MineArray[new_x][new_y])
                {
                    mines++;
                }
            }
        }
        return mines;
    }

    private boolean isAround(int x, int y)
    {
        if(x >= 0 && x< adjacentGrid.length && y >=0 && y < adjacentGrid[x].length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private void totalMines()
    {
        for(int x = 0; x<ROWS;x++)
        {
            for(int y = 0; y<COLS;y++)
            {
                if(isMine(grid[x*ROWS+y]))
                {
                    TotalMines = TotalMines + 1;
                }
            }
        }
        tilesLeft = ROWS*COLS;
        this.setTitle("Minesweeper	       Total Mines: "+ TotalMines);
    }
    private boolean isMine(JToggleButton mB)
    {
        int i= 0;
        for(int x = 0; x<ROWS;x++)
        {
            for(int y = 0; y<COLS;y++)
            {
                if(mB == grid[i])
                {
                    X = x;
                    Y = y;
                    return MineArray[x][y];
                }
                i++;
            }
        }
        return false;
    }
}

